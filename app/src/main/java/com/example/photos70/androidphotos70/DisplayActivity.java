package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.photos70.adapters.DisplayAdapter;
import com.example.photos70.adapters.PhotoAdapter;
import com.example.photos70.model.Album;
import com.example.photos70.model.Photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends Activity {
    public static final int REQUEST_CODE = 20;
    private ArrayList<Photo> photoList = new ArrayList<Photo>();
    private ArrayList<Album> albumList;
    private Album thisAlbum;
      private Photo selectedPhoto;
    private ArrayList<ImageView> imageViewArrayList;
    private PhotoAdapter photoAdapter;
    private DisplayAdapter displayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Bundle bundle = getIntent().getExtras();
        selectedPhoto = (Photo) bundle.getSerializable("selected_photo");
        thisAlbum = (Album) bundle.getSerializable("album");
        albumList = (ArrayList<Album>) bundle.getSerializable("album_list");

        displayAdapter = new DisplayAdapter(this, photoList);

        loadSerializedObject();


        System.out.println(albumList.toString());
        System.out.println(selectedPhoto.getFile().toString());
        System.out.println(thisAlbum.toString());

        ImageView imgView = findViewById(R.id.imgView);


        //imgView.setAdapter(displayAdapter);
        Bitmap myBitmap = BitmapFactory.decodeFile(selectedPhoto.getFile());
        Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 700, 700, true);
        imgView.setImageBitmap(resized);

        Button nextBtn = (Button) findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("next button clicked");
                //System.out.println(getNextFilePath());
                String path=getNextFilePath();
                if(!(path==null)) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(path);
                    Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 500, 500, true);
                    imgView.setImageBitmap(resized);
                }

            }
        });



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



    public String getNextFilePath(){
        int count = 0;
        int size = thisAlbum.getPhotos().size();
        System.out.println(thisAlbum.toString());
        List<Photo> x = thisAlbum.getPhotos();

        for(Photo p : thisAlbum.getPhotos()){
            System.out.println("p.getFile() : "+p.getFile());
            System.out.println("selectedPhoto.getFile() : "+selectedPhoto.getFile());
            if(p.getFile().equals(selectedPhoto.getFile())){

                if(count == size){

                    return null;
                }else{
                    count++;
                    System.out.println(count);
                    System.out.println("p inside else: "+p);
                    System.out.println("p file inside else"+p.getFile());
                    System.out.println("x :"+x.get(count));
                    selectedPhoto= x.get(count);
                    System.out.println(selectedPhoto);
                    System.out.println(selectedPhoto.getFile());
                    return x.get(count).getFile();

                }

            }else {
                count++;
            }

        }
    return null;
    }


}
