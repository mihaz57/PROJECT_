package rest;
public class AssignModel {
    private String patientOib;
    private Integer doctorId;

    public String getPatientOib() {
        return patientOib;
    }
    public void setPatientOib(String patientOib) {
        this.patientOib = patientOib;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
}
