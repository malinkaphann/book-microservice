/**
 * This is the book entity class.
 *
 * @author Phann Malinka
 */
package myapp.book.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import myapp.book.utils.CategoryEnum;
import myapp.book.validators.ValueOfEnum;

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
    String code;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "author", nullable = false)
    String author;

    @ValueOfEnum(enumClass = CategoryEnum.class, message = "must be one of NOVEL, STUDY, COMICS")
    @Column(name = "category", nullable = false)
    String category;

    @ValueOfEnum(enumClass = STATUS.class, message = "must be one of GOOD, OLD, DELETED")
    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "description")
    String description;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "books")
    List<User> users = new ArrayList<>();

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