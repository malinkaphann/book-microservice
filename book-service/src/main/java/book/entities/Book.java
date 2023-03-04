/**
 * This is the book entity class.
 *
 * @author Phann Malinka
 */
package book.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "book", schema = "public")
public class Book extends BaseEntity {

    public enum STATUS {
        GOOD,    // default
        OLD,
        DELETED
    };

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "category", nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private STATUS status;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "books")
    private List<User> users = new ArrayList<>();

    public Book(String code, String title, String author, String category,
        STATUS status, String description) {
        this.code = code;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.description = description;
    }
}