package com.vivek.backend.Management.entity;

import com.vivek.backend.Management.enums.Permissions;
import com.vivek.backend.Management.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class UserPrincipal implements UserDetails {


    @Autowired
    private User user;

    @Autowired
    private Role role;

    public UserPrincipal(User user) {
        this.user = user;
    }



    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     return Collections.singleton(new SimpleGrantedAuthority("USER"));
    // }

    /*
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

         // return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));


        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Add role
         authorities.add(new SimpleGrantedAuthority("ROLE_"+ Role.ADMIN.name())); // USER
        //authorities.add(new SimpleGrantedAuthority("ROLE_"+ "USER"));

        // Add Pemissions                                           // USER
        Set<SimpleGrantedAuthority> premissionsAuthorities = Role.ADMIN.getPermissions().stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.name()))
                .collect(Collectors.toSet());



        authorities.addAll(premissionsAuthorities);
        return authorities;

    }

     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // Add role
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // Add permissions
        Set<SimpleGrantedAuthority> permissionsAuthorities = user.getRole().getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());

        authorities.addAll(permissionsAuthorities);
        return authorities;
    }


    @Override
    public String getPassword() {
        //return user.getPassword();
        return "{noop}" + user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

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
