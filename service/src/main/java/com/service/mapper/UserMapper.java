package com.service.mapper;

import com.domain.User;
import com.dataaccess.model.UserEntity;
import com.service.UserService;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setFname(user.getFname());
        entity.setLname(user.getLname());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;

        if (entity.getFname() == null && entity.getLname() == null) {
            UserService us = new UserService();
            User full = us.getUserById(entity.getId());
            if (full != null) return full;
        }

        User user = new User();
        user.setId(entity.getId());
        user.setFname(entity.getFname());
        user.setLname(entity.getLname());
        user.setPhoneNumber(entity.getPhoneNumber());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        return user;
    }
}
