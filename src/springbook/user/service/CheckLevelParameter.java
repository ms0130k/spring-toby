package springbook.user.service;

import springbook.user.domain.User;

public class CheckLevelParameter {
    public User user;

    public CheckLevelParameter(User user) {
        this.user = user;
    }
}