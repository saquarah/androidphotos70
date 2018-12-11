package com.example.photos70.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.photos70.model.Photo;

public class DisplayAdapter extends BaseAdapter {

    private Context context;
    private Photo display;

    public DisplayAdapter(Context context, Photo photo) {
        this.context = context;
        this.display = photo;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return display;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        Photo photo = display;
        Bitmap bitmap = photo.getImage();
        imageView.setImageBitmap(bitmap);
        imageView.setPadding(5,5,5,5);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
        return imageView;
    }
}
