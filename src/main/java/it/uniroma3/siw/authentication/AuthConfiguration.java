package it.uniroma3.siw.authentication;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import it.uniroma3.siw.model.Credentials;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)

public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;
    
   

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .authoritiesByUsernameQuery("SELECT email, role from credentials WHERE email=?")
                .usersByUsernameQuery("SELECT email, password, 1 as enabled FROM credentials WHERE email=?");
    }
    
    @Bean
     PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
     AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    

    @Bean
    @Order(1)
     SecurityFilterChain configure(HttpSecurity http) throws Exception {
    	
    	http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/**","/index", "/register", "/css/**", "/images/**", "favicon.ico", "/books/**", "/authors/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/register", "/register/librarian", "/login").permitAll()
                    .requestMatchers("/myProfile").authenticated()
                    .requestMatchers("/librarian/**").hasAuthority(Credentials.LIBRARIAN_ROLE)
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/myProfile", true)
                    .failureUrl("/login?error=true")
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
