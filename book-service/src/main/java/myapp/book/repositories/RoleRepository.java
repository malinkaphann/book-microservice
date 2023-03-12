/**
 * This is the role repository.
 * 
 * @author Phann Malinka
 */
package myapp.book.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import myapp.book.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(String name);
    Optional<Role> findByName(String name);
    Page<Role> findAll(Specification<Role> specification, Pageable pageable);
}
