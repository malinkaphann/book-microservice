/**
 * This is the base entity class.
 * All entities are supposed to be the children of this class.
 *
 * @author Phann Malinka
 */
package library.entities;

import java.io.Serializable;

public abstract class BaseEntity<IdType extends Serializable> {
    IdType id;
}