package zsp.diploma.mintriang.exception;

public class InvalidInputPointsException extends TriangulationException {

    public InvalidInputPointsException() {
        super();
    }

    public InvalidInputPointsException(String message) {
        super(message);
    }

    public InvalidInputPointsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputPointsException(Throwable cause) {
        super(cause);
    }

    protected InvalidInputPointsException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
