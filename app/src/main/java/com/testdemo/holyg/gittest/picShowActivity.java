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
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.FileNotFoundException;
import java.io.IOException;


public class picShowActivity extends AppCompatActivity {
    private Button back;
    private Bitmap resultBitmap;
    private Uri uri;
    private ImageView imageView;
    private SeekBar SeekbarK;
    private SeekBar SeekbarK0;


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
        Bitmap bitmap = null;
        back.setOnClickListener(buttonListener);

        SeekbarK = (SeekBar)findViewById(R.id.seekBarK);
        SeekbarK0 = (SeekBar)findViewById(R.id.seekBarK0);

        //System.out.println(stringFromJNI());

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为:" + uri);
            e.printStackTrace();
        }
        resultBitmap = new ImgProcess().bitmap2jni(bitmap);
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
            switch (v.getId()){
                case R.id.imageView:
                    if(SeekbarK.getVisibility()==View.VISIBLE){
                        SeekbarK.setVisibility(View.INVISIBLE);
                        SeekbarK0.setVisibility(View.INVISIBLE);
                    }else{
                        SeekbarK.setVisibility(View.VISIBLE);
                        SeekbarK0.setVisibility(View.INVISIBLE);
                    }
                    break;

                default:
                    break;
            }
        }
    };

}
