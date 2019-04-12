package com.testdemo.holyg.gittest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;

import java.io.FileNotFoundException;
import java.io.IOException;


public class picShowActivity extends AppCompatActivity {
    private Button back;
    private Bitmap resultBitmap;
    private Uri uri;
    private ImageView imageView;
    private SeekBar SeekbarK;
    private SeekBar SeekbarK0;

    private Bitmap bitmap = null;
    private Switch aSwitch;

    private boolean argFlag = true;

    double Kratio = 0.5;
    double K0ratio =0.0;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    //public native void opencvPicShow(String uri);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pic_show);
        Intent intent = getIntent();
        uri = Uri.parse(intent.getStringExtra("imageUri"));
        System.out.println("Uri = "+uri);

        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(buttonListener);

        SeekbarK = (SeekBar)findViewById(R.id.seekBarK);
        SeekbarK0 = (SeekBar)findViewById(R.id.seekBarK0);

        SeekbarK0.setOnSeekBarChangeListener(seekBarK0ChangeListener);
        SeekbarK.setOnSeekBarChangeListener(seekBarKChangeListener);

        aSwitch = findViewById(R.id.switch1);

        aSwitch.setOnCheckedChangeListener(checkedChangeListener);



        //System.out.println(stringFromJNI());

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为:" + uri);
            e.printStackTrace();
        }
        resultBitmap = new ImgProcess().bitmap2jni(bitmap,Kratio,K0ratio,argFlag);
        //opencvPicShow(uri.toString());
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(imgListener);
        imageView.setBackgroundColor(Color.BLACK);
        imageView.setImageBitmap(resultBitmap);
    }


    private Button.OnClickListener buttonListener = new Button.OnClickListener(){
        public void onClick(View v){
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
                 default:
                        break;
            }
        }
    };

    private ImageView.OnClickListener imgListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Android","img OnClick");
            switch (v.getId()){
                case R.id.imageView:
                    if(SeekbarK.getVisibility()==View.VISIBLE){
                        SeekbarK.setVisibility(View.INVISIBLE);
                        SeekbarK0.setVisibility(View.INVISIBLE);
                        aSwitch.setVisibility(View.INVISIBLE);
                    }else{
                        SeekbarK.setVisibility(View.VISIBLE);
                        SeekbarK0.setVisibility(View.VISIBLE);
                        aSwitch.setVisibility(View.VISIBLE);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarKChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Kratio = (double) progress/100;

            Log.e("seekbarlistener", "onProgressChanged: Kratio = "+Kratio+"progress = "+progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            resultBitmap = new ImgProcess().bitmap2jni(bitmap,Kratio,K0ratio,argFlag);
            imageView.setImageBitmap(resultBitmap);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarK0ChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            K0ratio = (double)progress/100;

            Log.e("seekbarlistener", "onProgressChanged: K0ratio = "+K0ratio+"progress = "+progress);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            resultBitmap = new ImgProcess().bitmap2jni(bitmap,Kratio,K0ratio,argFlag);
            imageView.setImageBitmap(resultBitmap);
        }
    };

    private Switch.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                argFlag = false;
            }
            else {
                argFlag = true;
            }
            K0ratio = 0.0;
            Kratio = 0.5;
            resultBitmap = new ImgProcess().bitmap2jni(bitmap,Kratio,K0ratio,argFlag);
            imageView.setImageBitmap(resultBitmap);
        }
    };

}
