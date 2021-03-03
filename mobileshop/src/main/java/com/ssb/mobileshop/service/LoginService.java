package com.ssb.mobileshop.service;

import com.ssb.mobileshop.dao.UserDao;
import com.ssb.mobileshop.model.User;

public class LoginService {
    private static LoginService loginService;

    public static LoginService getInstance() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

    public User loginValidation(String mobileNumber, String password) {
        User user = UserDao.getInstance().findByMobileNumber(mobileNumber, password);
        if (user.getMobileNumber().equals(mobileNumber) && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }
}
