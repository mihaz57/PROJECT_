package rest;

import dto.DoctorDto;

public class DoctorModel {
    private int id;
    private String name;
    private String surname;
    private String oib;
    private int phone;
    private String mail;
    private String password;
    private int capacity;
    private int clinic_id;

    public DoctorModel() {
    }

    public DoctorModel(int id, String name, String surname, String oib, int phone, String mail, String password, int capacity, int clinic_id) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.oib = oib;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
        this.capacity = capacity;
        this.clinic_id = clinic_id;

    }
    public DoctorDto modelToDto(DoctorModel doctorModel){
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(null);
        doctorDto.setName(doctorModel.getName());
        doctorDto.setSurname(doctorModel.getSurname());
        doctorDto.setOib(doctorModel.getOib());
        doctorDto.setPhone(doctorModel.getPhone());
        doctorDto.setMail(doctorModel.getMail());
        doctorDto.setPassword(doctorModel.getPassword());
        doctorDto.setCapacity(doctorModel.getCapacity());
        doctorDto.setClinic_id(doctorModel.getClinic_id());

        return doctorDto;

    }




    public int getClinic_id() {
        return clinic_id;
    }
    public void setClinic_id(int clinic_id) {
        this.clinic_id = clinic_id;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
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


    public int getPhone(){
        return phone;
    }
    public void setPhone(int phone){
        this.phone = phone;
    }


    public String getOib(){
        return oib;
    }
    public void setOib(String oib){
        this.oib = oib;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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