package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.photos70.adapters.PhotoAdapter;
import com.example.photos70.model.Album;
import com.example.photos70.model.Photo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AlbumActivity extends Activity {
    public static final int REQUEST_CODE = 20;
    private ArrayList<Photo> photoList = new ArrayList<Photo>();
    private ArrayList<Album> albumList;
    private Album thisAlbum;
    private Photo selectedPhoto;
    private ArrayList<ImageView> imageViewArrayList;
    private PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        selectedPhoto = null;
        Bundle bundle = getIntent().getExtras();
        thisAlbum = (Album) bundle.getSerializable("album");
        albumList = (ArrayList<Album>) bundle.getSerializable("album_list");


        photoAdapter = new PhotoAdapter(this, photoList);
        loadPhotosList();


        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(photoAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPhoto = photoAdapter.photos.get(position);

            }
        });

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
                askForPhoto();
                break;
            case R.id.deletePhoto:
                //TODO implement deletePhoto
               // System.out.println("delete press");
                //System.out.println(photoList.size());
                deletePhotoDialog();
                //System.out.println(photoLi);
                //System.out.println(thisAlbum.getPhotos().toString());

                break;
            case R.id.displayItem:
                //TODO transfer to display screen
                break;
        }
        // Not sure if this is needed
        return super.onOptionsItemSelected(item);
    }

    private void askForPhoto() {
        //invoke the image gallery using implicit intent
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        // where will we find the data?
        File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picDirPath = pictureDir.getPath();
        // get URI representation
        Uri data = Uri.parse(picDirPath);
        // set the data and type, get all image types
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            // successful process
            if(requestCode == REQUEST_CODE) {
                // we are getting something back from the image gallery

                // the address of the image on the SD card
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Photo photo = new Photo(bitmap);
                 //   photoList.add(photo);
                    photoAdapter.photos.add(photo);
                    photoAdapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void deletePhotoDialog() {

        if(selectedPhoto == null){
            Toast.makeText(AlbumActivity.this, "No Photo Selected", Toast.LENGTH_SHORT).show();
        }else {

            new AlertDialog.Builder(this)
                    .setTitle("Delete Photo")
                    .setMessage("Do you really want to delete?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            photoAdapter.photos.remove(selectedPhoto);
                            photoAdapter.notifyDataSetChanged();
                            selectedPhoto = null;
                            Toast.makeText(AlbumActivity.this, "Photo Deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }


    }

    private void displayPhoto() {}

    private void loadPhotosList() {
        for(Photo p: thisAlbum.getPhotos()) {
            photoList.add(p);
            photoAdapter.photos.add(p);
        }
    }



}
