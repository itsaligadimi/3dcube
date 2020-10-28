package com.agadimi.a3dcube.glversion;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.agadimi.a3dcube.R;

import timber.log.Timber;

public class GlSceneView extends GLSurfaceView implements ScaleGestureDetector.OnScaleGestureListener
{

    private GlRenderer renderer;

    private ScaleGestureDetector scaleGestureDetector;

    private final float TOUCH_SCALE_FACTOR = 100.0f / 320;
    private float previousX;
    private float previousY;

    public GlSceneView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        // using OpenGL ES 2.0
        setEGLContextClientVersion(2);

        // set renderer
        renderer = new GlRenderer(context);
        setRenderer(renderer);

        //render when there's a change
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        scaleGestureDetector.onTouchEvent(e);

        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction())
        {
            case MotionEvent.ACTION_MOVE:

                float dx = x - previousX;
                float dy = y - previousY;

//                // reverse direction of rotation above the mid-line
//                if (y > getHeight() / 2) {
//                    dx = dx * -1;
//                }
//
//                // reverse direction of rotation to left of the mid-line
//                if (x < getWidth() / 2) {
//                    dy = dy * -1;
//                }

                renderer.setxAngle(renderer.getxAngle() + (dx * TOUCH_SCALE_FACTOR));
                renderer.setyAngle(renderer.getyAngle() + (dy * TOUCH_SCALE_FACTOR));

                renderer.setScale(renderer.getyAngle() / 30);

                requestRender();
        }

        previousX = x;
        previousY = y;
        return true;
    }

    public void load(int raw)
    {
        renderer.setModel(raw);
        requestRender();
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector)
    {
        Timber.d("scaling: %f", scaleGestureDetector.getScaleFactor());
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector)
    {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector)
    {

    }

//    private class ScaleListener
//            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            mScaleFactor *= detector.getScaleFactor();
//
//            // Don't let the object get too small or too large.
////            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
//
//            invalidate();
//            return true;
//        }
//    }
}
