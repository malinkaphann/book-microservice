/**
 * This is the specification of the book pagination object.
 *
 * @author Phann Malinka
 */
package myapp.book.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import myapp.book.dto.SearchDto;
import myapp.book.entities.Book;
import myapp.book.exceptions.ValidationException;
import myapp.book.utils.PaginationUtil.ORDER;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.criteria.Predicate;

@Component
public class BookSpecification {

    public Specification<Book> search(SearchDto searchDto) {

        Objects.requireNonNull(searchDto, "the input search dto must not be null");

          // validate the column to sort
          String[] sortables = new String[] { "id", "code", "title", "author", "description" };
          if (!Arrays.asList(sortables).contains(searchDto.getSort())) {
              throw new ValidationException(String.format(
                  "can not sort by column = %s, correct values = %s", 
                  searchDto.getSort(), Arrays.toString(sortables)));
          }

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
            if (Objects.equals(searchDto.getOrder(), ORDER.desc)) {
                query.orderBy(criteriaBuilder.desc(root.get(searchDto.getSort())));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get(searchDto.getSort())));
            }

            return criteriaBuilder.or(searchByCategory, searchByTitle, searchByAuthor, searchByDesc);
        };
    }
}
