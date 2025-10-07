package com.vivek.backend.Management.dao;

import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.vo.RecentUserVO;
import com.vivek.backend.Management.vo.UserVO;

import java.util.List;


// Use UserDaoImpl for custom queries:
// ✅ Why this matters: Keeps complex queries out of Repository, and returns VO directly.




public interface UserDao {


    // methods using VO -> UserVO
    List<UserVO> getAllUsers();
    UserVO getUserById(Long id);


    // get all the users registered in last 7 days with role USER
    List<RecentUserVO> getUsersRegisteredInLast7Days();




  /*


    void save(User user);

    User delete(Long id);

    User findById(Long id);

    List<User> findAll();

*/



}
