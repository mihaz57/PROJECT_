package exceptions;
public class DBConnectionException extends Exception {
    public DBConnectionException(Throwable cause) {                    /* cause-uzrok iznimke */

        super("Query execution failed: ", cause);                      //poruka i uzrok
    }


}