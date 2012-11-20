package net.solajpafistoj.tag.client;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

//http://stackoverflow.com/questions/2967904/implementing-a-customized-drawable-in-android

public class TagDrawable extends Drawable {

	    private final Paint mPaint;
	    private final RectF mRect;
	    
	    public TagDrawable()
	    {
	        mPaint = new Paint();
	        mRect = new RectF();
	    }

	    @Override
	    public void draw(Canvas canvas)
	    {
	        // Set the correct values in the Paint
	        mPaint.setARGB(255, 255, 0, 0);
	        mPaint.setStrokeWidth(20);
	        mPaint.setStyle(Style.FILL);
	        
	        
	        /*
	         *  0,0 -----------> X
	         *   |
	         *   |
	         *   |
	         *   ¡
	         *   Y
	         */
	        

	        canvas.drawLine(0f, 0f, 100f,100f, mPaint);

	        mPaint.setARGB(255, 255, 255, 0);
	        canvas.drawLine(0f, 0f, 0f,100f, mPaint);
	    }
	    

	    @Override
	    public int getOpacity()
	    {
	        return PixelFormat.OPAQUE;
	    }

	    @Override
	    public void setAlpha(int arg0)
	    {
	    }

	    @Override
	    public void setColorFilter(ColorFilter arg0)
	    {
	    }
	}