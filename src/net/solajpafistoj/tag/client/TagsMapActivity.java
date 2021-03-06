package net.solajpafistoj.tag.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import net.solajpafistoj.tag.client.TagDrawActivity;



//based on https://developers.google.com/maps/documentation/android/hello-mapview

public class TagsMapActivity extends MapActivity {

	final int DRAW_INTENT = 1;
	
	//for controlling zooming, location etc...
	private MapController mapController;
	
	protected TagsMapOverlays activeOverlay= null;
	protected MyLocationOverlay mylocationOverlay;	
		
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
        
        mylocationOverlay = new MyLocationOverlay(this, mapView);
        mylocationOverlay.enableMyLocation();
        mapView.getOverlays().add(mylocationOverlay);
        
        //as soon as we get fix, move map to that position
        mylocationOverlay.runOnFirstFix( new Runnable(){
        									public void run() {
        										mapController.animateTo( mylocationOverlay.getMyLocation() );		
        									}
        								});
        
        //automatically load the first view
    	downloadTask = new DownloadTagsTask();
    	downloadTask.execute( mapView.getMapCenter() );
        
        
    }

    
    protected void displayTags( ArrayList<TagItem> items )
    {
    	ExtendedMapView mapView = (ExtendedMapView) findViewById(R.id.mapview);
    	
    	if(activeOverlay != null){
    		mapView.getOverlays().remove(activeOverlay);
    		activeOverlay = null;
    	}
        
        //Drawable drawable =new TagDrawable();// this.getResources().getDrawable(R.drawable.androidmarker);
        activeOverlay = new TagsMapOverlays(this);
        
        
        int count = items.size();
        for (int i = 0; i < count; i++) {
        	TagItem item = items.get(i);
            OverlayItem overlayitem = new OverlayItem(item.location, null, null);
            Drawable marker = new TagDrawable( item.pck );
            marker.setBounds(0, 0, marker .getIntrinsicWidth(), marker .getIntrinsicHeight());

            
            overlayitem.setMarker( marker );
            
            activeOverlay.addOverlay(overlayitem);
            //break; //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        
        //if you try to add overlay that is empty, you will get null exception when you try to touch screen...
        if(activeOverlay.size() > 0)   mapView.getOverlays().add(activeOverlay);
        
    	
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

        	if(point == null){
        		error = true;
        		return null;
        	}
        	
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
        	        StrokesPackager pckg = new StrokesPackager(  jsonObject.getString("content") );  
        	        
        	        item.location = new GeoPoint( (int)Math.round(location.getDouble(0) * 1E6) , (int)Math.round(location.getDouble(1) * 1E6) );
        	        item.pck = pckg;
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
    
    private class UploadTagTask extends AsyncTask<JSONArray, Void, String > {
    	
    	protected GeoPoint location;
    	
    	public UploadTagTask(GeoPoint loc){
    		super();
    		location = loc;
    	}

    	
        protected String doInBackground(JSONArray... data) {
        	JSONArray combined = data[0];
        	String txtResponse = null;
        	
        	if(location==null){
        		return "Cannot access your current location. Upload failed.";
        	}
        	
        	double lat = location.getLatitudeE6()  * 1E-6;
        	double lon = location.getLongitudeE6()  * 1E-6;
        
    		try {
    			
    			HttpClient httpclient = new DefaultHttpClient();
    			HttpPost httppost = new HttpPost("http://virtualtagmap.appspot.com/s?lat="+URLEncoder.encode(String.valueOf(lat), "UTF-8")+"&lon="+URLEncoder.encode(String.valueOf(lon), "UTF-8") );

    		
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("strokes", combined.toString() ));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        
		        txtResponse = EntityUtils.toString(response.getEntity());

    	    } catch (Exception e) {
    	    	e.printStackTrace();
    	    }
    	
    		JSONObject object;
    		if(txtResponse != null){
				try {
					object = new JSONObject( txtResponse );
		        	return object.getString("result") ;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		
        	return "you should not be here";
        }

        protected void onPostExecute(String msg) {
        	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        	
        	
        	if( downloadTask != null ){
        		downloadTask.abort();
        	}
        	downloadTask = null;
        	downloadTask = new DownloadTagsTask();
        	downloadTask.execute(location);
        	
        	
        }
        
        

		
        
    }    
    
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK){	//let's skip all results that aren't ok
			return;
		}
    	
    	switch(requestCode){
    		case DRAW_INTENT:
    			
    			UploadTagTask task = new UploadTagTask( mylocationOverlay.getMyLocation() );

    			StrokesPackager pckg = new StrokesPackager( data.getExtras() );
            	 
    			ArrayList<ArrayList<Integer>> strokes = pckg.getStrokes();
    			ArrayList<Integer> colors = pckg.getColors();
    			
    			JSONArray jsonCombined = new JSONArray();
    			int count = strokes.size();
    			
    			for (int i = 0; i < count; i++) {
    				JSONArray jsonStroke = new JSONArray();
    				ArrayList<Integer> stroke = strokes.get(i);
    				
    				
    				jsonStroke.put( colors.get(i));
    				
    				for(Integer f : stroke ){
    					jsonStroke.put(f);
    				}
    			 
    				jsonCombined.put(jsonStroke);
    				
				}

    				
                task.execute(jsonCombined);

    			Toast.makeText(this, "Uploading", Toast.LENGTH_SHORT).show();
    			break;
    		default:
    			Toast.makeText(this, "Unknown result", Toast.LENGTH_SHORT).show();
    	}
    	
    
    }//onActivityResult    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map, menu);
        return true;
    }
    

    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case R.id.menu_settings:
	        	
	        	Intent i = new Intent(this, TagDrawActivity.class);
	        	startActivityForResult(i, DRAW_INTENT);
	        	
	        return true;
	        
	        case R.id.menu_delete:
	        	// Future function
	        default:
	        	return super.onOptionsItemSelected(item);
	        	
	     
    	}
    }
    
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}
