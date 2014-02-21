package com.github.robi42.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ROLE_ADMIN = "ADMIN";

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // SPA paths
                .antMatchers("/", "/views/**", "/scripts/**", "/styles/**", "/images/**").permitAll()
                // API endpoints
                .antMatchers("/api/**").permitAll()
                // Admin endpoints (protected)
                .antMatchers("/admin/**").hasRole(ROLE_ADMIN)
                .anyRequest().authenticated();
        http.httpBasic().realmName("Admin");
        http.csrf().disable();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(adminUsername).password(adminPassword)
                .roles(ROLE_ADMIN);
    }
}
