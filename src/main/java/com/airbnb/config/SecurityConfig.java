package com.airbnb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class); // it will run our custom filtered method(JWTRequestFilter) first
        http.authorizeHttpRequests().anyRequest().permitAll();
            //    requestMatchers("api/v1/users/addUser","/api/v1/users/login")  //its keeping these 2 URL open
              //  .permitAll()
                //.requestMatchers("api/v1/countries/addCountry").hasRole("ADMIN")
                //.requestMatchers("api/v1/users/profile").hasAnyRole("ADMIN","USER")
                //.anyRequest().authenticated();//it is securing other uRLs

        return http.build();
  /* build is a method that helps us to build the object , so here the object is http ,
 the build method will  put all that information  into that object and
 it returns back as a securityFilterChain then goes to a spring security framework and ark this with @Bean so that
  automatically when the project starts the configuration will run ,  */

    }

}
