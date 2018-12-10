package com.example.photos70.model;

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
    private String caption;
    private transient Image image;
    private transient Image thumbnail;
    private File file;
//    private transient ObservableList<Tag> tags = FXCollections.observableArrayList();
//    private Calendar date = Calendar.getInstance();

    /**
     * Initializes the photo
     * @param image the image of the photo
     * @param file the file the photo is from
     */
    public Photo(Image image, File file) {
        this.image = image;
 //       this.date = date;
        this.file = file;
        try {
            this.thumbnail = createThumbnail();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        caption = "";
    }
    private Image createThumbnail() throws MalformedURLException {
        //TODO make thumbnail somehow
   //     Image thumbnail = new Image(file.toURI().toURL().toString(), 100, 100, false, false);
        //Image thumbnail = image;
        return thumbnail;
    }

    /**
     *
     * @return the image of the photo
     */
    public Image getImage() {
        return image;
    }
    /**
     *
     * @return the photo's thumbnail
     */
    public Image getThumbnail() {
        return thumbnail;
    }
    /**
     *
     * @return the photo's caption
     */
    public String getCaption() {
        return caption;
    }
    /**
     * Change the caption of the photo
     * @param caption the new caption
     */
//    public void setCaption(String caption) {
//        this.caption = caption;
//    }
    /**
     *
     * @return the list of all tags the photo has
     */
    //TODO make tags in just an arrayList and implement proper methods
//    public ObservableList<Tag> getTags() {
//        return tags;
//    }
    /**
     *
     * @return the file that the photo is from
     */
    public File getFile() {
        return file;
    }
    /**
     *
     * @return the date the photo was taken
     */
//    public Calendar getDate() {
//        return date;
//    }
    /**
     * Sets the image for the photo
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
    }
    /**
     * Sets the thumbnail for the photo
     * @param thumbnail
     */
    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }
    /**
     * sets the list of tags for the photo
     * @param tags
     */
    //TODO marker for method that needs to use arrayList
//    public void setTags(ObservableList<Tag> tags) {
//        this.tags = tags;
//    }
    /**
     * Adds a new tag to the photo
     * @param type the name of the tag
     * @param value the tag's value
     */
//    public void addTag(String type, String value) {
//        Tag newTag = new Tag(type, value);
//        tags.add(newTag);
//    }

//    private void writeObject(ObjectOutputStream out) throws IOException {
//        out.defaultWriteObject();
//
//        out.writeObject(new ArrayList<Tag>(tags));
//
//
//    }

    //TODO fix reading in photo
//    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
//        //System.out.println("reading");
//        in.defaultReadObject();
//
//        image = new Image(file.toURI().toURL().toString());
//        thumbnail = createThumbnail();
//
//
//
//
//
//        tags = FXCollections.observableArrayList();
//        List<Tag> list = (List<Tag>)in.readObject();
//        if(!list.isEmpty()) {
//            tags.addAll(list);
//        }
//        else {
//            tags = FXCollections.observableArrayList();
//        }
//
//
//    }
}
