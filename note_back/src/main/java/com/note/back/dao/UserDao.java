package com.note.back.dao;

import com.note.back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer> {
    public User findByUsername(String username);
}

