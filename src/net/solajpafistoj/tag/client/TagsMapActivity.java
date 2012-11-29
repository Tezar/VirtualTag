package net.solajpafistoj.tag.client;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

//based on https://developers.google.com/maps/documentation/android/hello-mapview

public class TagsMapActivity extends MapActivity {

	EnableDraw enableDraw;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        //todo: get current position and zoom into it
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        
        
        Drawable drawable =new TagDrawable();// this.getResources().getDrawable(R.drawable.androidmarker);
        TagsMapOverlays itemizedoverlay = new TagsMapOverlays(drawable);
        
        GeoPoint point = new GeoPoint(19240000,-99120000);
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        
        itemizedoverlay.addOverlay(overlayitem);
        
        mapView.getOverlays().add(itemizedoverlay);

        // todo: We need a button that lets us change to a draw mode.
        enableDraw = new EnableDraw(this);
        enableDraw.setBackgroundColor(Color.BLUE);
        enableDraw.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map, menu);
        return true;
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
