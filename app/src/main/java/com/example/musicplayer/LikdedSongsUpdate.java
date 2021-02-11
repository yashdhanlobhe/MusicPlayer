package com.example.musicplayer;

import java.util.List;

import static com.example.musicplayer.MainActivity.likedSongs;

public class LikdedSongsUpdate {


    public static void addLikedSong(String path){
     User user= new User();
     user.setName(path);
     MainActivity.likedSongs.myDao().adduser(user);
    }

    public static boolean checkLikedOrNot(String path){
        List<User> llusers = MainActivity.likedSongs.myDao().getUsers();
        for (User u : llusers) {
            if(path.equals(u.getName())){
                return true;
            }

        }
        return false;
    }

    public static void deleteLikedSong(String path){
        List<User> lllusers = likedSongs.myDao().getUsers();
        for (User u : lllusers) {
                 if(path.equals(u.getName())){
           likedSongs.myDao().delete(u);
            }
        }
    }

}
