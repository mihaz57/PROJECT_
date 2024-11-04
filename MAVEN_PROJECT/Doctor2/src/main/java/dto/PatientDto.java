package dto;

public class PatientDto {

    private Integer id;
    private String name;
    private String surname;
    private String oib;
    private Integer phone;
    private String mail;
    private String password;
    private Integer doctor_id;

    public PatientDto() {}



    public PatientDto(Integer id, String name, String surname, String oib, Integer phone, String mail, String password, Integer doctor_id) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.oib = oib;
        this.phone = phone;
        this.mail = mail;
        this.password = password;
        this.doctor_id = doctor_id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }

    //////


}
