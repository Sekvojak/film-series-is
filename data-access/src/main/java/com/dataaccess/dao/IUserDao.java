package com.dataaccess.dao;

import com.dataaccess.model.UserEntity;

import java.util.List;

public interface IUserDao {
    void save(UserEntity user);
    UserEntity getById(Long id);
    List<UserEntity> getAll();
    UserEntity findByEmail(String email);
}