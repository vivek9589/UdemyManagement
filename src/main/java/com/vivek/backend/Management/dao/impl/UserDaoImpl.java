package com.vivek.backend.Management.dao.impl;

import com.vivek.backend.Management.dao.UserDao;
import com.vivek.backend.Management.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import com.vivek.backend.Management.vo.UserVO;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {


    private EntityManager entityManager;


    @Override
   public List<UserVO> getAllUsers()
    {
        return null;
    }


    @Override
    public UserVO getUserById(Long id)
    {
        User user = entityManager.find(User.class,id);
        return null;
    }





    public void save(User user)
    {
         entityManager.persist(user);
    }

    public User delete(Long id)
    {
        User user = findById(id);
        entityManager.remove(user);
        return user;
    }

    public User findById(Long id)
    {
        return entityManager.find(User.class,id);
    }

    public List<User> findAll()
    {
        return entityManager.createQuery("from User",User.class).getResultList();

    }





}
