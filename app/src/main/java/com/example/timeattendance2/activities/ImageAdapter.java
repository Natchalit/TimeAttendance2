package com.example.timeattendance2.activities;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.timeattendance2.model.Images;

import java.io.File;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    Uri[] imgs;

    public ImageAdapter(Context mContext, Images[] detail) {
        this.mContext = mContext;
        imgs = new Uri[detail.length];
        for (int i = 0; i < detail.length; i++) {
            File f = new File(detail[i].getUrl());
            Uri paths = Uri.fromFile(f);
            imgs[i] = paths;
        }
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return imgs[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageURI(imgs[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}
