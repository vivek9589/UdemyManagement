package com.vivek.backend.Management.config;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Order(1)
public class SecurityConfig {


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;




@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                    // Public endpoints for register
                    .requestMatchers(HttpMethod.POST, "/user/register","/user/dto/register", "/user/login").permitAll() // allow POST to /users and /users/login


                    /*

                    // Permission based access  ---> instead of this --> we implement @PreAuthorize method annotations

                    .requestMatchers(HttpMethod.GET  ,"/user/**").hasAuthority(Permissions.UDEMY_READ.name())
                    .requestMatchers(HttpMethod.POST,"/user/**").hasAuthority(Permissions.UDEMY_WRITE.name())
                    .requestMatchers(HttpMethod.DELETE,"/user/**").hasAuthority(Permissions.UDEMY_DELETE.name())


                     */

                    /*
                    // Only Role Based access
                    .requestMatchers("/ADMIN/**").hasRole("ADMIN") // authorization
                    .requestMatchers(HttpMethod.POST, "/user/register", "/user/login") // allow POST to /users and /users/login
                    .permitAll()

                    */

                    .anyRequest().authenticated()

            )

            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
}




    // AuthenticationProvider

    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setUserDetailsService(userDetailsService);

        return provider;

    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}


