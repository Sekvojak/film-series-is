package com.service;

import com.dataaccess.GlobalConf;
import com.dataaccess.dao.IUserDao;
import com.dataaccess.model.UserEntity;
import com.domain.User;
import com.service.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final IUserDao userDao;

    public UserService() {
        this.userDao = GlobalConf.getConnector().createUserDao();
    }

    public List<User> getAllUsers() {
        return userDao.getAll()
                .stream()
                .map(UserMapper::toDomain)
                .collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        UserEntity entity = userDao.getById(id);
        return entity != null ? UserMapper.toDomain(entity) : null;
    }

    // ---------------------------
    //     🔹 REGISTRÁCIA
    // ---------------------------
    public boolean register(String fname, String lname, String email, String password) {

        // kontrola duplicity emailu
        User existing = getUserByEmail(email);
        if (existing != null) {
            return false; // email existuje
        }

        User newUser = new User();
        newUser.setFname(fname);
        newUser.setLname(lname);
        newUser.setEmail(email);
        newUser.setPassword(password);

        userDao.save(UserMapper.toEntity(newUser));

        return true;
    }

    // ---------------------------
    //     🔹 LOGIN
    // ---------------------------
    public User login(String email, String password) {
        User user = getUserByEmail(email);

        if (user == null) return null;
        if (!user.getPassword().equals(password)) return null;

        return user;
    }

    // ---------------------------
    //  🔹 Vyhľadanie podľa emailu
    // ---------------------------
    public User getUserByEmail(String email) {
        return getAllUsers()
                .stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
