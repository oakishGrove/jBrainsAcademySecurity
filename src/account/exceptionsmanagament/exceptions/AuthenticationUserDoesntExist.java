package account.exceptionsmanagament.exceptions;

public class AuthenticationUserDoesntExist extends RuntimeException {

    public AuthenticationUserDoesntExist(String s) {
        super(s);
    }
}
