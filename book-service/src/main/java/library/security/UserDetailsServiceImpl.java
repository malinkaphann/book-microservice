/**
 * This is how to give the spring security the detail of a user.
 * 
 * @author Phann Malinka
 */

package library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import library.entities.User;
import library.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                String.format("username = %s is not found", username)
            ));

        return UserDetailsImpl.build(user);
    }
}
