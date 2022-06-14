package com.company.bessties;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class pictureHandler {
    static int imageId;

    public static void saveImageID(int input){
        imageId = input;
    }

    public static int getImageID(){
        return imageId;
    }


}
