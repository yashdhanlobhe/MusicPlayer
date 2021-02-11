package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import static com.example.musicplayer.LikdedSongsUpdate.checkLikedOrNot;

public class PlayerActivity extends AppCompatActivity {

    Button btn_next , btn_back , btn_pause ;
    String[] items;
    TextView songLable , previoussongLable , nextsongLable  ;
    SeekBar songseekbar;
    ImageView cover_album;

    Button btn_repeat , btn_shuffle , btn_like ;

    static MediaPlayer myMediaPlayer;
    static int position;
    ArrayList<String> smysongs;
    Thread updateseekBar;


     boolean reapeat = false ;
     boolean swap = false;
     boolean like = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_back= findViewById(R.id.back);
        btn_next= findViewById(R.id.next);
        btn_pause= findViewById(R.id.pause);
        btn_like=findViewById(R.id.like);
        btn_repeat=findViewById(R.id.repeat);
        btn_shuffle=findViewById(R.id.shuffle);
        songLable = findViewById(R.id.songname);
        previoussongLable = findViewById(R.id.previoussongname);
        nextsongLable = findViewById(R.id.nextsongname);
        songseekbar= findViewById(R.id.seekbar);
        cover_album=findViewById(R.id.album_cover);
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Playing Song");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i =getIntent();
        Bundle bundle = i.getExtras();

        assert bundle != null;
        smysongs=(ArrayList) bundle.getParcelableArrayList("songs");
        position =bundle.getInt("pos" , 0);
        items = bundle.getStringArray("item");

        assert items != null;
        songLable.setText(items[position]);
        songLable.setSelected(true);
        if(position==items.length-1){nextsongLable.setText(items[0]);}else{nextsongLable.setText(items[position+1]);}
        nextsongLable.setSelected(true);
        if(position==0){previoussongLable.setText(items[items.length-1]);}else{previoussongLable.setText(items[position-1]);}
        previoussongLable.setSelected(true);

        play_song();



        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause_play();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_next_song();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_back_button();
            }
        });
        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(swap){
                    swap=false;
                    btn_shuffle.setBackgroundResource(R.drawable.icon_shuffle);
                }
                else{
                    btn_shuffle.setBackgroundResource(R.drawable.icon_shuffle_highlight);
                    swap=true;
                    if(reapeat){
                        reapeat=false;
                        btn_repeat.setBackgroundResource(R.drawable.icon_repeat);
                    }
                }


            }
        });
        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(reapeat){
                    reapeat=false;
                    btn_repeat.setBackgroundResource(R.drawable.icon_repeat);
                }
                else{
                    btn_repeat.setBackgroundResource(R.drawable.icon_repeat_highlight);
                    reapeat=true;
                    if(swap){
                        swap=false;
                        btn_shuffle.setBackgroundResource(R.drawable.icon_shuffle);
                    }
                }
            }
        });
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(like){
                    btn_like.setBackgroundResource(R.drawable.icon_unfavourite);
                    like = false;
                    LikdedSongsUpdate.deleteLikedSong(smysongs.get(position));
                }
                else{
                    btn_like.setBackgroundResource(R.drawable.icon_favourite_color);
                    like=true;
                    LikdedSongsUpdate.addLikedSong(smysongs.get(position));
                }

            }
        });
    }
//pause play
         public void pause_play(){
             songseekbar.setMax(myMediaPlayer.getDuration());

             if(myMediaPlayer.isPlaying()){
                 btn_pause.setBackgroundResource(R.drawable.icon_play);
                 myMediaPlayer.pause();
             }
             else{
                 btn_pause.setBackgroundResource(R.drawable.icon_pause);
                 myMediaPlayer.start();
             }
        }

//back play
        public void play_back_button() {
        myMediaPlayer.stop();
        myMediaPlayer.release();

        if (position > 1) { position -= 1; } else { position = smysongs.size() - 1; }
        Uri u = Uri.parse(smysongs.get(position));
        myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        songLable.setText(items[position]);

        if (position == items.length - 1) { nextsongLable.setText(items[0]); }else { nextsongLable.setText(items[position + 1]); }
        if (position == 0) { previoussongLable.setText(items[items.length - 1]); } else { previoussongLable.setText(items[position - 1]); }

        play_song();

    }
//next play
    public void play_next_song(){
        myMediaPlayer.stop();
        myMediaPlayer.release();

        if(position<smysongs.size()-1){ position+=1; } else{ position=0; }
        Uri u= Uri.parse(smysongs.get(position));
        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        songLable.setText(items[position]);

        if(position==items.length-1){nextsongLable.setText(items[0]);}else{nextsongLable.setText(items[position+1]);}
        if(position==0){previoussongLable.setText(items[items.length-1]);}else{previoussongLable.setText(items[position-1]);}

        play_song();


    }
    public void play_song() {
        if (myMediaPlayer != null) {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        Uri u = Uri.parse(smysongs.get(position));
        myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

        if(checkLikedOrNot(smysongs.get(position))){
            btn_like.setBackgroundResource(R.drawable.icon_favourite_color);
            like = true ;
        }
        else{
        btn_like.setBackgroundResource(R.drawable.icon_unfavourite);
        like = false;
        }

        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(smysongs.get(position));

        byte [] data = mmr.getEmbeddedPicture();
        if(data!= null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0 , data.length);
            cover_album.setImageBitmap(bitmap);
        }

        updateseekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;
                while (totalDuration > currentPosition) {
                    try {
                        sleep(500);
                        try {
                            currentPosition = myMediaPlayer.getCurrentPosition();
                        } catch (RuntimeException e) {
                            e.getStackTrace();
                        }
                        songseekbar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.getStackTrace();
                    }
                }
                Thread.currentThread().interrupt();
            }
        };
        songseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });


        try {
            myMediaPlayer.start();
            songseekbar.setMax(myMediaPlayer.getDuration());
            updateseekBar.start();
        } catch (Exception e) {
            Toast.makeText(this, "May Song has been Deleted !", Toast.LENGTH_SHORT).show();
        }

        songseekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        songseekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        //after song ends
        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Random rand = new Random();
                int int_random = rand.nextInt(smysongs.size());

                if(reapeat){PlayerActivity.position -= 1;}
                if(swap){PlayerActivity.position = int_random;}
                play_next_song();
            }
        });
    }






////back button
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if(item.getItemId() == android.R.id.home){
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}