/**
 * This is the pagination dto.
 * 
 * @author Phann Malinka
 */
package myapp.book.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
public class PaginationDto<T> {
    int page;
    int size;
    int totalPage;
    long totalSize;

    @ToString.Exclude
    List<T> list;
}