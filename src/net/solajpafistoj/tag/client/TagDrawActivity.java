package net.solajpafistoj.tag.client;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TagDrawActivity extends Activity {
	EnableDraw enableDraw= null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             
        setContentView(R.layout.draw_tag);
        enableDraw = (EnableDraw) findViewById(R.id.draw_view);
        
        //todo: read data from accelerometer
        // maybe only when button is pushed / or movement on certain axis
        
        Button b = (Button) findViewById(R.id.done_button);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	 Intent returnIntent = new Intent();
            	 returnIntent.putExtra("result","returned text"); // adds String that is sent back to TagMapsActivity
            	
            	 ArrayList<Float> points = enableDraw.getListOfPoints(); 
            	 float[] strokes = new float[points.size()]; 
            	 for (int i = 0; i < strokes.length; i++) { 
            	      strokes[i] = points.get(i); 
            	 } 
            	 returnIntent.putExtra("strokes", strokes);
            	 setResult(RESULT_OK,returnIntent);     
            	 finish();
            }
          });
          
         
        
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        enableDraw.pause();
    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        enableDraw.resume();
    }
}
