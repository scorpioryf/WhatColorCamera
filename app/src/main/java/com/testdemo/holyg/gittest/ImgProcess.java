package com.testdemo.holyg.gittest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImgProcess {

    static {
        System.loadLibrary("native-lib");
    }
    public native int[] opencvHSVProcess(int[] pixels,int w,int h,double K,double K0);
    public native int[] opencvRGBProcess(int[] pixels,int w,int h);
    public Bitmap bitmap2jni(Bitmap bitmap,double Kratio,double K0ratio,boolean argFlag){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        double K0 = K0ratio*102.0;
        double K = 1.0 + Kratio*1.3;
        int [] pixels = new int [w*h];
        int [] resultData = new int [w*h];
        bitmap.getPixels(pixels,0,w,0,0,w,h);
        if(argFlag) {
           resultData = opencvHSVProcess(pixels, w, h, K, K0);
        }
        else {
            resultData = opencvRGBProcess(pixels, w, h);
        }
        Bitmap resultImage = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        resultImage.setPixels(resultData,0,w,0,0,w,h);
        return resultImage;
    }

}
