package com.mrdolphin.red.didroide;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.net.Uri.Builder;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.net.URI;

public class Activity_Gallery extends AppCompatActivity {


    private TouchImageView imageV;
    private SeekBar seekBar;
    private SeekBar seekBar_B;
    private SeekBar seekBar_C;
    private ImageLoader imageL;

    private String positID;
    private String positSer;
    private String IP;
    private int imgNum;
    private int maxI;
    private SharedPreferences mSettings;





    int multipl = 100;
    double multiplD = 100.0;

    Bitmap bitmap1;
    //private String appath = new String(Environment.getExternalStorageDirectory().getPath() + "/Dicom/1/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mSettings = getSharedPreferences(MyApp.APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(MyApp.APP_PREFERENCES_IP)) {
            // Получаем IP из настроек
            IP = mSettings.getString(MyApp.APP_PREFERENCES_IP, "");
            //Toast.makeText(getApplicationContext(), IP, Toast.LENGTH_SHORT).show();
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Var.setB(0);
        Var.setC(1);

        imageV = (TouchImageView) findViewById(R.id.touchIV);
        seekBar = (SeekBar) findViewById(R.id.seekBar_gall);
        seekBar_B = (SeekBar) findViewById(R.id.seekBar_B);
        seekBar_C = (SeekBar) findViewById(R.id.seekBar_C);

        positID = getIntent().getStringExtra("pos");
        positSer = getIntent().getStringExtra("ser");
        imgNum = getIntent().getIntExtra("numOfImg", 0);
        maxI = getIntent().getIntExtra("maxI", 0);
        seekBar.setMax(maxI);
        final String sernm = getIntent().getStringExtra("seriesName");

        seekBar_B.setMax(510*multipl);
        seekBar_B.setProgress((int) ((Var.getB())+255)*multipl);
        seekBar_C.setMax(10*multipl);
        seekBar_C.setProgress((int)(Var.getC()*multipl));

        setTitle("Просмотр серии " + sernm);

        String i_num;
        final String ip = IP+":8080";

        //seekBar.setMax(maximage[Integer.valueOf(positID)][Integer.valueOf(positSer)]);

        Log.d("Pos & Ser", positID+"] ["+positSer);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(ip)
                .appendPath("Test2")
                .appendPath("DownloadFileServlet")
                .appendQueryParameter("p_id", positID)
                .appendQueryParameter("series_number", positSer)
                .appendQueryParameter("image_number", String.valueOf(imgNum));
        String myUrl = builder.build().toString();

        Log.d("Series: ", positSer);
        Log.d("URL RQ: ", myUrl);
        //imageL = MySingleton.getInstance(this).getImageLoader();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.resetViewBeforeLoading()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); // Получили экземпляр
        //imageLoader.init(ImageLoaderConfiguration.createDefault(mContext)); // Проинициализировали конфигом по умолчанию
        imageLoader.displayImage(myUrl, imageV, options); // Запустили асинхронный показ картинки
        //SomeFunctions.openNetImage(myUrl, this, imageV);
        //imageV.setImageUrl(URL, imageL);
        Log.d("HW",String.valueOf(imageV.isHardwareAccelerated()));
        //BitmapDrawable drawable = (BitmapDrawable) imageV.getDrawable();
        //Bitmap bitmap = drawable.getBitmap();//получение bmp из imageView
        //imageV.setImageBitmap(bitmap);

        BitmapDrawable drawable = (BitmapDrawable) imageV.getDrawable();
        Var.setBitmap(drawable.getBitmap());//получение bmp из imageView
        //cbc = new Activity_Gallery.ChangeBC();
        //cbc.execute(Var.getBitmap());
        //BitmapDrawable drawable = (BitmapDrawable) imageV.getDrawable();
        bitmap1 = ImageWLWWConverter.changeBitmapContrastBrightness(Var.getBitmap(), Var.getC(), Var.getB());
        imageV.setImageBitmap(bitmap1);
        Log.d("B", String.valueOf(Var.getB()));
        Log.d("C", String.valueOf(Var.getC()));

