package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.photos70.model.Album;

import java.util.ArrayList;

public class AlbumActivity extends Activity {
    private ArrayList<Album> albumList;
    private Album thisAlbum;

    private ArrayList<ImageView> imageViewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        GridView gridView = findViewById(R.id.gridView);


        Bundle bundle = getIntent().getExtras();
        thisAlbum = (Album) bundle.getSerializable("album");
        albumList = (ArrayList<Album>) bundle.getSerializable("album_list");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // I honestly have no idea what this means, I just found this online.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addPhoto:

                break;
            case R.id.deletePhoto:
                break;
            case R.id.displayItem:
                break;
        }
        // Not sure if this is needed
        return super.onOptionsItemSelected(item);
    }

    private void askForPhoto() {

    }

    private void deletePhotoDialog() {}

    private void displayPhoto() {}
}
