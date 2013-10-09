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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SketchView extends View {

	private Paint brush = new Paint();
	private Paint eraseBrush = new Paint(brush);
	
	
	public LayoutParams params;
	String TAG = "sketch";

	Boolean mode = false;
	
	private int width;
	private int height;
	
	Matrix matrix;

	Context context;
	
	float touchPointX;
	float touchPointY;
	
	//initial circuit
	private Path initialCircuit = new Path();

	// details for the erase
	ArrayList<Path> erasePath = new ArrayList<Path>();
	ArrayList<RectF> eraseRegion = new ArrayList<RectF>();

	// details for the component
	ArrayList<Path> componentPath = new ArrayList<Path>();
	ArrayList<RectF> componentRegion = new ArrayList<RectF>();

	/* author sarim */

	public void erase() {

		eraseRegion.removeAll(eraseRegion);
		componentPath.removeAll(componentPath);
		refreshCanvas(width, height);
	}

	public void export()
	{
		
	}
	/*--------------------*/

	public SketchView(Context context, int w, int h) {
		super(context);
		// TODO Auto-generated constructor stub

		width = w;
		height = h;
		this.context = context;

		this.initializeEraseBrush();
		this.resetbrush();

		// to make the initial circuit
		refreshCanvas(width, height);

	}

	public void setErase(RectF rt, Path r) {

		erasePath.add(r);
		eraseRegion.add(rt);
		Rect ro = new Rect();
		rt.round(ro);
		invalidate(ro);
	}

	protected void onDraw(Canvas canvas) {
		setBackgroundColor(Color.WHITE);

		//initial path is drawn
		canvas.drawPath(initialCircuit, brush);

		//for erase region
		if (eraseRegion.size() > 0) {

			for (int i = 0; i < eraseRegion.size(); i++) {

				canvas.drawRect(eraseRegion.get(i), eraseBrush);

			}
		}
		
		//for component path
		if (componentPath.size() > 0) {

			for (int i = 0; i < componentPath.size(); i++) {

				canvas.drawPath(componentPath.get(i), brush);

			}
		}
		

	}

	public void initializeEraseBrush() {

		eraseBrush.setAntiAlias(true);
		eraseBrush.setColor(Color.GRAY);
		eraseBrush.setStyle(Paint.Style.STROKE);
		eraseBrush.setStrokeJoin(Paint.Join.ROUND);
		eraseBrush.setStrokeWidth(4f);
		eraseBrush.setColor(Color.WHITE);
		eraseBrush.setStyle(Paint.Style.FILL);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		touchPointX = event.getX();
		touchPointY = event.getY();

		return false;

	}

	void resetbrush() {
		brush.reset();
		brush.setAntiAlias(true);
		brush.setColor(Color.BLACK);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
		brush.setStrokeWidth(4f);
	}

	public Boolean refreshCanvas(int w, int h) {

		initialCircuit.reset();
		initialCircuit.moveTo(100, 100);
		initialCircuit.lineTo((w - 220) / 2, 100);

		// the longer stroke of the battery
		initialCircuit.moveTo((w - 220) / 2, 60);
		initialCircuit.lineTo((w - 220) / 2, 140);

		// the shorter stroke of the battery
		initialCircuit.moveTo((w - 180) / 2, 80);
		initialCircuit.lineTo((w - 180) / 2, 120);

		initialCircuit.moveTo((w - 180) / 2, 100);
		initialCircuit.lineTo(w - 100, 100);
		initialCircuit.lineTo(w - 100, h - 400);
		initialCircuit.lineTo((w - 180) / 2, h - 400);

		// the inclined of the switch

		initialCircuit.moveTo((w - 180) / 2, h - 440);
		initialCircuit.lineTo((w - 260) / 2, h - 400);
		initialCircuit.lineTo(100, h - 400);
		initialCircuit.lineTo(100, 100);
		postInvalidate();
		return true;

	}
}
