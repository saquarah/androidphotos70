package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.photos70.adapters.PhotoAdapter;
import com.example.photos70.model.Album;
import com.example.photos70.model.Photo;
import com.example.photos70.model.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends Activity {

    private ArrayList<Album> albumList;
    private ArrayList<Photo> allPhotos = new ArrayList<Photo>();
    private ArrayList<Photo> searchResults = new ArrayList<Photo>();
    private PhotoAdapter photoAdapter;
    private GridView gridView;

    private String tagType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle bundle = getIntent().getExtras();
        albumList = (ArrayList<Album>) bundle.getSerializable("album_list");
        fillAllPhotosList();

        // TODO will need to set the photo adapter after the search has happened
        gridView = findViewById(R.id.gridViewSearch);
        photoAdapter = new PhotoAdapter(this, new ArrayList<Photo>());
        gridView.setAdapter(photoAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // I honestly have no idea what this means, I just found this online.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){ // there's only one option but i'm doing this anyway
            case R.id.searchItem2:
                searchDialog();
        }
        // Not sure if this is needed
        return super.onOptionsItemSelected(item);
    }

    private void fillAllPhotosList() {
        for(Album a: albumList) {
            for(Photo p: a.getPhotos()) {
                allPhotos.add(p);
            }
        }
    }

    private void searchDialog() {
        tagType = "Person";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the type and then enter the value of the tag to search for");
        final String[] choiceTypes = {"Person", "Location", "Both"};
        builder.setSingleChoiceItems(choiceTypes, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tagType = choiceTypes[which];
            }
        });


        final EditText input = new EditText(this);
        input.setHint("Enter value here");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tagValue = input.getText().toString().trim();
                if(tagValue.equals("")) {
                    Toast.makeText(getBaseContext(), "No value entered", Toast.LENGTH_LONG).show();
                    return;
                }
                search(tagType, tagValue);

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

    private void search(String type, String value) {
        // this is a dumb and repetitive way of doing this but i do not care at this stage
        searchResults.clear();
        photoAdapter.photos.clear();
        for(Photo p: allPhotos) {

            if(type.equals("Person")) { // person only

                for(Tag t: p.getTags()) {
                    if(t.getTag().equals("Person")) {
                        String lcSearchValue = value.toLowerCase();
                        String lcTagValue = t.getValue().toLowerCase();

                        if(lcTagValue.startsWith(lcSearchValue)) {
                            searchResults.add(p);
                        }

                    }
                }
            } else if (type.equals("Location")) { // location only

                for(Tag t: p.getTags()) {
                    if(t.getTag().equals("Location")) {
                        String lcSearchValue = value.toLowerCase();
                        String lcTagValue = t.getValue().toLowerCase();

                        if(lcTagValue.startsWith(lcSearchValue)) {
                            searchResults.add(p);
                        }

                    }
                }

            } else { // both

                for(Tag t: p.getTags()) {
                    String lcSearchValue = value.toLowerCase();
                    String lcTagValue = t.getValue().toLowerCase();

                    if(lcTagValue.startsWith(lcSearchValue)) {
                        searchResults.add(p);
                    }
                }

            }

        }

       loadPhotosList();

    }

    private void loadPhotosList() {
        System.out.println("loadphotolist "+searchResults.size());
        for(Photo p: searchResults) {
            System.out.println("loadphoto "+p.getFile());

            File f = new File(p.getFile());

            if (f.exists()) {
                System.out.println("found");
            } else {
                System.out.println("not found");
            }

            InputStream inputStream;
            try {

                Bitmap myBitmap = BitmapFactory.decodeFile(f.getPath());


                Photo photo = new Photo(myBitmap,f);

                photoAdapter.photos.add(photo);
                photoAdapter.notifyDataSetChanged();





            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }
            //photoList.add(p);
            //photoAdapter.photos.add(p);

        }}


}
