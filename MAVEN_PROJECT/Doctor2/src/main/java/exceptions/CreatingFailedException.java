package exceptions;

public class CreatingFailedException extends Exception{
    public CreatingFailedException() {

        super("Creating patient failed. ");
    }
}