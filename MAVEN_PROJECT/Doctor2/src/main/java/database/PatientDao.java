package database;

import dto.PatientDto;
import exceptions.CreatingFailedException;
import exceptions.DBConnectionException;
import exceptions.OibNotFoundException;
import exceptions.UpdatingFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Data Access Object - pristup podacima iz baze
public class PatientDao {
    private static final Logger logger = LoggerFactory.getLogger(PatientDao.class);
    /*
    public void createTable() throws Exception {
        String query = "CREATE TABLE IF NOT EXISTS patient (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "name TEXT NOT NULL,\n"
                + "surname TEXT NOT NULL,\n"
                + "oib TEXT NOT NULL,\n"
                + "phone INTEGER NOT NULL,\n"
                + "mail TEXT NOT NULL,\n"
                + "password TEXT NOT NULL,\n"
                + "assigned_doctor TEXT\n"
                //+ "FOREIGN KEY (assigned_doctor_id) REFERENCES Doctor(id) ON DELETE SET NULL,\n"
                + ");";

        try (Connection conn = SConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertTable() {
        String sql = "INSERT INTO patient ( name, surname, oib, phone, mail, password, assigned_doctor) VALUES ('Joe', 'Joe', 11111111111, 0921418726,'lalal@gmail.com', '12345', '111')";

        try (Connection conn = SConnection.getConnection();  //vraca konekciju koja se zatvara nakon izlaska iz bloka
             Statement stmt = conn.createStatement()) {       //Izvršavanje SQL upita pomoću stmt.executeUpdate(sql)
            stmt.executeUpdate(sql);                          // izvršava SQL upit definiran varijablom 'sql'.
        } catch (SQLException e) {
            e.printStackTrace();                            //ispisuje informacije o iznimci na standardni izlaz.
        }
    }

    public boolean isTablePopulated() {
        String sql = "SELECT COUNT(*) AS number FROM patient";
        try (Connection conn = SConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() && rs.getInt("number") > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isIdUnique(int id) {
        String sql = "SELECT COUNT(*) AS count FROM patient WHERE id = " + id;
        try (Connection conn = SConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() && rs.getInt("count") == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }*/

