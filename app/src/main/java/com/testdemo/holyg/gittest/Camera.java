package com.testdemo.holyg.gittest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.testdemo.holyg.gittest.R;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Camera extends AppCompatActivity{


    private RelativeLayout previewLayout;
    private MyGLSurfaceView myGLSurfaceView;
    private TextView textView;
    private SeekBar seekBarK;
    private SeekBar seekBarK0;
    private Switch aSwitch;

    public static double Kratio;
    public static double K0ratio;
    public static boolean arg = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        previewLayout=(RelativeLayout)findViewById(R.id.previewLayout);
        myGLSurfaceView=new MyGLSurfaceView(this, new MyGLSurfaceView.GetUiHandlerInterface() {
            @Override
            public void getUiHandler(Message leftUpPt) {
                mHandler.sendMessage(leftUpPt);
            }
        });
        previewLayout.addView(myGLSurfaceView);

        seekBarK = findViewById(R.id.seekBarK);
        seekBarK0 = findViewById(R.id.seekBarK0);
        seekBarK.setOnSeekBarChangeListener(seekBarKChangeListener);
        seekBarK0.setOnSeekBarChangeListener(seekBarK0ChangeListener);
        aSwitch = findViewById(R.id.switch2);
        aSwitch.setOnCheckedChangeListener(checkedChangeListener);

        textView = (TextView) findViewById(R.id.sample_text);
//        ResultFromJni resultFromJni =MyNDKOpencv.structFromNative();
//        textView.setText(resultFromJni.text + resultFromJni.num);

    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    textView.setText("LeftUp Point is "+msg.arg1+" , "+msg.arg2);
                    break;
                default:
                    break;
            }
        }
    };

    void requestPermission(){
        final int REQUEST_CODE = 1;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
    }

    public void clickText(View view) {
        if (myGLSurfaceView.model ==2)
            myGLSurfaceView.model=0;
        else myGLSurfaceView.model++;
        Toast.makeText(this,"Change Effect",Toast.LENGTH_SHORT).show();
    }

    private SeekBar.OnSeekBarChangeListener seekBarKChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Kratio = (double) progress/100;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarK0ChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            K0ratio = (double)progress/100;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    private Switch.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                arg = false;
            }
            else {
                arg = true;
            }
            K0ratio = 0.0;
            Kratio = 0.5;
        }
    };
}
