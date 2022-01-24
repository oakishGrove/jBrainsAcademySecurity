package account.exceptionsmanagament;

import account.dtos.ErrorDto;
import account.exceptionsmanagament.exceptions.AuthenticationUserDoesntExist;
import account.exceptionsmanagament.exceptions.SignUpValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestControllerAdvice
public class CustomControllerAdvice extends ExceptionHandlerExceptionResolver {

    ResponseEntity<ErrorDto> handleException(RuntimeException ex, HttpStatus status, String path) {
        ex.printStackTrace();
        return ResponseEntity
                .status(status.value())
                .body(
                        ErrorDto.builder()
                                .status(status.value())
                                .error(status.getReasonPhrase())
                                .timestamp(new Date())
                                .path(path)
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(SignUpValidationException.class)
    ResponseEntity<ErrorDto> handleSignUpException(SignUpValidationException ex, HttpServletRequest request) {
        return handleException(ex, HttpStatus.BAD_REQUEST, request.getServletPath());
    }

    @ExceptionHandler(AuthenticationUserDoesntExist.class)
    ResponseEntity<ErrorDto> handleAuthenticationUserDoesntExistException(SignUpValidationException ex, HttpServletRequest request) {
        return handleException(ex, HttpStatus.FORBIDDEN, request.getServletPath());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var bindingResult = ex.getBindingResult();
        String msg = new String();
        for (var fieldError : bindingResult.getFieldErrors()) {
//            System.out.println(fieldError.getDefaultMessage());
            msg += fieldError.getDefaultMessage();
            System.out.println(request.getServletPath() + " " + msg + "\n  ");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorDto.builder()
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(msg)
                        .status(400)
                        .timestamp(new Date())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorDto> handleAccessDeniendException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN.value())
                .body(ErrorDto.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                        .timestamp(new Date())
                        .path(request.getServletPath())
                        .message("Access Denied!")
                        .build());
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorDto> handleRuntimeExceptions(RuntimeException ex, HttpServletRequest request) {
        System.out.println("DEFAULT RUNTIME EXCEPTION HANDLER CALLED + " + ex);
        return handleException(ex, HttpStatus.BAD_REQUEST, request.getServletPath());
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ErrorDto> handleResponseStatusEx(ResponseStatusException ex,HttpServletRequest request) {
        return ResponseEntity
                .status(ex.getStatus().value())
                .body(
                        ErrorDto.builder()
                                .status(ex.getStatus().value())
                                .error(ex.getStatus().getReasonPhrase())
                                .timestamp(new Date())
                                .path(request.getServletPath())
                                .message(ex.getReason())
                                .build()
                );
    }
}
