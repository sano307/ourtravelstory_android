package com.example.user.test_ots;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

public class MystoryDetailActivity extends AppCompatActivity {
    private ImageView story_represent_image;

    public static final String EXTRA_POSTION = "mystory_detail";

    private AQuery aq = new AQuery(this);

    Intent received_intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystorydetail);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        received_intent = getIntent();

        collapsingToolbar.setTitle(received_intent.getExtras().getString("StoryName"));
        story_represent_image = (ImageView) findViewById(R.id.detail_story_image);
        story_represent_image.setImageBitmap((Bitmap) received_intent.getExtras().get("StoryRepresentImage"));
    }
}
