package com.service.session;

import com.domain.User;

public class UserSession {

    private static User loggedUser;

    public static void login(User user) {
        loggedUser = user;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void logout() {
        loggedUser = null;
    }
}
