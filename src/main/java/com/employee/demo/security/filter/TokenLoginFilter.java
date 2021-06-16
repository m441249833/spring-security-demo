package com.employee.demo.security.filter;

import com.employee.demo.model.User;
import com.employee.demo.repository.UserRepository;
import com.employee.demo.security.SecurityUser;
import com.employee.demo.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    public TokenLoginFilter(AuthenticationManager authenticationManager, UserRepository userRepository){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
    }


    //Get user information and attempt username password authentication.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            if (userRepository.findByUsername(user.getUsername()).isPresent()){
                return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername()
                                ,user.getPassword()
                                ,new ArrayList<>()
                        )
                );
            }else {
                new ObjectMapper().writeValue(response.getWriter(),"user "+user.getUsername()+" does not exist");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return null;
    }

    //What to do on successful Authentication
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        //Get the successful authenticated user information;
        SecurityUser user = (SecurityUser) authResult.getPrincipal();

        //use customized jwt signer to create a token;
        String token = JwtUtil.sign(user);

        response.setStatus(HttpStatus.OK.value());
        JsonObject json = new JsonObject();
        json.addProperty("TOKEN",token);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(json.toString());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write("{\"message\" : \"Login Failed.\"}");
    }
}
