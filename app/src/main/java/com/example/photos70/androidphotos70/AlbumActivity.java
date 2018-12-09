package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.photos70.model.Album;

import java.util.ArrayList;

public class AlbumActivity extends Activity {
    private ArrayList<Album> albumList;
    private Album thisAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Bundle bundle = getIntent().getExtras();
        thisAlbum = (Album) bundle.getSerializable("album");
        albumList = (ArrayList<Album>) bundle.getSerializable("album_list");

    }
}
