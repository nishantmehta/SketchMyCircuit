package com.sketchmycircuit.component;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.sketchmycircuit.R;

public class CustomCircuitSketch extends View implements OnGesturePerformedListener,OnGestureListener  {

	private GestureLibrary gestureLib;
	Context context;
	public LayoutParams params;
	public GestureOverlayView gestureOverlayView;
	SketchView sv;
	public CustomCircuitSketch(Context context, AttributeSet attrs)
	{
		super(context, attrs); 
		
		Toast.makeText(context, "HI", Toast.LENGTH_SHORT);
		gestureOverlayView = new GestureOverlayView(context);
        gestureOverlayView.setEventsInterceptionEnabled(false);
        
        gestureOverlayView.setGestureStrokeType(gestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureOverlayView.addOnGestureListener(this);
        params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        sv = new SketchView(context);
        
        gestureOverlayView.addView(sv);
        gestureLib = GestureLibraries.fromRawResource(context,R.raw.gestures );
        if (!gestureLib.load()) {
            
          }
	}
	
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
		
//		ArrayList<GestureStroke> strokes=gesture.getStrokes();
//		GestureStroke st= strokes.get(1);
//		pst.getPath();
//	    for (Prediction prediction : predictions) {
//	      if (prediction.score > 2.0) {
//	        Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT)
//	            .show();
//	        break;
//	        
//	      }
//	    }
		
		Prediction bestPrediction= findBestPrediction(predictions);
		if (bestPrediction.score>2)
		{
		
		//sv.makeDefaultSketch();
		if(bestPrediction.name.equalsIgnoreCase("delete"))
		{
			ArrayList<GestureStroke> strokes=gesture.getStrokes();
			GestureStroke st= strokes.get(0);
			
			RectF rec =st.boundingBox;
			Path pt = st.getPath();
			sv.setErase(rec,pt);
		}
		Toast.makeText(context, bestPrediction.name + " " + bestPrediction.score, Toast.LENGTH_SHORT)
        .show();
		}
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
		sv.gestureEnded();
		
	}
	@Override
	public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
		// TODO Auto-generated method stub
		sv.gestureStarted();
//		Toast.makeText(context, "started", Toast.LENGTH_SHORT)
//        .show();
	}

}
