package com.jemsbond.jbgallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ImageFolderAdapter extends RecyclerView.Adapter<ImageFolderAdapter.ImageFolder_ViewHolder> {

    String[] folder_list;
    String[] initial_Image_Path;
    String[] total_Images;
    Context context;
    LayoutInflater layoutInflater;

    public ImageFolderAdapter(Context context, String[] folder_list, String[] initial_Image_Path,String[] total_Images) {
        this.folder_list = folder_list;
        this.initial_Image_Path = initial_Image_Path;
        this.context = context;
        this.total_Images = total_Images;
        layoutInflater=LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ImageFolder_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.image_folder_list,parent,false);

        return new ImageFolder_ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFolder_ViewHolder holder, final int position) {

        //Bitmap b = BitmapFactory.decodeFile(initial_Image_Path[position]);
        Glide.with(context).load(initial_Image_Path[position]).into(holder.iv);
        holder.tv.setText(folder_list[position]);
        holder.tv_num.setText(total_Images[position]);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ImageLists.class);
                i.putExtra("FolderName",folder_list[position]);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return folder_list.length;
    }

    public class ImageFolder_ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv;
        TextView tv,tv_num;
        LinearLayout layout;


        public ImageFolder_ViewHolder(@NonNull View itemView) {

            super(itemView);
            iv=(ImageView)itemView.findViewById(R.id.folder_imageView);
            tv=(TextView)itemView.findViewById(R.id.folder_textView);
            tv_num=(TextView)itemView.findViewById(R.id.folder_tv_no);
            layout=(LinearLayout)itemView.findViewById(R.id.folder_layout);
        }
    }
}
