package net.solajpafistoj.tag.client;

// from: http://bricolsoftconsulting.com/2011/10/31/extending-mapview-to-add-a-change-event/
// provides means for notifying taht   

import java.util.Timer;
import java.util.TimerTask;
 
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
 
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
 
public class ExtendedMapView extends MapView
{
    // ------------------------------------------------------------------------
    // LISTENER DEFINITIONS
    // ------------------------------------------------------------------------
 
	// Change listener
    public interface OnChangeListener
    {
        public void onChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter, int newZoom, int oldZoom);
    }
 
    // ------------------------------------------------------------------------
    // MEMBERS
    // ------------------------------------------------------------------------
 
    private ExtendedMapView mThis;
    private long mEventsTimeout = 250L;     // Set this variable to your preferred timeout
    private boolean mIsTouched = false;
    private GeoPoint mLastCenterPosition;
    private int mLastZoomLevel;
    private Timer mChangeDelayTimer = new Timer();
    private ExtendedMapView.OnChangeListener mChangeListener = null;
 
    // ------------------------------------------------------------------------
    // RUNNABLES
    // ------------------------------------------------------------------------
    private Runnable mOnChangeTask = new Runnable(){
						public void run() {
						        if (mChangeListener != null) mChangeListener.onChange(mThis, getMapCenter(), mLastCenterPosition, getZoomLevel(), mLastZoomLevel);
						        mLastCenterPosition = getMapCenter();
						        mLastZoomLevel = getZoomLevel();
						}
    			};
    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------
 
    public ExtendedMapView(Context context, String apiKey)
    {
        super(context, apiKey);
        init();
    }
 
    public ExtendedMapView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
 
    public ExtendedMapView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }
 
    private void init()
    {
        mThis = this;
        mLastCenterPosition = this.getMapCenter();
        mLastZoomLevel = this.getZoomLevel();
    }
 
    // ------------------------------------------------------------------------
    // GETTERS / SETTERS
    // ------------------------------------------------------------------------
 
    public void setOnChangeListener(ExtendedMapView.OnChangeListener l)
    {
        mChangeListener = l;
    }
 
    // ------------------------------------------------------------------------
    // EVENT HANDLERS
    // ------------------------------------------------------------------------
 
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        // Set touch internal
        mIsTouched = (ev.getAction() != MotionEvent.ACTION_UP);
 
        return super.onTouchEvent(ev);
    }
 
    @Override
    public void computeScroll()
    {
        super.computeScroll();
 
        // Check for change
        if (isSpanChange() || isZoomChange())
        {
            // If computeScroll called before timer counts down we should drop it and
            // start counter over again
            resetMapChangeTimer();
        }
    }
 
    // ------------------------------------------------------------------------
    // TIMER RESETS
    // ------------------------------------------------------------------------
 
    private void resetMapChangeTimer()
    {
    	ExtendedMapView.this.removeCallbacks(mOnChangeTask);
    	ExtendedMapView.this.postDelayed(mOnChangeTask, mEventsTimeout);
    }
 
    // ------------------------------------------------------------------------
    // CHANGE FUNCTIONS
    // ------------------------------------------------------------------------
 
    private boolean isSpanChange()
    {
        return !mIsTouched && !getMapCenter().equals(mLastCenterPosition);
    }
 
    private boolean isZoomChange()
    {
        return (getZoomLevel() != mLastZoomLevel);
    }
 
}