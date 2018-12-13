package com.example.photos70.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.photos70.model.Album;
import com.example.photos70.model.Photo;
import com.example.photos70.model.Tag;

import java.util.ArrayList;

public class DisplayAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<Photo> photos;

    public DisplayAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;


    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        Photo photo = photos.get(position);
        Bitmap bitmap = photo.getImage();
        imageView.setImageBitmap(bitmap);
        imageView.setPadding(5,5,5,5);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,500);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }
}
