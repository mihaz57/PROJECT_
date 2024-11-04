package manager;

import database.DoctorDao;
import dto.DoctorDto;
import exceptions.*;
import manager.validation.AddValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DoctorManagement {

    private static final Logger logger = LoggerFactory.getLogger(DoctorManagement.class);

    private DoctorDao doctorDao;
    public DoctorManagement(){
        this.doctorDao = new DoctorDao();
    }
    public DoctorDto addDoctor( DoctorDto doctorDto) throws OibAlreadyExistsException, CreatingFailedException, DBConnectionException {
        logger.debug("Starting method addDoctor. ");
        try{
            AddValidation validator = new AddValidation();
            validator.validate(doctorDto);

            DoctorDao doctorDao = new DoctorDao();
            boolean oibUnique = doctorDao.isOibUnique(doctorDto.getOib());
            if (oibUnique) {
                return doctorDao.addDoctor(doctorDto);
            } else {
                logger.info("Doctor with the given Oib does not exist. Unable to add. ");
                throw new OibAlreadyExistsException();
            }}
        finally{
            logger.debug("Finished method addDoctor. ");
        }
    }
    public DoctorDto updateDoctorByOib(DoctorDto doctorDto) throws DBConnectionException, OibAlreadyExistsException, UpdatingFailedException {
        logger.debug("Starting method getDoctorByOib. ");
        try {
            AddValidation validator = new AddValidation();
            validator.validate(doctorDto);

            DoctorDao doctorDao = new DoctorDao();
            boolean doctorExists = doctorDao.checkOibExists(doctorDto.getOib());
            if (doctorExists) {
                return doctorDao.updateDoctorByOib(doctorDto);
            } else {
                logger.info("Doctor with the given Oib does not exist. Unable to update. ");
                throw new OibAlreadyExistsException();
            }
        } catch (OibNotFoundException e) {
            throw new RuntimeException(e);
        } finally{
            logger.debug("Finished method getDoctorByOib. ");
        }
    }
    public boolean deleteDoctor(String doctorOib) throws Exception {
        try {
            logger.debug("Starting method deleteDoctor. ");
            DoctorDao doctorDao = new DoctorDao();
            AddValidation validator = new AddValidation();
            validator.validateOib(doctorOib);
            return doctorDao.deleteDoctorByOib(doctorOib);
        }
        finally {
            logger.debug("Finished method deleteDoctor. ");
        }

    }
    public DoctorDto getDoctorByOib(String oib) throws  Exception {
        logger.debug("Starting method getDoctorByOib. ");
        try {
            return doctorDao.getDoctorByOib(oib);
        }
        finally{
            logger.debug("Finished method getDoctorByOib. ");
        }
    }
    public List<DoctorDto> getDoctorsWithoutPatient() throws DBConnectionException {
        logger.debug("Starting method getDoctorsWithoutPatient.");
        try {
            return doctorDao.getDoctorsWithoutPatient();
        } finally {
            logger.debug("Finished method getDoctorsWithoutPatient.");
        }
    }


}