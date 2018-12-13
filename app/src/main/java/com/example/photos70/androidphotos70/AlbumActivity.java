package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.photos70.adapters.PhotoAdapter;
import com.example.photos70.model.Album;
import com.example.photos70.model.Photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        //System.out.println("oncreate"+photoAdapter.photos.size());

        //added to de-Serialize Album

        File f = new File(getFilesDir(), "save_object.bin");
        if (f.exists()) {
            System.out.println("it is there");
            loadSerializedObject();
        }

        loadPhotosList();


        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(photoAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPhoto = photoAdapter.photos.get(position);


            }
        });

    }

    //nikky
    @Override
    public void onBackPressed(){
        //saveAlbumObject();
        System.out.println("back pressed");
        finish();
    }

    //added to de-Serialize the ablum
    public Object loadSerializedObject(){
        try{
            //System.out.println(getFilesDir().toString());
            File inputFile = new File(getFilesDir(),"save_object.bin");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));
            albumList = (ArrayList)ois.readObject();
            // System.out.println(alist.size());
            //albumList.addAll(alist);
            ois.close();



        }catch(java.io.IOException e){
            //System.out.println("first error");
            //e.printStackTrace();

        } catch (ClassNotFoundException e) {
            System.out.println("second error");
            e.printStackTrace();
        }
        return null;
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
                openAlbum();
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

    //added to Serialize the ablum
    public void saveAlbumObject(){
        try {
            System.out.println(getFilesDir().toString());
            File outputFile = new File(getFilesDir(),"save_object.bin");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
            oos.writeObject(albumList);
            oos.flush();
            oos.close();
            //System.out.println("saved photo in AlbumActivity.java");
        }catch (java.io.IOException e){
            System.out.println("saved album error");
            System.out.println(e.fillInStackTrace().toString());

        }


    }

    //added to check if album name is in albumList - not sure if we need this. is there a default album need in photo?
    public int albuminlist(String album_name){
        int found=-1;
        //System.out.println("albuminlist "+album_name);
        for (int i=0;i<albumList.size();i++) {
            //System.out.println(userList.get(i).toString());
            if(albumList.get(i).getName().equals(album_name)) {
                found = i;
            }
        }
        return found;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            // successful process
            if(requestCode == REQUEST_CODE) {
                // we are getting something back from the image gallery

                // the address of the image on the SD card
                Uri imageUri = data.getData();
                System.out.println("Adding image "+imageUri);
                String realPath=getRealPathFromURI(imageUri);
                System.out.println("real path : "+getRealPathFromURI(imageUri));

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    //File file = new File(imageUri.getPath());
                    File f= new File(realPath);
                    System.out.println("imageuri "+imageUri.getPath().toString());
                    //System.out.println("imageuri "+file.getAbsolutePath());
                    Photo photo = new Photo(bitmap,f);
                    if(!(isAlreadyAdded(photo))) {

                        //photoList.add(photo);
                        thisAlbum.addToAlbum(photo);

                        albumList.get(albuminlist(thisAlbum.getName().toString())).addToAlbum(photo);
                        System.out.println("this album " + thisAlbum.toString());
                        System.out.println("albumlist arraylist " + albumList.toString());
                        photoAdapter.photos.add(photo);
                        photoAdapter.notifyDataSetChanged();
                        saveAlbumObject();
                    }else{
                        Toast.makeText(this, "Photo already added", Toast.LENGTH_LONG).show();
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isAlreadyAdded(Photo photo){
        for (Photo p:thisAlbum.getPhotos()){
            //System.out.println(p.getFile().toString());
            // System.out.println("selectedphoto delete: "+selectedPhoto.getFile().toString());
            if(p.getFile().equals(photo.getFile())){
                return true;
            }
        }
        return false;
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
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
                           // System.out.println("photoadpater selected photo delete: "+photoAdapter.getItem(0));
                           // System.out.println("delete selected photo : "+selectedPhoto.toString());
                            photoAdapter.photos.remove(selectedPhoto);
                           // System.out.println("thisalbum from delete : "+thisAlbum.getPhotos());
                           // System.out.println("Index of in delete : "+thisAlbum.getPhotos());
                            int count =0;
                            for (Photo p:thisAlbum.getPhotos()){
                                System.out.println(p.getFile().toString());
                                System.out.println("selectedphoto delete: "+selectedPhoto.getFile().toString());
                                if(p.getFile().equals(selectedPhoto.getFile())){
                                    //System.out.println("match found count : "+count);
                                    thisAlbum.rmPhoto(count);
                                    albumList.get(albuminlist(thisAlbum.getName().toString())).rmPhoto(count);
                                    saveAlbumObject();
                                    //System.out.println(count);
                                    count++;
                                    break;
                                }else {
                                    count++;
                                }
                            }
                            //thisAlbum.removeFromAlbum(selectedPhoto);


                           // System.out.println("thisalbum delete: "+thisAlbum.toString());
                            //albumList.remove(albuminlist(thisAlbum.getName().toString()));
                            //albumList.add(thisAlbum);



                            //System.out.println("getphoto delete : "+albumList.get(albuminlist(thisAlbum.getName().toString())).getPhotos());

                           // System.out.println("albumlist from delete : "+albumList.toString());
                            photoAdapter.notifyDataSetChanged();
                            selectedPhoto = null;
                            Toast.makeText(AlbumActivity.this, "Photo Deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        }


    }

    private void displayPhoto() {}

    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("something"+photoList.size());
        //System.out.println("onresume"+photoAdapter.photos.isEmpty());
    }

    private void loadPhotosList() {
        System.out.println("loadphotolist "+thisAlbum.getPhotos().size());
        for(Photo p: thisAlbum.getPhotos()) {
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
                //photoList.add(photo);
                //thisAlbum.addToAlbum(photo);
                //albumList.get(albuminlist(thisAlbum.getName().toString())).addToAlbum(photo);
                System.out.println("this album "+thisAlbum.toString());
                System.out.println("albumlist arraylist "+ albumList.toString());
                photoAdapter.photos.add(photo);
                photoAdapter.notifyDataSetChanged();





            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }
                //photoList.add(p);
                //photoAdapter.photos.add(p);

    }}


    private void openAlbum() {
        if(selectedPhoto!=null) {
            Intent openIntent = new Intent(getApplicationContext(), DisplayActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selected_photo",selectedPhoto);
            bundle.putSerializable("album", thisAlbum);
            bundle.putSerializable("album_list", albumList);
            openIntent.putExtras(bundle);
            startActivity(openIntent);
        }
    }

}
