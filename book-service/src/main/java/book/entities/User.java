/**
 * This is a user entity class.
 * This user entity class should be small and only
 * contains the fields used to login.
 * Any more information should stay in the user detail entity.
 *
 * @author Phann Malinka
 */
package book.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "username", nullable = false)
  private String username;

  @ToString.Exclude
  @Column(name = "password", nullable = false)
  private String password;

  @ToString.Exclude
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private List<Role> roles = new ArrayList<>();

  @ToString.Exclude
  @ManyToMany
  @JoinTable(name = "user_hold_book", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
  private List<Book> books = new ArrayList<>();

  @ToString.Exclude
  @OneToOne(mappedBy = "user")
  private UserProfile profile;
  
  public User(String username, String password, List<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }
}
