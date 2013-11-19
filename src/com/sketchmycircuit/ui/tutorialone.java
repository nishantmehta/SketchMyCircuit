package com.sketchmycircuit.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sketchmycircuit.R;

public class tutorialone extends Activity {
	static int count=0;
	ImageView IV ;
	Button previous,next;
	int[] images={R.drawable.initialcircuit,R.drawable.erasegesture,R.drawable.componentgestures,R.drawable.clearall,R.drawable.exportbutton,R.drawable.exportgesture};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorialone);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		IV = (ImageView)findViewById(R.id.Iv);
		
		previous=(Button)findViewById(R.id.Button01);
		previous.setVisibility(View.INVISIBLE);
		refreshScreen();
		
		next=(Button)findViewById(R.id.button1);
		
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	count++;
            	
            	refreshScreen();
            	
            }
        });

		
        previous.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	count--;
            	refreshScreen();
            	
            	
            }
        });
        
		
	}
	public void refreshScreen()
	{
		if(count>0)
			previous.setVisibility(View.VISIBLE);
		else
			previous.setVisibility(View.INVISIBLE);
		if(count==5)
			next.setText("go to app");
		if(count<6)
		{
			
			IV.setImageResource(images[count]);
		}
		else
		{	
			SharedPreferences settings = getSharedPreferences("setting", MODE_PRIVATE);
			Editor editor = settings.edit();
		     editor.putString("done", "some value");
		     editor.commit();
			Intent i=new Intent(tutorialone.this,CircuitSketchCanvas.class);
        	
        	startActivity(i);
        	overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        	finish();
    	}
			
		
		
		
	
       
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_circuit_sketch_canvas, menu);
		return true;
	}
	

}
