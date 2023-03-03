/**
 * This is the user detail repository.
 * 
 * @author Phann Malinka
 */
package library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import library.entities.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
}
