/**
 * This is the crud specification.
 *
 * @author Phann Malinka
 */
package myapp.book.specifications;

import org.springframework.data.jpa.domain.Specification;
import myapp.book.dto.SearchDto;

public interface CrudSpecification<E> {
    Specification<E> search(SearchDto searchDto);
}
