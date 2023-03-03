/**
 * This exception is used when the record with that same value already exists.
 *
 * @author Phann Malinka
 */
package library.exceptions;

import library.utils.StatusEnum;

public class ResourceDuplicatedException extends GeneralException {
    public ResourceDuplicatedException(String msg) {
        super(msg);
        this.status = StatusEnum.ERROR_RESOURCE_DUPLICATED;
    }
}
