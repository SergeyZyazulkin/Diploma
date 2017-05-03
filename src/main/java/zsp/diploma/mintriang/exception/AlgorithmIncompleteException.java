package zsp.diploma.mintriang.exception;

public class AlgorithmIncompleteException extends TriangulationException {

    public AlgorithmIncompleteException() {
    }

    public AlgorithmIncompleteException(String message) {
        super(message);
    }

    public AlgorithmIncompleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlgorithmIncompleteException(Throwable cause) {
        super(cause);
    }

    public AlgorithmIncompleteException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
