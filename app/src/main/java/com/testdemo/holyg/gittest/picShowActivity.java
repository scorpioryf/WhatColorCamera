package com.testdemo.holyg.gittest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;


public class picShowActivity extends AppCompatActivity {
    private Button back;
    private Bitmap resultBitmap;
    private Uri uri;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_show);
        Intent intent = getIntent();
        uri = Uri.parse(intent.getStringExtra("imageUri"));
        System.out.println("Uri = "+uri);

        back = (Button)findViewById(R.id.back);
        Bitmap bitmap = null;
        back.setOnClickListener(buttonListener);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为:" + uri);
            e.printStackTrace();
        }
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
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

}
