package utils.exceptions;

public class InvalidNotationException extends Exception {
    public InvalidNotationException(String errorMessage) {
        //Checked.  Because it's probably gonna happen.
        super(errorMessage);
    }
}
