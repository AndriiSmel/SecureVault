package com.securevault.repository;

import com.securevault.model.User;
import java.util.ArrayList;

public class UserRepository {
    private ArrayList<User> userArrayList = new ArrayList<User>();
    public int count = 0;

    public int generateId() {
        count++; return count;
    }
    public void addUser(String username, String passwordHash) {
        User user = new User(generateId(), username, passwordHash);
        userArrayList.add(user);
    }
    public User searchByUsername(String username) {
        for ( User user : userArrayList) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }
}