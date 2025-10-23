package com.vivek.backend.Management.service.impl;

import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.entity.UserPrincipal;
import com.vivek.backend.Management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    private static final Logger logger =  LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        logger.info("Loading User by email {}",email);

        User user = userRepository.findByEmail(email);

        if(user == null) {

            logger.warn("User not found with Email : {}",user.getEmail());
            throw new UsernameNotFoundException("user not found with this email ");
        }

        logger.info("user Found  with this email :{}",user.getEmail());

        return new UserPrincipal(user);



    }

}
