package com.mrdolphin.red.didroide;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ActivitySeriesCards extends AppCompatActivity {
    private RecyclerView mRecyclerView2;
    public EditText search;
    private ArrayList<String> serdescr_list = new ArrayList<String>();
    private ArrayList<String> serid_list = new ArrayList<String>();
    //private ArrayList<String> serpid_list = new ArrayList<String>();
    private ArrayList<String> series_mod = new ArrayList<String>();
    private List<String> filteredSerpidList = new ArrayList<>();
    private List<String> filteredSeriesidList = new ArrayList<>();
    private List<String> filteredSeriesdescrList = new ArrayList<>();
    private ArrayList<String> filteredSeriesModList = new ArrayList<String>();
    private String IP;
    public SimpleSerAdapter mAdapter2;
    private SharedPreferences mSettings;
    String PID;
    String SerNum;
    ActivitySeriesCards.MyTask2 mt;

    String ser_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        setContentView(R.layout.activity_series_cards);
        mSettings = getSharedPreferences(MyApp.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(MyApp.APP_PREFERENCES_IP)) {
            // Получаем IP из настроек
            IP = mSettings.getString(MyApp.APP_PREFERENCES_IP, "");
            //Toast.makeText(getApplicationContext(), IP, Toast.LENGTH_SHORT).show();
        }

        search = (EditText) findViewById(R.id.searchseries);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerviewseries);

        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        //idList();  // in this method, Create a list of items.

        // call the adapter with argument list of items and context.
        mAdapter2 = new SimpleSerAdapter(serid_list, serdescr_list, series_mod, this);
        mRecyclerView2.setAdapter(mAdapter2);

        addTextListener();

        String resultJson = getIntent().getStringExtra("p_res");


/*
        String string = null;
        try {
            byte[] ISO8859_5Data = resultJson.getBytes(); //данные в кодировке iso-8859-5
            string = new String(ISO8859_5Data,"ISO-8859-5"); //Преобразуем из iso-8859-5 в Unicode
            byte[] utf8Data = string.getBytes("UTF-8");// Преобразуем из Unicode в UTF8
            resultJson = utf8Data.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
*/

        String patName = getIntent().getStringExtra("p_name");
        final String patStadyInsta = getIntent().getStringExtra("p_studyInst");

        setTitle("Серии пациента " + patName);

        Log.d("JSon_Result", resultJson);
        try {
            JSONObject object = new JSONObject(resultJson);
            JSONArray Jarray = object.getJSONArray("patients");
            for (int i = 0; i < Jarray.length(); i++) {
                serid_list.add(Jarray.getJSONObject(i).getString("series").toString());
                //serpid_list.add(Jarray.getJSONObject(i).getString("id").toString());
                serdescr_list.add(Jarray.getJSONObject(i).getString("desc").toString());
                series_mod.add(Jarray.getJSONObject(i).getString("modal").toString());
                Log.d("TestRes", Jarray.getJSONObject(i).getString("series").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TestRes2", patStadyInsta);

        mRecyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mRecyclerView2 ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(filteredSeriesidList.isEmpty()){
                            //Toast toast = Toast.makeText(getApplicationContext(),
                            //        serid_list.get(position), Toast.LENGTH_SHORT);
                            //toast.show();
                            ser_name = serid_list.get(position);
                            mt = new ActivitySeriesCards.MyTask2();
                            mt.execute(patStadyInsta, serid_list.get(position).toString());
                        }else {
                            //Toast toast = Toast.makeText(getApplicationContext(),
                            //        filteredSeriesidList.get(position), Toast.LENGTH_SHORT);
                            //toast.show();
                            ser_name = serid_list.get(position);
                            mt = new ActivitySeriesCards.MyTask2();
                            mt.execute(patStadyInsta, filteredSeriesidList.get(position).toString());
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );


    }

    // this method is used to create list of items.
    public void idList() {
        for (int i = 0; i < 20; i++) {
            //serpid_list.add("id: " + i);
        }
    }

    public void addTextListener() {

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                filteredSeriesidList = new ArrayList<>();
                filteredSerpidList = new ArrayList<>();
                filteredSeriesdescrList = new ArrayList<>();
                filteredSeriesModList = new ArrayList<>();

                for (int i = 0; i < serid_list.size(); i++) {

                    final String idtext = serid_list.get(i).toLowerCase();
                    final String text = serdescr_list.get(i).toLowerCase();
                    if ((text.contains(query))||(idtext.contains(query))) {

                        //filteredSerpidList.add(serpid_list.get(i));
                        filteredSeriesidList.add(serid_list.get(i));
                        filteredSeriesModList.add(series_mod.get(i));
                        filteredSeriesdescrList.add(serdescr_list.get(i));

                    }
                }

                mRecyclerView2.setLayoutManager(new LinearLayoutManager(ActivitySeriesCards.this));
                mAdapter2 = new SimpleSerAdapter(filteredSeriesdescrList, filteredSeriesidList, filteredSeriesModList, ActivitySeriesCards.this);
                mRecyclerView2.setAdapter(mAdapter2);
                mAdapter2.notifyDataSetChanged();  // data set changed


            }
        });
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

    LoadingDialog dialog = new LoadingDialog();
    FragmentManager fm = getSupportFragmentManager();

    class MyTask2 extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            dialog.show(fm, "my_tag");
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

            Log.d("URL", myUrl );

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

            final RequestQueue queue2 = Volley.newRequestQueue(ActivitySeriesCards.this);
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, myUrl2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response max", response);
                            //maxI = Integer.valueOf(response)-1;
                            Intent intent = new Intent(ActivitySeriesCards.this, Grid_View.class);
                            intent.putExtra("ip", IP);
                            intent.putExtra("pos", PID);
                            intent.putExtra("ser", SerNum);
                            intent.putExtra("maxImages",Integer.valueOf(response)-1);
                            intent.putExtra("seriesName", ser_name);

                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySeriesCards.this);
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
            dialog.dismiss();
            super.onPostExecute(result);
        }
    }

}
