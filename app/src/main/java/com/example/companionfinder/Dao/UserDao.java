package com.example.companionfinder.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.companionfinder.Model.User;

import java.util.List;


@Dao
public interface UserDao {
    @Query("SELECT * FROM [User]")
    List<User> getAll();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM [User] WHERE name=:login")
    User getUserByLogin(String login);

    @Query("SELECT * FROM [User] WHERE password=:password and name=:login")
    User getUserByPassword(String password, String login);
};




