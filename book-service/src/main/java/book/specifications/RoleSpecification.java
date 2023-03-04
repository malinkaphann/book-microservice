/**
 * This is the specification of the book pagination object.
 *
 * @author Phann Malinka
 */
package book.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import book.dto.SearchDto;
import book.entities.Role;
import book.utils.PaginationUtil.ORDER;

import javax.persistence.criteria.Predicate;
import java.util.Objects;

@Component
public class RoleSpecification implements CrudSpecification<Role> {

    public Specification<Role> search(SearchDto searchDto) {
        Objects.requireNonNull(searchDto, "the input search dto must not be null");

        return (root, query, criteriaBuilder) -> {

             String keyword = searchDto.getSearch();
             
            // search by name
            Predicate searchByName = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");

            // sort
            if (Objects.equals(searchDto.getOrder(), ORDER.DESC.getValue())) {
                query.orderBy(criteriaBuilder.desc(root.get(searchDto.getSort())));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get(searchDto.getSort())));
            }

            return criteriaBuilder.or(searchByName);
        };
    }
}
