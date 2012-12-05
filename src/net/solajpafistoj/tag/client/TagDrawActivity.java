package net.solajpafistoj.tag.client;

import android.app.Activity;
import android.os.Bundle;

public class TagDrawActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_tag);
        
        EnableDraw enableDraw;
	    enableDraw = new EnableDraw(this);
        enableDraw.requestFocus();
        //todo: read data from accelerometer
        // maybe only when button is pushed / or movement on certain axis
    }
}
