package com.oshaev.wayfarer2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class ShowArticleActivity extends AppCompatActivity {

    ImageView showArticleImage;
    TextView showArticleTitle;
    TextView showArticlePaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        showArticleImage = findViewById(R.id.showArticleImage);
        showArticleTitle = findViewById(R.id.showArticleTitle);
        showArticlePaper = findViewById(R.id.showArticlePaper);

        Intent intent = getIntent();
        if(intent.getStringExtra("imgUrl")!=null) {
            Glide.with(showArticleImage)
                    .load(intent.getStringExtra("imgUrl"))
                    .into(showArticleImage);
        }
        showArticleTitle.setText(intent.getStringExtra("title"));
        showArticlePaper.setText(intent.getStringExtra("paper"));

    }
}
