package com.ssb.mobileshop.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

import com.ssb.mobileshop.model.Phone;
import com.ssb.mobileshop.model.Role;
import com.ssb.mobileshop.model.User;

/**
 * HibernateUtil is used to connect with database it opens the session
 * 
 * @author Pradeep
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory = createSessionFactory();

    /**
     * creating the session factory according to given properties
     * 
     * @return SessionFactory
     * @throws HibernateException
     */
    private static SessionFactory createSessionFactory() throws HibernateException {
        try {
            Properties properties = new Properties();
            properties.setProperty("hibernate.connection.url",
                    "jdbc:mysql://localhost:3306/emp?createDatabaseIfNotExist=TRUE");
            properties.setProperty("hibernate.connection.username", "kishore");
            properties.setProperty("hibernate.connection.password", "kishore");
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            properties.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            properties.setProperty("hibernate.hbm2ddl.auto", "update");
            properties.setProperty("hibernate.show_sql", "true");

            Configuration configure = new Configuration().addProperties(properties).addAnnotatedClass(User.class)
                    .addAnnotatedClass(Role.class).addAnnotatedClass(Phone.class);
            sessionFactory = configure.buildSessionFactory();
            System.out.println("sessionFactory: " + sessionFactory);
            return sessionFactory;

        } catch (Throwable ex) {
            ex.printStackTrace();

            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * createSession is returning a session
     * 
     * @return Session
     */
    public static Session createSession() {
        return sessionFactory.openSession();
    }
}
