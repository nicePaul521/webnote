package com.note.back.service;

import com.note.back.dao.UserDao;
import com.note.back.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public User getUserByUsername(String username){
        return userDao.findByUsername(username);
    }

    public boolean isExit(String username){
        User user = getUserByUsername(username);
        return user != null;
    }

    public void addUser(User user){
        userDao.save(user);
    }
}
