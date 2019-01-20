package com.testdemo.holyg.gittest;

import android.Manifest;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public  class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button picBtn;
    Button camBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picBtn = (Button) findViewById(R.id.picBtn);
        camBtn = (Button) findViewById(R.id.cameraBtn);

        picBtn.setOnClickListener(this);
        camBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cameraBtn:
                Intent intentcam = new Intent();
                intentcam.setClass(MainActivity.this,Camera.class);
                startActivity(intentcam);
            case R.id.picBtn:
                new AlertDialog.Builder(this)
                        .setTitle("Plz Chose Mode")
                        .setItems(new String[]{"Open file", "Take photo"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 1:
                                }
                            }
                        })
                        .show();
            break;
        }
    }
}
