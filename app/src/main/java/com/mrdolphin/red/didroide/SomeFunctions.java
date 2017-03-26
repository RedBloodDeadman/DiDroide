package com.mrdolphin.red.didroide;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.params.HttpParams;

import java.io.IOException;

import static java.lang.String.valueOf;

/**
 * Created by Red on 11.05.2016.
 */
public class SomeFunctions {

public static void openNetImage(String url, Context cont, final TouchImageView imView) {

    // Retrieves an image specified by the URL, displays it in the UI.
    ImageRequest request = new ImageRequest(url,
            new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    imView.setImageBitmap(bitmap);
                }
            }, 0, 0, null,
            new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                }
            });
// Access the RequestQueue through your singleton class.
    MySingleton.getInstance(cont).addToRequestQueue(request);

}

    public static void openNetImage(String url, Context cont, final ImageView imView) {

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(cont).addToRequestQueue(request);

    }

}
