package com.company;

import com.company.model.User;

public class UserManager {

    private static UserManager instance = new UserManager();

    private User user;

    private UserManager() {}

    public static UserManager getInstance() {
        return instance;
    }

    public boolean isUserOnline() {
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
