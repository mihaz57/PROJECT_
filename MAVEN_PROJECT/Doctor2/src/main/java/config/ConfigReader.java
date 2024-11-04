package config;

import java.util.ResourceBundle;

public class ConfigReader {
    private static final ResourceBundle configBundle = ResourceBundle.getBundle("config");
    public static int getMaxDoctorCapacity() {
        return Integer.parseInt(configBundle.getString("doctor.MAX_CAPACITY").trim());
    }
}

