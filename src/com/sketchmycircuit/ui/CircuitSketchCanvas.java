package com.sketchmycircuit.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sketchmycircuit.R;
import com.sketchmycircuit.component.CustomCircuitSketch;

public class CircuitSketchCanvas extends Activity {

	CustomCircuitSketch CCS;
	LinearLayout L1;
	LinearLayout L2;
	ImageView image;
	int height;
	int width;
	int countForExport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circuit_sketch_canvas);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		/* Author Sarim */
		// finding the height and width and setting it to global members
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		/*
		 * ((MyApplication)this.getApplication()).setHeigth(displaymetrics.
		 * heightPixels);
		 * ((MyApplication)this.getApplication()).setWidth(displaymetrics
		 * .widthPixels);
		 */

		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;

		/*--------------------------------*/

		CCS = new CustomCircuitSketch(this, height, width);

		L1 = (LinearLayout) findViewById(R.id.lin);
		L2 = (LinearLayout) findViewById(R.id.lNew);

		LinearLayout ll = (LinearLayout) findViewById(R.id.lin);

		ll.addView(CCS.gestureOverlayView, CCS.params);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_circuit_sketch_canvas, menu);
		return true;
	}

	/* Author Sarim */

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
		case R.id.action_screenshot:
			openExport();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		// TODO Auto-generated method stub
		Intent i = new Intent(CircuitSketchCanvas.this, tutorialone.class);
		startActivity(i);
		finish();
	}

	private void openErase() {
		// TODO Auto-generated method stub

		CCS.erase();

	}

	public void openExport() {
		countForExport++;
		View v1 = L2.getRootView();
		v1.setDrawingCacheEnabled(true);
		Bitmap bm = v1.getDrawingCache();

		/*
		 * Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show
		 * ();
		 */

		// storing in sdcard

		String filename = "circuit" + countForExport + ".png";
		File sd = Environment.getExternalStorageDirectory();
		File dest = new File(sd, filename);

		try {
			FileOutputStream out = new FileOutputStream(dest);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Bitmap bmp=BitmapFactory.decodeResource(bm);

		// resizedbitmap = Bitmap.createBitmap(bmp, 0,100,width, height-100);

		/*
		 * image = (ImageView) findViewById(R.id.imageView1);
		 * image.setBackgroundDrawable(bitmapDrawable);
		 */
		// CCS.export();
	}

	// defining class members, which can be used globally
	/*
	 * public class MyApplication extends Application {
	 * 
	 * private int height; private int width;
	 * 
	 * public int getHeight() { return height; }
	 * 
	 * public void setHeigth(int height) { this.height = height; }
	 * 
	 * 
	 * public int getWidth() { return width; }
	 * 
	 * public void setWidth(int width) { this.width = width; } }
	 */
	/*-------------------------------------------------*/

}
