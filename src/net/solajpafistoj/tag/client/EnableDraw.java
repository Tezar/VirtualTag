package net.solajpafistoj.tag.client;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.util.AttributeSet;

public class EnableDraw extends SurfaceView implements OnTouchListener, Runnable {
   private ArrayList<Path> pointsToDraw = new ArrayList<Path>();
    private Paint mPaint;
    Path path;

    public EnableDraw(Context context, AttributeSet attrs) {
        // ODO Auto-generated method stub
    	super(context, attrs);
    	mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setColor(Color.RED);
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

        

        @Override
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
            for (Path path : pointsToDraw) {
                canvas.drawPath(path, mPaint);
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



    


    @Override
    public boolean onTouch(View v, MotionEvent me) {
        // TODO Auto-generated method stub
                synchronized(pointsToDraw)
                {
        if(me.getAction() == MotionEvent.ACTION_DOWN){
            path = new Path();
            path.moveTo(me.getX(), me.getY());
            //path.lineTo(me.getX(), me.getY());
            pointsToDraw.add(path);
        }else if(me.getAction() == MotionEvent.ACTION_MOVE){
            path.lineTo(me.getX(), me.getY());
        }else if(me.getAction() == MotionEvent.ACTION_UP){
            //path.lineTo(me.getX(), me.getY());
        }
        }       
        return true;

    }

}