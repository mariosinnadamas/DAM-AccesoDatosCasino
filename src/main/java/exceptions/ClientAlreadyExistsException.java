package exceptions;

public class ClientAlreadyExistsException extends RuntimeException {
    public ClientAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
