package com.example.musicplayer;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

import static com.example.musicplayer.ui.notifications.NotificationsFragment.mp3pressentindic;


public class UpdateDictionary {
    public  static  final ArrayList<File> fsortedfiles = new  ArrayList<>();
    public static  final ArrayList<String> fstringlist = new ArrayList<>();
    public static void update_Dictionary(){
        new Updatedictionary().execute();
    }

    public static class Updatedictionary extends AsyncTask<String ,String ,String > {

        @Override
        protected String doInBackground(String... strings) {
            show_dictionaries(Environment.getExternalStorageDirectory());
            return null;
        }
    }
    public  static void show_dictionaries(File current_file) {
        final File[] files = current_file.listFiles();
        for (File file : files) {
            if (((file.isDirectory() && !file.isHidden()) || file.getName().endsWith(".mp3") || file.getName().endsWith(".wav"))) {
                if (mp3pressentindic(file)) {
                    fstringlist.add(file.getName());
                    fsortedfiles.add(file);
                }
            }
        }
    }
}



