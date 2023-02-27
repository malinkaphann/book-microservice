/**
 * This exception is the top most parent of all exception classes.
 * Having it here is useful when we want to change not to use
 * RuntimeException anymore.
 *
 * @author Phann Malinka
 */
package library.exceptions;

public class GeneralException extends RuntimeException {

    public GeneralException(Throwable t) {
        super(t);
    }
    
    public GeneralException(String msg) {
        super(msg);
    }

    public GeneralException(String msg, Throwable t) {
        super(msg, t);
    }
}
