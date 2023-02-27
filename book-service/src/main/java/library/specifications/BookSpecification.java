/**
 * This is the specification of the book pagination object.
 *
 * @author Phann Malinka
 */
package library.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import library.dto.SearchDto;
import library.entities.Book;
import library.utils.PaginationUtil.ORDER;
import javax.persistence.criteria.Predicate;
import java.util.Objects;

@Component
public class BookSpecification implements CrudSpecification<Book> {

    public Specification<Book> search(SearchDto searchDto) {
        Objects.requireNonNull(searchDto, "the input search dto must not be null");

        return (root, query, criteriaBuilder) -> {

            String keyword = searchDto.getSearch();

            // search by category
            Predicate searchByCategory = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("category")),
                    "%" + keyword.toLowerCase() + "%");

            // search by title
            Predicate searchByTitle = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + keyword.toLowerCase() + "%");

            // search by author
            Predicate searchByAuthor = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("author")),
                    "%" + keyword.toLowerCase() + "%");

            // search by description
            Predicate searchByDesc = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + keyword.toLowerCase() + "%");

            // sort
            if (Objects.equals(searchDto.getOrder(), ORDER.DESC.getValue())) {
                query.orderBy(criteriaBuilder.desc(root.get(searchDto.getSort())));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get(searchDto.getSort())));
            }

            return criteriaBuilder.or(searchByCategory, searchByTitle, searchByAuthor, searchByDesc);
        };
    }
}