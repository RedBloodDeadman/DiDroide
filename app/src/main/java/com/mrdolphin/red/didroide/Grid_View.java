package com.mrdolphin.red.didroide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.mrdolphin.red.didroide.ImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import static com.mrdolphin.red.didroide.R.id.imageView;

public class Grid_View extends AppCompatActivity {

    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        final String posIP = getIntent().getStringExtra("ip");
        final String positID = getIntent().getStringExtra("pos");
        final String positSer = getIntent().getStringExtra("ser");
        final int maxI = getIntent().getIntExtra("maxImages", 0);
        final String sern = getIntent().getStringExtra("seriesName");

        setTitle("Галерея серии " + sern);

        Var.getInstance().setMaxImagesOnSeries(maxI);
        Var.getInstance().setip(posIP);
        Var.getInstance().setpID(positID);
        Var.getInstance().setnSer(positSer);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(Grid_View.this, "" + position,
                //        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Grid_View.this, Activity_Gallery.class);
                intent.putExtra("pos", positID);
                intent.putExtra("ser", positSer);
                intent.putExtra("numOfImg", position);
                intent.putExtra("maxI", maxI);
                intent.putExtra("seriesName", sern);
                startActivity(intent);
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
