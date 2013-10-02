package com.sketchmycircuit.component;

import java.util.ArrayList;
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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SketchView extends View {

	private Paint brush = new Paint();
	private Path path = new Path();
	public Button btnEraseAll;
	public LayoutParams params;
	String TAG="sketch";
	String TAG = "sketch";
	Boolean mode = false;
	private int width;
	Matrix matrix;
	public float lastX;
	public float lastY;
	public boolean gestureStarted;
	public boolean erase;
	Path eraseRegion;
	RectF rec;
	ArrayList<Path> erasePath = new ArrayList<Path>();
	ArrayList<RectF> recF= new ArrayList<RectF>();
	static int eraseCount=0;
	int firstTime=0;
	Context context;

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
		
		
		
		this.context=context;
		this.resetbrush();
		
		
		btnEraseAll = new Button(context);
		btnEraseAll.setText("erase all");
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		btnEraseAll.setLayoutParams(params);
		

	}

	 void resetbrush()
	{
		brush.reset();
		brush.setAntiAlias(true);
		brush.setColor(Color.GRAY);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
		brush.setStrokeWidth(4f);
	}
	public void makeDefaultSketch(float x, float y) {
//		matrix = new Matrix();
//		matrix.setScale(1f, 1f);
//
//		int w = 1000;
//		int h = 1000;
//		path.reset();
//		path.moveTo(x, y);
//
//		path.lineTo(x + 20, y);
//		path.lineTo(x + 40, y + 25);
//		path.lineTo(x + 60, y - 25);
//		path.lineTo(x + 80, y + 25);
//		path.lineTo(x + 100, y);
//		path.lineTo(x + 120, y);
		
		// path.lineTo(350, 100) ;

//		path.transform(matrix);
		
		// *****************code to rotate
		
		// Matrix mMatrix = new Matrix();
		// RectF bounds = new RectF();
		// path.computeBounds(bounds, true);
		// mMatrix.postRotate(45,
		// (bounds.right + bounds.left)/2,
		// (bounds.bottom + bounds.top)/2);
		// path.transform(mMatrix);
//		
//		postInvalidate();

	}

	public void gestureStarted() {
		gestureStarted = true;
	}

	public void gestureEnded() {
		makeDefaultSketch(lastX, lastY);
	}
	public void setErase(RectF rt,Path r)
	{
		rec = new RectF();
		//rec=rt;
		r.close();
		erasePath.add(r);
		recF.add(rt);
		eraseRegion=r;
		erase=true;
		Rect ro = new Rect();
		rt.round(ro);
		invalidate(ro);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// *** code to paste the fair diagram
//		if (gestureStarted) {
//
//			lastX = event.getX();
//			lastY = event.getY();
//			gestureStarted = false;
//		}
		 //Log.d(TAG,"inside touch");
		
		 float pointX = event.getX();
		 float pointY = event.getY();
		 //if (mode)
		 {
//			 brush.setAlpha(0xFF);
//			 brush.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//			brush.setAntiAlias(true);
		 //brush.setAlpha(0xFF);
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
		 case MotionEvent.ACTION_UP:
			 invalidate();
			 break;
		 default:
		 return false;
		 }
		// Force a view to draw again
		
		return false;
		
		
	}

	public void onDrawCustom(Canvas canvas)
	{
		
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
	
	
	
	protected void onDraw(Canvas canvas) {
			setBackgroundColor(Color.WHITE);
			
			Toast.makeText(this.context, "fisttime" +firstTime, Toast.LENGTH_SHORT);
			Log.d(TAG,"fdhg" +firstTime);
			//if(firstTime<=4)
			{
			Toast.makeText(context, "fisttime" +firstTime, Toast.LENGTH_SHORT);
			//path.setFillType(Path.FillType.WINDING);
			//brush.setStyle(Paint.Style.FILL);
			canvas.drawPath(path, brush);
			
			firstTime++;
			
			}
			if(erase)
			{
				
				RectF rect = new RectF();
				
				 brush.setColor(Color.WHITE);
				 brush.setStyle(Paint.Style.FILL);
				 
				
				for(int i =0 ;i<erasePath.size();i++)
				 {
					 
					 canvas.drawRect(recF.get(i), brush);
					//canvas.drawPath(erasePath.get(i), brush);
				 }
				 
				 erase=false;
				 this.resetbrush();
				 //canvas.drawPath(eraseRegion,brush);
				 
			}
			
		
	}

}
