/**
 * This is the api response with data.
 * 
 * @author Phann Malinka
 */
package caller.dto;

import caller.entities.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookDataApiResponseDto extends ApiResponseDto {
    private Book data;

    public BookDataApiResponseDto(int status, String message, String requestId, 
        Book data) {
        super(status, message);
        this.data = data;
    }
}