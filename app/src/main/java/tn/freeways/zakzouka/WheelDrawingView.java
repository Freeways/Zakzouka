package tn.freeways.zakzouka;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by mohamed on 30/12/15.
 */
public class WheelDrawingView extends View implements OnGestureListener,Animation.AnimationListener {

    private GestureDetector myGesture;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    // setup initial color
    private final int paintColor = Color.BLACK;

    // defines paint and canvas
    private Paint drawPaint;
    private Animation anim;

    private Integer margin;
    private Float radius;

    private Float centerX;
    private Float centerY;


    public WheelDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        myGesture = new GestureDetector(this);
    }

    // Setup paint with color and stroke styles
    private void init() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        // Init Wheel params
        margin = 20;
        radius = (float) getWidth() / 2 - margin;
        centerX = (float) getWidth() / 2;
        centerY = (float) radius + margin;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        init();

        Integer strokeWidth = 30;
        Integer segments =  16;
        Float angle = (float) 360/segments;
        Float pinDistance = (float) 15.00;
        Float segmentX = centerX + (radius * (float) Math.cos(Math.toRadians(0)));
        Float segmentY =  centerY + (radius * (float) Math.sin(Math.toRadians(0)));

        // Border
        drawPaint.setColor(Color.parseColor("#DB9E36"));
        drawPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(centerX, centerY, radius, drawPaint);

        // Segments
        for (int i = 1; i < segments+1; i++){
            // Segment as a wheel slice
            Float newSegmentX =  centerX + (radius * (float) Math.cos(Math.toRadians(angle * i)));
            Float newSegmentY =  centerY + (radius * (float) Math.sin(Math.toRadians(angle * i)));
            Path path = new Path();
            path.moveTo(centerX, centerY);
            path.lineTo(segmentX, segmentY);
            // path.arcTo(new RectF(segmentX, segmentY, newSegmentX, newSegmentY), (float) (180 - angle)/2 , (float) (180 - angle)/2);
            path.lineTo(newSegmentX, newSegmentY);
            path.close();
            // Draw segment
            drawPaint.setColor(Color.parseColor((i % 2 == 0) ? "#BD4932" : "#FFFAD5"));
            drawPaint.setStrokeWidth(5);
            drawPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, drawPaint);
            // Draw Pin
            Float pinX =  centerX + ((radius-pinDistance) * (float) Math.cos(Math.toRadians(angle * (i-1))));
            Float pinY =  centerY + ((radius-pinDistance) * (float) Math.sin(Math.toRadians(angle * (i-1))));
            drawPaint.setColor(Color.parseColor("#401911"));
            drawPaint.setStrokeWidth(1);
            drawPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(pinX,pinY,pinDistance,drawPaint);

            // Next segment
            segmentX = newSegmentX;
            segmentY = newSegmentY;
        }

    }

    public void rotate(float velocity) {
        // Rotate the wheel
        anim = new RotateAnimation(0, velocity, centerX, centerY);
        anim.setDuration((long) Math.abs(velocity));
        anim.setAnimationListener(this);
        anim.setFillAfter(true);
        startAnimation(anim);
    }

    @Override
    public void onAnimationStart(Animation animation){

    }
    @Override
    public void onAnimationEnd(Animation animation){
        // Get random receipt
        String[] receipts = {"3ejja", "Makloub Escalope", "Lablebi"};
        String zakzouka = receipts[(int) Math.floor(Math.random()*receipts.length)];
        RelativeLayout r = (RelativeLayout) ((ViewGroup) this.getParent());
        TextView t=(TextView) r.findViewById(R.id.zakzouka);
        t.setText(zakzouka);
    }

    @Override
    public void onAnimationRepeat(Animation animation){

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        return myGesture.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        Log.i("VELOCITY","" + velocityX);
        try {
            //do not do anything if the swipe does not reach a certain length of distance
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;

            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                rotate(-1 * velocityX);
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                rotate(velocityX);
            }
        } catch (Exception e) {
            // nothing
        }
        return false;

    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
        return false;
    }



}
