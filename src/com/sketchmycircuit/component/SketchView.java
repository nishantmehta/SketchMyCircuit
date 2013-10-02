package com.sketchmycircuit.component;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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
	String TAG = "sketch";
	Boolean mode = false;
	Matrix matrix;
	public float lastX;
	public float lastY;
	public boolean gestureStarted;
	public boolean erase;
	Path eraseRegion;
	RectF rec;
	ArrayList<Path> erasePath = new ArrayList<Path>();
	ArrayList<RectF> recF = new ArrayList<RectF>();
	static int eraseCount = 0;
	int firstTime = 0;
	Context context;
	float pointX;
	float pointY ;

	public SketchView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resetbrush();

		btnEraseAll = new Button(context);
		btnEraseAll.setText("erase all");
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		btnEraseAll.setLayoutParams(params);
		
	}

	void resetbrush() {
		brush.reset();
		brush.setAntiAlias(true);
		brush.setColor(Color.GRAY);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
		brush.setStrokeWidth(4f);
	}

	public void makeDefaultSketch(float x, float y) {
		matrix = new Matrix();
		matrix.setScale(1f, 1f);
		//
		int w = 1000;
		int h = 1000;
		//path.reset();
		path.moveTo(x, y);
		
		path.lineTo(x + 20, y);
		path.lineTo(x + 40, y + 25);
		path.lineTo(x + 60, y - 25);
		path.lineTo(x + 80, y + 25);
		path.lineTo(x + 100, y);
		path.lineTo(x + 120, y);

		//path.lineTo(350, 100) ;

		path.transform(matrix);

		// *****************code to rotate

		// Matrix mMatrix = new Matrix();
		// RectF bounds = new RectF();
		// path.computeBounds(bounds, true);
		// mMatrix.postRotate(45,
		// (bounds.right + bounds.left)/2,
		// (bounds.bottom + bounds.top)/2);
		// path.transform(mMatrix);
		//
		// postInvalidate();

	}

	public void gestureStarted() {
		gestureStarted = true;
	}

	public void gestureEnded() {
		//makeDefaultSketch(lastX, lastY);
	}

	public void setErase(RectF rt, Path r) {
		rec = new RectF();
		// rec=rt;
		r.close();
		erasePath.add(r);
		recF.add(rt);
		eraseRegion = r;
		erase = true;
		Rect ro = new Rect();
		rt.round(ro);
		invalidate(ro);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// *** code to paste the fair diagram
		// if (gestureStarted) {
		//
		// lastX = event.getX();
		// lastY = event.getY();
		// gestureStarted = false;
		// }
		// Log.d(TAG,"inside touch");

		pointX = event.getX();
		pointY = event.getY();
		//this.makeDefaultSketch(pointX, pointY);
		// if (mode)
		{
			// brush.setAlpha(0xFF);
			// brush.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			// brush.setAntiAlias(true);
			// brush.setAlpha(0xFF);
			// brush.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		}
		// Checks for the event that occurs
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(pointX, pointY);
			break;
			//return false;
		case MotionEvent.ACTION_MOVE:
			//path.lineTo(pointX, pointY);
			break;
		case MotionEvent.ACTION_UP:
			//invalidate();
			break;
		default:
			return false;
		}
		// Force a view to draw again

		return false;

	}

	public void onDrawCustom(Canvas canvas) {

	}

	@Override
	protected void onDraw(Canvas canvas) {
		setBackgroundColor(Color.WHITE);

		Toast.makeText(this.context, "fisttime" + firstTime, Toast.LENGTH_SHORT);
		Log.d(TAG, "fdhg" + firstTime);
		// if(firstTime<=4)
		{
			Toast.makeText(context, "fisttime" + firstTime, Toast.LENGTH_SHORT);
			// path.setFillType(Path.FillType.WINDING);
			// brush.setStyle(Paint.Style.FILL);
			
			
			//replace this line by initial circuit
			this.makeDefaultSketch(pointX, pointY);
			
			canvas.drawPath(path, brush);

			firstTime++;

		}
		//if (erase) {

			RectF rect = new RectF();

			brush.setColor(Color.WHITE);
			brush.setStyle(Paint.Style.FILL);

			for (int i = 0; i < erasePath.size(); i++) {

				canvas.drawRect(recF.get(i), brush);
				// canvas.drawPath(erasePath.get(i), brush);
			}

			erase = false;
			this.resetbrush();
			// canvas.drawPath(eraseRegion,brush);

		}

	//}

}
