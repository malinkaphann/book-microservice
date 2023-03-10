/**
 * This is the search request dto.
 * 
 * @author Phann Malinka
 */
package myapp.book.dto;

import java.util.Objects;
import org.apache.commons.lang3.EnumUtils;
import myapp.book.exceptions.ValidationException;
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
    String search = "";
    String page = "1";
    String size = "10";
    String sort = "id";
    String order = ORDER.DESC.getValue();

    public void validate() {

         // validate search keyword
         if (this.search.length() > ValidationUtil.MAX_LEN_SEARCH) {
             throw new ValidationException(String.format(
                 "keyword must not be longer than %d character long",
                     ValidationUtil.MAX_LEN_SEARCH));
         }

         // validate order
         if ((Objects.equals(this.order, ORDER.ASC.getValue())) && 
            (Objects.equals(this.order, ORDER.DESC.getValue()))) {
             throw new ValidationException(String.format(
                 "can not be ordered by %s, the correct values = %s", 
                    order, EnumUtils.getEnumList(ORDER.class).toString().toLowerCase()));
         }

         // validate page
         try {
            Integer.parseInt(this.page);
         } catch(NumberFormatException e) {
            throw new ValidationException(String.format(
                "page = %s is not a number", this.page));
         }
         
         // validate size
         try {
            Integer.parseInt(this.size);
         } catch(NumberFormatException e) {
            throw new ValidationException(String.format(
                "size = %s is not a number", this.size));
         }
    }
}