    public boolean isOibUnique(String oib) throws DBConnectionException {
        logger.debug("Starting method isOibUnique. ");
        String sql = "SELECT COUNT(*) AS count FROM patient WHERE oib = '" + oib + "'";
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
        String sql = "SELECT COUNT(*) AS count FROM patient WHERE oib = ?";
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
/////////////////////////////////////////////////////////////////////////////
    public boolean deletePatientByOib(String patientOib) throws Exception {
        String query = "DELETE FROM patient WHERE oib = ?";
        logger.debug("Starting method deletePatientByOib with OIB. ");

        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, patientOib);                             //postavljanje oiba u sql upit

            int rowsAffected = pstmt.executeUpdate();                                //broj redaka koji su promjenjeni mu db
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error executing query: ", e.getMessage());
            throw new DBConnectionException(e);
        }
        finally {
            logger.debug("Finished method deletePatientByOib. ");
        }
    }



    public PatientDto getPatientByOib(String patientoib) throws DBConnectionException, OibNotFoundException {
        logger.debug("Starting method getPatientByOib with OIB: ", patientoib);
        String query = "SELECT * FROM patient WHERE oib = ?";

        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, patientoib);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    String oib = rs.getString("oib");
                    Integer phone = rs.getInt("phone");
                    String mail = rs.getString("mail");
                    String password = rs.getString("password");
                    Integer doctor_id = rs.getInt("doctor_id");

                    return new PatientDto(id, name, surname, oib, phone, mail, password, doctor_id);
                }else{
                    logger.info("Patient with OIB " + patientoib + " does not exist");
                    throw new OibNotFoundException();
                }
            }
        } catch (SQLException e) {

            logger.error("Error executing query for OIB: "+ patientoib, e.getMessage());
            throw new DBConnectionException(e);

        }finally {
            logger.debug("Finished method getPatientByOib. ");
        }

    }

    public PatientDto addPatient(PatientDto patient) throws DBConnectionException,CreatingFailedException {
        logger.debug("Starting method addPatient. ");
        String query = "INSERT INTO patient (name, surname, oib, phone, mail, password, doctor_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        ResultSet resultSet;
        PatientDto patientResponse = null;
        try (Connection conn = SConnection.getConnection();
            PreparedStatement st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            st.setString(1, patient.getName());
            st.setString(2, patient.getSurname());
            st.setString(3, patient.getOib());
            st.setInt(4, patient.getPhone());
            st.setString(5, patient.getMail());
            st.setString(6, patient.getPassword());
            st.setInt(7, patient.getDoctor_id());
            st.executeUpdate();
            resultSet = st.getGeneratedKeys();
            if (resultSet.next()) {                                                             //akom postoji iduci redvracab true
                int generatedId = resultSet.getInt(1);
                patientResponse = new PatientDto(generatedId, patient.getName(), patient.getSurname(), patient.getOib(), patient.getPhone(), patient.getMail(), patient.getPassword(), patient.getDoctor_id());
                return patientResponse;
            }
            else{
                logger.info("Creating patient failed." );
                throw new CreatingFailedException();
            }
        }
        catch (SQLException e){
            logger.error("Query execution failed:", e.getMessage());
            throw new DBConnectionException(e);
        }finally {
            logger.debug("Finished method addPatient. ");
        }
    }

    public PatientDto updatePatientByOib(PatientDto patient) throws DBConnectionException, UpdatingFailedException, OibNotFoundException {
        logger.debug("Starting method addPatient. ");
        String query = "UPDATE patient SET name = ?, surname = ?, oib = ?, phone = ?, mail = ?, password = ?, doctor_id = ? WHERE oib = ?";
        PatientDto patientResponse = getPatientByOib(patient.getOib());
        Integer generatedId = patientResponse.getId();

        try (Connection conn = SConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, patient.getName());
            st.setString(2, patient.getSurname());
            st.setString(3, patient.getOib());
            st.setInt(4, patient.getPhone());
            st.setString(5, patient.getMail());
            st.setString(6, patient.getPassword());
            st.setInt(7, patient.getDoctor_id());
            st.setString(8, patient.getOib());
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated == 0) {
                logger.info("Updating patient failed. " );
                throw new UpdatingFailedException();
            }
            patientResponse = new PatientDto(generatedId, patient.getName(), patient.getSurname(), patient.getOib(), patient.getPhone(), patient.getMail(), patient.getPassword(), patient.getDoctor_id());
            return patientResponse;
        } catch (SQLException e) {
            logger.error("Query execution failed:", e.getMessage());
            throw new DBConnectionException(e);
        }finally {
            logger.debug("Finished method updatePatient. ");
        }
    }
    public void assignPatientToDoctor(String patientOib, int doctorId) throws SQLException, DBConnectionException {
        logger.debug("Starting method assignPatientToDoctor.");
        String query = "UPDATE patient SET doctor_id = ? WHERE oib = ?";
        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, doctorId);
            pstmt.setString(2, patientOib);
            pstmt.executeUpdate();
            logger.debug("AssignPatientToDoctor.");
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        } finally {
            logger.debug("Finished method assignPatientToDoctor.");
        }
    }
    public int countPatientsByDoctor(int doctorId) throws SQLException, DBConnectionException {
        logger.debug("Starting method countPatientsByDoctor.");
        String query = "SELECT COUNT(*) FROM patient WHERE doctor_id = ?";
        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, doctorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        } finally {
            logger.debug("Finished method countPatientsByDoctor.");
        }
    }
    public void unassignPatientFromDoctor(String patientOib) throws SQLException, DBConnectionException {
        logger.debug("Starting method unassignPatientFromDoctor.");
        String query = "UPDATE patient SET doctor_id = NULL WHERE oib = ?";
        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, patientOib);
            pstmt.executeUpdate();
            logger.debug("UnassignPatientFromDoctor.");
        } catch (SQLException e) {
            throw new DBConnectionException(e);
        } finally {
            logger.debug("Finished method unassignPatientFromDoctor.");
        }
    }
    public List<PatientDto> getPatientsWithoutDoctor() throws DBConnectionException {
        logger.debug("Starting method getPatientsWithoutDoctor.");
        String sql = "SELECT * FROM patient WHERE doctor_id IS NULL";
        try (Connection conn = SConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<PatientDto> patients = new ArrayList<>();
            while (rs.next()) {
                PatientDto patient = new PatientDto();

                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setSurname(rs.getString("surname"));
                patient.setOib(rs.getString("oib"));
                patient.setPhone(rs.getInt("phone"));
                patient.setMail(rs.getString("mail"));
                patient.setPassword(rs.getString("password"));
                patient.setDoctor_id(rs.getInt("doctor_id"));
                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            logger.error("Error executing query: " + e.getMessage());
            throw new DBConnectionException(e);
        } finally {
            logger.debug("Finished method getPatientsWithoutDoctor.");
        }
    }




}







//https://www.tabnine.com/code/java/methods/java.sql.Statement/executeUpdate
//https://www.geeksforgeeks.org/how-to-use-preparedstatement-in-java/
/*PreparedStatement sučelje proširuje Statement sučelje ono predstavlja unaprijed kompajliranu SQL naredbu koja se može izvršiti više puta. Ovo prihvaća parametrizirane SQL upite i ovom upitu možete proslijediti 0 ili više parametara.*/