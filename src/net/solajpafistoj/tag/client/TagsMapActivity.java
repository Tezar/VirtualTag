package net.solajpafistoj.tag.client;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import net.solajpafistoj.tag.client.ExtendedMapView;

//based on https://developers.google.com/maps/documentation/android/hello-mapview

public class TagsMapActivity extends MapActivity {

	EnableDraw enableDraw;
	
	//for controlling zooming, location etc...
	private MapController mapController;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        ExtendedMapView mapView = (ExtendedMapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        // Add listener
        mapView.setOnChangeListener(new MapViewChangeListener());
        
        
        mapController = mapView.getController();
        
        
        //test overlay of items 
        Drawable drawable =new TagDrawable();// this.getResources().getDrawable(R.drawable.androidmarker);
        TagsMapOverlays itemizedoverlay = new TagsMapOverlays(drawable);
        
        GeoPoint point = new GeoPoint(19240000,-99120000);
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        
        itemizedoverlay.addOverlay(overlayitem);
        
        mapView.getOverlays().add(itemizedoverlay);
        
        
        //current location overlay
        
        final MyLocationOverlay mylocationOverlay = new MyLocationOverlay(this, mapView);
        mylocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mylocationOverlay);
        
        //as soon as we get fix, move map to that position
        mylocationOverlay.runOnFirstFix( new Runnable(){
        									public void run() {
        										mapController.animateTo( mylocationOverlay.getMyLocation() );		
        									}
        								});
        
        
        // todo: We need a button that lets us change to a draw mode.
        enableDraw = new EnableDraw(this);
        enableDraw.setBackgroundColor(Color.BLUE);
        enableDraw.requestFocus();
    }

    
    
    
    
    private class MapViewChangeListener implements ExtendedMapView.OnChangeListener
    {
 
        public void onChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter, int newZoom, int oldZoom)
        {
            // Check values
            if ((!newCenter.equals(oldCenter)) && (newZoom != oldZoom))
            {
                // Map Zoom and Pan Detected
                // TODO: Add special action here
            }
            else if (!newCenter.equals(oldCenter))
            {
                // Map Pan Detected
                // TODO: Add special action here
            }
            else if (newZoom != oldZoom)
            {
                // Map Zoom Detected
                // TODO: Add special action here
            }
        }
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
