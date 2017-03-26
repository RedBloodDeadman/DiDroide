package com.mrdolphin.red.didroide;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ActivitySearch extends AppCompatActivity {
   private String IP;

    Spinner spinnerOtd;


    long dateTo;
    long dateFrom;


    SimpleDateFormat sdfTo;
    SimpleDateFormat sdfFrom;
    String dateStringTo;
    String dateStringFrom;

    //String namer;
    String modality = "";
    String datateps1;
    String datateps2;
    String otd;

    //Cheks
    CheckBox All;
    CheckBox RF;
    CheckBox RT;
    CheckBox CR;
    CheckBox CT;
    CheckBox DX;
    CheckBox US;
    CheckBox XA;
    CheckBox ES;
    CheckBox MG;
    CheckBox MR;
    CheckBox SC;
    CheckBox NM;
    CheckBox OT;
    CheckBox PT;

    //Calendar
    CheckBox checkBoxFrom;
    CheckBox checkBoxTo;

    //Today Button
    Button ButtonToday;

    //Yesterday Button
    Button ButtonYesterday;

    //Clear Button
    Button ButtonClear;

    //Next Button
    Button ButtonNext;

    //Date Pickers
    DatePicker DatePicker1;
    DatePicker DatePicker2;

    //EditText
    EditText editTextFrom;
    EditText editTextTo;

    private SharedPreferences mSettings;

    //TextView tvFrom;
    //TextView tvTo;

    int myYearTo;
    int myMonthTo;
    int myDayTo;

    int myYearFrom;
    int myMonthFrom;
    int myDayFrom;

    @Override
    protected void onResume() {
        mSettings = getSharedPreferences(MyApp.APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.contains(MyApp.APP_PREFERENCES_IP)) {
            // Получаем IP из настроек
            IP = mSettings.getString(MyApp.APP_PREFERENCES_IP, "");
            //Toast.makeText(getApplicationContext(), IP, Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(getApplicationContext(), "Не введён IP сервера!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivitySearch.this, SettingActivity.class);
            startActivity(intent);
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dateTo = System.currentTimeMillis();
        dateFrom = System.currentTimeMillis() - 21*24*60*(6*10000);


        sdfTo = new SimpleDateFormat("EEE, d MMM, yyyy");
        sdfFrom = new SimpleDateFormat("EEE, d MMM, yyyy");
        dateStringTo = sdfTo.format(dateTo);
        dateStringFrom = sdfFrom.format(dateFrom);

        myYearTo = Integer.valueOf(new SimpleDateFormat("yyyy").format(dateTo));
        Log.d("YEAR_TO", String.valueOf(myYearTo));
        myMonthTo = Integer.valueOf(new SimpleDateFormat("MM").format(dateTo));
        Log.d("MONTH_TO", String.valueOf(myMonthTo));
        myDayTo = Integer.valueOf(new SimpleDateFormat("dd").format(dateTo));
        Log.d("DAY_TO", String.valueOf(myDayTo));

        myYearFrom = Integer.valueOf(new SimpleDateFormat("yyyy").format(dateFrom));
        Log.d("YEAR_FROM", String.valueOf(myYearFrom));
        myMonthFrom = Integer.valueOf(new SimpleDateFormat("MM").format(dateFrom));
        Log.d("MONTH_FROM", String.valueOf(myMonthFrom));
        myDayFrom = Integer.valueOf(new SimpleDateFormat("dd").format(dateFrom));
        Log.d("DAY_FROM", String.valueOf(myDayFrom));

        Log.d("ON_DATA_TO_SET", String.valueOf(myYearTo) +"|"+String.valueOf(myMonthTo)+"|"+String.valueOf(myDayTo));
        Log.d("ON_DATA_FROM_SET", String.valueOf(myYearFrom) +"|"+String.valueOf(myMonthFrom)+"|"+String.valueOf(myDayFrom));

        //tvFrom = (TextView) findViewById(R.id.textViewDateFrom);
        //tvTo = (TextView) findViewById(R.id.textViewDateTo);

        //tvFrom.setText(dateStringFrom);
        //tvTo.setText(dateStringTo);

        setTitle("Фильтры");
        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);

        mSettings = getSharedPreferences(MyApp.APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.contains(MyApp.APP_PREFERENCES_IP)) {
            // Получаем IP из настроек
            IP = mSettings.getString(MyApp.APP_PREFERENCES_IP, "");
            //Toast.makeText(getApplicationContext(), IP, Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(getApplicationContext(), "Не введён IP сервера!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivitySearch.this, SettingActivity.class);
            startActivity(intent);
        }

        //Departments
        spinnerOtd = (Spinner) findViewById(R.id.spinnerOtd);

        //Cheks

        //DateText
        editTextFrom = (EditText) findViewById(R.id.editTextFrom) ;
        editTextFrom.setText(dateStringFrom);

        editTextTo = (EditText) findViewById(R.id.editTextTo) ;
        editTextTo.setText(dateStringTo);



        //Modality
        All = (CheckBox) findViewById(R.id.checkBoxAll);
        All.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();

                }
            }
        });
        RF = (CheckBox) findViewById(R.id.checkBoxRF);
        RF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(true);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = RF.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        RT = (CheckBox) findViewById(R.id.checkBoxRT);
        RT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(true);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = RT.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        CR = (CheckBox) findViewById(R.id.checkBoxCR);
        CR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(true);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = CR.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        CT = (CheckBox) findViewById(R.id.checkBoxCT);
        CT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(true);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = CT.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        DX = (CheckBox) findViewById(R.id.checkBoxDX);
        DX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(true);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = DX.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        US = (CheckBox) findViewById(R.id.checkBoxUS);
        US.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(true);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = US.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        XA = (CheckBox) findViewById(R.id.checkBoxXA);
        XA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(true);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = XA.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        ES = (CheckBox) findViewById(R.id.checkBoxES);
        ES.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(true);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality =ES.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        MG = (CheckBox) findViewById(R.id.checkBoxMG);
        MG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(true);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = MG.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        MR = (CheckBox) findViewById(R.id.checkBoxMR);
        MR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(true);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = MR.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        SC = (CheckBox) findViewById(R.id.checkBoxSC);
        SC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(true);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = SC.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        NM = (CheckBox) findViewById(R.id.checkBoxNM);
        NM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(true);
                    OT.setChecked(false);
                    PT.setChecked(false);

                    modality = NM.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        OT = (CheckBox) findViewById(R.id.checkBoxOT);
        OT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(true);
                    PT.setChecked(false);

                    modality = OT.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });
        PT = (CheckBox) findViewById(R.id.checkBoxPT);
        PT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    RF.setChecked(false);
                    RT.setChecked(false);
                    CR.setChecked(false);
                    CT.setChecked(false);
                    DX.setChecked(false);
                    US.setChecked(false);
                    XA.setChecked(false);
                    ES.setChecked(false);
                    MG.setChecked(false);
                    MR.setChecked(false);
                    SC.setChecked(false);
                    NM.setChecked(false);
                    OT.setChecked(false);
                    PT.setChecked(true);

                    modality = PT.getText().toString();
                }else{
                    //Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    modality = "";

                }
            }
        });

        //Calendar
        /*
        checkBoxFrom = (CheckBox) findViewById(R.id.checkBoxFrom);
        checkBoxFrom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    DatePicker1.setEnabled(true);
                }else{
                    Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    DatePicker1.setEnabled(false);
                }
            }
        });

        checkBoxTo = (CheckBox) findViewById(R.id.checkBoxTo);
        checkBoxTo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                    DatePicker2.setEnabled(true);
                }else{
                    Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
                    DatePicker2.setEnabled(false);
                }
            }
        });
*/
        //Today Button
        ButtonToday = (Button) findViewById(R.id.buttonToday);

        //Yesterday Button
        ButtonYesterday = (Button) findViewById(R.id.buttonYesterday);

        //Clear Button
        ButtonClear = (Button) findViewById(R.id.buttonClear);

        //Next Button
        ButtonNext = (Button) findViewById(R.id.buttonNext);

        //Date Pickers
        DatePicker1 = (DatePicker) findViewById(R.id.datePicker1);
        DatePicker2 = (DatePicker) findViewById(R.id.datePicker2);

        String[] otdel = {"1", "2", "3"};

        //String[] mod = {"CR", "CT", "MR", "DX", "NX"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, otdel) {
        };
  //      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerOtd.setAdapter(adapter);

        All.setEnabled(false);
        //RF.setEnabled(false);
        //RT.setEnabled(false);
        //CR.setEnabled(false);
        //CT.setEnabled(false);
        //DX.setEnabled(false);
        //US.setEnabled(false);
        //XA.setEnabled(false);
        //ES.setEnabled(false);
        //MG.setEnabled(false);
        //MR.setEnabled(false);
        //SC.setEnabled(false);
        //NM.setEnabled(false);
        //OT.setEnabled(false);
        //PT.setEnabled(false);

