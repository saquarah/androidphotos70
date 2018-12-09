package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.photos70.model.Album;

public class AlbumActivity extends Activity {

    private Album thisAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        Bundle bundle = getIntent().getExtras();
        thisAlbum = (Album) bundle.getSerializable("album");
    }
}
