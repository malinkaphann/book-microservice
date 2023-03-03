/**
 * This exception is used when there is any error from running
 * the query against the database.
 *
 * @author Phann Malinka
 */
package library.exceptions;

import library.utils.StatusEnum;

public class DatabaseException extends GeneralException {
    
    public DatabaseException(Throwable t) {
        super(t);
        this.status = StatusEnum.ERROR_DATABASE;
    }

    public DatabaseException(String msg) {
        super(msg);
        this.status = StatusEnum.ERROR_DATABASE;
    }

    public DatabaseException(String msg, Throwable t) {
        super(msg, t);
        this.status = StatusEnum.ERROR_DATABASE;
    }
}
