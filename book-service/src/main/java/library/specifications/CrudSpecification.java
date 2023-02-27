/**
 * This is the general specification class.
 *
 * @author Phann Malinka
 */
package library.specifications;

import library.dto.SearchDto;

import org.springframework.data.jpa.domain.Specification;

public interface CrudSpecification<E> {
    Specification<E> search(SearchDto searchDto);
}
