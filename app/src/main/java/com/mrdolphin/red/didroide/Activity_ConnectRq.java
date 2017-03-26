package com.mrdolphin.red.didroide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Activity_ConnectRq extends AppCompatActivity {
    String url;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__connect_rq);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final RequestQueue queue = Volley.newRequestQueue(this);

        //url ="http://192.168.1.3:8080/Test2/index.htm";
        url ="http://www.google.com";

        final Button button_rq = (Button) findViewById(R.id.b_conn);
        final TextView tv_rq = (TextView) findViewById(R.id.textV_rq);

        username = null;
        final EditText pt1 = (EditText) findViewById(R.id.editT1);

        tv_rq.setMovementMethod(new ScrollingMovementMethod());


        button_rq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = pt1.getText().toString();

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url + "?username=" + username,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                tv_rq.setText("Response is: "+ response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv_rq.setText("That didn't work!");
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

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
}

