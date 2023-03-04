/**
 * This is book repository.
 * 
 * @author Phann Malinka
 */
package book.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import book.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsByCode(String code);
    Optional<Book> findByCodeAndIdNot(String code, int id);
    Page<Book> findAll(Specification<Book> specification, Pageable pageable);
}