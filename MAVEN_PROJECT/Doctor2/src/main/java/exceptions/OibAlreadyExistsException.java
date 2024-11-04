package exceptions;

public class OibAlreadyExistsException extends Exception{
    public OibAlreadyExistsException() {
        super("Patient/Doctor with the given OIB already exists. Unable to add. ");
    }
}
