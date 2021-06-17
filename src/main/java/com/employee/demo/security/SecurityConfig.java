package com.employee.demo.security;

import com.employee.demo.repository.UserRepository;
import com.employee.demo.security.filter.TokenAuthFilter;
import com.employee.demo.security.filter.TokenLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .exceptionHandling()
                .authenticationEntryPoint(new UnAuthEntryPoint())
                .and().csrf().disable().cors().and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(new TokenLoginFilter(this.authenticationManager(),userRepository))
                .addFilter(new TokenAuthFilter(this.authenticationManager(),userRepository))
                .httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //Ignoring the authentication of forget password service.
        web.ignoring().antMatchers("/user/signup").antMatchers(HttpMethod.DELETE,"/**");
    }

    /**
     * add config on auth.
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
