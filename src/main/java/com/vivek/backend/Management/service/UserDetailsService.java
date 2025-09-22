package com.vivek.backend.Management.service;

import com.vivek.backend.Management.entity.User;




public interface UserDetailsService {

    public User findByUserName(String email);


}
