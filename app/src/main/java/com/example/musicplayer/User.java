package com.example.musicplayer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "users")
public class User
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo (name = "files")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setName(String name) {
        this.name = name;
    }
}
