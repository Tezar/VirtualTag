package net.solajpafistoj.tag.client;

import java.util.ArrayList;

import android.os.Bundle;

//convenient (un)packer for sending strokes thru Intents

public class StrokesPackager extends Object{
	
	protected ArrayList<ArrayList<Point>> strokes = null;

	public static final String STROKE_COUNT = "StrokesPackager.STROKE_COUNT";
	public static final String STROKE_STORAGE = "StrokesPackager.STROKE_STORAGE";
	
	public void StrokesPackager(Bundle bundle){
		assert(bundle != null);
		int count = bundle.getInt(STROKE_COUNT, 0);
		 
		strokes = new ArrayList<ArrayList<Point>>();
		
		for (int i = 0; i < count; i++){
			ArrayList<Point> stroke = new ArrayList<Point>();
			
			float[] points = bundle.getFloatArray(STROKE_STORAGE + i);
			
			assert( (points.length % 2) == 0);	//aray must have even number
			
			int pointCount = points.length/2;
			
			for(int g = 0; g < pointCount ; g++){
				Point p = new Point(0,0);
				p.x = points[g];
				p.y = points[g+1];
				stroke.add(p);
			}
			
			strokes.add(stroke);
			
		}
	}
	
	public void StrokesPackager(ArrayList<ArrayList<Point>> lines){
		strokes = lines;
	}
	
	public ArrayList<ArrayList<Point>> getList(){
		assert(strokes != null);
		return strokes;
	}
	
	
	public Bundle getBundle(){
		assert(strokes != null);
		
		Bundle bundle = new Bundle();
		
        int count = strokes.size();

        bundle.putInt(STROKE_COUNT, count);

        for (int i = 0; i < count; i++){
        	ArrayList<Point> stroke = strokes.get(i);
        	int pointCount = stroke.size();
        	 
        	float[] flattened = new float[ pointCount*2 ];
        	
        	int g=0;
        	for (Point p:  strokes.get(i) ){
        		flattened[g++] = p.x;
        		flattened[g++] = p.y;
			}	
        	
        	bundle.putFloatArray(STROKE_STORAGE + i , flattened );
        }

		return bundle;
	}

	

}
