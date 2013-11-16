package com.sketchmycircuit.component;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.sketchmycircuit.R;
import com.sketchmycircuit.ui.CircuitSketchCanvas;

public class CustomCircuitSketch extends View implements
		OnGesturePerformedListener, OnGestureListener {

	private GestureLibrary gestureLib;
	Context context;
	public LayoutParams params;
	public GestureOverlayView gestureOverlayView;
	SketchView sv;
	public RectF compRect;
	public Path compPt;
	
	Context mContext;
	
	
	CircuitSketchCanvas CSC;

	public CustomCircuitSketch(Context context, int width, int height) {
		super(context);
		this.context = context;
		gestureOverlayView = new GestureOverlayView(context);
		gestureOverlayView.setEventsInterceptionEnabled(false);

		gestureOverlayView
				.setGestureStrokeType(gestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
		gestureOverlayView.addOnGesturePerformedListener(this);
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		sv = new SketchView(context, height, width);

		gestureOverlayView.addView(sv);
		gestureLib = GestureLibraries.fromRawResource(context, R.raw.gestures);
		if (!gestureLib.load()) {

		}
	}

	// function to call sketchview on erase

	public void erase() {
		sv.erase();
	}

	public void export()
	{
		sv.export();
	}
	@Override
	public void onGesturePerformed(GestureOverlayView arg0, Gesture gesture) 
	{
		// TODO Auto-generated method stub

		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);

		Prediction bestPrediction = findBestPrediction(predictions);
		if (bestPrediction.score > 0) 
		{
			String component = null;
			// if the gesture is a delete gesture
			if (bestPrediction.name.contains("delete")) 
			{
				ArrayList<GestureStroke> strokes = gesture.getStrokes();
				GestureStroke st = strokes.get(0);

				RectF rec = st.boundingBox;
				Path pt = st.getPath();
				compRect = rec;
				compPt = pt;
				
				sv.setErase(rec, pt);
			}
			else if (bestPrediction.name.contains("resistor")) 
			{
				component = "resistor";
			}
			else if (bestPrediction.name.contains("capacitor")) 
			{
				component = "capacitor";
			}
			else if (bestPrediction.name.contains("diode")) 
			{
				component = "diode";
			}
			else if (bestPrediction.name.contains("inductor")) 
			{
				component = "inductor";
			}
			else if(bestPrediction.name.contains("export"))
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
		 
					// set title
					alertDialogBuilder.setTitle("Your Title");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("Do you want to Export this Circuit Sketch? ")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, 
								// call export
								mContext=getContext();
								
								if(mContext instanceof CircuitSketchCanvas)
								{
								    CircuitSketchCanvas activity = (CircuitSketchCanvas)mContext;
								    // Then call the method in the activity.
								    activity.openExport();
								    
								    Toast.makeText(context,
											"Export Successful",
											Toast.LENGTH_SHORT).show();
								    
								}
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
				
				
				
				
				
				
				/* Toast.makeText(context,
							bestPrediction.name + " " + bestPrediction.score,
							Toast.LENGTH_SHORT).show();*/
				
				return;
			}
			
			sv.componentPoints(compRect, compPt, component);

		/*	Toast.makeText(context,
					bestPrediction.name + " " + bestPrediction.score,
					Toast.LENGTH_SHORT).show();*/
		} 
		else
			Toast.makeText(context, bestPrediction.name, Toast.LENGTH_SHORT)
					.show();
}

	public Prediction findBestPrediction(ArrayList<Prediction> predictions) {
		Prediction bestPrediction = predictions.get(0);
		for (Prediction prediction : predictions) {
			if (prediction.score > bestPrediction.score)
				bestPrediction = prediction;
		}
		return bestPrediction;
	}

	@Override
	public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub

	}

}
