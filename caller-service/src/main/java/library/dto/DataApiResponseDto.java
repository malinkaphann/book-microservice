/**
 * This is the api response with data.
 * 
 * @author Phann Malinka
 */
package library.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataApiResponseDto<T> extends ApiResponseDto {
    private T data;

    public DataApiResponseDto(int status, String message, String requestId, 
        T data) {
        super(status, message);
        this.data = data;
    }
}