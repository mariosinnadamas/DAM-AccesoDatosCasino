package exceptions;

public class ClientAlreadyExistsException extends Exception {
    public ClientAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
