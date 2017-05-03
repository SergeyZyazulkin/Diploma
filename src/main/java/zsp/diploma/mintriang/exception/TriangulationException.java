package zsp.diploma.mintriang.exception;

public class TriangulationException extends Exception {

    public TriangulationException() {
        super();
    }

    public TriangulationException(String message) {
        super(message);
    }

    public TriangulationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TriangulationException(Throwable cause) {
        super(cause);
    }

    protected TriangulationException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
