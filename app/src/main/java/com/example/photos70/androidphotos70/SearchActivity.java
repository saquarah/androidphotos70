package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.os.Bundle;

import com.example.photos70.model.Album;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends Activity {

    private ArrayList<Album> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle bundle = getIntent().getExtras();
        albumList = (ArrayList<Album>) bundle.getSerializable("album_list");

    }
}
