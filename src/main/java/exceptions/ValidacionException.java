package exceptions;

public class ValidacionException extends Exception {
    public ValidacionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidacionException(String message) {
        super(message);
    }
}
