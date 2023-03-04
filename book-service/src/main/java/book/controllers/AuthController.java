/**
 * This file is for auth controller class.
 *
 * @author Phann Malinka
 */
package book.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import book.dto.ApiResponseDto;
import book.dto.DataApiResponseDto;
import book.dto.auth.LoginRequestDto;
import book.dto.auth.LoginResponseDto;
import book.dto.auth.ProfileResponseDto;
import book.dto.auth.SignupRequestDto;
import book.entities.User;
import book.services.AuthService;
import book.utils.AttributeUtil;
import book.utils.StatusEnum;

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
    final @RequestBody SignupRequestDto requestDto
  ) {
    logger.info("signup request dto = {}", requestDto);

    // validate
    requestDto.validate();

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
    final @RequestBody LoginRequestDto requestDto
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
