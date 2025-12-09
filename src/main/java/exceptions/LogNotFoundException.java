package exceptions;

public class LogNotFoundException extends RuntimeException {
    public LogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogNotFoundException(String message) {
        super(message);
    }
}
