package com.example.musicplayer.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomMyAdapter<String> extends ArrayAdapter<File> {
    private Context mcontext;
    int mresource;
    public CustomMyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, (List<File>) objects);
        mresource=resource;
        mcontext =context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = (String) getItem(position).getName();
        int img = (int) getImageReferance(getItem(position));

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mresource,parent,false);

        TextView nameitem = (TextView) convertView.findViewById(R.id.text_in_list_view);
        ImageView imgitem = (ImageView) convertView.findViewById(R.id.image_view_item_in_list);

        nameitem.setText((CharSequence) name);
        imgitem.setImageResource(img);


        return convertView;
    }

    private Object getImageReferance(File file) {
        if(file.isDirectory()){
            return R.drawable.icon_folder;
        }
        else {
            return R.drawable.icon_music;
        }
    }
}
