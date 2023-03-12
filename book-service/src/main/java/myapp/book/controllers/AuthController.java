/**
 * This file is for auth controller class.
 *
 * @author Phann Malinka
 */
package myapp.book.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import myapp.book.dto.ApiResponseDto;
import myapp.book.dto.DataApiResponseDto;
import myapp.book.dto.auth.LoginRequestDto;
import myapp.book.dto.auth.LoginResponseDto;
import myapp.book.dto.auth.ProfileResponseDto;
import myapp.book.dto.auth.SignupRequestDto;
import myapp.book.entities.User;
import myapp.book.services.AuthService;
import myapp.book.utils.AttributeUtil;
import myapp.book.utils.StatusEnum;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  private final Logger logger = LoggerFactory.getLogger(AuthController.class);

  /**
   * Signup a user
   *
   * @param requestDto a signup request dto
   * @return ApiResponseDto an api response dto
   */
  @PostMapping(value = "/signup")
  public ResponseEntity<ApiResponseDto> signup(
    final @Validated @RequestBody SignupRequestDto requestDto
  ) {
    logger.info("signup request dto = {}", requestDto);

    // validate
    //requestDto.validate();

    // create user
    User user = authService.signup(requestDto);

    logger.debug("user just created = {}", user);

    // build response object
    ApiResponseDto response = new ApiResponseDto(
      StatusEnum.STATUS_SUCCESS.getValue(),
      String.format(
        "user = %s is created successfully",
        requestDto.getUsername()
      ),
      MDC.get(AttributeUtil.REQUEST_ID)
    );

    logger.info("sign up response dto = {}", response);
    return ResponseEntity.ok(response);
  }

  /**
   * Login a user
   *
   * @param requestDto a login request dto
   * @return LoginResponseDto a login response dto
   */
  @PostMapping(value = "/login")
  public ResponseEntity<DataApiResponseDto<LoginResponseDto>> login(
    final @Validated @RequestBody LoginRequestDto requestDto
  ) {
    logger.info("login request dto = {}", requestDto);

    // build response
    DataApiResponseDto<LoginResponseDto> response = new DataApiResponseDto<>(
      StatusEnum.STATUS_SUCCESS.getValue(),
      "login successfully",
      MDC.get(AttributeUtil.REQUEST_ID),
      authService.login(requestDto)
    );

    logger.info("login response dto = {}", response);

    return ResponseEntity.ok(response);
  }

  /**
   * Get own profile
   *
   * @param loginRequestDto a login request dto
   * @return LoginResponseDto a login response dto
   */
  @GetMapping(value = "/profile")
  public ResponseEntity<DataApiResponseDto<ProfileResponseDto>> profile(
    Authentication authentication
  ) {
    logger.info("requested to get own profile");
    logger.debug("authentication = {}", authentication.getDetails());

    DataApiResponseDto<ProfileResponseDto> response = new DataApiResponseDto<>(
      StatusEnum.STATUS_SUCCESS.getValue(),
      "successfully fetch the own profile",
      MDC.get(AttributeUtil.REQUEST_ID),
      authService.profile(authentication)
    );

    logger.info("response dto = {}", response);
    return ResponseEntity.ok(response);
  }
}
