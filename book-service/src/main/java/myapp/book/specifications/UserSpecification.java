/**
 * This is the specification of the book pagination object.
 *
 * @author Phann Malinka
 */
package myapp.book.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import myapp.book.dto.SearchDto;
import myapp.book.entities.User;
import myapp.book.utils.PaginationUtil.ORDER;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.criteria.Predicate;
import javax.validation.ValidationException;

@Component
public class UserSpecification implements CrudSpecification<User> {

    public Specification<User> search(SearchDto searchDto) {

        Objects.requireNonNull(searchDto, "the input search dto must not be null");

        // validate the column to sort
        String[] sortables = new String[] {"id", "username" };
        if (!Arrays.asList(sortables).contains(searchDto.getSort())) {
            throw new ValidationException(String.format(
                "users can not be sorted by %s, correct values = %s", 
                    searchDto.getSort(), Arrays.toString(sortables)));
        }

        return (root, query, criteriaBuilder) -> {

             String keyword = searchDto.getSearch();

            // search by username
            Predicate searchByUsername = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("username")),
                    "%" + keyword.toLowerCase() + "%");

            // order
            if (Objects.equals(searchDto.getOrder(), ORDER.desc)) {
                query.orderBy(criteriaBuilder.desc(root.get(searchDto.getSort())));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get(searchDto.getSort())));
            }

            return criteriaBuilder.or(searchByUsername);
        };
    }
}
