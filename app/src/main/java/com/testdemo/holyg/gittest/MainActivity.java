package com.testdemo.holyg.gittest;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.DocumentsContract;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.content.pm.PackageManager;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.util.Log;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public  class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private OnBooleanListener onPermissionListener;

    private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
    private static final int REQUEST_CODE_IMAGE_OP = 2;
    private static final int REQUEST_CODE_OP = 3;
    public static final String PHOTO_FILE_NAME = "temp.png";


    Button picBtn;
    Button camBtn;

    File tempFile;

    public void onPermissionRequests(String permission, OnBooleanListener onBooleanListener) {
        onPermissionListener = onBooleanListener;
        Log.d("MainActivity", "0");
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            Log.d("MainActivity", "1");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                //权限已有
                onPermissionListener.onClick(true);
            } else {
                //没有权限，申请一下
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        1);
            }
        }else{
            onPermissionListener.onClick(true);
            Log.d("MainActivity", "2"+ContextCompat.checkSelfPermission(this,
                    permission));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(true);
                }
            } else {
                //权限拒绝
                if (onPermissionListener != null) {
                    onPermissionListener.onClick(false);
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        System.out.println("数据" +" "+ resultCode + " " + this.RESULT_OK);
        if(requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == this.RESULT_OK){
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(
                        this, "com.testdemo.holyg.gittest.fileprovider",
                        tempFile);
                System.out.println(uri+" high");
            } else{
                uri = Uri.fromFile(tempFile);
                System.out.println(uri+" low");
            }
        }
        else if(requestCode == REQUEST_CODE_IMAGE_OP){
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                System.out.println(uri);
            }
        }
        else{
            System.out.println("Do not have correct request");
            return;
        }

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
                                    case 1:
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  // N以上的申请权限实例
                                            Log.d("MainActivity", "进入权限");
                                            onPermissionRequests(Manifest.permission.CAMERA, new OnBooleanListener() {
                                                @Override
                                                public void onClick(boolean bln) {

                                                    if (bln) {
                                                        Log.d("MainActivity", "进入权限11");
                                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                                        tempFile = new File(Environment.getExternalStorageDirectory(),
                                                                PHOTO_FILE_NAME);
                                                        //System.out.println("file created successfully");
                                                        Uri uri;
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                            uri = FileProvider.getUriForFile(
                                                                    MainActivity.this, "com.testdemo.holyg.gittest.fileprovider",
                                                                    tempFile);
                                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                                        } else {
                                                            uri = Uri.fromFile(tempFile);
                                                        }
                                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
                                                    } else {
                                                        Toast.makeText(MainActivity.this, "拍照或无法正常使用", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                            startActivityForResult(intent,REQUEST_CODE_IMAGE_CAMERA);
                                        }
                                        break;
                                    default:;
                            }
                            }
                        })
                        .show();
            break;
            default:;
        }
    }



}
