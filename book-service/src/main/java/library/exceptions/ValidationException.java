/**
 * This exception is used when a validation is failed.
 * 
 * @author Phann Malinka
 */

package library.exceptions;

import library.utils.StatusEnum;

public class ValidationException extends GeneralException {
    
    public ValidationException(String msg) {
        super(msg);
    }

    public int getStatusCode() {
        return StatusEnum.ERROR_VALIDATION.getValue();
    }
}
