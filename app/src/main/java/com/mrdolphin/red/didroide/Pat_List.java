package com.mrdolphin.red.didroide;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ParseError;
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
import java.util.List;

public class Pat_List extends AppCompatActivity {

    String[] names = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
            "Костя", "Игорь", "Анна", "Денис", "Андрей" };

    ArrayList<String> id_list = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> sudyI_list = new ArrayList<String>();

    String IP = "192.168.1.147";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(myToolbar);



        String resultJson = getIntent().getStringExtra("p_res");

        Log.d("JSon_Result", resultJson);
        try {
            JSONObject object = new JSONObject(resultJson);
            JSONArray Jarray = object.getJSONArray("patients");
            for (int i = 0; i < Jarray.length(); i++) {
                list.add(Jarray.getJSONObject(i).getString("name").toString());
                id_list.add(Jarray.getJSONObject(i).getString("id").toString());
                sudyI_list.add(Jarray.getJSONObject(i).getString("StudyInsta").toString());
                Log.d("TestRes", Jarray.getJSONObject(i).getString("StudyInsta").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_pat_list);

        Log.d("TestRes2", list.get(0));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Pat_List.this,
                android.R.layout.simple_list_item_1, list ) {
        };
        adapter.notifyDataSetChanged();
        ListView pat_List = (ListView) findViewById(R.id.pat_List);
        pat_List.setAdapter(adapter);

        pat_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("LOG_TAG", "itemClick: position = " + position + ", id = "
                        + id);
                Toast toast = Toast.makeText(getApplicationContext(), id_list.get(position)
                        , Toast.LENGTH_SHORT);
                toast.show();
                new ParseTask().execute(id_list.get(position).toString());
            }
        });
    }


    class ParseTask extends AsyncTask<String, Void, Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        StringBuilder sb;

               @Override
               protected Void doInBackground(String... params) {
                   // получаем данные с внешнего ресурса
                   String ip = IP + ":8080";
                   String idpat = params[0].toString();

                   Uri.Builder builder = new Uri.Builder();
                   builder.scheme("http")
                           .encodedAuthority(ip)
                           .appendPath("Test2")
                           .appendPath("SeriesSendServ")
                           .appendQueryParameter("idpat", idpat);
                   String myUrl = builder.build().toString();

                   Log.d("Con", "Beg");
                   URL url = null;
                   try {
                       url = new URL(myUrl);
                   } catch (MalformedURLException e) {
                       e.printStackTrace();
                   }

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
                           reader = new BufferedReader(new InputStreamReader(in));

                           StringBuilder result = new StringBuilder();
                           String line;
                           try {
                               while ((line = reader.readLine()) != null) {
                                   result.append(line);
                               }
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                           Log.d("URLConnection Result", result.toString());
                           resultJson = result.toString();
                           Log.d("ResultJson", resultJson.toString());

                       } catch (Exception e) {
                           e.printStackTrace();
                       }
                   } finally {
                       urlConnection.disconnect();
                       Intent intent = new Intent(Pat_List.this, Ser_List.class);
                       intent.putExtra("p_res", resultJson);
                       Log.d("P_Res", resultJson.toString());
                       if (resultJson.length()>0) {
                           startActivity(intent);
                       }
                   }
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
