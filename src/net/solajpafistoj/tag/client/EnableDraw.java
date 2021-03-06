package net.solajpafistoj.tag.client;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class EnableDraw extends SurfaceView implements OnTouchListener, Runnable {
   private ArrayList<Path> pointsToDraw = new ArrayList<Path>();
   private ArrayList<Integer> listOfPoints;
   private ArrayList<ArrayList<Integer>> strokes = new ArrayList<ArrayList<Integer>>();
   private ArrayList<Integer> colors = new ArrayList<Integer>();
   
   private Paint mPaint;
   Path path;
   
   int currentPaint;

    
    public EnableDraw(Context context, AttributeSet attrs) {
        // ODO Auto-generated method stub
    	super(context, attrs);
    	mPaint = new Paint();
        mPaint.setDither(true); // decieves the human eye
        
        float[] hsv = {(float) ((System.currentTimeMillis() / 1000f)%(3600*6))/60.f,1.f, 1.f};
        
        currentPaint = Color.HSVToColor(hsv);
        mPaint.setColor(currentPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);

        holder = getHolder();
        setOnTouchListener(this);
    }


        Thread t = null;
        SurfaceHolder holder;
        boolean isItOk = false ;

        

        //@Override
        public void run() {
            // TODO Auto-generated method stub
            while( isItOk == true){

                if(!holder.getSurface().isValid()){
                    continue;
                }

                Canvas c = holder.lockCanvas();
                c.drawARGB(255, 0, 0, 0);
                onDraw(c);
                holder.unlockCanvasAndPost(c);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
                        synchronized(pointsToDraw)
                        {
                        	int count = pointsToDraw.size();
                        	
                        	for (int i = 0; i < count; i++) {
                        		mPaint.setColor(colors.get(i));
                        		canvas.drawPath(pointsToDraw.get(i), mPaint);
							}
                        }
        }

        public void pause(){
            isItOk = false;
            while(true){
                try{
                    t.join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                break;
            }
            t = null;
        }

        public void resume(){
            isItOk = true;  
            t = new Thread(this);
            t.start();

        }



    


    //@Override
    public boolean onTouch(View v, MotionEvent me) {
        // TODO Auto-generated method stub
                synchronized(pointsToDraw)
                {
        if(me.getAction() == MotionEvent.ACTION_DOWN){
            colors.add(currentPaint);
        	
        	path = new Path();
            pointsToDraw.add(path);
            
            path.moveTo(me.getX(), me.getY());
            
            listOfPoints = new ArrayList<Integer>();
            strokes.add(listOfPoints);
            
            listOfPoints.add( Math.round(me.getX()) );
            listOfPoints.add( Math.round(me.getY()) );
            
        }else if(me.getAction() == MotionEvent.ACTION_MOVE){
            path.lineTo(me.getX(), me.getY());
            
			listOfPoints.add( Math.round(me.getX()) );
            listOfPoints.add( Math.round(me.getY()) );
            
        }else if(me.getAction() == MotionEvent.ACTION_UP){ 
        }       
                }//end sync
        return true;
        
        

    }
    
    // allows us to get the ArrayList
    public ArrayList<ArrayList<Integer>> getStrokes() {
    	return strokes;
    }

	public ArrayList<Integer> getColors() {
		return colors;
	}

}