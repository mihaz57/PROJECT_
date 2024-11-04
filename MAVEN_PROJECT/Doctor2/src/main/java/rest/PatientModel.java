package rest;

import dto.PatientDto;

public class PatientModel {

    
    private String name;
    private String surname;
    private String oib;
    private Integer phone;
    private String mail;
    private String password;
    private Integer doctor_id;

    public PatientModel() {}

    public PatientModel(String name, String surname, String oib, Integer phone, String mail, String password, Integer doctor_id) {
        
        this.name = name;
        this.surname = surname;
        this.oib = oib;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
        this.doctor_id = doctor_id;

    }
    public  PatientDto modelToDto(PatientModel patientModel){
        PatientDto patientDto = new PatientDto();
        patientDto.setId(null);
        patientDto.setName(patientModel.getName());
        patientDto.setSurname(patientModel.getSurname());
        patientDto.setOib(patientModel.getOib());
        patientDto.setPhone(patientModel.getPhone());
        patientDto.setMail(patientModel.getMail());
        patientDto.setPassword(patientModel.getPassword());
        patientDto.setDoctor_id(patientModel.getDoctor_id());

        return patientDto;

    }
    public Integer getDoctor_id() {
        return doctor_id;
    }
    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }


    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }


    public Integer getPhone(){
        return phone;
    }
    public void setPhone(Integer phone){
        this.phone = phone;
    }


    public String getOib(){
        return oib;
    }
    public void setOib(String oib){
        this.oib = oib;
    }





    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
}