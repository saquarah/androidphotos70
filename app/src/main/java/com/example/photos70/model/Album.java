package com.example.photos70.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class models an Album
 * @author Sarah Squillace, Nikita Zala
 *
 */
public class Album implements Serializable{
    private String name;
    private List<Photo> photos;

    /**
     * Initializes album
     * @param name the album's name
     */
    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<Photo>();
    }

    /**
     *
     * @return the album's name
     */
    public String getName() {
        return name;
    }

    /**
     * Shows string representation of album, showing the name, number of photos, and
     * the earliest and latest dates
     */
    public String toString() {
        String photoPlur = "photos";
        if(photos.size() == 1) {
            photoPlur = "photo";
        }
        if(photos.size() == 0) {
            return name + " | 0 photos |";
        }
        return name + "|" + photos.size() + " " + photoPlur;
    }
    /**
     * Changes album name
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds the photo to the album, also updating earliest and latest dates
     * @param photo
     */
    public void addToAlbum(Photo photo) {
        photos.add(photo);
    }
    /**
     * Removes the photo from the album, also updating earliest and latest dates
     * @param photo
     */
    public void removeFromAlbum(Photo photo) {
        photos.remove(photo);

    }


    /**
     *
     * @return the list of photos that the album contains
     */
    public List<Photo> getPhotos() {
        return photos;
    }



}

