/**
 * This is book repository.
 * 
 * @author Phann Malinka
 */
package library.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import library.entities.Book;

@Repository
public interface BookRepository extends CrudRepository<Book> {
    Boolean existsByCode(String code);
    Optional<Book> findByCodeAndIdNot(String code, Integer id);
}