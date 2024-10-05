package ku.cs.services.exceptions;

public class IllegalValidationException extends RuntimeException {
    public IllegalValidationException() {
        super();
    }

    public IllegalValidationException(String message){
        super(message);
    }
}
