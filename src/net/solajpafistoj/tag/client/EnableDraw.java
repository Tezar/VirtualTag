package net.solajpafistoj.tag.client;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class EnableDraw extends View implements OnTouchListener
{
	private ArrayList<Point> points;
	private ArrayList<ArrayList<Point>> lines;
	private Paint paint;

	private int brushSize;
	private int brushColor;

	public EnableDraw(Context context) {
		super(context);
		setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        // Initialize everything that needs initialization
        points = new ArrayList<Point>();
        lines = new ArrayList<ArrayList<Point>>();
        lines.add(points);
        paint = new Paint();
        brushColor = Color.RED;
        brushSize = 6;

        paint.setAntiAlias(true);
	}

	@Override
    public void onDraw(Canvas canvas) {
		drawLines(canvas);
}

	public boolean onTouch(View view, MotionEvent event) {
		float xPos = event.getX();
		float yPos = event.getY();

		// if the new point is too far away from the previous point, we assume it's the beginning of a new line
		if(points.size() > 1)
		{
			if((xPos < ((points.get(points.size()-2).x)-(getWidth()*0.05))) ||
			   (xPos > ((points.get(points.size()-2).x)+(getWidth()*0.05))) ||
			   (yPos < ((points.get(points.size()-2).y)-(getHeight()*0.05)))||
		   	   (yPos > ((points.get(points.size()-2).y)+(getWidth()*0.05))))
			{
				points = new ArrayList<Point>();
				lines.add(points);
			}
		}
		// This adds a point to our line and lets us redraw
		
			Point point = new Point(brushColor, brushSize);
			point.x = xPos;
			point.y = yPos;
			points.add(point);
			invalidate();
		
		
			return true; 
	}

	public void drawLines(Canvas canvas)
	{
		Point point;
		Point prev;
		int i = 0;
		int j = 0;

		// For each list of points draw lines between the points
		for(i = 0; i < lines.size(); i++){
			for(j = 0; j < lines.get(i).size(); j++) {
        		point = lines.get(i).get(j);

        		paint.setColor(point.color);
        		paint.setStrokeWidth(point.size);

            	if(j > 0){
            		prev = lines.get(i).get(j-1);
            		canvas.drawLine(prev.x, prev.y, point.x, point.y, paint);
            	}
        	}
		}
	}
}
