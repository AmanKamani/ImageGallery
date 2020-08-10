import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;

public class CompressImageFile {

    public static void getCompressedImageFile(File file, Context context)
    {
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds=true;

            /*if(getFileExt(file.getName()).equals("png") || getFileExt(file.getName()).equals("PNG"))
            {
                options.inSampleSize = 6;
            }
            else
            {
                options.inSampleSize = 6;
            }*/

            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream,null,options);
            inputStream.close();

            final int REQUIRED_SIZE=100;

            int scale=1;
            while((options.outWidth/scale/2 >= REQUIRED_SIZE) && (options.outHeight/scale/2 >= REQUIRED_SIZE))
            {
                scale*=2;
            }

            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize=scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream,null,options2);
            ExifInterface ei = new ExifInterface(file.getAbsolutePath());

        }
        catch (Exception ex)
        {

        }
    }

}
