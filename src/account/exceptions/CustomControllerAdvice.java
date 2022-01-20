package account.exceptions;

import account.dtos.ErrorDto;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestControllerAdvice
public class CustomControllerAdvice extends ExceptionHandlerExceptionResolver {

    ResponseEntity<ErrorDto> handleException(RuntimeException ex, HttpStatus status) {
        return ResponseEntity
                .status(status.value())
                .body(
                        ErrorDto.builder()
                                .status(status.value())
                                .error(status.getReasonPhrase())
                                .timestamp(new Date())
                                .path(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(SignUpValidationException.class)
    ResponseEntity<ErrorDto> handleSignUpException(SignUpValidationException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationUserDoesntExist.class)
    ResponseEntity<ErrorDto> handleAuthenticationUserDoesntExistException(SignUpValidationException ex) {
        return handleException(ex, HttpStatus.FORBIDDEN);
    }

//    @ExceptionHandler(RuntimeException.class)
//    ResponseEntity<ErrorDto> handleSignUpException(RuntimeException ex) {
//        return handleException(ex, HttpStatus.BAD_REQUEST);
//    }
}
