package com.example.musicplayer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static com.example.musicplayer.UpdateDictionary.update_Dictionary;
import static com.example.musicplayer.ui.UpdateSongs.update_songs;

public class MainActivity extends AppCompatActivity {

    public static ConstraintLayout constraintLayout;
    public static CoordinatorLayout coordinatorLayout;
    public static BottomNavigationView navView;
    public static ListView myListViewforsongs;
    public static String[] items;
    public static String[] savingdata;
    public static String[] update_items;
    public static String[] update_savingdata;
    public static MyAppDatabase myAppDatabase, likedSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        constraintLayout = findViewById(R.id.container);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "user.db").allowMainThreadQueries().build();
        likedSongs = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "likedsongs.db").allowMainThreadQueries().build();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        runTimePermission();


    }


    public void runTimePermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                update_Dictionary();
                update_songs(MainActivity.this);

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                final String permissionName = permissionDeniedResponse.getPermissionName();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false).setMessage("PERMISSION ERROR ::\n" + permissionName.trim().replace("android.permission.", "").replace("_", " ").toLowerCase() + " permission is not given.");

                AlertDialog builderalert = builder.create();
                builderalert.show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Exit app ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {PlayerActivity.myMediaPlayer.stop();}catch (Exception e){e.getMessage();}

                MainActivity.super.finishAndRemoveTask();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



}