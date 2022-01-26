package account.security.userdetails.repository;


import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    ACCOUNTANT,
    ADMINISTRATOR,
    ANONYMOUS,
    AUDITOR,
    USER;

    public String getAuthority() {
        return "ROLE_" + name();
    }
}
