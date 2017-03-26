package com.mrdolphin.red.didroide;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Ser_List extends AppCompatActivity {

    ArrayList<String> id_list = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();

    String IP = "192.168.1.147";

    String PID;
    String SerNum;

    Ser_List.MyTask mt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ser_list);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        String resultJson = getIntent().getStringExtra("p_res");

        Log.d("JSon_Result", resultJson);
        try {
            JSONObject object = new JSONObject(resultJson);
            JSONArray Jarray = object.getJSONArray("patients");
            for (int i = 0; i < Jarray.length(); i++) {
                list.add(Jarray.getJSONObject(i).getString("series").toString());
                id_list.add(Jarray.getJSONObject(i).getString("id").toString());
                Log.d("TestRes", Jarray.getJSONObject(i).getString("id").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_ser_list);

        Log.d("TestRes2", list.get(0));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ser_List.this,
                android.R.layout.simple_list_item_1, list ) {
        };
        adapter.notifyDataSetChanged();
        ListView ser_List = (ListView) findViewById(R.id.ser_List);
        ser_List.setAdapter(adapter);

        ser_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("LOG_TAG", "itemClick: position = " + position + ", id = "
                        + id);
                Toast toast = Toast.makeText(getApplicationContext(), id_list.get(position)
                        , Toast.LENGTH_SHORT);
                toast.show();

                mt = new Ser_List.MyTask();
                mt.execute(id_list.get(position).toString(), list.get(position).toString());


                //Intent intent = new Intent(Pat_List.this, Ser_List.class);
                //intent.putExtra("p_id", id_list.get(position));
                //startActivity(intent);
            }
        });

    }

    class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... params) {

            Log.d("aTask", "Begin");
            PID = params[0];
            Log.d("PID", PID);
            SerNum = params[1];
            Log.d("SerNu", SerNum);

            String ip = IP+":8080";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(ip)
                    .appendPath("Test2")
                    .appendPath("response")
                    .appendQueryParameter("p_id", String.valueOf(PID));
            String myUrl = builder.build().toString();

            Log.d("Con", "Beg");
            URL url = null;
            try {
                url = new URL(myUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream in = null;
                try {
                    in = new BufferedInputStream(urlConnection.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuilder result = new StringBuilder();
                    String line;
                    try {
                        while((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("URLConnection Result", result.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            } finally {
                urlConnection.disconnect();
            }
            /////

            Uri.Builder builder2 = new Uri.Builder();
            builder2.scheme("http")
                    .encodedAuthority(ip)
                    .appendPath("Test2")
                    .appendPath("MaxSize")
                    .appendQueryParameter("p_id", PID)
                    .appendQueryParameter("series_number", SerNum);
            String myUrl2 = builder2.build().toString();

            final RequestQueue queue2 = Volley.newRequestQueue(Ser_List.this);
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, myUrl2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response max", response);
                            //maxI = Integer.valueOf(response)-1;
                            Intent intent = new Intent(Ser_List.this, Grid_View.class);
                            intent.putExtra("ip", IP);
                            intent.putExtra("pos", PID);
                            intent.putExtra("ser", SerNum);
                            intent.putExtra("maxImages",Integer.valueOf(response)-1);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Ser_List.this);
                    builder.setTitle(R.string.Error)
                            .setMessage(R.string.Retype)
                            .setCancelable(false)
                            .setNegativeButton(R.string.Close,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            // Add the request to the RequestQueue.
            queue2.add(stringRequest2);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
