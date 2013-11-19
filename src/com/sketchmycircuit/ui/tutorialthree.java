package com.sketchmycircuit.ui;

import com.sketchmycircuit.R;

import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tutorialthree extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorialthree);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
		
		Button button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i=new Intent(tutorialthree.this,tutorialfour.class);
            	startActivity(i);
            	overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            	finish();
                // Perform action on click
            }
        });
		
		
	}
	

}
