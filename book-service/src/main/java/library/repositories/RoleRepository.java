/**
 * This is the role repository.
 * 
 * @author Phann Malinka
 */
package library.repositories;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import library.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role> {
    Optional<Role> findByName(String name);
}
