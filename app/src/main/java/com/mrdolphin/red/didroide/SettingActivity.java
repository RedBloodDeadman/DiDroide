package com.mrdolphin.red.didroide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private String IP;
    private SharedPreferences mSettings;
    private EditText ipEdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Настройки");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_setting);
        mSettings = getSharedPreferences(MyApp.APP_PREFERENCES, Context.MODE_PRIVATE);
         ipEdText = (EditText) findViewById(R.id.editTextIP);
         //Button settingSaveButton = (Button) findViewById(R.id.buttonSaveSetting);
        if (mSettings.contains(MyApp.APP_PREFERENCES_IP)) {
            // Получаем IP из настроек
            IP = mSettings.getString(MyApp.APP_PREFERENCES_IP, "");
            //Toast.makeText(getApplicationContext(), IP, Toast.LENGTH_SHORT).show();
            ipEdText.setText(IP);
        }



    }

    public void onSaveSettingButtonClick(View view) {

        mSettings = getSharedPreferences(MyApp.APP_PREFERENCES, Context.MODE_PRIVATE);

        if (ipEdText.getText().length()>0) {
            mSettings = getSharedPreferences(MyApp.APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putString(MyApp.APP_PREFERENCES_IP, ipEdText.getText().toString());
            editor.apply();
            //Toast.makeText(getApplicationContext(), ipEdText.getText().toString(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public void onBackPressed() {
            super.onBackPressed();
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
