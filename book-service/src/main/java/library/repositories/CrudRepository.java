/**
 * This is the parent class of all repository classes.
 * All the common functions are to be defined here.
 *
 * @author Phann Malinka
 */
package library.repositories;

import library.entities.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @param <E> entity
 * @param <IdType> the id type of the entity
 */
@NoRepositoryBean
public interface CrudRepository<E extends BaseEntity<? extends Serializable>>
        extends JpaRepository<E, Integer> {
    List<E> findAllByOrderByIdDesc();
    Page<E> findAll(Specification<E> specification, Pageable pageable);
    Optional<E> findById(Integer id);
}
