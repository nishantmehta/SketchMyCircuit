package com.sketchmycircuit.ui;

import com.sketchmycircuit.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		Thread logoTimer = new Thread(){
		
			            @Override
		
			            public void run() {
	
			                // TODO Auto-generated method stub
		
			                try {
			
			                    sleep(5000);
		
			                    Intent i = new Intent(SplashScreen.this,CircuitSketchCanvas.class);
			
			                    startActivity(i);
			
			                } catch (InterruptedException e) {
			
			                    // TODO: handle exception
		
			                    e.printStackTrace();
			
			                }
			
			                finally{
			
			                    finish();
			
			                }
			
			            }
			
			        };
			
			        logoTimer.start();

	}
	
	
	

}
