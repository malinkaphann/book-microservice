/**
 * This is the pagination dto.
 * 
 * @author Phann Malinka
 */
package book.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto<T> {
    int page;
    int size;
    int totalPage;
    long totalSize;

    @ToString.Exclude
    List<T> list;
}