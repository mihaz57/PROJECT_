package manager;

import config.ConfigReader;
import database.DoctorDao;
import database.PatientDao;
import dto.PatientDto;
import exceptions.*;
import manager.validation.AddValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
public class PatientManagement {
    private static final Logger logger = LoggerFactory.getLogger(PatientManagement.class);


    private PatientDao patientDao;
    private DoctorDao doctorDao;
    public PatientManagement() {
        this.patientDao = new PatientDao();
        this.doctorDao = new DoctorDao();
    }
    public PatientManagement(PatientDao patientDao) {
        this.patientDao = patientDao;
    }




    public PatientDto addPatient(PatientDto patientDto) throws OibAlreadyExistsException, CreatingFailedException, DBConnectionException {
        logger.debug("Starting method addPatient. ");
            try{AddValidation validator = new AddValidation();
            validator.validate(patientDto);

            PatientDao patientDao = new PatientDao();
            boolean oibUnique = patientDao.isOibUnique(patientDto.getOib());
            if (oibUnique) {
                return patientDao.addPatient(patientDto);
            } else {
                logger.info("Patient with the given Oib does not exist. Unable to add. ");
                throw new OibAlreadyExistsException();
            }}
            finally{
                logger.debug("Finished method addPatient. ");
            }
    }
    public PatientDto updatePatientByOib(PatientDto patientDto) throws DBConnectionException, OibAlreadyExistsException, UpdatingFailedException {
        logger.debug("Starting method getPatientByOib. ");
        try {
            AddValidation validator = new AddValidation();
            validator.validate(patientDto);

            PatientDao patientDao = new PatientDao();
            boolean patientExists = patientDao.checkOibExists(patientDto.getOib());
            if (patientExists) {
                return patientDao.updatePatientByOib(patientDto);
            } else {
                logger.info("Patient with the given Oib does not exist. Unable to update. ");
                throw new OibAlreadyExistsException();
            }
        } catch (OibNotFoundException e) {
            throw new RuntimeException(e);
        } finally{
            logger.debug("Finished method getPatientByOib. ");
        }
    }


    public boolean deletePatient(String patientOib) throws Exception {
            try {
                logger.debug("Starting method deletePatient. ");
                PatientDao patientDao = new PatientDao();
                AddValidation validator = new AddValidation();
                validator.validateOib(patientOib);
                return patientDao.deletePatientByOib(patientOib);
            }
            finally {
                logger.debug("Finished method deletePatient. ");
            }

    }


    public PatientDto getPatientByOib(String oib) throws  Exception {
        logger.debug("Starting method getPatientByOib. ");
        try {
            return patientDao.getPatientByOib(oib);
        }
        finally{
            logger.debug("Finished method getPatientByOib. ");
        }
    }
   /* public void assignPatientToDoctor(String patientOib, int doctorId) throws Exception {
        logger.debug("Starting method assignPatientToDoctor.");
        try {
            // Provjerite postoji li pacijent s navedenim OIB-om
            boolean patientExists = patientDao.checkOibExists(patientOib);
            if (patientExists == false) {
                throw new OibNotFoundException();
            }

            // Dodijelite pacijenta određenom liječniku
            patientDao.assignPatientToDoctor(patientOib, doctorId);

            // Povećajte kapacitet liječnika za jedan
            doctorDao.increaseDoctorCapacity(doctorId);
        } finally {
            logger.debug("Finished method assignPatientToDoctor.");
        }
    }*/
   public void assignPatientToDoctor(String patientOib, int doctorId) throws Exception {
       logger.debug("SStarting method assignPatientToDoctor.");

       int doctorCapacity = doctorDao.getDoctorCapacity(doctorId);
       int maxDoctorCapacity = ConfigReader.getMaxDoctorCapacity();
       logger.debug("doctorCapacity = " + doctorCapacity);
       logger.debug("maxDoctorCapacity = " + maxDoctorCapacity);
       logger.info(String.valueOf(doctorCapacity));

       if (doctorCapacity >= maxDoctorCapacity) {
           throw new IllegalArgumentException("Doctor has reached maximum capacity.");
       }

       try {
           boolean patientExists = patientDao.checkOibExists(patientOib);
           if (!patientExists) {
               throw new OibNotFoundException();
           }

           patientDao.assignPatientToDoctor(patientOib, doctorId);
           doctorDao.increaseDoctorCapacity(doctorId);
       } finally {
           logger.debug("Finished method assignPatientToDoctor.");
       }
   }




    public void unassignPatientFromDoctor(String patientOib) throws Exception {
        logger.debug("Starting method unassignPatientFromDoctor.");
        try {
            boolean patientExists = patientDao.checkOibExists(patientOib);
            if (!patientExists) {
                throw new OibNotFoundException();
            }
            patientDao.unassignPatientFromDoctor(patientOib);
        } finally {
            logger.debug("Finished method unassignPatientFromDoctor.");
        }
    }
    public List<PatientDto> getPatientsWithoutDoctor() throws DBConnectionException {
        logger.debug("Starting method getPatientsWithoutDoctor.");
        try {
            return patientDao.getPatientsWithoutDoctor();
        } finally {
            logger.debug("Finished method getPatientsWithoutDoctor.");
        }
    }




}




















