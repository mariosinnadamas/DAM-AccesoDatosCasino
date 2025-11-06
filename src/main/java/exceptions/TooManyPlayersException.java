package exceptions;

public class TooManyPlayersException extends RuntimeException {
    public TooManyPlayersException(String message) {
        super(message);
    }
}
