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

public class tutorialfour extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorialfour);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
		
		Button button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i=new Intent(tutorialfour.this,tutorialfive.class);
            	startActivity(i);
            	overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            	finish();
                // Perform action on click
            }
        });
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_circuit_sketch_canvas, menu);
		return true;
	}
	

}
