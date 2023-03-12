package myapp.book.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import myapp.book.dto.ApiResponseDto;
import myapp.book.services.UserService;
import myapp.book.services.UserService.UserHoldBook;
import myapp.book.utils.AttributeUtil;
import myapp.book.utils.StatusEnum;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Hold a book for a user.
     *
     * @param userId the id of the user to hold the book
     * @param bookId the id of the book to be hold by the user
     * @return ApiResponseDto an api response dto
     */
    @PostMapping("/{userId}/book/{bookId}")
    public ResponseEntity<ApiResponseDto> hold(
        final @PathVariable int userId,        
        final @PathVariable int bookId) {

        logger.debug("requested to hold a book id = {} for user id = {}",
                bookId, userId);

        UserHoldBook userHoldBook = userService.hold(userId, bookId);

        ApiResponseDto response = new ApiResponseDto(
                StatusEnum.STATUS_SUCCESS.getValue(),
                String.format(
                        "user = %s successfully hold the book = ",
                        userHoldBook.getUser().getUsername(),
                        userHoldBook.getBook().getTitle()),
                MDC.get(AttributeUtil.REQUEST_ID));

        logger.debug("user hold book response dto = {}", response);

        return ResponseEntity.ok(response);
    }

    /**
     * Release a book from a user.
     *
     * @param userId the id of the user to hold the book
     * @param bookId the id of the book to be hold by the user
     * @return ApiResponseDto an api response dto
     */
    @DeleteMapping("/{userId}/book/{bookId}")
    public ResponseEntity<ApiResponseDto> unhold(
            final @PathVariable int userId,
            final @PathVariable int bookId
            ) {

        logger.debug("requested to release a book id = {} from user id = {}",
                bookId, userId);

        UserHoldBook userHoldBook = userService.unhold(userId, bookId);

        ApiResponseDto response = new ApiResponseDto(
                StatusEnum.STATUS_SUCCESS.getValue(),
                String.format(
                        "user = %s successfully released the book = %s",
                        userHoldBook.getUser().getUsername(), 
                        userHoldBook.getBook().getTitle()),
                MDC.get(AttributeUtil.REQUEST_ID));

        logger.debug("user unhold book response dto = {}", response);

        return ResponseEntity.ok(response);
    }
}
