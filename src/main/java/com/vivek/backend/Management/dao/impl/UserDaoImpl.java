package com.vivek.backend.Management.dao.impl;

import com.vivek.backend.Management.dao.UserDao;
import com.vivek.backend.Management.entity.User;
import com.vivek.backend.Management.vo.RecentUserVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.vivek.backend.Management.vo.UserVO;

import java.time.LocalDateTime;
import java.util.List;

// Use UserDaoImpl for custom queries:
// ✅ Why this matters: Keeps complex queries out of Repository, and returns VO directly.


@Repository
public class UserDaoImpl implements UserDao {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserVO> getAllUsers() {
        // SELECT new com.vivek.backend.Management.vo.UserVO(u.firstName, u.email, u.role.toString()) FROM User u
        String jpql = "SELECT new com.vivek.backend.Management.vo.UserVO(u.firstName, u.email, u.role.toString()) FROM User u";
        return entityManager.createQuery(jpql, UserVO.class).getResultList();
    }




    @Override
    public UserVO getUserById(Long id)
    {
        User user = entityManager.find(User.class,id);
        return null;
    }



    @Override
    public List<RecentUserVO> getUsersRegisteredInLast7Days() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        String jpql = "SELECT new com.vivek.backend.Management.vo.RecentUserVO(u.firstName, u.email, u.createdAt) " +
                "FROM User u WHERE u.createdAt >= :sevenDaysAgo AND u.role = 'USER'";
        return entityManager.createQuery(jpql, RecentUserVO.class)
                .setParameter("sevenDaysAgo", sevenDaysAgo)
                .getResultList();
    }






/*



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


*/



}
