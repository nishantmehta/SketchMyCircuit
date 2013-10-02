package com.sketchmycircuit.component;

import java.util.ArrayList;

import com.sketchmycircuit.R;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CustomCircuitSketch extends View implements OnGesturePerformedListener  {

	private GestureLibrary gestureLib;
	Context context;
	public LayoutParams params;
	public GestureOverlayView gestureOverlayView;
	SketchView sv;
	
	
	public CustomCircuitSketch(Context context, int width, int height)
	{
		super(context);
		this.context=context;
		gestureOverlayView = new GestureOverlayView(context);
        gestureOverlayView.setEventsInterceptionEnabled(false);
        
        gestureOverlayView.setGestureStrokeType(gestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
        gestureOverlayView.addOnGesturePerformedListener(this);
        params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        sv = new SketchView(context,height,width);
        
        gestureOverlayView.addView(sv);
        gestureLib = GestureLibraries.fromRawResource(context,R.raw.gestures );
        if (!gestureLib.load()) {
            
          }
	}
	
	//function to call sketchview on erase
	
	public void erase()
	{
		sv.erase();
	}
	
	
	
	
	
	
	
	@Override
	public void onGesturePerformed(GestureOverlayView arg0, Gesture gesture) {
		// TODO Auto-generated method stub
		
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		
		
//	    for (Prediction prediction : predictions) {
//	      if (prediction.score > 2.0) {
//	        Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT)
//	            .show();
//	        break;
//	        
//	      }
//	    }
		
		Prediction bestPrediction= findBestPrediction(predictions);
		if (bestPrediction.score>1)
		Toast.makeText(context, bestPrediction.name + " " + bestPrediction.score, Toast.LENGTH_SHORT)
        .show();
		else
			Toast.makeText(context, "Sorry no match", Toast.LENGTH_SHORT)
	        .show();
	}
	public Prediction findBestPrediction(ArrayList<Prediction> predictions)
	{
		Prediction bestPrediction = predictions.get(0);
		for (Prediction prediction : predictions) {
		  if (prediction.score> bestPrediction.score)
			  bestPrediction=prediction;
		}
		return bestPrediction;
	}

}
