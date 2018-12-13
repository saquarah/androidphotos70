package com.example.photos70.androidphotos70;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photos70.adapters.DisplayAdapter;
import com.example.photos70.adapters.PhotoAdapter;
import com.example.photos70.model.Album;
import com.example.photos70.model.Photo;
import com.example.photos70.model.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private ArrayAdapter<Tag> tagAdapter;
    private ListView tagListView;
    private Tag selectedTag;
    private String tagType;
    private int sp;
    private int selectedTagInt;
    private Album newAlbum;
    private String new_Album;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // unpacking bundle
        Bundle bundle = getIntent().getExtras();
        selectedPhoto = (Photo) bundle.getSerializable("selected_photo");
        thisAlbum = (Album) bundle.getSerializable("album");
        albumList = (ArrayList<Album>) bundle.getSerializable("album_list");

        displayAdapter = new DisplayAdapter(this, photoList);

        loadSerializedObject();

        sp = photoinAlbum();

        // setting up tag adapter and listView
        //tagAdapter = new ArrayAdapter<Tag>(this, R.layout.tags_listview_detail, selectedPhoto.getTags());

            for (Tag t : albumList.get(albuminlist(thisAlbum.getName())).getPhoto(sp).getTags()) {
                if(!selectedPhoto.getTags().contains(t)) {
                    selectedPhoto.addTag(t.getTag(), t.getValue());
                }
            }

        System.out.println("oncreate : photoinALbum() : "+sp);
        System.out.println("on create thisalbum: "+thisAlbum.getPhoto(sp).getTags());
        System.out.println("on create albumlist: "+albumList.get(albuminlist(thisAlbum.getName())).getPhoto(sp).getTags());
        System.out.println("on create selected photo: "+selectedPhoto.getTags());
        tagAdapter = new ArrayAdapter<Tag>(this, R.layout.tags_listview_detail, albumList.get(albuminlist(thisAlbum.getName())).getPhoto(sp).getTags());

        tagListView = (ListView) findViewById(R.id.tagsList);
        tagListView.setAdapter(tagAdapter);
        tagListView.setOnItemClickListener( (p,v,pos,id) -> selectTag(pos) );



        System.out.println(albumList.toString());
        System.out.println(selectedPhoto.getFile().toString());
        System.out.println(thisAlbum.toString());

        ImageView imgView = findViewById(R.id.imgView);


        //imgView.setAdapter(displayAdapter);
        Bitmap myBitmap = BitmapFactory.decodeFile(selectedPhoto.getFile());
        Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 700, 700, true);
        imgView.setImageBitmap(resized);
        //tagAdapter.notifyDataSetChanged();
        //resetSelection();



        Button nextBtn = (Button) findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("next button clicked");
                //System.out.println(getNextFilePath());
                String path=getNextFilePath();
                if(!(path==null)) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(path);
                    Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 700, 700, true);
                    imgView.setImageBitmap(resized);
                    //for (Tag t:albumList.get(albuminlist(thisAlbum.getName())).getPhoto(photoinAlbum()).getTags()){
                    //    selectedPhoto.addTag(t.getTag(),t.getValue());
                    //}
                    for (Tag t:albumList.get(albuminlist(thisAlbum.getName())).getPhoto(photoinAlbum()).getTags()){
                        System.out.println("TAG T = "+t);
                        if(!(selectedPhoto.getTags().contains(t))) {
                            System.out.println("tag added");
                            System.out.println(selectedPhoto.getTags());

                            selectedPhoto.addTag(t.getTag(), t.getValue());
                        }
                    }
                    System.out.println(selectedPhoto.getTags());
                    tagAdapter.notifyDataSetChanged();
                    resetSelection();


                }

            }
        });

        Button prevBtn = (Button) findViewById(R.id.previous);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("prev button clicked");
                //System.out.println(getNextFilePath());
                String path=getPreviousFilePath();
                if(!(path==null)) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(path);
                    Bitmap resized = Bitmap.createScaledBitmap(myBitmap, 700, 700, true);
                    imgView.setImageBitmap(resized);
                    for (Tag t:albumList.get(albuminlist(thisAlbum.getName())).getPhoto(photoinAlbum()).getTags()){
                        System.out.println("TAG T = "+t);
                        if(!(selectedPhoto.getTags().contains(t))) {
                            selectedPhoto.addTag(t.getTag(), t.getValue());
                        }
                    }
                    System.out.println(selectedPhoto.getTags());
                    tagAdapter.notifyDataSetChanged();
                    resetSelection();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // I honestly have no idea what this means, I just found this online.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addTag:
                addTagDialog();
                break;
            case R.id.deleteTag:
                if(selectedTag == null) {
                    Toast.makeText(getBaseContext(), "No tag selected", Toast.LENGTH_LONG).show();
                    break;
                }
                deleteTagDialog();
                break;
            case R.id.moveToAlbum:
                moveToAlbum();
                break;

        }
        // Not sure if this is needed
        return super.onOptionsItemSelected(item);
    }

    public void moveToAlbum(){
        if(selectedPhoto==null){
            Toast.makeText(this, "No Photo Selected", Toast.LENGTH_SHORT).show();
        }else{
            selectAlbumDialog();
        }
    }

    private void selectAlbumDialog() {
        int sizeofAlbumlist = albumList.size();
        if(sizeofAlbumlist>1) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose the Album");

            String[] choiceTypes = new String[sizeofAlbumlist - 1];
            int i = 0;
            int c = 0;
            while (i < sizeofAlbumlist) {
                //System.out.println(i);
                //System.out.println(albumList.get(i).getName());
                if (!(thisAlbum.getName().equals(albumList.get(i).getName()))) {
                    choiceTypes[c] = albumList.get(i).getName();
                    c++;
                }
                i++;
            }


            new_Album = choiceTypes[0];


            builder.setSingleChoiceItems(choiceTypes, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    new_Album = choiceTypes[which];


                }
            });


            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try {
                        System.out.println("trying something !!!!!!!!!!!!!!!");
                        newAlbum = albumList.get(albuminlist(new_Album));
                        System.out.println("this is new album : " + newAlbum);

                        moveAlbum(newAlbum, thisAlbum);
                    } catch (Exception e) {
                        dialog.cancel();
                        Toast.makeText(getBaseContext(), "This tag already exists.", Toast.LENGTH_LONG).show();
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
        } else{
            Toast.makeText(getBaseContext(), "There is only one album.", Toast.LENGTH_LONG).show();
        }
    }

    private void moveAlbum(Album newAlbum,Album thisAlbum){
        

        movePhotoToNewAlbum(newAlbum);




    }

    private void deletePhotoFromThisAlbum(Photo photo){
        int count =0;
        System.out.println("delete photo form this album count : "+count);
        for (Photo p : thisAlbum.getPhotos()){
            if(p.getFile().equals(photo.getFile())){

                System.out.println("delete photo form this album count : "+count);
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
    }

    private void movePhotoToNewAlbum(Album newAlbum){
        if(!(isAlreadyAddedInNew(selectedPhoto))) {

            //photoList.add(photo);
            //newAlbum.addToAlbum(selectedPhoto);

            albumList.get(albuminlist(newAlbum.getName().toString())).addToAlbum(selectedPhoto);
            System.out.println("new album " + newAlbum.toString());
            System.out.println("albumlist arraylist " + albumList.toString());

            deletePhotoFromThisAlbum(selectedPhoto);

            saveAlbumObject();
            Toast.makeText(this, "Photo Move Successfully", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "Photo already exists.", Toast.LENGTH_LONG).show();
        }
    }
    private boolean isAlreadyAddedInNew(Photo photo){

        for (Photo p:newAlbum.getPhotos()){
            //System.out.println(p.getFile().toString());
            // System.out.println("selectedphoto delete: "+selectedPhoto.getFile().toString());
            if(p.getFile().equals(photo.getFile())){
                return true;
            }
        }
        return false;

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

    //added to check if album name is in albumList - not sure if we need this. is there a default album need in photo?
    public int photoinAlbum(){
        int count =0;
        for (Photo p:thisAlbum.getPhotos()){
            //System.out.println(p.getFile().toString());
            //System.out.println("selectedphoto delete: "+selectedPhoto.getFile().toString());
            if(p.getFile().equals(selectedPhoto.getFile())){
                System.out.println("photoinAlbum : p.getFile() :"+p.getFile());
                System.out.println("photoinAlbum : selectedPhoto.getFile() :"+selectedPhoto.getFile());
                System.out.println("PhotoinAlbum : "+ count);
                return count;

            }else {
                count++;
            }


        }
        return count;
    }

    //public int tagInPhoto(){
    //    return ;
    //}

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



    public String getNextFilePath(){
        int count = 0;
        int size = thisAlbum.getPhotos().size();
        System.out.println(thisAlbum.toString());
        List<Photo> x = thisAlbum.getPhotos();

        for(Photo p : thisAlbum.getPhotos()){
            System.out.println("p.getFile() : "+p.getFile());
            System.out.println("selectedPhoto.getFile() : "+selectedPhoto.getFile());
            if(p.getFile().equals(selectedPhoto.getFile())){
                System.out.println(count);
                System.out.println(size);
                if(count == size-1){

                    return null;
                }else{
                    x.set(count, selectedPhoto); // fixes the weird error

                    count++;
                    System.out.println("else: "+count);
                    System.out.println("p inside else: "+p);
                    System.out.println("p file inside else"+p.getFile());
                    System.out.println("x :"+x.get(count));
                    selectedPhoto= x.get(count);
                    System.out.println(selectedPhoto);
                    System.out.println(selectedPhoto.getFile());
                    tagListView.setAdapter(null);
                    tagAdapter = new ArrayAdapter<Tag>(this, R.layout.tags_listview_detail, albumList.get(albuminlist(thisAlbum.getName())).getPhoto(photoinAlbum()).getTags());
                    tagListView.setAdapter(tagAdapter);
                    tagAdapter.notifyDataSetChanged();
                    return x.get(count).getFile();

                }

            }else {
                count++;
            }

        }
        return null;
    }

    public String getPreviousFilePath(){
        int size = thisAlbum.getPhotos().size();
        int count = 0;

        System.out.println(thisAlbum.toString());
        List<Photo> x = thisAlbum.getPhotos();

        for(Photo p : thisAlbum.getPhotos()){
            System.out.println("p.getFile() : "+p.getFile());
            System.out.println("selectedPhoto.getFile() : "+selectedPhoto.getFile());
            if(p.getFile().equals(selectedPhoto.getFile())){
                System.out.println(count);
                System.out.println(size);
                if(count == 0){

                    return null;
                }else{
                    x.set(count, selectedPhoto);
                    count--;
                    System.out.println("else: "+count);
                    System.out.println("p inside else: "+p);
                    System.out.println("p file inside else"+p.getFile());
                    System.out.println("x :"+x.get(count));
                    selectedPhoto= x.get(count);
                    System.out.println(selectedPhoto);
                    System.out.println(selectedPhoto.getFile());
                    tagListView.setAdapter(null);
                    tagAdapter = new ArrayAdapter<Tag>(this, R.layout.tags_listview_detail, albumList.get(albuminlist(thisAlbum.getName())).getPhoto(photoinAlbum()).getTags());
                    tagListView.setAdapter(tagAdapter);
                    tagAdapter.notifyDataSetChanged();
                    return x.get(count).getFile();

                }

            }else {
                count++;
            }

        }

        return null;
    }

    private void addTagDialog() {
        tagType = "Person";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the type and then enter the value of the tag");
        String[] choiceTypes = {"Person", "Location"};
        builder.setSingleChoiceItems(choiceTypes, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tagType = choiceTypes[which];
            }
        });


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tagValue = input.getText().toString().trim();
                if(tagValue.equals("")) {
                    Toast.makeText(getBaseContext(), "No value entered", Toast.LENGTH_LONG).show();
                    return;
                }


                try {
                    addTag(tagType, tagValue);
                } catch (Exception e) {
                    dialog.cancel();
                    Toast.makeText(getBaseContext(), "This tag already exists.", Toast.LENGTH_LONG).show();
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

    private void deleteTagDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this tag?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTag();
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

    private void deleteTag() {
        System.out.println("selectedTag in delete : "+selectedTag);

        selectedPhoto.getTags().remove(selectedTag);
        selectedTag = null;

        int count =0;
        for (Photo p:thisAlbum.getPhotos()){
            System.out.println(count);
            if(p.getFile().equals(selectedPhoto.getFile())){
                albumList.get(albuminlist(thisAlbum.getName().toString())).getPhoto(photoinAlbum()).removeTag(selectedTagInt);
                count++;
                saveAlbumObject();
            }else{
                count++;
            }
        }
        tagAdapter.notifyDataSetChanged();
        resetSelection();

    }

    private void addTag(String tag, String value) throws Exception {
        for(Tag t: selectedPhoto.getTags()) {
            if(t.getTag().toLowerCase().equals(tag.toLowerCase()) && t.getValue().toLowerCase().equals(value.toLowerCase())) {
                throw new Exception();
            }
        }
        int count =0;
        System.out.println(selectedPhoto.getTags().toString());
        selectedPhoto.getTags().add(new Tag(tag, value));
        for (Photo p:thisAlbum.getPhotos()){
            System.out.println("printing all tags for albumlist count : " +count);

            System.out.println("printing all tags for albumlist : " +albumList.get(albuminlist(thisAlbum.getName().toString())).getPhoto(count).getTags());
            if(p.getFile().equals(selectedPhoto.getFile())){
               // p.addTag(tag, value);
               // thisAlbum.getPhoto(count).addTag(tag,value);

                albumList.get(albuminlist(thisAlbum.getName().toString())).getPhoto(count).addTag(tag,value);
                System.out.println(albumList.get(albuminlist(thisAlbum.getName().toString())).getPhoto(count).getTags());
                count++;
                saveAlbumObject();

            }else{
                count++;
            }
        }
        tagAdapter.notifyDataSetChanged();
    }


    private void selectTag(int pos) {
        System.out.println("selectTag pos : "+pos);
        selectedTag = selectedPhoto.getTags().get(pos);
        selectedTagInt = pos;
        System.out.println("selectTag pos : "+pos);
        System.out.println("selectTag  : "+selectedTag);
    }

    private void resetSelection() {
        selectedTag = null;
        tagListView.setAdapter(tagAdapter); // this somehow clears the selection highlight
        // Note: every time the photo is switched a new tag adapter must be created. This is because you can't
        // change the arraylist it's using after obejct creation
    }
}
