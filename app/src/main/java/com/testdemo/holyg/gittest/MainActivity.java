package com.testdemo.holyg.gittest;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.provider.MediaStore;
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

    private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
    private static final int REQUEST_CODE_IMAGE_OP = 2;
    private static final int REQUEST_CODE_OP = 3;

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
                break;
            case R.id.picBtn:
                new AlertDialog.Builder(this)
                        .setTitle("Plz Chose Mode")
                        .setItems(new String[]{"Open file", "Take photo"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                        getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                        getImageByalbum.setType("image/jpeg");
                                        startActivityForResult(getImageByalbum,REQUEST_CODE_IMAGE_OP);
                                        break;
                                        default:;
                                    case 1:
                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                        ContentValues values = new ContentValues(1);
                                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
                                        break;
                            }
                            }
                        })
                        .show();
            break;
        }
    }
}
