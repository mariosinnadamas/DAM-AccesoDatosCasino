package exceptions;

public class TooManyPlayersException extends RuntimeException {
    public TooManyPlayersException(String message, Throwable cause) {
        super(message, cause);
    }


    public TooManyPlayersException(String message) {
        super(message);
    }
}
