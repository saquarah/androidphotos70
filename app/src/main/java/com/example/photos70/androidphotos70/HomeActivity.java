package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.photos70.model.Album;

import java.util.ArrayList;

public class HomeActivity extends Activity {
    ArrayList<Album> albumList = new ArrayList<Album>();
    ArrayAdapter<Album> albumArrayAdapter;

    ListView albumsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Resources res = getResources();
        albumList.add(new Album("a"));
        albumArrayAdapter = new ArrayAdapter<Album>(this, R.layout.albums_listview_detail, albumList);
        albumsListView = (ListView) findViewById(R.id.albumsListView);
        albumsListView.setAdapter(albumArrayAdapter);
      //  albumsListView.setAdapter(new ArrayAdapter<Album>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addItem:
                createAlbumDialog();
                break;
            case  R.id.deleteItem:

                break;
            case R.id.renameItem:

                break;
            case R.id.searchItem:

                break;
            case R.id.openItem:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Album");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString();
                createAlbum(albumName);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createAlbum(String albumName) {

    }

}