/**
 * This is the user repository.
 * 
 * @author Phann Malinka
 */
package library.repositories;

import java.util.Optional;
import library.entities.User;

public interface UserRepository extends CrudRepository<User> {
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    
}
