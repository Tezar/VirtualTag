package net.solajpafistoj.tag.client;

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
	    protected boolean firstDraw =true;
	   
	    
	    public TagDrawable()
	    {
	        mPaint = new Paint();
	        mRect = new RectF();
	        mPaint.setStrokeWidth(3);
	    }

	    @Override
	    public void draw(Canvas canvas)
	    {
	    	RectF bounds = new RectF(this.getBounds());
	    	mPaint.setARGB(255, 255, 255, 255);
	        mPaint.setStyle(Style.FILL);
	        canvas.drawRoundRect(bounds, 10f, 10f, mPaint);
	        mPaint.setARGB(127, 0, 0, 0);
	        mPaint.setStyle(Style.STROKE);
	        canvas.drawRoundRect(bounds, 10f, 10f, mPaint);
	        

	        

	   
		    
	        
	        /*
	         *  0,0 -----------> X
	         *   |
	         *   |
	         *   |
	         *   ¡
	         *   Y
	         */

	    }
	    
	    @Override
	    public int getIntrinsicHeight()
	    {
	    	return 30;
	    }
	    
	    @Override
	    public int getIntrinsicWidth()
	    {
	    	return 50;
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