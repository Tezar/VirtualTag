package net.solajpafistoj.tag.client;

import java.util.ArrayList;

import android.os.Bundle;

//convenient (un)packer for sending strokes thru Intents

public class StrokesPackager extends Object{
	
	protected ArrayList<ArrayList<Float>> strokes = null;

	public static final String STROKE_COUNT = "StrokesPackager.STROKE_COUNT";
	public static final String STROKE_STORAGE = "StrokesPackager.STROKE_STORAGE";
	

	public StrokesPackager(Bundle bundle){
		assert(bundle != null);
		int count = bundle.getInt(STROKE_COUNT, 0);
		 
		strokes = new ArrayList<ArrayList<Float>>();
		
		for (int i = 0; i < count; i++){
			ArrayList<Float> stroke = new ArrayList<Float>();
			
			float[] points = bundle.getFloatArray(STROKE_STORAGE + i);
			
			//assert( (points.length % 2) == 0);	//array must have even number
			
			int pointCount = points.length/2;
			
			for(int g = 0; g < pointCount ; g++){
				stroke.add( points[g] );
			}
			
			strokes.add(stroke);
			
		}
	}
	
	public  StrokesPackager(ArrayList<ArrayList<Float>> lines){
		strokes = lines;
	}
	
	public ArrayList<ArrayList<Float>> getStrokes(){
		assert(strokes != null);
		return strokes;
	}
	
	
	public Bundle getBundle(){
		assert(strokes != null);
		
		Bundle bundle = new Bundle();
		
        int count = strokes.size();

        bundle.putInt(STROKE_COUNT, count);

        for (int i = 0; i < count; i++){
        	ArrayList<Float> stroke = strokes.get(i);
        	int pointCount = stroke.size();
        	 
        	float[] flattened = new float[ pointCount ];
        	
        	int g=0;
        	for (Float f:  strokes.get(i) ){
        		flattened[g++] = f;
			}	
        	
        	bundle.putFloatArray(STROKE_STORAGE + i ,  flattened );
        }

		return bundle;
	}

	

}
