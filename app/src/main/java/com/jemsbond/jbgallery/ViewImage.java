package com.jemsbond.jbgallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ViewImage extends AppCompatActivity {

    ImageView imageView;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor=1.0f;

    LinearLayout layout;
    LinearLayout options_layout;
    LinearLayout image_layout;

    String ImagePath;
    Bitmap b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        layout = (LinearLayout)findViewById(R.id.full_screen_layout);
        options_layout=(LinearLayout)findViewById(R.id.img_options_layout);
        image_layout=(LinearLayout)findViewById(R.id.layout_image);


        Intent i = getIntent();
        ImagePath = i.getStringExtra("ImagePath");


        b = BitmapFactory.decodeFile(ImagePath);

        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_click(v);
            }
        });

        Glide.with(this).load(ImagePath).into(imageView);

        mScaleGestureDetector = new ScaleGestureDetector(this,new ScaleListener());

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }
/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mScaleGestureDetector.onTouchEvent(ev);
    }
*/

    public void share_click(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        Uri uri = Uri.parse(ImagePath);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(intent,"Share Image"));

    }

    public void img_click(View view) {

        if(options_layout.getVisibility() == View.VISIBLE)
        {
            layout.setBackgroundColor(Color.BLACK);
            options_layout.setVisibility(View.INVISIBLE);
            getSupportActionBar().hide();
            int height = getSupportActionBar().getHeight();
            layout.setPadding(0,height,0,0);
        }
        else
        {
            layout.setBackgroundColor(Color.WHITE);
            options_layout.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
            layout.setPadding(0,0,0,0);
        }
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            mScaleFactor *= mScaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,Math.min(mScaleFactor,5f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);

            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        Log.e("***","--->Closed");
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1,1,1,"Enable Pinch(Zoom IN-OUT)");

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == 1) {
            if (item.getTitle().equals("Enable Click")) {
                item.setTitle("Enable Pinch(Zoom IN-OUT)");
                imageView.setOnTouchListener(null);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img_click(v);
                    }
                });
                Glide.with(this).load(ImagePath).into(imageView);

            } else {
                item.setTitle("Enable Click");
                imageView.setOnClickListener(null);
                imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mScaleGestureDetector.onTouchEvent(event);
                    }
                });
            }
        }


        return super.onOptionsItemSelected(item);
    }
}
