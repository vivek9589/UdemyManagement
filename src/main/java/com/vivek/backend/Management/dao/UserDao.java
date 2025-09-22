package com.vivek.backend.Management.dao;

import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.vo.UserVO;

import java.util.List;

public interface UserDao {


    // methods using VO -> UserVO
    List<UserVO> getAllUsers();
    UserVO getUserById(Long id);


    void save(User user);

    User delete(Long id);

    User findById(Long id);

    List<User> findAll();





}
