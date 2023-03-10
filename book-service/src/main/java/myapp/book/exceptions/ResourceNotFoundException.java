/**
 * This exception is used when that resource is not found.
 *
 * @author Phann Malinka
 */
package myapp.book.exceptions;

import myapp.book.utils.StatusEnum;

public class ResourceNotFoundException extends GeneralException {
    public ResourceNotFoundException(String msg) {
        super(msg);
        this.status = StatusEnum.ERROR_RESOURCE_NOT_FOUND;
    }
}
