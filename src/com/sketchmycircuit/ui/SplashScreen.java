package com.sketchmycircuit.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.sketchmycircuit.R;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		int GlobalStart = 0;

		Thread logoTimer = new Thread() {

			@Override
			public void run() {

				// TODO Auto-generated method stub

				try {

					sleep(1000);
					SharedPreferences settings = getSharedPreferences(
							"setting", MODE_PRIVATE);
					if (settings.contains("done")) {
						Intent i = new Intent(SplashScreen.this,
								CircuitSketchCanvas.class);

						startActivity(i);
					} else {
						Intent i = new Intent(SplashScreen.this,
								tutorialone.class);

						startActivity(i);
					}

				} catch (InterruptedException e) {

					// TODO: handle exception

					e.printStackTrace();

				}

				finally {

					finish();

				}

			}

		};

		logoTimer.start();

	}

}