//        DatePicker1.setEnabled(false);
//        DatePicker2.setEnabled(false);

//        checkBoxFrom.setChecked(false);
//        checkBoxTo.setChecked(false);

     //   spinnerOtd.setSelection(0);
    }

    public void onNextButtonClick(View view) {

        //namer = String.valueOf(FIO.getText());
        //modality = String.valueOf(spinnerMod.getSelectedItem().toString());
        //String currentDate=formatter.format(view.getSelectedDay());

      //  otd = spinnerOtd.getSelectedItem().toString();
/*
        if (DatePicker1.isEnabled()){
            String year = String.valueOf(DatePicker1.getYear());
            String month = String.valueOf(DatePicker1.getMonth());
            String day = String.valueOf(DatePicker1.getDayOfMonth());

            if(Integer.parseInt(month) < 10){
                month = "0" + month;
            }
            if(Integer.parseInt(day) < 10){
                day  = "0" + day ;
            }

            datateps1 = String.valueOf(year+month+day);

        }else{
            datateps1 = "19950101";
        }


        if (DatePicker2.isEnabled()) {
            String year = String.valueOf(DatePicker2.getYear());
            String month = String.valueOf(DatePicker2.getMonth());
            String day = String.valueOf(DatePicker2.getDayOfMonth());

            if(Integer.parseInt(month) < 10){
                month = "0" + month;
            }
            if(Integer.parseInt(day) < 10){
                day  = "0" + day ;
            }

            datateps2 = String.valueOf(year+month+day);

        }else{
            datateps2 = "29990909";
        }
*/
        datateps1 = new SimpleDateFormat("yyyyMMdd").format(dateFrom);
        datateps2 = new SimpleDateFormat("yyyyMMdd").format(dateTo);

        //Toast.makeText(getApplicationContext(), modality, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), otd, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), datateps1, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), datateps2, Toast.LENGTH_SHORT).show();

        new ParseTask().execute();


    }

    Calendar cal=Calendar.getInstance();

    public void onYesterdayButtonClick(View view) {

        //checkBoxFrom.setChecked(true);
        //checkBoxTo.setChecked(true);

        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        //DatePicker1.updateDate(year, month, day-1);
        //DatePicker2.updateDate(year, month, day-1);

        dateTo = System.currentTimeMillis() - 1*24*60*(6*10000);
        dateFrom = System.currentTimeMillis() - 1*24*60*(6*10000);


        sdfTo = new SimpleDateFormat("EEE, d MMM, yyyy");
        sdfFrom = new SimpleDateFormat("EEE, d MMM, yyyy");
        dateStringTo = sdfTo.format(dateTo);
        dateStringFrom = sdfFrom.format(dateFrom);

        myYearTo = Integer.valueOf(new SimpleDateFormat("yyyy").format(dateTo));
        Log.d("YEAR_TO", String.valueOf(myYearTo));
        myMonthTo = Integer.valueOf(new SimpleDateFormat("MM").format(dateTo));
        Log.d("MONTH_TO", String.valueOf(myMonthTo));
        myDayTo = Integer.valueOf(new SimpleDateFormat("dd").format(dateTo));
        Log.d("DAY_TO", String.valueOf(myDayTo));

        myYearFrom = Integer.valueOf(new SimpleDateFormat("yyyy").format(dateFrom));
        Log.d("YEAR_FROM", String.valueOf(myYearFrom));
        myMonthFrom = Integer.valueOf(new SimpleDateFormat("MM").format(dateFrom));
        Log.d("MONTH_FROM", String.valueOf(myMonthFrom));
        myDayFrom = Integer.valueOf(new SimpleDateFormat("dd").format(dateFrom));
        Log.d("DAY_FROM", String.valueOf(myDayFrom));

        //tvFrom = (TextView) findViewById(R.id.textViewDateFrom);
        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        //tvTo = (TextView) findViewById(R.id.textViewDateTo);
        editTextTo = (EditText) findViewById(R.id.editTextTo);

        //tvFrom.setText(dateStringFrom);
        editTextFrom.setText(dateStringFrom);
        //tvTo.setText(dateStringTo);
        editTextTo.setText(dateStringTo);

        Log.d("ON_DATA_TO_SET", String.valueOf(myYearTo) +"|"+String.valueOf(myMonthTo)+"|"+String.valueOf(myDayTo));
        Log.d("ON_DATA_FROM_SET", String.valueOf(myYearFrom) +"|"+String.valueOf(myMonthFrom)+"|"+String.valueOf(myDayFrom));

    }

    public void onClearButtonClick(View view) {

        dateTo = System.currentTimeMillis();
        dateFrom = System.currentTimeMillis() - 21*24*60*(6*10000);


        sdfTo = new SimpleDateFormat("EEE, d MMM, yyyy");
        sdfFrom = new SimpleDateFormat("EEE, d MMM, yyyy");
        dateStringTo = sdfTo.format(dateTo);
        dateStringFrom = sdfFrom.format(dateFrom);

        myYearTo = Integer.valueOf(new SimpleDateFormat("yyyy").format(dateTo));
        Log.d("YEAR_TO", String.valueOf(myYearTo));
        myMonthTo = Integer.valueOf(new SimpleDateFormat("MM").format(dateTo));
        Log.d("MONTH_TO", String.valueOf(myMonthTo));
        myDayTo = Integer.valueOf(new SimpleDateFormat("dd").format(dateTo));
        Log.d("DAY_TO", String.valueOf(myDayTo));

        myYearFrom = Integer.valueOf(new SimpleDateFormat("yyyy").format(dateFrom));
        Log.d("YEAR_FROM", String.valueOf(myYearFrom));
        myMonthFrom = Integer.valueOf(new SimpleDateFormat("MM").format(dateFrom));
        Log.d("MONTH_FROM", String.valueOf(myMonthFrom));
        myDayFrom = Integer.valueOf(new SimpleDateFormat("dd").format(dateFrom));
        Log.d("DAY_FROM", String.valueOf(myDayFrom));

        //tvFrom = (TextView) findViewById(R.id.textViewDateFrom);
        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        //tvTo = (TextView) findViewById(R.id.textViewDateTo);
        editTextTo = (EditText) findViewById(R.id.editTextTo);

        //tvFrom.setText(dateStringFrom);
        editTextFrom.setText(dateStringFrom);
        //tvTo.setText(dateStringTo);
        editTextTo.setText(dateStringTo);
/*
        RF.setChecked(false);
        RT.setChecked(false);
        CR.setChecked(false);
        CT.setChecked(false);
        DX.setChecked(false);
        US.setChecked(false);
        XA.setChecked(false);
        ES.setChecked(false);
        MG.setChecked(false);
        MR.setChecked(false);
        SC.setChecked(false);
        NM.setChecked(false);
        OT.setChecked(false);
        PT.setChecked(false);
*/
        //DatePicker1.setEnabled(false);
        //DatePicker2.setEnabled(false);

        //checkBoxFrom.setChecked(false);
        //checkBoxTo.setChecked(false);

        //spinnerOtd.setSelection(0);

        //modality = "";

        Log.d("ON_DATA_TO_SET", String.valueOf(myYearTo) +"|"+String.valueOf(myMonthTo)+"|"+String.valueOf(myDayTo));
        Log.d("ON_DATA_FROM_SET", String.valueOf(myYearFrom) +"|"+String.valueOf(myMonthFrom)+"|"+String.valueOf(myDayFrom));

    }

    public void onTodayButtonClick(View view) {

        //checkBoxFrom.setChecked(true);
        //checkBoxTo.setChecked(true);

        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);

        //DatePicker1.updateDate(year, month, day);
        //DatePicker2.updateDate(year, month, day);

        dateTo = System.currentTimeMillis();
        dateFrom = System.currentTimeMillis();


        sdfTo = new SimpleDateFormat("EEE, d MMM, yyyy");
        sdfFrom = new SimpleDateFormat("EEE, d MMM, yyyy");
        dateStringTo = sdfTo.format(dateTo);
        dateStringFrom = sdfFrom.format(dateFrom);

        myYearTo = Integer.valueOf(new SimpleDateFormat("yyyy").format(dateTo));
        Log.d("YEAR_TO", String.valueOf(myYearTo));
        myMonthTo = Integer.valueOf(new SimpleDateFormat("MM").format(dateTo));
        Log.d("MONTH_TO", String.valueOf(myMonthTo));
        myDayTo = Integer.valueOf(new SimpleDateFormat("dd").format(dateTo));
        Log.d("DAY_TO", String.valueOf(myDayTo));

        myYearFrom = Integer.valueOf(new SimpleDateFormat("yyyy").format(dateFrom));
        Log.d("YEAR_FROM", String.valueOf(myYearFrom));
        myMonthFrom = Integer.valueOf(new SimpleDateFormat("MM").format(dateFrom));
        Log.d("MONTH_FROM", String.valueOf(myMonthFrom));
        myDayFrom = Integer.valueOf(new SimpleDateFormat("dd").format(dateFrom));
        Log.d("DAY_FROM", String.valueOf(myDayFrom));

        //tvFrom = (TextView) findViewById(R.id.textViewDateFrom);
        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        //tvTo = (TextView) findViewById(R.id.textViewDateTo);
        editTextTo = (EditText) findViewById(R.id.editTextTo);

        //tvFrom.setText(dateStringFrom);
        editTextFrom.setText(dateStringFrom);
        //tvTo.setText(dateStringTo);
        editTextTo.setText(dateStringTo);

        Log.d("ON_DATA_TO_SET", String.valueOf(myYearTo) +"|"+String.valueOf(myMonthTo)+"|"+String.valueOf(myDayTo));
        Log.d("ON_DATA_FROM_SET", String.valueOf(myYearFrom) +"|"+String.valueOf(myMonthFrom)+"|"+String.valueOf(myDayFrom));

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Intent intent = new Intent(ActivitySearch.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    LoadingDialog dialog = new LoadingDialog();
    FragmentManager fm = getSupportFragmentManager();



    class ParseTask extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        StringBuilder sb;
        boolean npat = true;

        @Override
        protected void onPreExecute() {
            dialog.show(fm, "my_tag");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            String ip = IP + ":8080";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(ip)
                    .appendPath("Test2")
                    .appendPath("ConnectToServlet")
                    //.appendQueryParameter("namer", namer)
                    .appendQueryParameter("modality", modality)
                    .appendQueryParameter("datep1", datateps1)
                    .appendQueryParameter("datep2", datateps2);
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
                Intent intent = new Intent(ActivitySearch.this, ActivityCardView.class);
                intent.putExtra(MyApp.JSON_EXTRA, resultJson);
                intent.putExtra("dateFrom", dateFrom);
                intent.putExtra("dateTo", dateTo);
                Log.d("P_Res", resultJson.toString());
                JSONObject object = null;
                JSONArray Jarray = null;
                String test = null;
                try {
                    object = new JSONObject(resultJson);
                    Jarray = object.getJSONArray("patients");
                    test = Jarray.getJSONObject(0).getString("StudyInsta").toString();
                } catch (JSONException e) {
                    npat = false;
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
            if (!npat){
                AlertDialog.Builder builderAl = new AlertDialog.Builder(ActivitySearch.this);
                builderAl.setTitle(R.string.Error)
                        .setMessage(R.string.patients_list_is_empty)
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

    //@Override
    //public void onBackPressed() {
//
   // }

    public void buttonDateTo(View view) {
        //showDialog(DIALOG_DATE_TO);
        //DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYearTo, myMonthTo, myDayTo);
        DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYearTo, myMonthTo-1, myDayTo);
        tpd.show();
    }

    public void buttonDateFrom(View view) {
        //showDialog(DIALOG_DATE_FROM);
        DatePickerDialog tpd2 = new DatePickerDialog(this, myCallBack2, myYearFrom, myMonthFrom-1, myDayFrom);
        tpd2.show();
    }

    public void editDateFrom(View view){
        DatePickerDialog tpd2 = new DatePickerDialog(this, myCallBack2, myYearFrom, myMonthFrom-1, myDayFrom);
        tpd2.show();
    }

    public void editDateTo(View view){
        DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYearTo, myMonthTo-1, myDayTo);
        tpd.show();
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYearTo = year;
            myMonthTo = monthOfYear+1;
            myDayTo = dayOfMonth;

            Log.d("ON_DATA_TO_SET", String.valueOf(myYearTo) +"|"+String.valueOf(myMonthTo)+"|"+String.valueOf(myDayTo));

            Calendar c1 = Calendar.getInstance();
            c1.set(myYearTo, myMonthTo-1, myDayTo);  //January 30th 2000
            Date sDate = c1.getTime();

            dateTo = sDate.getTime();

            sdfTo = new SimpleDateFormat("EEE, d MMM, yyyy");
            dateStringTo = sdfTo.format(sDate);

            //tvTo.setText(dateStringTo);
            editTextTo.setText(dateStringTo);

            //tvDate.setText("Today is " + myDay + "/" + myMonth + "/" + myYear);
        }
    };

    DatePickerDialog.OnDateSetListener myCallBack2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYearFrom = year;
            myMonthFrom = monthOfYear+1;
            myDayFrom = dayOfMonth;

            Log.d("ON_DATA_FROM_SET", String.valueOf(myYearFrom) +"|"+String.valueOf(myMonthFrom)+"|"+String.valueOf(myDayFrom));

            Calendar c2 = Calendar.getInstance();
            c2.set(myYearFrom, myMonthFrom-1, myDayFrom);  //January 30th 2000
            Date sDate2 = c2.getTime();

            dateFrom = sDate2.getTime();

            sdfFrom = new SimpleDateFormat("EEE, d MMM, yyyy");
            dateStringFrom = sdfFrom.format(sDate2);

            //tvFrom.setText(dateStringFrom);
            editTextFrom.setText(dateStringFrom);

            //tvDate.setText("Today is " + myDay + "/" + myMonth + "/" + myYear);
        }
    };
}
