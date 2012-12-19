package net.solajpafistoj.tag.client;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Path;
import android.os.Bundle;

//convenient (un)packer for sending strokes thru Intents

public class StrokesPackager extends Object{
	
	protected ArrayList<ArrayList<Integer>> strokes = null;
	protected ArrayList<Integer> colors = null;

	public static final String STROKE_COUNT = "StrokesPackager.STROKE_COUNT";
	public static final String STROKE_STORAGE = "StrokesPackager.STROKE_STORAGE";
	

	public StrokesPackager(Bundle bundle){
		assert(bundle != null);
		int count = bundle.getInt(STROKE_COUNT, 0);
		 
		strokes = new ArrayList<ArrayList<Integer>>();
		colors = new ArrayList<Integer>();
		
		for (int i = 0; i < count; i++){
			ArrayList<Integer> stroke = new ArrayList<Integer>();
			
			int[] points = bundle.getIntArray(STROKE_STORAGE + i);
			
			//points are stored in flat array, each coordinates as two subsequent items
			
			int pointCount = points.length;
			
			//first point is color
			colors.add( points[0] );
			
			for(int g = 1; g < pointCount ; g++){
				stroke.add( points[g] );
			}
			
			strokes.add(stroke);
			
		}
	}
	
	public StrokesPackager(String data){
		try {
			JSONArray content = new JSONArray( data );	
			
			int count = content.length();
			
			strokes = new ArrayList<ArrayList<Integer>>();
			colors = new ArrayList<Integer>();
			
			for (int i = 0; i < count; i++){
					ArrayList<Integer> stroke = new ArrayList<Integer>();
					
					JSONArray dataStroke = content.getJSONArray(i);
					
					int pointCount = dataStroke.length();
					
					colors.add(  dataStroke.getInt(0) );
					
					for(int g = 1; g < pointCount ; g++){
							stroke.add( dataStroke.getInt(g) );
					}
					
					strokes.add(stroke);
			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
    
	
	public  StrokesPackager(ArrayList<ArrayList<Integer>> lines, ArrayList<Integer> cs ){
		strokes = lines;
		colors = cs;
	}
	
	
	
	
	public ArrayList<ArrayList<Integer>> getStrokes(){
		assert(strokes != null);
		return strokes;
	}
	
	public ArrayList<Integer> getColors(){
		assert(colors != null);
		return colors;
	}
		
	
	
	public Bundle getBundle(){
		assert(strokes != null);
		assert(colors != null);
		
		Bundle bundle = new Bundle();
		
        int count = strokes.size();

        bundle.putInt(STROKE_COUNT, count);

        for (int i = 0; i < count; i++){
        	ArrayList<Integer> stroke = strokes.get(i);
        	int pointCount = stroke.size();
        	 
        	int[] flattened = new int[ pointCount +1 ]; //+1 for colour
        	
        	int g=0;
        	
        	
        	flattened[g++] = colors.get(i);
        			
        	for (Integer f:  strokes.get(i) ){
        		flattened[g++] = f;
			}	
        	
        	bundle.putIntArray(STROKE_STORAGE + i ,  flattened );
        }

		return bundle;
	}

	
	
	public ArrayList<Path> getPaths2(int width, int height){
		assert(strokes != null);
		
		float conversionWidth = width/480.0f;
	    float conversionHeight = height/780.0f;
	     
	    ArrayList<Path> paths = new ArrayList<Path>();
	        
	    //convert list to paths
	    for(ArrayList<Integer> stroke : strokes){
			//just to be sure we by point, because there could be record with even number and that could cause crash :-/
			int countPoint = (int) Math.floor(stroke.size() / 2 );
			    	
			Path path = new Path();
			paths.add(path);
			
			path.moveTo(stroke.get(0)*conversionWidth, stroke.get(1)*conversionHeight);
			
			for(int g=1; g<countPoint;){
				float pX = stroke.get(g*2)*conversionWidth;
				float pY =  stroke.get(g*2+1)*conversionHeight;
				path.lineTo(pX, pY);
				g++;
			}
	     }
	     
	     return paths;
	}

}
