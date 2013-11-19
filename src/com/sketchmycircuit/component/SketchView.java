package com.sketchmycircuit.component;

import java.util.ArrayList;

import com.sketchmycircuit.ui.CircuitSketchCanvas;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class SketchView extends View {

	private Paint brush = new Paint();
	private Paint eraseBrush = new Paint(brush);
	public PointF tl = new PointF();
	public PointF tr = new PointF();
	public PointF bl = new PointF();
	public PointF br = new PointF();
	public PointF wire1;
	public PointF wireComponentStart = new PointF();
	public PointF wireComponentEnd = new PointF();
	int wirecount = 0;
	ArrayList<PointF> wires = new ArrayList<PointF>();
	public Boolean diodeInvert = false;
	CircuitSketchCanvas CSC = new CircuitSketchCanvas();
	public PointF eraseStart = new PointF();
	public PointF eraseEnd = new PointF();
	public LayoutParams params;

	int componentXOffset = 220;
	int componentYOffset = 220;
	String TAG = "sketch";

	Boolean mode = false;
	Boolean erase = false;

	private int width;
	private int height;

	Matrix matrix;

	Context context;

	float touchPointX;
	float touchPointY;

	// initial circuit
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

	public void export() {

		CSC.openExport();
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

	public void drawWire(RectF rt, Path r) {
		if (rt != null) {
			PointF ctl = new PointF();
			PointF ctr = new PointF();
			PointF cbl = new PointF();
			PointF cbr = new PointF();
			PointF start = new PointF();
			PointF end = new PointF();
			Boolean wire = false;

			ctl.x = rt.left;
			ctl.y = rt.top;
			ctr.x = rt.right;
			ctr.y = ctl.y;
			cbl.x = ctl.x;
			cbl.y = rt.bottom;
			cbr.x = ctr.x;
			cbr.y = cbl.y;

			if (tl.y <= cbl.y && tl.y >= ctl.y) {
				start.y = tl.y;
				end.y = bl.y;
				start.x = ctl.x;
				end.x = ctl.x;
				wire = true;
			}
			if (tl.y <= cbl.y && tl.y <= ctl.y && bl.y >= ctl.y
					&& bl.y <= cbl.y) {
				end.x = cbr.x;
				end.y = br.y;
				start.x = end.x;
				start.y = tl.y;
				wire = true;
			}
			if (ctl.x <= tl.x && cbl.x <= tl.x) {
				start.x = tl.x;
				start.y = ctl.y;
				end.x = tr.x;
				end.y = start.y;
				wire = true;
			}
			if (tl.x <= ctl.x && tl.x <= ctr.x && tr.x >= ctl.x
					&& tr.x <= ctr.x) {
				end.x = tr.x;
				end.y = cbr.y;
				start.x = tl.x;
				start.y = cbr.y;
				wire = true;
			}

			if (wire == true) {
				initialCircuit.moveTo(start.x, start.y);
				initialCircuit.lineTo(end.x, end.y);
				wire1 = new PointF();
				wire1.x = start.x;
				wire1.y = start.y;
				wires.add(wire1);
				wirecount = wirecount + 1;

				/*
				 * Toast.makeText(context, "WIRE SUCCESS",
				 * Toast.LENGTH_SHORT).show(); for( int i =0;
				 * i<wires.size();i++) { String
				 * a=String.valueOf(wires.get(i).x); Toast.makeText(context, a,
				 * Toast.LENGTH_SHORT).show(); String
				 * b=String.valueOf(wires.get(i).y); Toast.makeText(context, b,
				 * Toast.LENGTH_SHORT).show(); }
				 */
			}
		}
	}

	public int collide(RectF rt) {
		// int i;
		int sizeOfComponentRegion = componentRegion.size();

		for (int i = 0; i < sizeOfComponentRegion; i++) {
			if (RectF.intersects(rt, componentRegion.get(i))) {

				return i;
			}
		}

		return 1000;

	}

	public int setErase(RectF rt, Path r) {

		int k = collide(rt);

		if (k == 1000) {

			if (erase == true) {
				Toast.makeText(
						context,
						"Please draw a component before erasing another part of the circuit!",
						Toast.LENGTH_SHORT).show();
				return -2;
			}

			if (rt != null) {

				if (rt.contains(tl.x, tl.y) || rt.contains(tr.x, tr.y)
						|| rt.contains(bl.x, bl.y) || rt.contains(br.x, br.y)) {
					Toast.makeText(context, "Please erase a different area!",
							Toast.LENGTH_SHORT).show();
					return -1;
				}

				PointF ctl = new PointF();
				PointF ctr = new PointF();
				PointF cbl = new PointF();
				PointF cbr = new PointF();
				PointF start = new PointF();
				PointF end = new PointF();

				ctl.x = rt.left;
				ctl.y = rt.top;
				ctr.x = rt.right;
				ctr.y = ctl.y;
				cbl.x = ctl.x;
				cbl.y = rt.bottom;
				cbr.x = ctr.x;
				cbr.y = cbl.y;

				if (tl.y <= cbl.y && tl.y >= ctl.y) {
					start.y = tl.y;
					end.y = tl.y;
					start.x = ctl.x;
					end.x = ctr.x;
				}
				if (bl.y <= cbl.y && bl.y >= ctl.y) {
					start.y = bl.y;
					end.y = bl.y;
					start.x = ctl.x;
					end.x = ctr.x;
				}
				if (tr.x <= ctr.x && tr.x >= ctl.x) {
					start.x = tr.x;
					end.x = tr.x;
					start.y = ctl.y;
					end.y = cbl.y;
				}
				if (tl.x <= ctr.x && tl.x >= ctl.x) {
					start.x = tl.x;
					end.x = tl.x;
					start.y = ctl.y;
					end.y = cbl.y;
				}
				double x = start.x - end.x;
				double y = start.y - end.y;
				double dist = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));

				if (tl.y < ctl.y && bl.y > cbl.y && tl.x < ctl.x
						&& tr.x > ctr.x) {
					dist = ctr.x - ctl.x;
					for (int i = 0; i < wires.size(); i++) {
						if (ctl.y < wires.get(i).y && cbl.y > wires.get(i).y) {
							wireComponentStart.x = ctl.x;
							wireComponentStart.y = wires.get(i).y;
							wireComponentEnd.x = ctr.x;
							wireComponentEnd.y = wires.get(i).y;
							break;
						}
					}
				}

				if (dist < 200) {
					Toast.makeText(context, "Please erase a bigger area!",
							Toast.LENGTH_SHORT).show();
					return -1;
				}

				erasePath.add(r);
				eraseRegion.add(rt);
				Rect ro = new Rect();
				rt.round(ro);
				invalidate(ro);
				erase = true;
				return 1;
			}
			return -1;
		}// collision check ends

		else {

			if (!componentRegion.isEmpty() && !componentPath.isEmpty()
					&& !eraseRegion.isEmpty() && !erasePath.isEmpty()) {
				componentRegion.remove(k);
				componentPath.remove(k);
				eraseRegion.remove(k);
				erasePath.remove(k);

			}

			erase = false;
			return -3;
		}
	}

	public void componentPoints(RectF compRect, Path compPath, String comp) {
		if (compRect != null && comp != null) {
			PointF ctl = new PointF();
			PointF ctr = new PointF();
			PointF cbl = new PointF();
			PointF cbr = new PointF();
			PointF start = new PointF();
			PointF end = new PointF();

			ctl.x = compRect.left;
			ctl.y = compRect.top;
			ctr.x = compRect.right;
			ctr.y = ctl.y;
			cbl.x = ctl.x;
			cbl.y = compRect.bottom;
			cbr.x = ctr.x;
			cbr.y = cbl.y;

			if (tl.y <= cbl.y && tl.y >= ctl.y) {
				start.y = tl.y;
				end.y = tl.y;
				start.x = ctl.x;
				end.x = ctr.x;
				diodeInvert = false;
			}
			if (bl.y <= cbl.y && bl.y >= ctl.y) {
				start.y = bl.y;
				end.y = bl.y;
				start.x = ctl.x;
				end.x = ctr.x;
				diodeInvert = true;
			}
			if (tr.x <= ctr.x && tr.x >= ctl.x) {
				start.x = tr.x;
				end.x = tr.x;
				start.y = ctl.y;
				end.y = cbl.y;
				diodeInvert = false;
			}
			if (tl.x <= ctr.x && tl.x >= ctl.x) {
				start.x = tl.x;
				end.x = tl.x;
				start.y = ctl.y;
				end.y = cbl.y;
				diodeInvert = true;
			}
			if (tl.y < ctl.y && bl.y > cbl.y && tl.x < ctl.x && tr.x > ctr.x) {
				start.x = wireComponentStart.x;
				start.y = wireComponentStart.y;
				end.x = wireComponentEnd.x;
				end.y = wireComponentEnd.y;
			}

			componentRegion.add(compRect);
			// componentRegion = new ArrayList<RectF>();
			// componentRegion = eraseRegion;

			String direction;
			eraseStart = start;
			eraseEnd = end;
			if (start.x != end.x)
				direction = "h";
			else
				direction = "v";

			if (comp == "resistor")
				this.drawComponent(start, end, 1, direction);
			if (comp == "diode")
				drawComponent(start, end, 2, direction);
			if (comp == "capacitor")
				this.drawComponent(start, end, 3, direction);
			if (comp == "inductor")
				drawComponent(start, end, 4, direction);
		}
	}

	public void drawComponent(PointF start, PointF end, int comp,
			String direction) {

		// nishant erase path
		RectF eraseR = new RectF();
		if (direction == "h") {
			end.x = start.x + componentXOffset;

			eraseR.set(start.x, start.y + 3, end.x, start.y - 3);
		} else if (direction == "v") {
			end.y = start.y + componentXOffset;
			eraseR.set(start.x - 3, start.y, start.x + 3, end.y);
		}

		eraseRegion.set(eraseRegion.size() - 1, eraseR);
		//

		float x = start.x;
		float y = start.y;
		Path path = new Path();
		if (direction == "h") {
			switch (comp) {
			// Resistor
			case 1:
				path.moveTo(x, y);
				path.lineTo(x + 20, y);
				path.lineTo(x + 40, y - 25);
				path.lineTo(x + 60, y);
				path.lineTo(x + 80, y + 25);
				path.lineTo(x + 100, y);
				path.lineTo(x + 120, y - 25);
				path.lineTo(x + 140, y);
				path.lineTo(x + 160, y + 25);
				path.lineTo(x + 180, y);
				path.lineTo(x + 200, y - 25);
				path.lineTo(x + 220, y);
				path.lineTo(end.x, end.y);
				break;

			// Diode (equilateral triangle)
			case 2:
				path.moveTo(x, y);
				path.lineTo(x + 40, y);
				path.lineTo(x + 40, y + 40);
				path.lineTo(x + 40, y - 40);
				path.lineTo(x + 109, y);
				path.moveTo(x + 40, y + 40);
				path.lineTo(x + 109, y);
				path.lineTo(x + 109, y + 40);
				path.lineTo(x + 109, y - 40);
				path.lineTo(x + 109, y);
				path.lineTo(end.x, end.y);
				break;

			// Capacitor
			case 3:
				path.moveTo(x, y);
				path.lineTo(x + 60, y);
				path.lineTo(x + 60, y + 60);
				path.lineTo(x + 60, y - 60);
				path.moveTo(x + 80, y);
				path.lineTo(x + 80, y + 60);
				path.lineTo(x + 80, y - 60);
				path.moveTo(x + 80, y);
				path.lineTo(x + 140, y);
				path.lineTo(end.x, end.y);
				break;

			// Inductor
			case 4:
				path.moveTo(x, y);
				path.lineTo(x + 30, y);
				path.lineTo(x + 30, y - 30);
				getSemicircle(x + 30, y - 30, x + 60, y - 30, "Right", path);
				getSemicircle(x + 60, y - 30, x + 90, y - 30, "Right", path);
				getSemicircle(x + 90, y - 30, x + 120, y - 30, "Right", path);
				path.lineTo(x + 120, y);
				path.lineTo(x + 140, y);
				path.lineTo(end.x, end.y);
				break;
			}
		}

		if (direction == "v") {
			switch (comp) {
			// Resistor
			case 1:
				path.moveTo(x, y);
				path.lineTo(x, y + 20);
				path.lineTo(x - 25, y + 40);
				path.lineTo(x, y + 60);
				path.lineTo(x + 25, y + 80);
				path.lineTo(x, y + 100);
				path.lineTo(x - 25, y + 120);
				path.lineTo(x, y + 140);
				path.lineTo(x + 25, y + 160);
				path.lineTo(x, y + 180);
				path.lineTo(x - 25, y + 200);
				path.lineTo(x, y + 220);
				path.lineTo(end.x, end.y);
				break;

			// Diode (equilateral triangle)
			case 2:
				path.moveTo(x, y);
				path.lineTo(x, y + 40);
				path.lineTo(x + 40, y + 40);
				path.lineTo(x - 40, y + 40);
				path.lineTo(x, y + 109);
				path.moveTo(x + 40, y + 40);
				path.lineTo(x, y + 109);
				path.lineTo(x + 40, y + 109);
				path.lineTo(x - 40, y + 109);
				path.lineTo(x, y + 109);
				path.lineTo(end.x, end.y);
				break;

			// Capacitor
			case 3:
				path.moveTo(x, y);
				path.lineTo(x, y + 60);
				path.lineTo(x + 60, y + 60);
				path.lineTo(x - 60, y + 60);
				path.moveTo(x, y + 80);
				path.lineTo(x + 60, y + 80);
				path.lineTo(x - 60, y + 80);
				path.moveTo(x, y + 80);
				path.lineTo(x, y + 140);
				path.lineTo(end.x, end.y);
				break;

			// Inductor
			case 4:
				path.moveTo(x, y);
				path.lineTo(x, y + 30);
				path.lineTo(x + 30, y + 30);
				getSemicircle(x + 30, y + 30, x + 30, y + 60, "Right", path);
				getSemicircle(x + 30, y + 60, x + 30, y + 90, "Right", path);
				getSemicircle(x + 30, y + 90, x + 30, y + 120, "Right", path);
				path.lineTo(x, y + 120);
				path.lineTo(x, y + 140);
				path.lineTo(end.x, end.y);
				break;
			}
		}

		if (diodeInvert == true && comp == 2) {
			if (direction == "h") {
				Matrix myMatrix = new Matrix();
				myMatrix.postRotate(180, (x + end.x) / 2, y);
				path.transform(myMatrix);
			} else if (direction == "v") {
				Matrix myMatrix = new Matrix();
				myMatrix.postRotate(180, x, (y + end.y) / 2);
				path.transform(myMatrix);
			}

		}

		componentPath.add(path);

		postInvalidate();
		erase = false;
	}

	public Path getSemicircle(float xStart, float yStart, float xEnd,
			float yEnd, String direction, Path path) {
		float centerX = xStart + ((xEnd - xStart) / 2);
		float centerY = yStart + ((yEnd - yStart) / 2);

		double xLen = (xEnd - xStart);
		double yLen = (yEnd - yStart);
		float radius = (float) (Math.sqrt(xLen * xLen + yLen * yLen) / 2);

		RectF oval = new RectF((float) (centerX - radius),
				(float) (centerY - radius), (float) (centerX + radius),
				(float) (centerY + radius));

		// ovalRectOUT.set(oval);

		double radStartAngle = 0;
		if (direction == "Right") {
			radStartAngle = Math.atan2(yStart - centerY, xStart - centerX);
		} else {
			radStartAngle = Math.atan2(yEnd - centerY, xEnd - centerX);
		}
		float startAngle = (float) Math.toDegrees(radStartAngle);

		path.addArc(oval, startAngle, 180);
		return path;

	}

	protected void onDraw(Canvas canvas) {
		setBackgroundColor(Color.WHITE);

		// initial path is drawn
		canvas.drawPath(initialCircuit, brush);

		// for erase region
		if (eraseRegion.size() > 0) {

			for (int i = 0; i < eraseRegion.size(); i++) {

				canvas.drawRect(eraseRegion.get(i), eraseBrush);

			}
		}

		// for component path
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
		tl.x = 100;
		tl.y = 100;
		tr.x = w - 100;
		tr.y = 100;
		bl.x = 100;
		bl.y = h - 400;
		br.x = w - 100;
		br.y = h - 400;

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
		wires.clear();
		wirecount = 0;
		wireComponentStart = new PointF();
		wireComponentEnd = new PointF();
		erase = false;
		erasePath.clear();
		eraseRegion.clear();
		componentPath.clear();
		componentRegion.clear();
		postInvalidate();
		return true;

	}
}
