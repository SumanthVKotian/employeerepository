package com.instagramclone.security;

import com.instagramclone.entity.user.Role;
import com.instagramclone.entity.user.User;
import com.instagramclone.respository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailOrPhoneNumber) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmailOrPhoneNumber(usernameOrEmailOrPhoneNumber,usernameOrEmailOrPhoneNumber,usernameOrEmailOrPhoneNumber)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with " + usernameOrEmailOrPhoneNumber));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),mapRolesToGrantedAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToGrantedAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleType())).collect(Collectors.toList());
    }
}
