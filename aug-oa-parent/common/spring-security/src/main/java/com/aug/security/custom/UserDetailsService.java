package com.aug.security.custom;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author querkecor
 * @date 2023/8/18
 */

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public List<SimpleGrantedAuthority> getAuthList(Long userId);
}