        seekBar_B.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Var.setB((float) ((i-(255*multipl))/multiplD));
                //float ff = (float) (i/10.0);
                //Log.d("f", String.valueOf(ff));
                //cbc.execute(Var.getBitmap());

                bitmap1 = ImageWLWWConverter.changeBitmapContrastBrightness(Var.getBitmap(), Var.getC(), Var.getB());
                imageV.setImageBitmap(bitmap1);
                Log.d("B", String.valueOf(Var.getB()));
                Log.d("C", String.valueOf(Var.getC()));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //cbc.cancel(true);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBar_C.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Var.setC((float) (i/multiplD));
                //cbc.execute(Var.getBitmap());

                bitmap1 = ImageWLWWConverter.changeBitmapContrastBrightness(Var.getBitmap(), Var.getC(), Var.getB());
                imageV.setImageBitmap(bitmap1);
                Log.d("B", String.valueOf(Var.getB()));
                Log.d("C", String.valueOf(Var.getC()));

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //cbc.cancel(true);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //imageV.setImageURI(Uri.parse(appath + "MR.1.3.46.670589.11.0.0.11.4.2.0.18071.5.1976_0.jpg"));
        //imageV.setZoom(0);
        //seekBar.setMax(89);
        seekBar.setProgress(imgNum);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //imageV.setImageURI(Uri.parse(appath + "MR.1.3.46.670589.11.0.0.11.4.2.0.18071.5.1976_"+String.valueOf(progress)+".jpg"));
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .encodedAuthority(String.valueOf(ip))
                        .appendPath("Test2")
                        .appendPath("DownloadFileServlet")
                        .appendQueryParameter("p_id", String.valueOf(positID))
                        .appendQueryParameter("series_number", String.valueOf(positSer))
                        //.appendQueryParameter("image_number", String.valueOf(images[progress]))
                        .appendQueryParameter("image_number", String.valueOf(progress));
                String myUrl = builder.build().toString();
                imageL = MySingleton.getInstance(Activity_Gallery.this).getImageLoader();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        //.resetViewBeforeLoading()
                        .cacheInMemory()
                        .cacheOnDisc()
                        .build();

                com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance(); // Получили экземпляр
                //imageLoader.init(ImageLoaderConfiguration.createDefault(mContext)); // Проинициализировали конфигом по умолчанию
                imageLoader.displayImage(myUrl, imageV, options); // Запустили асинхронный показ картинки
                //SomeFunctions.openNetImage(myUrl, Activity_Gallery.this, imageV);

                BitmapDrawable drawable = (BitmapDrawable) imageV.getDrawable();
                Var.setBitmap(drawable.getBitmap());//получение bmp из imageView

                //Activity_Gallery.ChangeBC cbc = new Activity_Gallery.ChangeBC();
                //cbc.execute(Var.getBitmap());

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //cbc.cancel(true);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BitmapDrawable drawable = (BitmapDrawable) imageV.getDrawable();
                Var.setBitmap(drawable.getBitmap());//получение bmp из imageView
                bitmap1 = ImageWLWWConverter.changeBitmapContrastBrightness(Var.getBitmap(), Var.getC(), Var.getB());
                imageV.setImageBitmap(bitmap1);
                Log.d("B", String.valueOf(Var.getB()));
                Log.d("C", String.valueOf(Var.getC()));
                //cbc.execute(Var.getBitmap());
                //Activity_Gallery.ChangeBC cbc = new Activity_Gallery.ChangeBC();
                //cbc.execute(Var.getBitmap());
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


    class ChangeBC extends AsyncTask<Bitmap, Void , Void> {



        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            bitmap1 = ImageWLWWConverter.changeBitmapContrastBrightness(bitmaps[0], Var.getC(), Var.getB());
            //bitmap1 = ImageWLWWConverter.createContrast(bitmap1, Var.getC());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            imageV.setImageBitmap(bitmap1);
            Log.d("B", String.valueOf(Var.getB()));
            Log.d("C", String.valueOf(Var.getC()));
            super.onPostExecute(aVoid);
        }
    }

}



