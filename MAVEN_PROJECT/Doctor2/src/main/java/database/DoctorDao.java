package database;

import dto.DoctorDto;
import exceptions.CreatingFailedException;
import exceptions.DBConnectionException;
import exceptions.OibNotFoundException;
import exceptions.UpdatingFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao {
    private static final Logger logger = LoggerFactory.getLogger(DoctorDao.class);

    public boolean isOibUnique(String oib) throws DBConnectionException {
        logger.debug("Starting method isOibUnique. ");
        String sql = "SELECT COUNT(*) AS count FROM doctor WHERE oib = '" + oib + "'";
        try (Connection conn = SConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {                                 // Omogućava iteriranje kroz rezultate i dobijanje podataka
            return rs.next() && rs.getInt("count") == 0;                    //rs.next() pomice se u sljedeci red
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        }
        finally{
            logger.debug("Finished method isOibUnique. ");

        }
    }
    public boolean checkOibExists(String oib) throws DBConnectionException {
        logger.debug("Starting method checkOibExists. ");
        String sql = "SELECT COUNT(*) AS count FROM doctor WHERE oib = ?";
        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, oib);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error executing query: ", e.getMessage());
            throw new DBConnectionException(e);
            //return false;
        }finally{
            logger.debug("Finished method checkOinExist. ");
        }
    }
    public boolean deleteDoctorByOib(String patientOib) throws Exception {
        String query = "DELETE FROM doctor WHERE oib = ?";
        logger.debug("Starting method deleteDoctorByOib with OIB. ");

        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, patientOib);             //postavljanje oiba u sql upit

            int rowsAffected = pstmt.executeUpdate();                //broj redaka koji su promjenjeni mu db
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error executing query: ", e.getMessage());
            throw new DBConnectionException(e);
        }
        finally {
            logger.debug("Finished method deleteDoctorByOib. ");
        }
    }
    public DoctorDto getDoctorByOib(String doctoroib) throws DBConnectionException, OibNotFoundException {
        logger.debug("Starting method getDoctorByOib with OIB: ", doctoroib);
        String query = "SELECT * FROM doctor WHERE oib = ?";

        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, doctoroib);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    String oib = rs.getString("oib");
                    Integer phone = rs.getInt("phone");
                    String mail = rs.getString("mail");
                    String password = rs.getString("password");
                    Integer capacity = rs.getInt("capacity");
                    Integer clinic_id = rs.getInt("clinic_id");

                    return new DoctorDto(id, name, surname, oib, phone, mail, password, capacity, clinic_id);
                }else{
                    logger.info("Doctor with OIB " + doctoroib + " does not exist");
                    throw new OibNotFoundException();
                }
            }
        } catch (SQLException e) {

            logger.error("Error executing query for OIB: "+ doctoroib, e.getMessage());
            throw new DBConnectionException(e);

        }finally {
            logger.debug("Finished method getDoctorByOib. ");
        }

    }
    public DoctorDto addDoctor(DoctorDto doctor) throws DBConnectionException, CreatingFailedException {
        logger.debug("Starting method addDoctor. ");
        String query = "INSERT INTO doctor (name, surname, oib, phone, mail, password, capacity, clinic_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        ResultSet resultSet;


        DoctorDto doctorResponse = null;

        try (Connection conn = SConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            st.setString(1, doctor.getName());
            st.setString(2, doctor.getSurname());
            st.setString(3, doctor.getOib());
            st.setInt(4, doctor.getPhone());
            st.setString(5, doctor.getMail());
            st.setInt(6, doctor.getCapacity());
            st.setInt(7, doctor.getClinic_id());
            st.executeUpdate();

            resultSet = st.getGeneratedKeys();
            if (resultSet.next()) { //akom postoji iduci redvracab true
                int generatedId = resultSet.getInt(1);
                doctorResponse = new DoctorDto(generatedId, doctor.getName(), doctor.getSurname(), doctor.getOib(), doctor.getPhone(), doctor.getMail(), doctor.getPassword(), doctor.getCapacity(), doctor.getClinic_id());
                return doctorResponse;
            }
            else{
                logger.info("Creating doctor failed." );
                throw new CreatingFailedException();
            }
        }
        catch (SQLException e){
            logger.error("Query execution failed:", e.getMessage());
            throw new DBConnectionException(e);
        }finally {
            logger.debug("Finished method addDoctor. ");
        }
    }
    public DoctorDto updateDoctorByOib(DoctorDto doctor) throws DBConnectionException, UpdatingFailedException, OibNotFoundException {
        logger.debug("Starting method updateDoctorByOib.");
        String query = "UPDATE doctor SET name = ?, surname = ?, phone = ?, mail = ?, password = ?, capacity = ?, clinic_id = ? WHERE oib = ?";
        DoctorDto doctorResponse = getDoctorByOib(doctor.getOib());
        Integer generatedId = doctorResponse.getId();


        try (Connection conn = SConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {

            st.setString(1, doctor.getName());
            st.setString(2, doctor.getSurname());
            st.setInt(3, doctor.getPhone()); // Pretpostavljamo da je phone String
            st.setString(4, doctor.getMail());
            st.setString(5, doctor.getPassword()); // Dodano postavljanje password polja
            st.setInt(6, doctor.getCapacity());
            st.setInt(7, doctor.getClinic_id());
            st.setString(8, doctor.getOib());

            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                logger.info("Updating doctor failed.");
                throw new UpdatingFailedException();
            }
            doctorResponse = new DoctorDto(generatedId, doctor.getName(), doctor.getSurname(), doctor.getOib(), doctor.getPhone(), doctor.getMail(), doctor.getPassword(), doctor.getCapacity(), doctor.getClinic_id());
            return doctorResponse;
        } catch (SQLException e) {
            logger.error("Query execution failed:", e.getMessage());
            throw new DBConnectionException(e);
        } finally {
            logger.debug("Finished method updateDoctorByOib.");
        }
    }

    public int getDoctorCapacity(int doctorId) throws SQLException, DBConnectionException {
        String query = "SELECT capacity FROM doctor WHERE id = ?";
        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, doctorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("capacity");
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        }
    }

    public void increaseDoctorCapacity(int doctorId) throws SQLException, DBConnectionException {
        logger.debug("Starting method increaseDoctorCapacity.");
        String query = "UPDATE doctor SET capacity = capacity + 1 WHERE id = ?";
        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, doctorId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        } finally {
            logger.debug("Finished method increaseDoctorCapacity.");
        }
    }
    public List<DoctorDto> getDoctorsWithoutPatient() throws DBConnectionException {
        logger.debug("Starting method getDoctorsWithoutPatient.");
        String sql = "SELECT * FROM doctor WHERE capacity IS NULL OR capacity = 0";
        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<DoctorDto> doctors = new ArrayList<>();
            while (rs.next()) {
                DoctorDto doctor = new DoctorDto();

                doctor.setId(rs.getInt("id"));
                doctor.setName(rs.getString("name"));
                doctor.setSurname(rs.getString("surname"));
                doctor.setOib(rs.getString("oib"));
                doctor.setPhone(rs.getInt("phone"));
                doctor.setMail(rs.getString("mail"));
                doctor.setPassword(rs.getString("password"));
                doctor.setCapacity(rs.getInt("capacity"));
                doctor.setClinic_id(rs.getInt("clinic_id"));
                doctors.add(doctor);
            }
            return doctors;
        } catch (SQLException e) {
            logger.error("Error executing query: " + e.getMessage());
            throw new DBConnectionException(e);
        } finally {
            logger.debug("Finished method getDoctorsWithoutPatient.");
        }
    }





}



