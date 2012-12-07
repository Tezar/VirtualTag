package net.solajpafistoj.tag.client;

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
        //setContentView(R.layout.draw_tag);
        
        enableDraw = new EnableDraw(this);
        enableDraw.requestFocus();
        
        setContentView(enableDraw);
        
        //todo: read data from accelerometer
        // maybe only when button is pushed / or movement on certain axis
        /*
        
        Button b = (Button) findViewById(R.id.done_button);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	 Intent returnIntent = new Intent();
            	 returnIntent.putExtra("result","returned text");
            	 setResult(RESULT_OK,returnIntent);     
            	 finish();
            }
          });
          
          */
        
    }
}
