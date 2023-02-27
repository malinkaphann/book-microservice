/**
 * This is the book entity class.
 *
 * @author Phann Malinka
 */
package library.entities;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book", schema = "public")
public class Book extends BaseEntity<Integer> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    private Set<User> users;

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