/**
 * This is the specification of the book pagination object.
 *
 * @author Phann Malinka
 */
package library.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import library.dto.SearchDto;
import library.entities.User;
import library.utils.PaginationUtil.ORDER;
import javax.persistence.criteria.Predicate;
import java.util.Objects;

@Component
public class UserSpecification implements CrudSpecification<User> {

    public Specification<User> search(SearchDto searchDto) {
        Objects.requireNonNull(searchDto, "the input search dto must not be null");

        return (root, query, criteriaBuilder) -> {

             String keyword = searchDto.getSearch();

            // search by username
            Predicate searchByUsername = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")),
                    "%" + keyword.toLowerCase() + "%");

            // sort
            if (Objects.equals(searchDto.getOrder(), ORDER.DESC.getValue())) {
                query.orderBy(criteriaBuilder.desc(root.get(searchDto.getSort())));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get(searchDto.getSort())));
            }

            return criteriaBuilder.or(searchByUsername);
        };
    }
}
