/**
 * This exception is used when that resource is not found.
 *
 * @author Phann Malinka
 */
package library.exceptions;

import library.utils.StatusEnum;

public class ResourceNotFoundException extends GeneralException {
    
    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public int getStatusCode() {
        return StatusEnum.ERROR_RESOURCE_NOT_FOUND.getValue();
    }
}
