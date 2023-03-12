/**
 * This is for auth service.
 *
 * @author Phann Malinka
 */
package myapp.book.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import myapp.book.dto.auth.LoginRequestDto;
import myapp.book.dto.auth.LoginResponseDto;
import myapp.book.dto.auth.ProfileResponseDto;
import myapp.book.dto.auth.SignupRequestDto;
import myapp.book.entities.Role;
import myapp.book.entities.User;
import myapp.book.entities.UserProfile;
import myapp.book.exceptions.*;
import myapp.book.repositories.RoleRepository;
import myapp.book.repositories.UserProfileRepository;
import myapp.book.repositories.UserRepository;
import myapp.book.security.JwtTokenUtil;
import myapp.book.security.UserDetailsImpl;

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
   * @throws NullPointerException        when the given request is null
   * @throws ValidationException         when the validation fails
   * @throws ResourceDuplicatedException when username, studentId, email was already taken
   */
  private void validateSignupRequest(SignupRequestDto requestDto) {
    Objects.requireNonNull(
      requestDto,
      "the input request dto must not be null"
    );
    logger.info("started validating the sign up request = {}", requestDto);

    // check if the username is already taken
    if (userRepo.existsByUsername(requestDto.getUsername())) {
      throw new ResourceDuplicatedException(
        String.format("username = %s already taken", requestDto.getUsername())
      );
    }
    logger.debug("username = {} is ok to use", requestDto.getUsername());

    // check if that student already registered
    if (
      (requestDto.getStudentId() != null) &&
      (this.userProfileRepo.existsByStudentId(requestDto.getStudentId()))
    ) {
      throw new ResourceDuplicatedException(
        String.format(
          "studentId = %s already registered",
          requestDto.getStudentId()
        )
      );
    }
    logger.debug(
      "student with id = {} is ok to use",
      requestDto.getStudentId()
    );

    // check if that email is already there
    if (this.userProfileRepo.existsByEmail(requestDto.getEmail())) {
      throw new ResourceDuplicatedException(
        String.format("email = %s already registered")
      );
    }
    logger.debug("email = %s is ok to use", requestDto.getEmail());

    logger.info("done validating the sign up request");
  }

  /**
   * Sign up a user
   *
   * @param requestDto a signup request dto
   * @return user a newly created user
   * @throws NullPointerException when the given request dto is null
   * @throws ResourceNotFoundException when the given role is not found
   * @throws DatabaseException when error from database
   */
  public User signup(SignupRequestDto requestDto) {
    Objects.requireNonNull(
      requestDto,
      "the input request dto must not be null"
    );

    logger.info(
      "started creating a new user with request dto = {}",
      requestDto
    );

    // validate
    this.validateSignupRequest(requestDto);

    // work with the given roles
    List<Role> roles = new ArrayList<>();

    logger.debug("roles from the request dto = {}", requestDto.getRoles());

    for (Role roleFromDto : requestDto.getRoles()) {
      Role role = roleRepo
        .findByName(roleFromDto.getName())
        .orElseThrow(() ->
          new ResourceNotFoundException(
            String.format("role = %s is not found", roleFromDto.getName())
          )
        );
      roles.add(role);
      logger.debug("role to be assigned to new user = {}", role.getName());
    }

    logger.debug("new user will be created having these roles = {}", roles);

    // create new user object
    User user = new User(
      requestDto.getUsername(),
      encoder.encode(requestDto.getPassword()),
      roles
    );

    User createdUser;

    // create new user
    try {
      createdUser = userRepo.save(user);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("new user just created = {}", createdUser);

    UserProfile profile = new UserProfile(
      requestDto.getName(),
      requestDto.getPhone(),
      requestDto.getEmail(),
      requestDto.getStudentId(),
      user
    );

    UserProfile createdProfile;

    // create user profile
    try {
      createdProfile = userProfileRepo.save(profile);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("new user profile just created = {}", createdProfile);

    logger.info("done creating user = {}", createdUser.getUsername());

    return user;
  }

  /**
   * Login a user
   *
   * @param requestDto a login request dto
   * @return LoginResponseDto a login response dto
   * @throws NullPointerException when the given request dto is null
   * @throws ResourceNotFoundException when the calculated user id is not found in database
   */
  public LoginResponseDto login(LoginRequestDto requestDto) {
    Objects.requireNonNull(requestDto, "the input request must not be null");

    logger.info(
      "started to login a user by processing a request dto = {}",
      requestDto
    );

    // authenticate the user
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        requestDto.getUsername(),
        requestDto.getPassword()
      )
    );
    logger.debug(
      "login passed with authentication = {}",
      authentication.getDetails()
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // get user profile
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    // generate token
    String token = jwtTokenUtil.generateToken(userDetails);
    logger.debug("a token is generated");

    User user = userRepo
      .findById(userDetails.getId())
      .orElseThrow(() ->
        new ResourceNotFoundException(
          String.format("user id = %d is not found", userDetails.getId())
        )
      );
    logger.debug("user login profile = {}", user.getProfile());

    logger.info("logged in user = {}", user);

    return new LoginResponseDto(token, user.getProfile());
  }

  /**
   * Get own profile
   *
   * @param authentication the authentication object created by spring security
   * @return ProfileResponseDto
   * @throws NullPointerException when the given authentication is null
   * @throws GeneralException when there is no username in that authentication object
   * @throws ResourceNotFoundException when that username is not found in database
   */
  public ProfileResponseDto profile(Authentication authentication) {
    Objects.requireNonNull(
      authentication,
      "the input authentication object must not be null"
    );

    logger.info(
      "started to fetch the profile of the logged-in user " +
      "by processing the authentication object = {}",
      authentication.getDetails()
    );

    String username = authentication.getName();
    if (username == null) {
      throw new GeneralException(
        "there is no username in the spring boot authentication object"
      );
    }

    User user = userRepo
      .findByUsername(username)
      .orElseThrow(() ->
        new ResourceNotFoundException(
          String.format("user by username = %s is not found", username)
        )
      );
    logger.debug("found user in database = {}", user);

    UserProfile profile = user.getProfile();
    logger.debug("found user profile = {}", profile);

    ProfileResponseDto responseDto = new ProfileResponseDto(
      username,
      profile != null ? profile.getName() : "",
      profile != null ? profile.getPhone() : "",
      profile != null ? profile.getEmail() : "",
      profile != null ? profile.getPhoto() : "",
      profile != null ? profile.getStudentId() : ""
    );

    logger.info("done fetching for the profile of logged-in user = {}", 
      responseDto);
    return responseDto;
  }
}
