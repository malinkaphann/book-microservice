/**
 * This is the general specification class.
 *
 * @author Phann Malinka
 */
package book.specifications;

import org.springframework.data.jpa.domain.Specification;

import book.dto.SearchDto;

public interface CrudSpecification<E> {
    Specification<E> search(SearchDto searchDto);
}
