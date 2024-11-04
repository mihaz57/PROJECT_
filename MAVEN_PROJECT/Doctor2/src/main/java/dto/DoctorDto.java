package dto;

public class DoctorDto {
    private Integer id;
    private String name;
    private String surname;
    private String oib;
    private Integer phone;
    private String mail;
    private String password;
    private Integer capacity;
    private Integer clinic_id;

    public DoctorDto() {}

    public DoctorDto(Integer id, String name, String surname, String oib, Integer phone, String mail, String password, Integer capacity, Integer clinic_id) {
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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(Integer clinic_id) {
        this.clinic_id = clinic_id;
    }
}

