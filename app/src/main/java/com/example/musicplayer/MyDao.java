package com.example.musicplayer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
    void adduser(User user);

    @Query("select * from users")
    List<User> getUsers();

    @Query("DELETE FROM users")
    void deleteAll();


    @Delete
    void delete(User user);
}
