/**
 * This is the search request dto.
 * 
 * @author Phann Malinka
 */
package myapp.book.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import myapp.book.utils.PaginationUtil;
import myapp.book.utils.ValidationUtil;
import myapp.book.utils.PaginationUtil.ORDER;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class SearchDto {

    @Size(max = ValidationUtil.MAX_LEN_SEARCH, message = "the search keyword = '${validatedValue}' must be shorter than {max} characters long")
    String search = "";

    @Min(value = 1, message = "the page = '${validatedValue}' must be greater than or equal to {value}")
    @Max(value = 100, message = "the page = '${validatedValue}' must be less than or equal to {value}")
    int page = PaginationUtil.DEFAULT_PAGE_NUMBER;

    @Min(value = 1, message = "the size = '${validatedValue}' must be greater than {value}")
    @Max(value = 10, message = "the size = '${validatedValue}' must be less than or equal to {value}")
    int size = PaginationUtil.DEFAULT_PAGE_SIZE;
    @Size(max = ValidationUtil.MAX_LEN_SORT, message = "the sort = '${validatedValue}' must not be longer than {max} characters")
    String sort = "id";

    @Enumerated(EnumType.STRING)
    ORDER order = ORDER.desc;
}
