package com.sketchmycircuit.ui;


import com.sketchmycircuit.R;
import com.sketchmycircuit.R.layout;
import com.sketchmycircuit.R.menu;
import com.sketchmycircuit.component.CustomCircuitSketch;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

public class CircuitSketchCanvas extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circuit_sketch_canvas);
		CustomCircuitSketch CCS = new CustomCircuitSketch(this);
        
        
        LinearLayout ll = (LinearLayout)findViewById(R.id.lin);
        
        ll.addView(CCS.gestureOverlayView,CCS.params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_circuit_sketch_canvas, menu);
		return true;
	}

}
