package com.example.photos70.model;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable {

    private static final long serialVersionUID = -6324409348554636338L;
    private transient Bitmap image;
    private transient Bitmap thumbnail;
    private File file;
    private ArrayList<Tag> tags = new ArrayList<Tag>();
//    private transient ObservableList<Tag> tags = FXCollections.observableArrayList();
//    private Calendar date = Calendar.getInstance();

    /**
     * Initializes the photo
     * @param image the image of the photo
     */
    public Photo(Bitmap image,File file) {
        this.image = image;
 //       this.date = date;
        this.thumbnail = image;
        this.file = file;
//        try {
//            this.thumbnail = createThumbnail();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }
//    private Image createThumbnail() throws MalformedURLException {
//        //TODO make thumbnail somehow
//   //     Image thumbnail = new Image(file.toURI().toURL().toString(), 100, 100, false, false);
//        //Image thumbnail = image;
//        return thumbnail;
//    }

    public String getFile(){
        return this.file.toString();
    }

    /**
     *
     * @return the image of the photo
     */
    public Bitmap getImage() {
        return image;
    }
    /**
     *
     * @return the photo's thumbnail
     */
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    /**
     *
     * @return the list of all tags the photo has
     */
    //TODO make tags in just an arrayList and implement proper methods
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Sets the thumbnail for the photo
     * @param thumbnail
     */
    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
    /**
     * Adds a new tag to the photo
     * @param type the name of the tag
     * @param value the tag's value
     */
    public void addTag(String type, String value) {
        Tag newTag = new Tag(type, value);
        tags.add(newTag);
    }

//    private void writeObject(ObjectOutputStream out) throws IOException {
//        out.defaultWriteObject();
//
//        out.writeObject(new ArrayList<Tag>(tags));
//
//
//    }

    /*
    //TODO fix reading in photo
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
       //System.out.println("reading");
        in.defaultReadObject();

        image = new Image(file.toURI().toURL().toString());
        thumbnail = createThumbnail();





        tags = FXCollections.observableArrayList();
        List<Tag> list = (List<Tag>)in.readObject();
        if(!list.isEmpty()) {
            tags.addAll(list);
        }
        else {
            tags = FXCollections.observableArrayList();
        }


    }
    */
}
