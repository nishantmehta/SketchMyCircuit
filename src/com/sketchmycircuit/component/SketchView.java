package com.sketchmycircuit.component;

/*import com.sketchmycircuit.ui.CircuitSketchCanvas.MyApplication;*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class SketchView extends View{

	private Paint brush = new Paint();
	private  Path path=new Path();
	public Button btnEraseAll;
	public LayoutParams params;
	String TAG="sketch";
	Boolean mode=false;
	private int width;
	private int height;
	
	
	/*author sarim*/
	
	public void erase()
	{
		postInvalidate();
		refreshCanvas(width, height);
	}
	
	
	
	
	
	
	public Boolean refreshCanvas(int w, int h)
	{
		
		path.reset();
        path.moveTo(100, 100);
        path.lineTo((w-220)/2, 100);
        
        //the longer stroke of the battery
        path.moveTo((w-220)/2, 60);
        path.lineTo((w-220)/2, 140);
        
        //the shorter stroke of the battery
        path.moveTo((w-180)/2, 80);
        path.lineTo((w-180)/2, 120);
        
        path.moveTo((w-180)/2, 100);
		path.lineTo(w-100, 100);
		path.lineTo(w-100, h-400);
		path.lineTo((w-180)/2, h-400);
		
		//the inclined of the switch
		
		path.moveTo((w-180)/2, h-440);
		path.lineTo((w-260)/2, h-400);
		path.lineTo(100,h-400);
	    path.lineTo(100, 100);
		postInvalidate();
		return true;
		

	}
	/*--------------------*/
	
	public SketchView(Context context , int w, int h) {
		super(context);
		// TODO Auto-generated constructor stub
	
		
		width=w;
		height=h;
		
		
		
		
		//to make the initial circuit
		refreshCanvas(width, height);
		
		
		
		brush.setAntiAlias(true);
		brush.setColor(Color.GRAY);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
		brush.setStrokeWidth(6f);
		btnEraseAll= new Button(context);
		btnEraseAll.setText("erase all");
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		btnEraseAll.setLayoutParams(params);
		btnEraseAll.setOnClickListener(new View.OnClickListener() {
	            
	            @Override
	            public void onClick(View view) {
	                //reset the path
	               // path.reset();
	                mode=true;
	                //invalidate the view
	                postInvalidate();
	                
	            }
	        });
		
		
	}
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG,"inside touch");
		float pointX = event.getX();
        float pointY = event.getY();
        if (mode)
        {
        	brush.setAlpha(0xFF);
            //brush.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        // Checks for the event that occurs
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            path.moveTo(pointX, pointY);
            return true;
        case MotionEvent.ACTION_MOVE:
            path.lineTo(pointX, pointY);
            break;
        default:
            return false;
        }       
         // Force a view to draw again
        postInvalidate();
        return false;
    
    }
	@SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) {
		
		Bitmap toDisk = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		    canvas.setBitmap(toDisk);
		
	
		
		
        canvas.drawPath(path, brush);
        
        
        
       try {
			toDisk.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/sdcard/circuit.jpg")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
	
	
	

}
