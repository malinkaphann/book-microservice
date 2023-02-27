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
    }

    public DatabaseException(String msg) {
        super(String.format("DatabaseError: %s", msg));
    }

    public DatabaseException(String msg, Throwable t) {
        super(String.format("DatabaseError: %s", msg), t);
    }

    public int getStatusCode() {
        return StatusEnum.ERROR_DATABASE.getValue();
    }
}
