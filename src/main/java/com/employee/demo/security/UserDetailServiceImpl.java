package com.employee.demo.security;

import com.employee.demo.model.User;
import com.employee.demo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * invoked by attemptAuthentication , this is to access database for authentication.(with given passwordEncoder)
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userTry = userRepository.findByUsername(username);

        if(!userTry.isPresent()){
            throw new UsernameNotFoundException("User does not exist");
        }
        User user = userTry.get();
        List<GrantedAuthority> permissions = new ArrayList<>();
        permissions.add(new SimpleGrantedAuthority(user.getRole().getName()));
        SecurityUser securityUser = new SecurityUser(user,permissions);
        return securityUser;

    }
}
