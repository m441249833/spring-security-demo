package com.employee.demo.security.filter;

import com.employee.demo.model.User;
import com.employee.demo.repository.UserRepository;
import com.employee.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TokenAuthFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    @Autowired
    public TokenAuthFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Get the authorized user's authentications
        UsernamePasswordAuthenticationToken authRequest = getAuthtication(request);
        //If the authentication exists, give it to spring context.
        if (authRequest != null){
            SecurityContextHolder.getContext().setAuthentication(authRequest);
            chain.doFilter(request,response);
        }else {
            response.setStatus(401);
            response.getWriter().write("Access denied");
        }
    }


    /**
     * Parse token from request header and validate it.
     * @param request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthtication(HttpServletRequest request){
        // Get token from request header.
        String token = request.getHeader("Authorization");
        if (token != null){
            //Get the username from the jwt token
            String username = JwtUtil.getUsernameByToken(token);
            //Check the existence of the username.
            Optional<User> userTry = userRepository.findByUsername(username);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            if (userTry.isPresent()){
                User user = userTry.get();
                authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
                return new UsernamePasswordAuthenticationToken(user,token,authorities);
            }
        }
        return null;
    }
}
