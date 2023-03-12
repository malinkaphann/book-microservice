/**
 * This is the role specification.
 *
 * @author Phann Malinka
 */
package myapp.book.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import myapp.book.dto.SearchDto;
import myapp.book.entities.Role;
import myapp.book.utils.PaginationUtil.ORDER;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.criteria.Predicate;
import javax.validation.ValidationException;

@Component
public class RoleSpecification implements CrudSpecification<Role> {

    public Specification<Role> search(SearchDto searchDto) {

        // validate the column to sort
        String[] sortables = new String[] { "name" };
        if (!Arrays.asList(sortables).contains(searchDto.getSort())) {
            throw new ValidationException(String.format(
                "can not sort by column = %s, correct values = %s", 
                searchDto.getSort(), Arrays.toString(sortables)));
        }

        return (root, query, criteriaBuilder) -> {

             String keyword = searchDto.getSearch();
             
            // search by name
            Predicate searchByName = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + keyword.toLowerCase() + "%");

            // sort
            if (Objects.equals(searchDto.getOrder(), ORDER.desc)) {
                query.orderBy(criteriaBuilder.desc(root.get(searchDto.getSort())));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get(searchDto.getSort())));
            }

            return criteriaBuilder.or(searchByName);
        };
    }

}
