package com.sketchmycircuit.component;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
	
	public SketchView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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
	@Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, brush);
        
    }
	
	
	

}
