/**
 * This is the book entity class.
 *
 * @author Phann Malinka
 */
package library.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Book extends BaseEntity<Integer> {

    private Integer id;
    private String code;
    private String title;
    private String author;
    private String category;
    private String status;
    private String description;

    public Book(String code, String title, String author, String category,
        String status, String description) {
        this.code = code;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.description = description;
    }
}