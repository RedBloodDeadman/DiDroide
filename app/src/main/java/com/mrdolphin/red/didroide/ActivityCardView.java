package com.mrdolphin.red.didroide;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityCardView extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public EditText search;
    private ArrayList<String> id_list = new ArrayList<String>();
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> sudyI_list = new ArrayList<String>();
    private ArrayList<String> date_list = new ArrayList<String>();


    private List<String> filteredList = new ArrayList<>();
    private List<String> filteredIdList = new ArrayList<>();
    private ArrayList<String> filteredSudyIlist = new ArrayList<String>();
    private ArrayList<String> filteredDateList = new ArrayList<String>();

    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM, yyyy");

    private String IP;
    public SimpleAdapter mAdapter;
    private SharedPreferences mSettings;
    private String patName;
    private String patStadyInsta;
    SimpleDateFormat sdfTo;
    SimpleDateFormat sdfFrom;
    String dateStringTo;
    String dateStringFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        setContentView(R.layout.activity_card_view);
        mSettings = getSharedPreferences(MyApp.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(MyApp.APP_PREFERENCES_IP)) {
            // Получаем IP из настроек
            IP = mSettings.getString(MyApp.APP_PREFERENCES_IP, "");
            //Toast.makeText(getApplicationContext(), IP, Toast.LENGTH_SHORT).show();
            Var.getInstance().setip(IP);
        }

        search = (EditText) findViewById(R.id.search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //idList();  // in this method, Create a list of items.



        // call the adapter with argument list of items and context.
        mAdapter = new SimpleAdapter(date_list, list, this);
        mRecyclerView.setAdapter(mAdapter);

        addTextListener();

        String resultJson = getIntent().getStringExtra(MyApp.JSON_EXTRA);

        long dateFr = getIntent().getLongExtra("dateFrom", 0);
        long dateTo = getIntent().getLongExtra("dateTo", 0);

        dateStringTo = sdf.format(dateTo);
        dateStringFrom = sdf.format(dateFr);

        setTitle("Пациенты c " + dateStringFrom + " по " + dateStringTo);

        Log.d("JSon_Result", resultJson);
        try {
            JSONObject object = new JSONObject(resultJson);
            JSONArray Jarray = object.getJSONArray("patients");
            for (int i = 0; i < Jarray.length(); i++) {
                list.add(Jarray.getJSONObject(i).getString("name").toString());
                id_list.add(Jarray.getJSONObject(i).getString("id").toString());
                sudyI_list.add(Jarray.getJSONObject(i).getString("StudyInsta").toString());
                date_list.add(Jarray.getJSONObject(i).getString("Date"));
                Log.d("TestRes", Jarray.getJSONObject(i).getString("name").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TestRes2", list.get(0));

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if(filteredIdList.isEmpty()){
                            //Toast toast = Toast.makeText(getApplicationContext(),
                             //       sudyI_list.get(position), Toast.LENGTH_SHORT);
                            //toast.show();
                            patName = list.get(position);
                            patStadyInsta = sudyI_list.get(position);
                            new ParseTask().execute(sudyI_list.get(position).toString());
                        }else {
                            //Toast toast = Toast.makeText(getApplicationContext(),
                            //        filteredSudyIlist.get(position), Toast.LENGTH_SHORT);
                            //toast.show();
                            patName = filteredList.get(position);
                            patStadyInsta = sudyI_list.get(position);
                            new ParseTask().execute(filteredSudyIlist.get(position).toString());
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
            id_list.add("id: " + i);
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

                filteredList = new ArrayList<>();
                filteredIdList = new ArrayList<>();
                filteredSudyIlist = new ArrayList<>();
                filteredDateList = new ArrayList<>();

                for (int i = 0; i < id_list.size(); i++) {

                    final String idtext = id_list.get(i).toLowerCase();
                    final String text = list.get(i).toLowerCase();
                    final String sttext = sudyI_list.get(i).toLowerCase();
                    if ((text.contains(query))||(idtext.contains(query))||(sttext.contains(query))) {

                        filteredIdList.add(id_list.get(i));
                        filteredList.add(list.get(i));
                        filteredSudyIlist.add(sudyI_list.get(i));
                        filteredDateList.add(date_list.get(i));

                    }
                }

                mRecyclerView.setLayoutManager(new LinearLayoutManager(ActivityCardView.this));
                mAdapter = new SimpleAdapter(filteredDateList, filteredList, ActivityCardView.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed


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

    class ParseTask extends AsyncTask<String, Void, Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        StringBuilder sb;
        boolean nser = true;

        @Override
        protected void onPreExecute() {
            dialog.show(fm, "my_tag");
            super.onPreExecute();
        }

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
                    .appendQueryParameter("studyInst", idpat);
            String myUrl = builder.build().toString();
            Log.d("URL", myUrl );
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
                Intent intent = new Intent(ActivityCardView.this, ActivitySeriesCards.class);
                intent.putExtra("p_res", resultJson);
                intent.putExtra("p_name", patName);
                intent.putExtra("p_studyInst", patStadyInsta);
                Log.d("P_Res", resultJson.toString());
                JSONObject object = null;
                JSONArray Jarray = null;
                String test = null;
                try {
                    object = new JSONObject(resultJson);
                    Jarray = object.getJSONArray("patients");
                    test = Jarray.getJSONObject(0).getString("series").toString();
                } catch (JSONException e) {
                    nser = false;
                    e.printStackTrace();
                }

                if (test!=null) {
                    startActivity(intent);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            if (!nser){
                AlertDialog.Builder builderAl = new AlertDialog.Builder(ActivityCardView.this);
                builderAl.setTitle(R.string.Error)
                        .setMessage(R.string.series_list_is_empty)
                        .setCancelable(false)
                        .setNegativeButton(R.string.Close,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert2 = builderAl.create();
                alert2.show();
            }
            super.onPostExecute(result);

        }
    }

}
