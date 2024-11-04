package exceptions;

public class UpdatingFailedException extends Exception{
    public UpdatingFailedException() {

        super("Updating patient failed. ");
    }
}