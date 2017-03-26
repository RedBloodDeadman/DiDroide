package com.mrdolphin.red.didroide;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class SeriesList extends ListActivity {
    int positID;

    String data[] = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};

    public void onCreate(Bundle savedInstanceState) {
        positID = getIntent().getIntExtra("pos", 0);
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Toast.makeText(getActivity(), "position = " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Grid_View.class);
        intent.putExtra("pos", positID);
        intent.putExtra("ser", position);
        startActivity(intent);
    }
}
