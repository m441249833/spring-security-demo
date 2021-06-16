package com.employee.demo.security;

import com.employee.demo.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {

    private transient User userInfo;

    private List<GrantedAuthority> permissiomList;

    public SecurityUser(){

    }

    public SecurityUser(User user, List<GrantedAuthority> permissiomList){
        if (user != null){
            this.userInfo = user;
            this.permissiomList = permissiomList;
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissiomList;
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() { return userInfo.getUsername(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
