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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.sketchmycircuit.R;
import com.sketchmycircuit.ui.CircuitSketchCanvas;

public class CustomCircuitSketch extends View implements
		OnGesturePerformedListener, OnGestureListener {

	private GestureLibrary gestureLib1Stroke ,gestureLib4Stroke, gestureLib3Stroke;
	Context context;
	public LayoutParams params;
	public GestureOverlayView gestureOverlayView;
	SketchView sv;
	public RectF compRect;
	public Path compPt;
	int check = 0;
	int erase = 0;
	String name = "empty";
	
	Context mContext;
	String PROD="prodution";
	
	
	CircuitSketchCanvas CSC;

	
	/*
	 * 1 stroke = erase + circuit line+resistor+inductor+export
	 * 2 stroke= 
	 * 3 stroke=diode
	 * 4 stroke = capacitor.
	 */
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
		gestureLib1Stroke = GestureLibraries.fromRawResource(context, R.raw.gestures_1);
		if (!gestureLib1Stroke.load()) {

		}
		gestureLib4Stroke = GestureLibraries.fromRawResource(context, R.raw.gestures_4);
		if (!gestureLib4Stroke.load()) {

		}
		gestureLib3Stroke = GestureLibraries.fromRawResource(context, R.raw.gestures_3);
		if (!gestureLib3Stroke.load()) {

		}
	}

	// function to call sketchview on erase

	public void erase() {
		sv.erase();
		check = 0;
		erase = 0;
		name = "empty";
	}

	public void export()
	{
		sv.export();
	}
	@Override
	public void onGesturePerformed(GestureOverlayView arg0, Gesture gesture) 
	{
		// TODO Auto-generated method stub
				int NumberOfStrokes=gesture.getStrokesCount();
				ArrayList<Prediction> predictions =null;
				if(NumberOfStrokes==1)
				{
					predictions = gestureLib1Stroke.recognize(gesture);
					
				}
				else if(NumberOfStrokes==3)
				{
					predictions = gestureLib3Stroke.recognize(gesture);
				}
				else if(NumberOfStrokes==4)
				{
					predictions = gestureLib4Stroke.recognize(gesture);
				}
				else
				{
					predictions = gestureLib1Stroke.recognize(gesture);
				}

		Prediction bestPrediction = findBestPrediction(predictions);
		Log.d(PROD, bestPrediction.name+ " gesture");
		if (bestPrediction.score > 0) 
		{
			String component = null;
			// if the gesture is a delete gesture
			if (bestPrediction.name.contains("delete")) 
			{
				ArrayList<GestureStroke> strokes = gesture.getStrokes();
				GestureStroke st = strokes.get(0);
				
				RectF rec = new RectF();
				Path pt = new Path();
				rec = st.boundingBox;
				pt = st.getPath();
				erase = -1;
				erase = sv.setErase(rec, pt);
				if(erase == 1)
				{
				compRect = rec;
				compPt = pt;
				check = 1;
				}
				if(erase == -1)
				{
					check = 1;
				}
				if(erase == -3)
					check = -1;
				
			}
			else if (bestPrediction.name.contains("resistor") || name =="resistor") 
			{
				component = "resistor";
				Log.d(PROD, "space");
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
			else if (bestPrediction.name.contains("wire"))
			{
				ArrayList<GestureStroke> wirestrokes = gesture.getStrokes();
				GestureStroke st = wirestrokes.get(0);

				RectF rec = st.boundingBox;
				Path pt = st.getPath();
				compRect = rec;
				compPt = pt;
				
				sv.drawWire(rec, pt);
			}
			else if(bestPrediction.name.contains("export"))
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
		 
					// set title
					alertDialogBuilder.setTitle("Sketch My Circuit");
		 
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
			
			if (name != "wire" && !bestPrediction.name.contains("delete"))
			{
				if(check == 1)
				{
				sv.componentPoints(compRect, compPt, component);
				check = 0;
				}
				else if(check == 0)
				{
					Toast.makeText(context,
							"Please erase a part of the circuit before drawing components!" + " " + bestPrediction.score,
							Toast.LENGTH_SHORT).show();
				}
			}

		/*	Toast.makeText(context,
					bestPrediction.name + " " + bestPrediction.score,
					Toast.LENGTH_SHORT).show();*/
		} 
		else
			Toast.makeText(context, bestPrediction.name, Toast.LENGTH_SHORT)
					.show();
		name="empty";
}

	public Prediction findBestPrediction(ArrayList<Prediction> predictions) {
		Prediction bestPrediction = predictions.get(0);
		Log.d(PROD, bestPrediction.name+ " gesture with score " + bestPrediction.score);
		for (Prediction prediction : predictions) {
			if (prediction.score > bestPrediction.score)
				
				bestPrediction = prediction;
		}
		if (bestPrediction.score<10 && bestPrediction.name.contains("wire"))
		{
			Log.d(PROD, bestPrediction.name+ " gesture +++++++++++ with score " + bestPrediction.score);
			name="resistor";
		}
		else if(bestPrediction.score>20 && bestPrediction.name.contains("wire"))
			name = "wire";
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
