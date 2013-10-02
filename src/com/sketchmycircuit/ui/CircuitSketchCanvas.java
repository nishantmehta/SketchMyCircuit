package com.sketchmycircuit.ui;



import com.sketchmycircuit.R;
import com.sketchmycircuit.R.layout;
import com.sketchmycircuit.R.menu;
import com.sketchmycircuit.component.CustomCircuitSketch;
import com.sketchmycircuit.component.SketchView;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.app.Application;

public class CircuitSketchCanvas extends Activity {
	
	
	CustomCircuitSketch CCS;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circuit_sketch_canvas);
		
		/*Author Sarim*/
		//finding the height and width and setting it to global members
		DisplayMetrics displaymetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		/*((MyApplication)this.getApplication()).setHeigth(displaymetrics.heightPixels);
		((MyApplication)this.getApplication()).setWidth(displaymetrics.widthPixels);*/
		
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		
		
		
		
		/*--------------------------------*/
		
		
		
		
		
		
		
		CCS = new CustomCircuitSketch(this,height,width);
		
        
        
        LinearLayout ll = (LinearLayout)findViewById(R.id.lin);
        
        ll.addView(CCS.gestureOverlayView,CCS.params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_circuit_sketch_canvas, menu);
		return true;
	}
	
	
	
	/*Author Sarim*/
	
	// action bar buttons
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_erase:
	            openErase();
	            return true;
	        case R.id.menu_setting:
	            openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void openSettings() {
		// TODO Auto-generated method stub
		
	}

	private void openErase() {
		// TODO Auto-generated method stub
		
		CCS.erase();
		
	}
	
	
	
	
	//defining class members, which can be used globally
	/*public class MyApplication extends Application {

	    private int height;
	    private int width;

	    public int getHeight() {
	        return height;
	    }

	    public void setHeigth(int height) {
	        this.height = height;
	    }
	    
	    
	    public int getWidth() {
	        return width;
	    }

	    public void setWidth(int width) {
	        this.width = width;
	    }
	}*/
	/*-------------------------------------------------*/
	

}
