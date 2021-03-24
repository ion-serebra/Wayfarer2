package com.oshaev.wayfarer2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;

public class ShowImageActivity extends AppCompatActivity {

    ImageButton backButton;
    ImageView showingImage;
    ShapeableImageView shapeableShowingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        supportPostponeEnterTransition();
        supportStartPostponedEnterTransition();
        showingImage = findViewById(R.id.showingImage);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        //Glide.with(showingImage.getContext()).load(intent.getStringExtra("path")).

                Glide.with(showingImage.getContext())
                .asBitmap()
                .load(intent.getStringExtra("path"))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        //messageImg.setMaxHeight(resource.getScaledHeight(3));
                        ShapeAppearanceModel shapeAppearanceModel = new ShapeAppearanceModel();
                        //showingImage.setShapeAppearanceModel(shapeAppearanceModel.toBuilder().setAllCorners(CornerFamily.ROUNDED, 90).build());
                        showingImage.setImageBitmap(resource);
                        showingImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        //showingImage.setAdjustViewBounds(true);
                    }
                });
    }
}
