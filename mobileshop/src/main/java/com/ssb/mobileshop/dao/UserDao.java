package com.ssb.mobileshop.dao;

import com.ssb.mobileshop.dao.UserDao;
import com.ssb.mobileshop.model.User;
import com.ssb.mobileshop.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserDao {
    private static UserDao userDaoImpl;

    // singleton instance for UserDao
    public static UserDao getInstance() {
        if (userDaoImpl == null) {
            userDaoImpl = new UserDao();
        }
        return userDaoImpl;
    }

    // Save User to Database

    public int save(User user) {
        Session session = HibernateUtil.createSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            session.beginTransaction().rollback();
            return 0;
        }
    }

    // Get user by Mobile Number

    public User findByMobileNumber(String mobile, String password) {
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery("from User where mobileNumber=:mobile and password=:password");
            query.setParameter("mobile", mobile);
            query.setParameter("password", password);
            User user = (User) query.uniqueResult();
            return user;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
