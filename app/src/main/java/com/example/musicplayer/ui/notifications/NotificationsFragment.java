package com.example.musicplayer.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.UpdateDictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    public static ListView listdictionary;
    public static String[] items = new String[1];

    public static  ArrayList<String> smySongs = new ArrayList<>();
    public static  ArrayList<String> stringlist = new ArrayList<>();
    public static  ArrayList<File> sortedfiles = new  ArrayList<>();
    public static Stack<String> dirstack = new Stack<>();
    public static String backupString = Environment.getExternalStorageDirectory().toString();
    public static Button fb;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =ViewModelProviders.of(this).get(NotificationsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        listdictionary = root.findViewById(R.id.FilesDictionary);
        fb = root.findViewById(R.id.floating_button);
        sortedfiles= UpdateDictionary.fsortedfiles;
        stringlist=UpdateDictionary.fstringlist;
        setAdapter();

            fb.setOnClickListener(new View.OnClickListener(){

            @Override
                public void onClick(View view) {
                backupString=Environment.getExternalStorageDirectory().toString();
                try {
                    File file = new File(dirstack.pop());
                    show_dictionaries(file);
                }catch (EmptyStackException e){
                    show_dictionaries(Environment.getExternalStorageDirectory());
                }

            }
        });
          return root;
    }



    public  void show_dictionaries(File current_file) {

        sortedfiles.removeAll(sortedfiles);
        stringlist.removeAll(stringlist);

        final File[] files = current_file.listFiles();
        for (File file : files) {
            if (((file.isDirectory() && !file.isHidden()) || file.getName().endsWith(".mp3") || file.getName().endsWith(".wav"))) {
                if (mp3pressentindic(file)) {
                    stringlist.add(file.getName());
                    sortedfiles.add(file);
                }
            }
        }

        setAdapter();
    }
    public void setAdapter()
    {   CustomMyAdapter<String> myAdapter = new CustomMyAdapter(getContext(),  R.layout.list_item_view , sortedfiles);
        listdictionary.setAdapter(myAdapter);

        listdictionary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(stringlist.get(i).endsWith(".mp3") ||stringlist.get(i).endsWith(".wav")){
                    smySongs.add(sortedfiles.get(i).toString());
                    items[0] =  stringlist.get(i);
                    startActivity(new Intent(getActivity() , PlayerActivity.class).putExtra("songs", smySongs).putExtra("pos", 0).putExtra("item" , items));
                }
                if(sortedfiles.get(i).isDirectory()){
                    if(backupString.isEmpty())
                        dirstack.push(Environment.getExternalStorageDirectory().toString());
                    else
                        dirstack.push(backupString);
                    backupString=sortedfiles.get(i).toString();
                    show_dictionaries(sortedfiles.get(i));
                }
            }
        });
    }
    public static boolean mp3pressentindic(File file){

        if(file.getName().endsWith("mp3")||file.getName().endsWith(".wav")){
            return  true ;
        }
        else{
            File[] thisdic = file.listFiles();
            if(thisdic == null){
                return false;
            }
            for(File myfile : thisdic){
                if(mp3pressentindic(myfile))
                    return true;
            }
        }
        return  false ;
    }


    class MyAdapter extends ArrayAdapter<String>{
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView imageView_list_item = view.findViewById(R.id.image_view_item_in_list);
            TextView textView_list_item = view.findViewById(R.id.text_in_list_view);

            return view;
        }

        public MyAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        public MyAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public MyAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

        public MyAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public MyAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
        }

        public MyAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }
    }


}
