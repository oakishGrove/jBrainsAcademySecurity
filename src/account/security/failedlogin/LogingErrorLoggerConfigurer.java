package account.security.failedlogin;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class LogingErrorLoggerConfigurer extends AbstractHttpConfigurer<LogingErrorLoggerConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity httpSecurity) throws Exception {
        super.init(httpSecurity);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(
                new LogingErrorLoggerFilter(),
                FilterSecurityInterceptor.class);
    }
}
