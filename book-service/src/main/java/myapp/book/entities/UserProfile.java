/**
 * This is the user detail entity.
 * Any detail related to a user should be put here.
 * Those details are general and not really specific.
 * 
 * @author Phann Malinka
 */
package myapp.book.entities;

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
@Table(name = "user_profile", schema = "public")
public class UserProfile extends BaseEntity {
    
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "photo", nullable = true)
    private String photo;

    @Column(name = "email", nullable = false)
    private String email;

    public UserProfile(String name, String phone, String email, String studentId, User user) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.studentId = studentId;
        this.user = user;
      }
  }