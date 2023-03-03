/**
 * This file is for auth controller class.
 * 
 * @author Phann Malinka
 */
package library.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import library.dto.ApiResponseDto;
import library.dto.DataApiResponseDto;
import library.dto.auth.LoginRequestDto;
import library.dto.auth.LoginResponseDto;
import library.dto.auth.ProfileResponseDto;
import library.dto.auth.SignupRequestDto;
import library.services.AuthService;
import library.utils.AttributeUtil;
import library.utils.StatusEnum;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * Signup a user
     * 
     * @param signupRequestDto a signup request dto
     * @return ApiResponseDto an api response dto
     */
    @PostMapping(value = "/signup")
    public ResponseEntity<ApiResponseDto> signup(
        final @RequestBody SignupRequestDto requestDto
    ) {
        // debug log
        logger.debug("signup request dto = {}", requestDto);

         // validate
        requestDto.validate();

        // create user
        authService.signup(requestDto);

        // build response object
        ApiResponseDto response = new ApiResponseDto(
            StatusEnum.STATUS_SUCCESS.getValue(),
                String.format("user = %s is created successfully", 
                    requestDto.getUsername()),
                    MDC.get(AttributeUtil.REQUEST_ID));

        logger.debug("sign up response dto = {}", response);

        return ResponseEntity.ok(response);
    }

    /**
     * Login a user
     * 
     * @param requestId a request id
     * @param loginRequestDto a login request dto
     * @return LoginResponseDto a login response dto
     */
    @PostMapping(value = "/login")
    public ResponseEntity<DataApiResponseDto<LoginResponseDto>> login(
        final @RequestBody LoginRequestDto requestDto
    ) {
        // debug log
        logger.debug("login request dto = {}", requestDto);

        // build response
        DataApiResponseDto<LoginResponseDto> response = new DataApiResponseDto<>(
            StatusEnum.STATUS_SUCCESS.getValue(),
            "login successfully",
            MDC.get(AttributeUtil.REQUEST_ID),
            authService.login(requestDto)
        );

        logger.debug("login response dto = {}", response);
        
        return ResponseEntity.ok(response);
    }

        /**
     * Login a user
     * 
     * @param requestId a request id
     * @param loginRequestDto a login request dto
     * @return LoginResponseDto a login response dto
     */
    @GetMapping(value = "/profile")
    public ResponseEntity<DataApiResponseDto<ProfileResponseDto>> profile(
        Authentication authentication
    ) {
        DataApiResponseDto<ProfileResponseDto> response = new DataApiResponseDto<>(
            StatusEnum.STATUS_SUCCESS.getValue(),
            "successfully fetch the own profile",
            MDC.get(AttributeUtil.REQUEST_ID),
            authService.profile(authentication)
        );

        return ResponseEntity.ok(response);
    }

}
