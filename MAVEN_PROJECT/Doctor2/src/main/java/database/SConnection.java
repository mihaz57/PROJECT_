
package database;

import java.sql.DriverManager;                               //klasa koja se koristi za upravljanje JDBC drajverima
import java.sql.SQLException;


class SConnection {
    private static final String JDBC_URL = "jdbc:sqlite:C:\\Users\\Mirjam\\Desktop\\PROJEKTI\\MAVEN_PROJECT\\database\\database.db";
    private static  java.sql.Connection connection;
    //Privatni konstruktor sprečava instanciranje ove klase izvan same klase
    private SConnection() {}
    //public: Metoda je dostupna izvan klase.
    //static: Metoda pripada klasi, a ne instancama klase. Može se pozvati bez potrebe za kreiranjem instance klase.
    public static java.sql.Connection getConnection() throws SQLException {
        java.sql.Connection localRef = connection;                                //Kreira lokalnu referencu na trenutnu konekciju.
        if (localRef == null || localRef.isClosed()) {
            synchronized (SConnection.class) {
                localRef = connection;
                if (localRef == null || localRef.isClosed()) {
                    try {
                        Class.forName("org.sqlite.JDBC");
                        connection = localRef = DriverManager.getConnection(JDBC_URL);
                    } catch (ClassNotFoundException e) {
                        throw new SQLException("SQLite JDBC", e);
                    }
                }
            }
        }
        return localRef;
    }
}
