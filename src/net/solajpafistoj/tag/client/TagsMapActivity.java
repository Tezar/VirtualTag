package net.solajpafistoj.tag.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import net.solajpafistoj.tag.client.ExtendedMapView;

//based on https://developers.google.com/maps/documentation/android/hello-mapview

public class TagsMapActivity extends MapActivity {

	
	
	//for controlling zooming, location etc...
	private MapController mapController;
	
	protected TagsMapOverlays activeOverlay= null;
	
	DownloadTagsTask downloadTask = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        ExtendedMapView mapView = (ExtendedMapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        // Add listener
        mapView.setOnChangeListener(new MapViewChangeListener());
        
        
        mapController = mapView.getController();
        
   
        
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
        
        
    }

    
    protected void displayTags( ArrayList<TagItem> items )
    {
    	ExtendedMapView mapView = (ExtendedMapView) findViewById(R.id.mapview);
    	
    	if(activeOverlay != null){
    		mapView.getOverlays().remove(activeOverlay);
    		activeOverlay = null;
    	}
        
        //Drawable drawable =new TagDrawable();// this.getResources().getDrawable(R.drawable.androidmarker);
        activeOverlay = new TagsMapOverlays();
        
        
        int count = items.size();
        for (int i = 0; i < count; i++) {
            OverlayItem overlayitem = new OverlayItem(items.get(i).location, null, null);
            Drawable marker = new TagDrawable();
            marker.setBounds(0, 0, marker .getIntrinsicWidth(), marker .getIntrinsicHeight());
            overlayitem.setMarker( marker );
            
            activeOverlay.addOverlay(overlayitem);
            //break; //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        
        mapView.getOverlays().add(activeOverlay);
        
    	
    }
    
    
    private class MapViewChangeListener implements ExtendedMapView.OnChangeListener
    {
 
        public void onChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter, int newZoom, int oldZoom)
        {
        	if( downloadTask != null ){
        		downloadTask.abort();
        	}
        	downloadTask = null;
        	downloadTask = new DownloadTagsTask();
        	downloadTask.execute(newCenter);
        	
        	
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
    
    
    
    private class DownloadTagsTask extends AsyncTask<GeoPoint, Integer, ArrayList<TagItem> > {
    	protected boolean error = false;
    	protected boolean aborted = false;
    	
        protected ArrayList<TagItem> doInBackground(GeoPoint... points) {
        	GeoPoint point = points[0];

        	//error free
        	error = false;
        	
        	ArrayList<TagItem> items = new ArrayList<TagItem>(); 

        	double lat = point.getLatitudeE6()  * 1E-6;
        	double lon = point.getLongitudeE6()  * 1E-6;
        	
        	String url = "http://virtualtagmap.appspot.com/s?lat="+String.valueOf(lat)+"&lon="+String.valueOf(lon);
        	String data = fetchURL(url);
        	
        	if(aborted) return null;
        	
        	try {
        		JSONObject object = new JSONObject( data );
        		if(aborted) return null;
        		
        		String state  = object.getString("state");
        		if(! state.equals("ok")){
        			error = true;
        			return null;
        		}
        		
        		JSONArray fetchedItems = object.getJSONArray("result");

        	      for (int i = 0; i < fetchedItems.length(); i++) {
        	        JSONObject jsonObject = fetchedItems.getJSONObject(i);
        	        TagItem item = new TagItem();
        	        
        	        JSONArray location = jsonObject.getJSONArray("loc");
        	        
        	        item.location = new GeoPoint( (int)Math.round(location.getDouble(0) * 1E6) , (int)Math.round(location.getDouble(1) * 1E6) );
        	        items.add(item);
        	      }
        	    } catch (Exception e) {
        	      e.printStackTrace();
        	    }
        	
            
            return items;
                
               
        }

        protected void onProgressUpdate(Integer... progress) {
            
        }

        protected void onPostExecute(ArrayList<TagItem> result) {
        	//if our task is aborted, do not attemp to update results
        	if(aborted) return;
        	
        	if(error){
        		Toast.makeText(getApplicationContext(), "Error during fetch", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	if(result == null){
        		Toast.makeText(getApplicationContext(), "You shall not be here", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	Toast.makeText(getApplicationContext(), "Displaying", Toast.LENGTH_LONG).show();
        	displayTags(result);   
        }
        
        
        protected  String fetchURL(String url) {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
              HttpResponse response = client.execute(httpGet);
              StatusLine statusLine = response.getStatusLine();
              int statusCode = statusLine.getStatusCode();
              if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                  builder.append(line);
                }
              } else {
                Log.e("fetchUrl","Failed to download file");
              }
            } catch (ClientProtocolException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
            return builder.toString();
          }
        
        public void abort(){
        		aborted = true;
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
