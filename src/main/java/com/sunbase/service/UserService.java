package com.sunbase.service;

import com.sunbase.entity.User;

public interface UserService {

    User getUserByUsername(String username);

    User createUser(User user);
}
