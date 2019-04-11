package com.testdemo.holyg.gittest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImgProcess {
    static {
        System.loadLibrary("native-lib");
    }
    public  native int[] opencvPicProcess(int[] pixels,int w,int h,double K,double K0);
    public Bitmap bitmap2jni(Bitmap bitmap,double K,double K0){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int [] pixels = new int [w*h];
        bitmap.getPixels(pixels,0,w,0,0,w,h);
        int [] resultData = opencvPicProcess(pixels,w,h,K,K0);
        Bitmap resultImage = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        resultImage.setPixels(resultData,0,w,0,0,w,h);
        return resultImage;
    }

}
