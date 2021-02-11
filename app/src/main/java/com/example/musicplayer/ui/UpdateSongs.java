package com.example.musicplayer.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.example.musicplayer.MainActivity;
import com.example.musicplayer.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.musicplayer.MainActivity.coordinatorLayout;
import static com.example.musicplayer.MainActivity.myAppDatabase;
import static com.example.musicplayer.MainActivity.update_items;
import static com.example.musicplayer.MainActivity.update_savingdata;
import static com.example.musicplayer.ui.home.HomeFragment.pd;

public class UpdateSongs {

    public static  void update_songs(MainActivity mainActivity){

        new UpdateSongs.UpdateSong(mainActivity).execute();
        Snackbar.make(coordinatorLayout, "Updating Songs in background" , BaseTransientBottomBar.LENGTH_SHORT).setAction("Okay" , new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
            }
        }).show();

    }

    @SuppressLint("StaticFieldLeak")
    public static class UpdateSong extends AsyncTask<String , String , String> {
        MainActivity mActivity;
        public UpdateSong(MainActivity mainActivity) {
            mActivity = mainActivity;
        }

        @Override
        protected String doInBackground(String... strings) {

//            Note that you can opt out of these "scoped storage" changes on Android 10 and 11, if your targetSdkVersion is
//        below 30, using android:requestLegacyExternalStorage="true" in the <application> element of the manifest. This is not
//        a long-term solution, as your targetSdkVersion will need to be
//            30 or higher sometime in 2021 if you are distributing your app through the Play Store (and perhaps elsewhere)

            final ArrayList<File> Update_mySongs = findSong(Environment.getExternalStorageDirectory());
            update_items = new String[Update_mySongs.size()];
            update_savingdata= new String[Update_mySongs.size()];
            myAppDatabase.myDao().deleteAll();
            for (int i = 0; i < Update_mySongs.size(); i++) {
                update_items[i] = Update_mySongs.get(i).getName().replace(".mp3", "").replace(".wav", "");
                update_savingdata[i] = Update_mySongs.get(i).toString();
                User update_user = new User();
                update_user.setName(Update_mySongs.get(i).toString());

                myAppDatabase.myDao().adduser(update_user);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Snackbar.make(coordinatorLayout, "All songs are updated " , BaseTransientBottomBar.LENGTH_LONG).show();
            pd.dismiss();
            mActivity.startActivity(new Intent(mActivity , MainActivity.class));
        }
    }
    public static ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File singleFile : Objects.requireNonNull(files)) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                findSong(singleFile);
                arrayList.addAll(findSong(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }


}



