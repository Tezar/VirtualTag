package net.solajpafistoj.tag.client;

import android.os.Bundle;
import android.app.Activity;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;


import android.view.Menu;

//based on https://developers.google.com/maps/documentation/android/hello-mapview

public class MapTagsActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
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
