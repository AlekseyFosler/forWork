package com.example.aleks.forwork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by aleks on 10.07.2016.
 */
public class TwoFragment extends Fragment {

    public TwoFragment () {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);

        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rect);
        relativeLayout.addView(new DrawingView(getActivity()));

        return rootView;
    }

    private class DrawingView extends View {
        public static final int LINE = 1;

        public static final float TOUCH_TOLERANCE = 4;
        public static final float TOUCH_STROKE_WIDTH = 5;

        protected Path mPath;
        protected Paint mPaint;
        protected Paint mPaintFinal;
        protected Bitmap mBitmap;
        protected Canvas mCanvas;

        protected boolean isDrawing = false;
        protected boolean isDrawingEnded = false;

        protected float mStartX;
        protected float mStartY;

        protected float mx;
        protected float my;

        public DrawingView(Context context) {
            super(context);
            init();
        }

        public DrawingView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            onDrawLine(canvas);
        }

        protected void init() {
            mPath = new Path();

            mPaint = new Paint(Paint.DITHER_FLAG);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(TOUCH_STROKE_WIDTH);


            mPaintFinal = new Paint(Paint.DITHER_FLAG);
            mPaintFinal.setAntiAlias(true);
            mPaintFinal.setDither(true);
            mPaintFinal.setColor(getContext().getResources().getColor(android.R.color.holo_orange_dark));
            mPaintFinal.setStyle(Paint.Style.STROKE);
            mPaintFinal.setStrokeJoin(Paint.Join.ROUND);
            mPaintFinal.setStrokeCap(Paint.Cap.ROUND);
            mPaintFinal.setStrokeWidth(TOUCH_STROKE_WIDTH);
        }

        protected void reset() {
            mPath = new Path();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            mx = event.getX();
            my = event.getY();
            onTouchEventSmoothLine(event);
            return true;
        }

        private void onDrawLine(Canvas canvas) {

            float dx = Math.abs(mx - mStartX);
            float dy = Math.abs(my - mStartY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                canvas.drawLine(mStartX, mStartY, mx, my, mPaint);
            }
        }

        private void onTouchEventSmoothLine(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrawing = true;
                    mStartX = mx;
                    mStartY = my;
                    mPath.reset();
                    mPath.moveTo(mx, my);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = Math.abs(mx - mStartX);
                    float dy = Math.abs(my - mStartY);
                    if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                        mPath.quadTo(mStartX, mStartY, (mx + mStartX) / 2, (my + mStartY) / 2);
                        mStartX = mx;
                        mStartY = my;
                    }
                    mCanvas.drawPath(mPath, mPaint);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    isDrawing = false;
                    mPath.lineTo(mStartX, mStartY);
                    mCanvas.drawPath(mPath, mPaintFinal);
                    mPath.reset();
                    invalidate();
                    break;
            }
        }

/*        Paint paint = new Paint();

        public DrawingView(Context context) {
            super(context);
        }
        @Override
        public void onDraw(Canvas canvas) {
            paint.setColor(Color.GREEN);
            Rect rect = new Rect(100, 100, 700, 112);
            canvas.drawRect(rect, paint );
        }*/
    }
}

