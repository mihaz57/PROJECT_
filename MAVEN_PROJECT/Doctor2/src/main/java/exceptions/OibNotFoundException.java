package exceptions;



public class OibNotFoundException extends Exception {
    public OibNotFoundException() {
        super("Given oib does not exist. ");
    }
}
