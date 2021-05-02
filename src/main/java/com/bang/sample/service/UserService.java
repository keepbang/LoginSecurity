package com.bang.sample.service;

import com.bang.sample.model.User;

public interface UserService {
    Long save(User user, boolean isOwner);

    User login(User user);
}
