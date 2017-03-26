package com.mrdolphin.red.didroide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Array;

/**
 * Created by Red on 11.05.2016.
 */
public class MainList extends ListFragment {

    String data[] = new String[] { "", "ANON1"};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT).show();

        final RequestQueue queue = Volley.newRequestQueue(this.getContext());

        String ip = "192.168.1.100"+":8080";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(ip)
                .appendPath("Test2")
                .appendPath("response")
                .appendQueryParameter("p_id", String.valueOf("08906"));
        String myUrl = builder.build().toString();

        Log.d("BeforeDwnPat: ", "Success");


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("DwnPat: ", "Success");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorResponse: ", error.toString());
            }
        });

        queue.add(stringRequest);

        Intent intent = new Intent(getActivity(), SeriesList.class);
        intent.putExtra("pos", position-1);
        startActivity(intent);
    }
}
