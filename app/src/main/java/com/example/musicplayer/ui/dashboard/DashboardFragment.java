package com.example.musicplayer.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.musicplayer.MainActivity.items;
import static com.example.musicplayer.MainActivity.likedSongs;
import static com.example.musicplayer.MainActivity.savingdata;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    public static ListView likedSongPlylist;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        likedSongPlylist=root.findViewById(R.id.likedSongPlaylist);
        displaySongs();
           return root;
    }

    void displaySongs() {
        final ArrayList<String> smySongs = new ArrayList<>();
        List<User> fusers = likedSongs.myDao().getUsers();
        for (User u : fusers) {
            smySongs.add(u.getName());
        }
        items = new String[smySongs.size()];
        savingdata = new String[smySongs.size()];
        for (int i = 0; i < smySongs.size(); i++) {
            items[i] = smySongs.get(i).split("/")[(smySongs.get(i).split("/").length - 1)].replace(".mp3", "").replace(".wav", "");
            savingdata[i] = smySongs.get(i);
        }
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        likedSongPlylist.setAdapter(myAdapter);

        likedSongPlylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), PlayerActivity.class).putExtra("songs", smySongs).putExtra("pos", i).putExtra("item" , items));
            }
        });
    }
}
