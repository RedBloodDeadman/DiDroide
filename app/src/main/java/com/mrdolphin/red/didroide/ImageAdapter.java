package com.mrdolphin.red.didroide;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import static android.content.Intent.getIntent;
import static java.lang.String.valueOf;


/**
 * Created by mrdolphin on 02.10.16.
 */

public class ImageAdapter extends BaseAdapter {
    String ipM = Var.getInstance().getip()+":8080";
    String positIDM = Var.getInstance().getpID();
    String positSerM = Var.getInstance().getnSer();
    private Context mContext;
    //final RequestQueue queue = Volley.newRequestQueue(mContext);
    public ImageAdapter(Context c) {
        mContext = c;
    }


    @Override
    public int getCount() {
        Log.d("iaMax", String.valueOf(Var.getInstance().getMaxImagesOnSeries()));
        return Var.getInstance().getMaxImagesOnSeries()+1;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView;

        Log.d("getView", "Start");

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
            //imageView.setPadding(100, 100, 100, 100);
        } else {
            imageView = (ImageView) convertView;
        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(valueOf(ipM))
                .appendPath("Test2")
                .appendPath("DownloadFileServlet")
                .appendQueryParameter("p_id", valueOf(positIDM))
                .appendQueryParameter("series_number", valueOf(positSerM))
                //.appendQueryParameter("image_number", String.valueOf(images[progress]))
                .appendQueryParameter("image_number", valueOf(position));
        String myUrl = builder.build().toString();
/*
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(myUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(mContext).addToRequestQueue(request);
*/


        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); // Получили экземпляр
        //imageLoader.init(ImageLoaderConfiguration.createDefault(mContext)); // Проинициализировали конфигом по умолчанию

        DisplayImageOptions options = new DisplayImageOptions.Builder()

                .cacheInMemory()
                .cacheOnDisc()
                //.decodingOptions(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .resetViewBeforeLoading()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

        imageLoader.displayImage(myUrl, imageView, options); // Запустили асинхронный показ картинки

        //queue.add(request);

        Log.d("getView", "Return imageView");

        return imageView;
    }

    public void setIPM(String ip){
        ipM = ip;
    }

    public void setpositIDM(String positID){
        positIDM = positID;
    }

    public void setpositSerM(String positSer){
        positSerM = positSer;
    }

}





