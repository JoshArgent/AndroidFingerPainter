package psyja2.coursework1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by psyja2 on 20/10/2017.
 */

/*
An extension of FingerPaintView that adds extra functionality
 */
public class MyFingerPainterView extends FingerPainterView {

    private int width = 0;
    private int height = 0;

    /*
    Same constructors from super class
     */
    public MyFingerPainterView(Context context) {
        super(context);
    }

    public MyFingerPainterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFingerPainterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
    Reset the canvas and background image - clears the screen
     */
    public void clear()
    {
        // Create a new bitmap and canvas object
        bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        // Reset any image URIs
        super.load(null);
        // Redraw the finger painter
        invalidate();
    }

    /*
    The original load method did not actually force the image to be loaded if the canvas was already initialized
    This override will actually display an image, loaded from Uri
     */
    @Override
    public void load(Uri uri)
    {
        // Call the super class load method
        super.load(uri);

        // Use the same code to load an image as seen in the super class onSizeChanged() event
        try {
            // attempt to load the uri provided, scale to fit our canvas
            InputStream stream = context.getContentResolver().openInputStream(uri);
            Bitmap bm = BitmapFactory.decodeStream(stream);
            bitmap  = Bitmap.createScaledBitmap(bm, Math.max(Math.max(getWidth(), getHeight()), width), Math.max(Math.max(getWidth(), getHeight()), height), false);
            stream.close();
            bm.recycle();
        } catch(IOException e) {
            Log.e("FingerPainterView", e.toString());
        }
        // Reset the canvas and force a redraw
        canvas = new Canvas(bitmap);
        invalidate();
    }

    /*
    Save the width and height of the canvas so that if load() is called before the width and height are established, it will have the correct values
    Only seems to be an issue when activities are not maintained (low memory)
     */
    @Override
    public Parcelable onSaveInstanceState()
    {
        Bundle bundle = (Bundle) super.onSaveInstanceState();
        bundle.putInt("canvasWidth", getWidth());
        bundle.putInt("canvasHeight", getHeight());
        return bundle;
    }

    /*
    Restore the width and height of the canvas so that if load() is called before the width and height are established, it will have the correct values
    Only seems to be an issue when activities are not maintained (low memory)
     */
    @Override
    public void onRestoreInstanceState(Parcelable state)
    {
        super.onRestoreInstanceState(state);
        if (state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            if(bundle.containsKey("width") && bundle.containsKey("height"))
            {
                width = bundle.getInt("width");
                height = bundle.getInt("height");
            }
        }
    }

}
