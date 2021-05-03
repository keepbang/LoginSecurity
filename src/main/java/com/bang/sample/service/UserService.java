package com.bang.sample.service;

import com.bang.sample.model.User;

public interface UserService {
    Long save(User user, boolean isOwner);

    String login(User user);
}
