/**
 * This is the search request dto.
 * 
 * @author Phann Malinka
 */
package library.dto;

import java.util.Objects;
import library.exceptions.ValidationException;
import library.utils.PaginationUtil.ORDER;
import library.utils.ValidationUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SearchDto extends ApiRequestDto {
    
    private String search = "";
    private String page = "1";
    private String size = "10";
    private String sort = "id";
    private String order = ORDER.DESC.getValue();

    public void validate() {

         // validate search keyword
         if (this.search.length() > ValidationUtil.MAX_LEN_SEARCH) {
             throw new ValidationException(String.format(
                 "keyword must not be longer than %d character long",
                     ValidationUtil.MAX_LEN_SEARCH));
         }

         // validate order
         if ((!Objects.equals(this.order, ORDER.ASC.getValue()) && 
             (!Objects.equals(this.order, ORDER.DESC.getValue())))) {
             throw new ValidationException(String.format(
                 "can not be ordered by %s, the correct values = [desc, asc]", 
                 order));
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

    public String toString() {
        return String.format(
            "SearchDto(request id = %s, search = %s, " +
            "page = %s, size = %s, sort = %s, order = %s)",
            super.requestId, this.search, this.page, 
            this.size, this.sort, this.order);
    }
}
