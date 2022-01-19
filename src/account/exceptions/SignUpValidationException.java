package account.exceptions;

public class SignUpValidationException extends RuntimeException {
    public SignUpValidationException(String s) {
        super(s);
    }
}
