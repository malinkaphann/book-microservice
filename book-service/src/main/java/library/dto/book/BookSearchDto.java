/*
 * This is a book search request dto.
 * 
 * @author: Phann Malinka
 */
package library.dto.book;

import java.util.Arrays;
import library.dto.SearchDto;
import library.exceptions.ValidationException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchDto extends SearchDto {
    
    public void validate() {

        super.validate();

        String[] sortFields = new String[] { "id", "code", "title", "author", "category", "description" };
        if(Arrays.stream(sortFields).noneMatch(super.getSort()::equals)) {
            throw new ValidationException(String.format(
                "books can not be sorted by %s, the correct values = %s",
                super.getSort(), Arrays.toString(sortFields)
            ));
        }
    }
}
