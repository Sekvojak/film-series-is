package com.dataaccess.dao.text;

import com.dataaccess.model.UserEntity;
import com.dataaccess.dao.IUserDao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserTextDao implements IUserDao {

    private static final String filePath = "data/users.txt";

    @Override
    public void save(UserEntity user) {

        if (user.getId() == null) {
            long newId = getAll().size() + 1;
            user.setId(newId);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(
                    user.getId() + ";" +
                            user.getFname() + ";" +
                            user.getLname() + ";" +
                            user.getPhoneNumber() + ";" +
                            user.getEmail() + ";" +
                            user.getPassword()
            );

            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserEntity getById(Long id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                long userId = Long.parseLong(parts[0]);
                if (userId == id) {
                    UserEntity u = new UserEntity();
                    u.setId(userId);
                    u.setFname(parts[1]);
                    u.setLname(parts[2]);
                    u.setPhoneNumber(parts[3]);
                    u.setEmail(parts[4]);
                    u.setPassword(parts[5]);

                    return u;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserEntity> getAll() {
        List<UserEntity> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts[0] == null || parts[0].equals("null")) {
                    continue;
                }

                UserEntity u = new UserEntity();
                u.setId(Long.parseLong(parts[0]));
                u.setFname(parts[1]);
                u.setLname(parts[2]);
                u.setPhoneNumber(parts[3]);
                u.setEmail(parts[4]);
                u.setPassword(parts[5]);

                users.add(u);
            }
        } catch (FileNotFoundException e) {
            return users;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public UserEntity findByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 6) continue;

                if (parts[4].equalsIgnoreCase(email)) {
                    UserEntity u = new UserEntity();
                    u.setId(Long.parseLong(parts[0]));
                    u.setFname(parts[1]);
                    u.setLname(parts[2]);
                    u.setPhoneNumber(parts[3]);
                    u.setEmail(parts[4]);
                    u.setPassword(parts[5]);
                    return u;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
