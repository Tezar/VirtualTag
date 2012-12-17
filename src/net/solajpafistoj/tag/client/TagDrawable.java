package net.solajpafistoj.tag.client;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

//http://stackoverflow.com/questions/2967904/implementing-a-customized-drawable-in-android


public class TagDrawable extends Drawable {

	    private final Paint mPaint;
	    private final Paint mPathPaint;
	    private final RectF mRect;
	    
	    protected ArrayList<ArrayList<Float>> strokes;
	    
	    private ArrayList<Path> pointsToDraw = new ArrayList<Path>();
	    
	    
	    public TagDrawable(ArrayList<ArrayList<Integer>> strokes)
	    {
	    	super();
	    	
	    	mPaint = new Paint();
	    	mPathPaint = new Paint();
	        mRect = new RectF();
	        mPaint.setStrokeWidth(3);
	        
	        
	        mPathPaint.setColor(Color.RED);
	        mPathPaint.setStyle(Paint.Style.STROKE);
	        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
	        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
	        mPathPaint.setStrokeWidth(2);
	        
	        if(strokes != null){

		        float conversionWidth = getIntrinsicWidth()/480.0f;
		        float conversionHeight = getIntrinsicHeight()/640.0f;
		        
		        //convert list to paths
		        for(ArrayList<Integer> stroke : strokes){
		        	//just to be sure we by point, because there could be record with even number and taht could cause crash :-/
		        	int countPoint = (int) Math.floor(stroke.size() / 2 );
		        	
		        	Path path = new Path();
		        	pointsToDraw.add(path);
		        	
		        	
		        	
		        	path.moveTo(stroke.get(0)*conversionWidth, stroke.get(1)*conversionHeight);
		        	
		        	for(int g=1; g<countPoint;){
		        		float pX = stroke.get(g*2)*conversionWidth;
		        		float pY =  stroke.get(g*2+1)*conversionHeight;
		        		path.lineTo(pX, pY);
		        		g++;
		        	}
		        }
	        }
	        
	        
	    }

	    @Override
	    public void draw(Canvas canvas)
	    {
	    	RectF bounds = new RectF(this.getBounds());
	    	mPaint.setARGB(255, 255, 255, 255);
	        mPaint.setStyle(Style.FILL);
	        canvas.drawRoundRect(bounds, 10f, 10f, mPaint);
	        
	        
	        
            for (Path path : pointsToDraw) {
                canvas.drawPath(path, mPathPaint);
            }
	        
	        
	        
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
	    	return 4*30;
	    }
	    
	    @Override
	    public int getIntrinsicWidth()
	    {
	    	return 3*30;
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