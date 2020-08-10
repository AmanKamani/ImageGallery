package com.jemsbond.jbgallery;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageFolderList extends AppCompatActivity {

    RecyclerView rv;
    static String orderby;
    private final int READ_EXTERNAL_STORAGE_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder_list);

        getSupportActionBar().setTitle("JB Gallery");

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE_CODE);
        }
        else {

            LoadImageFolders();
        }
    }

    private void LoadImageFolders() {
        rv = (RecyclerView) findViewById(R.id.rv_folderList);

        ArrayList<String> folder_list = new ArrayList<String>();
        ArrayList<String> Image_list = new ArrayList<String>();
        ArrayList<String> Total_Images = new ArrayList<String>();

        ContentResolver cr = getContentResolver();


        orderby = LocalAppSettings.getOrderBy();

        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"Distinct " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME}, null, null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + orderby);

        while (cursor.moveToNext()) {
            String folder_name = cursor.getString(0);
            folder_list.add(folder_name);

            Cursor cur2 = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Thumbnails.DATA}, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?", new String[]{folder_name}, MediaStore.Images.Thumbnails.DATA + " Limit 1");

            cur2.moveToFirst();
            Image_list.add(cur2.getString(cur2.getColumnIndex(MediaStore.Images.Thumbnails.DATA)));

            Cursor cur3 = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Thumbnails.DATA}, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?", new String[]{folder_name}, null);

            Total_Images.add(String.valueOf(cur3.getCount()));
        }

        Object[] arr1 = folder_list.toArray();
        Object[] arr2 = Image_list.toArray();
        Object[] arr3 = Total_Images.toArray();

        String[] folder_arr = Arrays.copyOf(arr1, arr1.length, String[].class);
        String[] image_arr = Arrays.copyOf(arr2, arr2.length, String[].class);
        String[] total_arr = Arrays.copyOf(arr3, arr3.length, String[].class);

        rv.setLayoutManager(new LinearLayoutManager(this));
        ImageFolderAdapter adpt = new ImageFolderAdapter(ImageFolderList.this, folder_arr, image_arr, total_arr);
        rv.setAdapter(adpt);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            LoadImageFolders();
        }
        else
        {
            Toast.makeText(this, "Please Allow to open JB Gallery", Toast.LENGTH_SHORT).show();
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String title="";
        if(LocalAppSettings.getOrderBy().equals(" ASC"))
            title="Descending";
        else
            title="Ascending";

        menu.add(1,1,1,title);
        Log.e("***","option created");

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == 1)
        {
            if(item.getTitle().equals("Ascending"))
            {
                item.setTitle("Descending");
                LocalAppSettings.setOrderBy(" ASC");
                recreate();
            }
            else if(item.getTitle().equals("Descending"))
            {
                item.setTitle("Ascending");
                LocalAppSettings.setOrderBy(" DESC");
                recreate();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
