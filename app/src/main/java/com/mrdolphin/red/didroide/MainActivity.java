package com.mrdolphin.red.didroide;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.mrdolphin.red.didroide.R.styleable.Toolbar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String PID;
    String SerNum;
    String IP;
    MyTask mt;
    Var va;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText etPatID = (EditText) findViewById(R.id.etPatID);
        final EditText etSerNum = (EditText) findViewById(R.id.etSerNum);
        final EditText etIPi = (EditText) findViewById(R.id.etIP);
        Button btContinue = (Button) findViewById(R.id.but_main_continue);
        ProgressBar pBar = (ProgressBar) findViewById(R.id.pbMain);

        pBar.setVisibility(View.INVISIBLE);
        btContinue.setVisibility(View.VISIBLE);

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mt = new MyTask();
                mt.execute();

            }


        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.About)
                    .setMessage("All right reserved" +
                            "\n"+"Togliatti State University"+
                            "\n"+"IT Student")
                    .setCancelable(false)
                    .setNegativeButton(R.string.Close,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.About)
                    .setMessage("All right reserved" +
                            "\n"+"Togliatti State University"+
                            "\n"+"IT Student")
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
/*
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, Activity_ConnectRq.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    ////

    class MyTask extends AsyncTask<Void, Void, Void> {


        final EditText etPatID = (EditText) findViewById(R.id.etPatID);
        final EditText etSerNum = (EditText) findViewById(R.id.etSerNum);
        final EditText etIPi = (EditText) findViewById(R.id.etIP);
        Button btContinue = (Button) findViewById(R.id.but_main_continue);
        ProgressBar pBar = (ProgressBar) findViewById(R.id.pbMain);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            btContinue.setVisibility(View.INVISIBLE);
            pBar.setVisibility(View.VISIBLE);
            Log.d("aTask", "Begin");
            PID = etPatID.getText().toString();
            Log.d("PID", PID);
            SerNum = etSerNum.getText().toString();
            Log.d("SerNu", SerNum);
            IP = etIPi.getText().toString();
            Log.d("IP", IP);
        }
        @Override
        protected Void doInBackground(Void... params) {

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

            final RequestQueue queue2 = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, myUrl2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response max", response);
                            //maxI = Integer.valueOf(response)-1;
                            Intent intent = new Intent(MainActivity.this, Grid_View.class);
                            intent.putExtra("ip", IP);
                            intent.putExtra("pos", PID);
                            intent.putExtra("ser", SerNum);
                            intent.putExtra("maxImages",Integer.valueOf(response)-1);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            btContinue.setVisibility(View.VISIBLE);
            pBar.setVisibility(View.INVISIBLE);
            Log.d("aTask", "End");
        }
    }


}
