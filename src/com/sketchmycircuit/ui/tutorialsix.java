package com.sketchmycircuit.ui;

import com.sketchmycircuit.R;

import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class tutorialsix extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorialsix);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
		
		Button button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i=new Intent(tutorialsix.this,CircuitSketchCanvas.class);
            	
            	            	
            	startActivity(i);
            	finish();
                // Perform action on click
            }
        });
		
		
	}
	

}
