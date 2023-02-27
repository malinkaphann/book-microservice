/**
 * This is a user entity class.
 * This user entity class should be small and only
 * contains the fields used to login.
 * Any more information should stay in the user detail entity.
 *
 * @author Phann Malinka
 */
package library.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User extends BaseEntity<Integer> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles = new HashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_hold_book", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"))
  private Set<Book> books = new HashSet<>();

  @OneToOne(mappedBy = "user")
  private UserProfile profile;
  
  public User(String username, String password, Set<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }
}
