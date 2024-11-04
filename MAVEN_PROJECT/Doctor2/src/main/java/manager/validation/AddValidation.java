package manager.validation;

import dto.DoctorDto;
import dto.PatientDto;

import java.util.regex.Pattern;

public class AddValidation {

    public void validate(PatientDto patient) {
        validateNameSurname(patient.getName(), "Name");
        validateNameSurname(patient.getSurname(), "Surname");
        validateOib(patient.getOib());
        //validatePhone(patient.getPhone());
        validateEmail(patient.getMail());
        validatePassword(patient.getPassword());

    }
    public void validate(DoctorDto doctor) {
        validateNameSurname(doctor.getName(), "Name");
        validateNameSurname(doctor.getSurname(), "Surname");
        validateOib(doctor.getOib());
        //validatePhone(doctor.getPhone());
        validateEmail(doctor.getMail());
        validatePassword(doctor.getPassword());

    }


    private void validateNameSurname(String value, String fieldName) {
        String regex =  "^[A-Za-z ]+$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid "+ fieldName);
        }
    }

    public void validateOib(String oib) {
        String regex = "^\\d{11}$";
        Pattern pattern = Pattern.compile(regex);

        if (!(pattern.matcher(oib).matches())) {
            throw new IllegalArgumentException("Invalid oib");
        }
    }
    public void validatePhone(Integer phone){
        String regex =  "^\\(\\d{3}\\) \\d{3}-\\d{4}$"; //(123) 456-7890
        String phoneString = String.valueOf(phone);
        Pattern pattern = Pattern.compile(regex);

        if (!(pattern.matcher(phoneString).matches())) {
            throw new IllegalArgumentException("Invalid phone");
        }

    }

    private void validateEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[\\a-zA-Z]{2,6}";
        Pattern pattern = Pattern.compile(regex);

        if (!(pattern.matcher(email).matches())) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    private void validatePassword(String password){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);

        if (!(pattern.matcher(password).matches())) {
            throw new IllegalArgumentException("Invalid password");
        }

    }


}


