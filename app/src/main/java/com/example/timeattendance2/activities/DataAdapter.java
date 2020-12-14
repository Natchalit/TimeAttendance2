package com.example.timeattendance2.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.timeattendance2.R;
import com.example.timeattendance2.model.Images;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends BaseAdapter {
    private ArrayList<Images> imageUrls;
    private Context context;

    public DataAdapter(Context context, ArrayList<Images> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;

    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return imageUrls.get(position).getStaffid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            row = LayoutInflater.from(context).inflate(R.layout.image_layout, null);
            holder = new ViewHolder(row);
            holder.img = (ImageView) row.findViewById(R.id.imageView2);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

       Images detail = imageUrls.get(position);
        String url = "https://advancedchecker.blob.core.windows.net/c01/" + detail.getUrl();
        //Glide.with(context).load().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.img);
        Picasso.get().load(url).resize(300, 400).into(holder.img);
        return row;
    }

    public class ViewHolder {

        ImageView img;

        public ViewHolder(View view) {
            img = view.findViewById(R.id.imageView);
        }
    }
}