package com.jemsbond.jbgallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class MediaStoreAdapter extends RecyclerView.Adapter<MediaStoreAdapter.ViewHolder>
{

    private String[] mData;
    private LayoutInflater inflater;
    Context c;
    int shape;

    MediaStoreAdapter(Context con,String[] data,int shape)
    {
        this.mData=data;
        this.inflater = LayoutInflater.from(con);
        this.c=con;
        this.shape=shape;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.image,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.ic_launcher);
        options.placeholder(R.mipmap.ic_launcher);

        if(shape == R.id._round)
        {
            options.circleCrop();
        }
        else
        {
            options = new RequestOptions();
        }

        Glide.with(c).load(mData[position]).apply(options).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(c,ViewImage.class);
                intent.putExtra("ImagePath",mData[position]);
                c.startActivity(intent);
            }
        });


        /*
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true; // this means we just need the image information without loading the image into the memory
        options.inSampleSize=calculateInSampleSize(options,500,500);*/


        /*options.inJustDecodeBounds=false; // now we allow to load image in the memory
        Bitmap smallBitmap = BitmapFactory.decodeFile(mData[position],options);
        holder.imageView.setImageBitmap(smallBitmap);*/

    }

    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight)
    {
        //Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth)
        {
            final int halfHeight = height/2;
            final int halfWidth = width/2;


            //Calculate the largest inSampleSize value that is a power of 2 and keeps both
            //height and width larger than the requested height and width.
            while((halfHeight / inSampleSize) >= reqHeight && (halfWidth/inSampleSize) >= reqWidth)
            {
                inSampleSize*=2;
            }
        }
        return inSampleSize;
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        Button mButton;
        ImageView imageView;


        ViewHolder(View itemView)
        {
            super(itemView);
            /*mButton=itemView.findViewById(R.id.btn_no);
            mButton.setOnClickListener(this);*/
            imageView = itemView.findViewById(R.id.image_iv1);
        }

    }
}