package com.test.myapplication.adadpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test.myapplication.R;

import java.util.ArrayList;


public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {

    private ArrayList<Uri> lisOfUris;
    private Context mContext;

    public ThumbnailAdapter(ArrayList<Uri> lisOfUris, Context mContext) {
        this.lisOfUris = lisOfUris;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ThumbnailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.ivImageItem.setImageURI(lisOfUris.get(position));
    }

    @Override
    public int getItemCount() {
        return lisOfUris.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivImageItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageItem = itemView.findViewById(R.id.imageItem);
        }
    }

    public void updateItems(ArrayList<Uri> uris) {
        lisOfUris = uris;
    }
}
