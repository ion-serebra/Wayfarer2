package com.oshaev.wayfarer2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserPhotosAdapter extends RecyclerView.Adapter<UserPhotosAdapter.UserPhotosViewHolder> {

    ArrayList<String> photosUrlList;
    Context context;

    public UserPhotosAdapter(ArrayList<String> photosUrlList, Context context) {
        this.photosUrlList = photosUrlList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserPhotosAdapter.UserPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_photo_card, parent, false);
        UserPhotosViewHolder holder = new UserPhotosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserPhotosAdapter.UserPhotosViewHolder holder, int position) {
        Glide.with(holder.userPhoto)
                .load(photosUrlList.get(position))
                .into(holder.userPhoto);
    }

    @Override
    public int getItemCount() {
        return photosUrlList.size();
    }

    public class UserPhotosViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        public UserPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.userPhoto);
            userPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent showImageIntent = new Intent(context, ShowImageActivity.class);
                    Bundle bundle;
                    //View messageImage = view.findViewById(R.id.messageImg);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) (context),
                            userPhoto, "showingImage");
                    bundle = options.toBundle();
                    showImageIntent.putExtra("path", photosUrlList.get(getAdapterPosition()));

                    if (bundle == null) {
                        context.startActivity(showImageIntent);
                    } else {
                        context.startActivity(showImageIntent, bundle);
                    }
                }
            });
        }
    }
}
