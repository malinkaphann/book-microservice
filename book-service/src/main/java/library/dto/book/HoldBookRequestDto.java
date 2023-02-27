package library.dto.book;

import library.dto.ApiRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import library.exceptions.ValidationException;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class HoldBookRequestDto extends ApiRequestDto {
    private String userId;
    private String bookId;

    @Override
    public void setRequestId(String requestId) {
        super.setRequestId(requestId);
    }

    public void validate() {
        
        if (this.userId == null || this.userId.isEmpty()) {
            throw new ValidationException("userId is required");
        }

        try {
            Integer.parseInt(this.userId);
        } catch(NumberFormatException e) {
            throw new ValidationException(String.format("userId = %s is not a number", 
                this.userId));
        }

        if (this.bookId == null || this.bookId.isEmpty()) {
            throw new ValidationException("bookId is required");
        }

        try {
            Integer.parseInt(this.bookId);
        } catch(NumberFormatException e) {
            throw new ValidationException(String.format("bookId = %s is not a number", 
                this.bookId));
        }
    }
}
