package rest;

import dto.PatientDto;
import exceptions.OibAlreadyExistsException;
import exceptions.DBConnectionException;
import exceptions.OibNotFoundException;
import manager.PatientManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
@Path("/patient")
public class PatientController {
    private static final Logger logger = LoggerFactory.getLogger(PatientManagement.class);


    @GET
    @Path("/get/{oib}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatient(@PathParam("oib") String oib){
        logger.debug("Starting method getPatient. ");
        try {
            PatientManagement patientManagement = new PatientManagement();
            PatientDto patientDto = patientManagement.getPatientByOib(oib);
            return Response.status(Response.Status.OK).entity(patientDto).build();
        } catch (DBConnectionException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error connecting to database."+e.getMessage()).build();
        } catch (OibNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Patient not found: "+e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request.").build();
        }finally {
            logger.debug("Finished method getPatientByOib. ");
        }

    }



    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPatient(PatientModel patientModel) {
        logger.debug("Starting method addPatient. ");
        try{
        PatientDto patientDto = patientModel.modelToDto(patientModel);

        PatientManagement patientManagement = new PatientManagement();
        PatientDto addedPatientDto = patientManagement.addPatient(patientDto);

        return Response.status(Response.Status.OK).entity(addedPatientDto).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity( e.getMessage()).build();
        }catch (OibAlreadyExistsException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity( e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request."+ e.getMessage()).build();
        }
        finally{
            logger.debug("Finished method addPatient. ");
        }

    }
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePatientById(PatientModel patientModel) throws OibAlreadyExistsException, DBConnectionException {
        logger.debug("Starting method updatePatient. ");
        try {
            PatientDto patientDto = patientModel.modelToDto(patientModel);

            PatientManagement patientManagement = new PatientManagement();
            PatientDto updatedPatientDto = patientManagement.updatePatientByOib(patientDto);
            return Response.status(Response.Status.OK).entity(updatedPatientDto).build();

        }
        catch (DBConnectionException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error connecting to database."+e.getMessage()).build();
        }
        catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }catch (OibAlreadyExistsException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request."+ e.getMessage()).build();
        }finally{
            logger.debug("Finished method updatePatient. ");
        }
    }


    @DELETE
    @Path("/delete/{patientOib}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePatient(@PathParam("patientOib") String oib) throws Exception {
        logger.debug("Starting method deletePatient. ");
        try {
            PatientManagement patientManagement = new PatientManagement();
            boolean deletePatient = patientManagement.deletePatient(oib);
            if (deletePatient) {
                return Response.status(Response.Status.OK).entity("Patient successfully deleted").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Patient with provided OIB does not exist. ").build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request.").build();
        }finally {
            logger.debug("Finished method deletePatient. ");
        }
    }
    @PUT
    @Path("/assign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignPatientToDoctor(AssignModel request) {
        logger.debug("Starting method assignPatientToDoctor.");
        try {
            PatientManagement patientManagement = new PatientManagement();
            patientManagement.assignPatientToDoctor(request.getPatientOib(), request.getDoctorId());
            return Response.status(Response.Status.OK).entity("Patient successfully assigned to doctor.").build();
        } catch (DBConnectionException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error connecting to database: " + e.getMessage()).build();
        } catch (OibNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Patient not found: " + e.getMessage()).build();
        }catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An error occurred while processing the request: " + e.getMessage()).build();
        } finally {
            logger.debug("Finished method assignPatientToDoctor.");
        }
    }


    @PUT
    @Path("/unassign")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unassignPatientFromDoctor(UnassignModel request) {
        logger.debug("Starting method unassignPatientFromDoctor.");
        try {
            PatientManagement patientManagement = new PatientManagement();
            patientManagement.unassignPatientFromDoctor(request.getPatientOib());
            return Response.status(Response.Status.OK).entity("Patient successfully unassigned from doctor.").build();
        } catch (DBConnectionException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error connecting to database: " + e.getMessage()).build();
        } catch (OibNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Patient not found: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("An error occurred while processing the request: " + e.getMessage()).build();
        } finally {
            logger.debug("Finished method unassignPatientFromDoctor.");
        }
    }
    @Path("withoutDoctor")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPatientsWithoutDoctor() {
        logger.debug("Starting REST method getPatientsWithoutDoctor.");
        try {
            PatientManagement patientManagement = new PatientManagement();
            List<PatientDto> patientsWithoutDoctor = patientManagement.getPatientsWithoutDoctor();
            return Response.status(Response.Status.OK).entity(patientsWithoutDoctor).build();
        } catch (DBConnectionException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error connecting to database: " + e.getMessage()).build();
        } finally {
            logger.debug("Finished REST method getPatientsWithoutDoctor.");
        }
    }







}
