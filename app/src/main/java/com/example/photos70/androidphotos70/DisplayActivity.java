package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.os.Bundle;

import com.example.photos70.model.Album;
import com.example.photos70.model.Photo;

import java.util.ArrayList;

public class DisplayActivity extends Activity {

    private ArrayList<Photo> photosList;
    private Photo currentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Bundle bundle = getIntent().getExtras();
        currentPhoto = (Photo) bundle.getSerializable("selected_photo");
        photosList = (ArrayList<Photo>) bundle.getSerializable("photos_list");
    }
}
