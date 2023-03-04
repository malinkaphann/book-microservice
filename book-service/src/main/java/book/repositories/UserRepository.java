/**
 * This is the user repository.
 * 
 * @author Phann Malinka
 */
package book.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import book.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
