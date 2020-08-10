package com.jemsbond.jbgallery;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageLists extends AppCompatActivity {

    /******Just for Gallery Control******/
    Gallery g1;
    String array[]={"kem 60","how are"};

    /******For GridView********/
    GridView gv1;
    Cursor cur;
    RecyclerView myrv;
    MediaStoreAdapter adpt;
    private RecyclerView.LayoutManager lm;

    LinearLayout layout;
    int shape,theme;
    String folder_name;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_lists);

        folder_name = getIntent().getStringExtra("FolderName");

        layout = (LinearLayout) findViewById(R.id.layout_all_images);

        shape = LocalAppSettings.getShape();
        theme = LocalAppSettings.getTheme();

        if (theme == R.id._light)
            layout.setBackgroundColor(Color.WHITE);
        else
            layout.setBackgroundColor(Color.BLACK);

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,MediaStore.Images.Media.BUCKET_DISPLAY_NAME+"=?", new String[]{folder_name}, "datetaken");

        ArrayList<String> ls = new ArrayList<String>();

        for (int i = 0; i < cur.getCount(); i++) {
            cur.moveToNext();
            ls.add(cur.getString(cur.getColumnIndex("_DATA")));
            /*String sss = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
            Log.e("***", sss + "-->" + ls.get(i));*/
        }

        Object[] objpaths = ls.toArray();
        String[] strPaths = Arrays.copyOf(objpaths, objpaths.length, String[].class);

        RecyclerView rv = findViewById(R.id.myrv);
        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setLayoutManager(new GridLayoutManager(this, 4));

        adpt = new MediaStoreAdapter(this, strPaths, shape);
        rv.setAdapter(adpt);

        //DisplayColumnName(cur);
        DisplayDistinctBucket();
    }

    private void DisplayDistinctBucket() {

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[]{"Distinct "+MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME},null,null,null);
        Log.e("***","Total Columns : "+cur.getCount());
    }

    private void DisplayColumnName(Cursor cur) {
            for (String s:cur.getColumnNames()
             ) {
            Log.e("***",s);
        }
        /*for(int i=0;i<cur.getCount();i++)
        {
            Log.e("***",cur.getString(cur.getColumnIndex(MediaStore.Images.Thumbnails._ID)));
        }*/
    }

    /* gv1=(GridView)findViewById(R.id.gv1);
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        cur.moveToFirst();
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getApplicationContext(),R.layout.image,cur,new String[]{MediaStore.Images.Thumbnails.DATA},new int[]{R.id.image_iv1});

        gv1.setAdapter(sca);
*/
    boolean isGranted=false;
    int STORAGE_PERMISSION_CODE=1;
    public void CheckForPermission()
    {
        if(ContextCompat.checkSelfPermission(ImageLists.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            isGranted=true;
        }
        else
        {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Storage Permission Required")
                    .setMessage("Allow to access the storage.")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            isGranted=true;
                            ActivityCompat.requestPermissions(ImageLists.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isGranted=false;
                            dialog.dismiss();
                        }
                    }).create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                isGranted=true;
            }
            else
            {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
                isGranted=false;
            }
        }
    }

    private void DisplayInGalleryControl() {/*
        g1=(Gallery)findViewById(R.id.gallery1);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_gallery_item,array);

        g1.setAdapter(aa);

        g1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ImageLists.this, array[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.dot_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id._dark:

                LocalAppSettings.setTheme(R.id._dark);
                layout.setBackgroundColor(Color.BLACK);
                break;
            case R.id._light:

                LocalAppSettings.setTheme(R.id._light);
                layout.setBackgroundColor(Color.WHITE);
                break;
            case R.id._round:
                if(shape != R.id._round)
                {
                    shape = R.id._round;
                    recreate();
                }
                LocalAppSettings.setShape(R.id._round);
                break;
            case R.id._rect:
                if(shape != R.id._rect)
                {
                    shape = R.id._rect;
                    recreate();
                }
                LocalAppSettings.setShape(R.id._rect);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}


/****
 /*ArrayList<String> lst = new ArrayList<String>();

 for(int i=0;i<5000;i++)
 {
 lst.add(String.valueOf(i+1));
 }
 Log.e("***","Before data");
 String[] data = new String[lst.size()];
 for(int i=0;i<data.length;i++)
 {
 data[i]=String.valueOf(i);
 }
 Log.e("***","After data");
 RecyclerView rv = findViewById(R.id.myrv);
 int numOfColumns=3;
 rv.setLayoutManager(new GridLayoutManager(this,numOfColumns));
 adpt = new MediaStoreAdapter(this,data);
 adpt.setClicklistener(this);
 rv.setAdapter(adpt);
******/