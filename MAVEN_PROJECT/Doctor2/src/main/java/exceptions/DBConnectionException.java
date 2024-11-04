package exceptions;
public class DBConnectionException extends Exception {
    public DBConnectionException(Throwable cause) {

        super("Query execution failed: ", cause);
    }


}