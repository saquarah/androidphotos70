package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private ArrayAdapter<Album> albumArrayAdapter;

    private Album selectedAlbum;

    private ListView albumsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        selectedAlbum = null; // no selection upon entering activity
        albumList.add(new Album("test")); //TODO TEST ALBUM: DELETE ME LATER
        albumArrayAdapter = new ArrayAdapter<Album>(this, R.layout.albums_listview_detail, albumList);
        albumsListView = (ListView) findViewById(R.id.albumsListView);
        albumsListView.setAdapter(albumArrayAdapter);
        albumsListView.setOnItemClickListener( (p,v,pos,id) -> selectAlbum(pos) );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // I honestly have no idea what this means, I just found this online.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    /**
     *This method is basically the directory for the menu items on the tool bar.
     *The switch statement decides what to do depending on what menu item was pressed.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addItem: // User pressed add
                createAlbumDialog();
                break;
            case R.id.deleteItem: // User pressed delete
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
                }
                deleteAlbumDialog();
                break;
            case R.id.renameItem: // User pressed rename
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
                }
                renameAlbumDialog();
                break;
            case R.id.searchItem: // User pressed search
                if(albumList.isEmpty()) {
                    Toast.makeText(getBaseContext(), "You have no albums", Toast.LENGTH_LONG).show();
                    break;
                }
                searchAlbums();
                break;
            case R.id.openItem: // User pressed open
                if(selectedAlbum == null) {
                    noAlbumSelectedDialog();
                    break;
                }
                openAlbum();
                break;
        }
        // Not sure if this is needed
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method shows a dialog that has an EditText where the user can enter the
     * name of the new album. The user can also cancel at any time.
     */
    private void createAlbumDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter album name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString().trim();
                if(albumName.equals("")) {
                    Toast.makeText(getBaseContext(), "No name entered", Toast.LENGTH_LONG).show();
                    return;
                }
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

    /**
     * This method first checks if the there is already an album with the same name as albumName
     * and then it creates a new album with that name. The album is added to albumList, the ArrayList
     * of albums, and then the albumArrayAdapter is updated so the ListView is updated.
     * @param albumName
     * @throws Exception when there is an album with the same name as albumName already
     */
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

    /**
     * Shows a dialog asking if the user really wants to delete the selected album.
     */
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

    /**
     * Removes selected album from the ArrayList of albums, updates the albumArrayAdapter, and
     * resets the selection highlighter.
     */
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
                String albumName = input.getText().toString().trim();
                if(albumName.equals("")) {
                    Toast.makeText(getBaseContext(), "No name entered", Toast.LENGTH_LONG).show();
                    return;
                }

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

    private void openAlbum() {
        Intent openIntent = new Intent(getApplicationContext(), AlbumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("album", selectedAlbum);
        bundle.putSerializable("album_list", albumList);
        openIntent.putExtras(bundle);
        startActivity(openIntent);
    }

    private void searchAlbums() {
        Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("album_list", albumList);
        searchIntent.putExtras(bundle);
        startActivity(searchIntent);
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
