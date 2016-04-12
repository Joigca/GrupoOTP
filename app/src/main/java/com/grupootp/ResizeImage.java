package com.grupootp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class ResizeImage {

    public ResizeImage(){

    }

    public Drawable resize(Drawable image, int ancho, int alto) {

        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, ancho, alto, false);
        Drawable drawableResized =  new BitmapDrawable(bitmapResized);

        return drawableResized;
    }

}
