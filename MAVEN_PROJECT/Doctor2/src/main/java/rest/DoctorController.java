package rest;


import dto.DoctorDto;
import exceptions.DBConnectionException;
import exceptions.OibAlreadyExistsException;
import exceptions.OibNotFoundException;
import manager.DoctorManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/doctor")
public class DoctorController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorManagement.class);


    @GET
    @Path("/get/{oib}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoctor(@PathParam("oib") String oib){
        logger.debug("Starting method getDoctor. ");
        try {
            DoctorManagement doctorManagement = new DoctorManagement();
            DoctorDto doctorDto = doctorManagement.getDoctorByOib(oib);
            return Response.status(Response.Status.OK).entity(doctorDto).build();
        } catch (DBConnectionException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error connecting to database."+e.getMessage()).build();
        } catch (OibNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Doctor not found: "+e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request.").build();
        }finally {
            logger.debug("Finished method getDoctorByOib. ");
        }

    }
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDoctor(DoctorModel doctorModel) {
        logger.debug("Starting method addDoctor. ");
        try{
            DoctorDto doctorDto = doctorModel.modelToDto(doctorModel);

            DoctorManagement doctorManagement = new DoctorManagement();
            DoctorDto addedDoctorDto = doctorManagement.addDoctor(doctorDto);

            return Response.status(Response.Status.OK).entity(addedDoctorDto).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity( e.getMessage()).build();
        }catch (OibAlreadyExistsException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity( e.getMessage()).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request."+ e.getMessage()).build();
        }
        finally{
            logger.debug("Finished method addDoctor. ");
        }

    }
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDoctorById(DoctorModel doctorModel) throws OibAlreadyExistsException, DBConnectionException {
        logger.debug("Starting method updateDoctor. ");
        try {
            DoctorDto doctorDto = doctorModel.modelToDto(doctorModel);

            DoctorManagement doctorManagement = new DoctorManagement();
            DoctorDto updateDoctorDto = doctorManagement.updateDoctorByOib(doctorDto);

            return Response.status(Response.Status.OK).entity(updateDoctorDto).build();

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
            logger.debug("Finished method updateDoctor. ");
        }
    }
    @DELETE
    @Path("/delete/{doctorOib}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDoctor(@PathParam("doctorOib") String oib) throws Exception {
        logger.debug("Starting method deleteDoctor. ");
        try {
            DoctorManagement doctorManagement = new DoctorManagement();
            boolean deleteDoctor = doctorManagement.deleteDoctor(oib);
            if (deleteDoctor) {
                return Response.status(Response.Status.OK).entity("Doctor successfully deleted").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Doctor with provided OIB does not exist. ").build();
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while processing the request.").build();
        }finally {
            logger.debug("Finished method deleteDoctor. ");
        }


    }
    @Path("withoutPatient")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDoctorsWithoutPatient() {
        logger.debug("Starting REST method getDoctorsWithoutPatient.");
        try {
            DoctorManagement doctorManagement = new DoctorManagement();
            List<DoctorDto> doctorsWithoutPatient = doctorManagement.getDoctorsWithoutPatient();
            return Response.status(Response.Status.OK).entity(doctorsWithoutPatient).build();
        } catch (DBConnectionException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error connecting to database: " + e.getMessage()).build();
        } finally {
            logger.debug("Finished REST method getDoctorsWithoutPatient.");
        }
    }


}