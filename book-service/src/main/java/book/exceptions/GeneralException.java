/**
 * This exception is the top most parent of all exception classes.
 * Having it here is useful when we want to change not to use
 * RuntimeException anymore.
 *
 * @author Phann Malinka
 */
package book.exceptions;

import book.utils.StatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralException extends RuntimeException {

    protected StatusEnum status;
    
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
