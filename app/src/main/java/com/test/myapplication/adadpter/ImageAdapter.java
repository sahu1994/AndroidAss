package com.test.myapplication.adadpter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test.myapplication.R;
import com.test.myapplication.listeners.SelectedCallback;
import com.test.myapplication.model.ImageStatus;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final ArrayList<ImageStatus> imageStatuses;
    private final SelectedCallback selectedCallback;
    private ArrayList<Integer> images;
    private Context mContext;

    public ImageAdapter(ArrayList<Integer> images, ArrayList<ImageStatus> imageStatuses, Context mContext, SelectedCallback selectedCallback) {
        this.images = images;
        this.mContext = mContext;
        this.imageStatuses = imageStatuses;
        this.selectedCallback = selectedCallback;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.ivImageItem.setImageResource(images.get(position));
        viewHolder.ivImageItem.setOnClickListener(new View.OnClickListener() {
            private boolean isFlipped;

            @Override
            public void onClick(View view) {
                if (!isFlipped) {
                    AnimatorSet shrinkSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.shrink_to_middle);
                    shrinkSet.setTarget(viewHolder.ivImageItem);
                    shrinkSet.start();
                    viewHolder.ivImageItem.setImageResource(R.drawable.ic_launcher_background);
                    imageStatuses.get(position).setSelected(true);
                } else {
                    AnimatorSet growSet = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.grow_from_middle);
                    growSet.setTarget(viewHolder.ivImageItem);
                    growSet.start();
                    imageStatuses.get(position).setSelected(false);
                }
                isFlipped = !isFlipped;
                selectedCallback.onSelection(getTotalCount());
            }
        });


    }

    private int getTotalCount() {
        int count = 0;
        for (ImageStatus imageStatus : imageStatuses) {
            if (imageStatus.isSelected()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivImageItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageItem = itemView.findViewById(R.id.ivImageItem);
        }
    }
}
