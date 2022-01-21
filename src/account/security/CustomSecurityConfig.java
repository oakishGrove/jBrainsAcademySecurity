package account.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.attribute.UserPrincipal;

@EnableWebSecurity
@AllArgsConstructor
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

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
            .httpBasic()
            .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "api/auth/signup").permitAll()
                .mvcMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
                .anyRequest().authenticated()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.
                csrf().disable().headers().frameOptions().disable();

//        http.authorizeRequests()
//                .mvcMatchers("/admin").hasRole("ADMIN")
//                .mvcMatchers("/user").hasAnyRole("ADMIN", "USER")
//                .mvcMatchers("/", "/public").permitAll()
//                .mvcMatchers("/**").authenticated() // or .anyRequest().authenticated()
//                .and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        var authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        return authenticationProvider;
//    }
}
