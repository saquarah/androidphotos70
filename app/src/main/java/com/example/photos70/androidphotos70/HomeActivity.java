package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.photos70.model.Album;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

public class HomeActivity extends Activity {
    private ArrayList<Album> albumList = new ArrayList<Album>();
    // array adapter here
    private ArrayAdapter<Album> albumArrayAdapter;

    private Album selectedAlbum;
    private int selectedIndex;

    private ListView albumsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        selectedAlbum = null;
        Resources res = getResources();
        albumList.add(new Album("a"));
        // array adapter here
        albumArrayAdapter = new ArrayAdapter<Album>(this, R.layout.albums_listview_detail, albumList);
        albumsListView = (ListView) findViewById(R.id.albumsListView);
        // array adapter here
        albumsListView.setAdapter(albumArrayAdapter);
        albumsListView.setOnItemClickListener( (p,v,pos,id) -> selectAlbum(pos) );
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
            case R.id.deleteItem:
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
                }
                deleteAlbumDialog();
                break;
            case R.id.renameItem:
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
                }
                renameAlbumDialog();
                break;
            case R.id.searchItem:
                break;
            case R.id.openItem:
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter album name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString();
                try {
                    createAlbum(albumName);
                } catch (Exception e) {
                    dialog.cancel();
                    Toast.makeText(getBaseContext(), "This album already exists.", Toast.LENGTH_LONG).show();
                }
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

    private void createAlbum(String albumName) throws Exception {
        for(Album a: albumList) {
            if(a.getName().equals(albumName)) {
                throw new Exception();
            }
        }

        Album newAlbum = new Album(albumName);
        albumList.add(newAlbum);
        // array adapter here
        albumArrayAdapter.notifyDataSetChanged();
    }

    private void selectAlbum(int pos) {
        selectedAlbum = albumList.get(pos);
    }

    private void deleteAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    deleteAlbum();
                } catch (Exception e) {
                    dialog.cancel();
                    // show toast saying album already exists
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteAlbum() {

        albumList.remove(selectedAlbum);
        albumArrayAdapter.notifyDataSetChanged();
        resetSelection();
    }

    private void renameAlbumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter new album name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString();
                //TODO make sure user cannot input blank name here and in createAlbumDialog
                try {
                    renameAlbum(albumName);
                } catch (Exception e) {
                    dialog.cancel();
                    Toast.makeText(getBaseContext(), "This album name already exists.", Toast.LENGTH_LONG).show();
                }
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

    private void renameAlbum(String newName) throws Exception{
        for(Album a: albumList) {
            if(a.getName().equals(newName)) {
                throw new Exception();
            }
        }

        selectedAlbum.setName(newName);
        albumArrayAdapter.notifyDataSetChanged();
    }

    private void resetSelection() {
        selectedAlbum = null;
        albumsListView.setAdapter(albumArrayAdapter); // this somehow clears the selection highlight
    }

    private void noAlbumSelectedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No album selected");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
}
