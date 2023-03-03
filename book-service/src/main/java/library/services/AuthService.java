/**
 * This service class is used to do all business logic related to user.
 *
 * @author Phann Malinka
 */
package library.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import library.dto.auth.LoginRequestDto;
import library.dto.auth.LoginResponseDto;
import library.dto.auth.ProfileResponseDto;
import library.dto.auth.SignupRequestDto;
import library.entities.Role;
import library.entities.User;
import library.entities.UserProfile;
import library.exceptions.*;
import library.repositories.RoleRepository;
import library.repositories.UserProfileRepository;
import library.repositories.UserRepository;
import library.security.JwtTokenUtil;
import library.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private UserProfileRepository userProfileRepo;

  @Autowired
  private RoleRepository roleRepo;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  private final Logger logger = LoggerFactory.getLogger(AuthService.class);

  /**
   * This function is used to validate the sign up request.
   * Before running an SQL query, the request dto must be validated.
   * This also helps the database server from running
   * the unnecessary queries.
   *
   * @param requestDto
   * @exception NullPointerException        when the given request is null
   * @exception ValidationException         when the validation fails
   * @exception ResourceDuplicatedException when the username is already taken
   */
  private void validateSignupRequest(SignupRequestDto requestDto) {
    Objects.requireNonNull(
        requestDto,
        "the input request dto must not be null");

    // validate the request
    requestDto.validate();

    // check if the username is already taken
    if (this.userRepo.existsByUsername(requestDto.getUsername())) {
      throw new ResourceDuplicatedException(
          String.format("username = %s already taken", requestDto.getUsername()));
    }

    // check if that student already registered
    if ((requestDto.getStudentId() != null) &&
        (this.userProfileRepo.existsByStudentId(requestDto.getStudentId()))) {
      throw new ResourceDuplicatedException(
          String.format(
              "studentId = %s already registered",
              requestDto.getStudentId()));
    }

    // check if that email is already there
    if (this.userProfileRepo.existsByEmail(requestDto.getEmail())) {
      throw new ResourceDuplicatedException(
          String.format("email = %s already registered"));
    }
  }

  /**
   * Sign up a user
   *
   * @param requestDto a signup request dto
   * @return user a newly created user
   */
  public User signup(SignupRequestDto requestDto) {
    logger.debug("signup request dto = {}", requestDto);

    // validate
    this.validateSignupRequest(requestDto);

    // work with the given roles
    List<Role> roles = new ArrayList<>();
    String[] roleArray = requestDto.getRoles().split(",");

    for (String roleName : roleArray) {
      Role role = roleRepo
          .findByName(roleName)
          .orElseThrow(() -> new ResourceNotFoundException(
              String.format("role = %s is not found", roleName)));

      roles.add(role);
      logger.debug("added role = {}", role.getName());
    }

    try {
      // create new user object
      User user = new User(
          requestDto.getUsername(),
          encoder.encode(requestDto.getPassword()),
          roles);

      // save user
      user = userRepo.save(user);

      UserProfile profile = new UserProfile(
          requestDto.getName(),
          requestDto.getPhone(),
          requestDto.getEmail(),
          requestDto.getStudentId(),
          user);

      // save user profile
      userProfileRepo.save(profile);

      logger.debug(
          "recently created user = {}", user);

      return user;
    } catch (Exception e) {
      throw new DatabaseException(e);
    }
  }

  /**
   * Login a user
   *
   * @param requestDto a login request dto
   * @return LoginResponseDto a login response dto
   */
  public LoginResponseDto login(LoginRequestDto requestDto) {
    logger.debug("login request dto = {}", requestDto);

    // validate
    requestDto.validate();

    // authenticate the user
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            requestDto.getUsername(),
            requestDto.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // get user profile
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // generate token
    String token = jwtTokenUtil.generateToken(userDetails);

    User user = userRepo
        .findById(userDetails.getId())
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("user id = %d is not found", userDetails.getId())));

    logger.debug("user who just logged in = {}", user);

    return new LoginResponseDto(token, user.getProfile());
  }

  /**
   * Get own profile
   *
   * @return ProfileResponseDto
   */
  public ProfileResponseDto profile(Authentication authentication) {
    Objects.requireNonNull(
        authentication,
        "the input authentication object must not be null");

    String username = authentication.getName();
    if (username == null) {
      throw new GeneralException(
          "there is no username in the spring boot authentication object");
    }

    User user = userRepo
        .findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("user by username = %s is not found", username)));

    UserProfile profile = user.getProfile();

    ProfileResponseDto responseDto = new ProfileResponseDto(
        username,
        profile != null ? profile.getName() : "",
        profile != null ? profile.getPhone() : "",
        profile != null ? profile.getEmail() : "",
        profile != null ? profile.getPhoto() : "",
        profile != null ? profile.getStudentId() : "");

    return responseDto;
  }
}
