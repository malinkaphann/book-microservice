package myapp.book.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import myapp.book.utils.ValidationUtil;

@Data
public class FilterDto {

    @NotEmpty(message = "column is requied")
    @Size(max = ValidationUtil.MAX_LEN_COLUMN, message = "the column = {validatedValue} must be shorter than {max} characters long")
    String column;

    @NotEmpty(message = "value is requied")
    @Size(max = ValidationUtil.MAX_LEN_COLUMN, message = "the value = {validatedValue} must be shorter than {max} characters long")
    String value;

    @Min(value = 1, message = "the page = {validatedValue} must be greater than or equal to {value}")
    @Max(value = 100, message = "the page = {validatedValue} must be less than or equal to {value}")
    int page = 1;

    @Min(value = 1, message = "the size = {validatedValue} must be greater than {value}")
    @Max(value = 10, message = "the size = {validatedValue} must be less than or equal to {value}")
    int size = 10;
}
