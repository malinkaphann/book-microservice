/**
 * This is the api response with data.
 * 
 * @author Phann Malinka
 */
package book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DataApiResponseDto<T> extends ApiResponseDto {
    
    @ToString.Exclude
    private T data;

    public DataApiResponseDto(int status, String message, String requestId, T data) {
        this.status = status;
        this.message = message;
        this.requestId = requestId;
        this.data = data;
    }

    /*
    public String toString() {
        return String.format("DataApiResponse(status=%d, " +
        "message=%s, request id=%s)", super.getStatus(), 
        this.getMessage(), this.getRequestId(), this.data);
    }
     */
}