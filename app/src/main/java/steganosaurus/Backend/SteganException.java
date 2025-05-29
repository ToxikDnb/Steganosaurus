package steganosaurus.Backend;

public class SteganException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new SteganException with the specified detail message.
     *
     * @param message the detail message
     */
    public SteganException(String message) {
        super(message);
    }

    /**
     * Constructs a new SteganException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public SteganException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new SteganException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public SteganException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
