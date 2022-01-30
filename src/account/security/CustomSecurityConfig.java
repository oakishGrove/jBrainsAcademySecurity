package account.security;

//import account.security.failedlogin.CustomForbiddenExceptionHandler;
import account.security.failedlogin.CustomForbiddenExceptionHandler;
import account.security.failedlogin.LogingErrorLoggerConfigurer;
import account.security.failedlogin.RestAuthenticationEntryPoint;
import account.security.securityevents.SecurityEventsService;
import account.security.userdetails.repository.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@AllArgsConstructor
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final SecurityEventsService securityEventsService;
//    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                    .withUser("user1")
                    .password(passwordEncoder().encode("pass1"))
                    .roles()
                .and()
                    .withUser("user2")
                    .password(passwordEncoder().encode("pass2"))
                    .roles("USER")
                .and()
                    .withUser("user3")
                    .password(passwordEncoder().encode("pass3"))
                    .roles("ADMIN")
                .and()
                    .passwordEncoder(passwordEncoder());

        auth
                .userDetailsService(userDetailsService);
//        auth
//                .authenticationProvider(authenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
//            .addFilterAfter(customExceptionTranslationFilter(), ExceptionTranslationFilter.class)
//            .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)

            .httpBasic()
//                .authenticationEntryPoint(authenticationEntryPoint)
                .authenticationEntryPoint(authenticationEntryPoint())
            .and()
                .authorizeRequests()
                .mvcMatchers("api/admin/user/**").hasRole(RoleEnum.ADMINISTRATOR.name())
                .mvcMatchers("api/security/events").hasRole(RoleEnum.AUDITOR.name())
                .mvcMatchers(HttpMethod.POST, "api/auth/changepass")
                    .hasAnyRole(
                            RoleEnum.ADMINISTRATOR.name(),
                            RoleEnum.ACCOUNTANT.name(),
                            RoleEnum.USER.name())
                .mvcMatchers(HttpMethod.POST, "api/auth/signup").permitAll()
                .mvcMatchers(HttpMethod.POST, "actuator/shutdown").permitAll()
                .anyRequest().authenticated()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .exceptionHandling()
                    .accessDeniedHandler(customAccessDeniendHandler())
//                    .defaultAccessDeniedHandlerFor(customAccessDeniendHandler(), new AntPathRequestMatcher("**"))
        ;
//            .and()
//                .apply(loginErrorLoggerConfigurer());
//                .accessDeniedHandler(customAccessDeniendHandler());

        httpSecurity.
                csrf().disable().headers().frameOptions().disable();

//        http.authorizeRequests()
//                .mvcMatchers("/admin").hasRole("ADMIN")
//                .mvcMatchers("/user").hasAnyRole("ADMIN", "USER")
//                .mvcMatchers("/", "/public").permitAll()
//                .mvcMatchers("/**").authenticated() // or .anyRequest().authenticated()
//                .and().httpBasic();
    }

    public LogingErrorLoggerConfigurer loginErrorLoggerConfigurer() {
        return new LogingErrorLoggerConfigurer();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AccessDeniedHandler customAccessDeniendHandler () {
        return new CustomForbiddenExceptionHandler(securityEventsService);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint(securityEventsService, basicAuthenticationConverter());
    }

    @Bean
    public CustomExceptionTranslationFilter customExceptionTranslationFilter() {
//        return new CustomExceptionTranslationFilter(authenticationEntryPoint, securityEventsService);
        return new CustomExceptionTranslationFilter(authenticationEntryPoint(), securityEventsService);
    }

//    @Bean
//    public FilterSecurityInterceptor customFilterSecurityInterceptor() {
//        return new CustomFilterSecurityInterceptor();
//    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        var authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        return authenticationProvider;
//    }

    @Bean
    public BasicAuthenticationConverter basicAuthenticationConverter() {
        return new BasicAuthenticationConverter();
    }
}
