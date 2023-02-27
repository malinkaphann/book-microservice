/**
 * This is the pagination dto.
 * 
 * @author Phann Malinka
 */
package library.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto<T> {
    private int page;
    private int size;
    private int totalPage;
    private long totalSize;
    private List<T> list;

    @Override
    public String toString() {
        return "PaginationDto(page=" + page + ", size=" + size + ", totalPage=" + totalPage + ", totalSize="
                + totalSize + ")";
    }
}