package com.agadimi.a3dcube;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SceneView extends SurfaceView implements SurfaceHolder.Callback
{
    private final int PIXEL_PER_UNIT = 100; // 20 pixels per unit
    private SurfaceHolder holder;

    public SceneView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        this.holder = holder;
        render();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        this.holder = null;
        holder.removeCallback(this);
    }

    private void render()
    {
        Canvas canvas = holder.lockCanvas();

        //draw background
        canvas.drawColor(Color.argb(255, 37, 74, 93));

        //draw screen info
        drawScreenInfo(canvas);

        //draw guide lines
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 199, 81, 143));
        canvas.drawLine(getMeasuredWidth() / 2, 0,
                getMeasuredWidth() / 2, getMeasuredHeight(),
                paint);
        canvas.drawLine(0, getMeasuredHeight() / 2,
                getMeasuredWidth(), getMeasuredHeight() / 2,
                paint);


        //draw a cube :O
        drawCube(canvas);

        holder.unlockCanvasAndPost(canvas);
    }

    private void drawScreenInfo(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 199, 81, 143));
        paint.setTextSize(30);

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        String text = String.format("Width: %d, Height: %d", w, h);

        canvas.drawText(text, 50, h - 50, paint);
    }

    private double[][] cube = {
            {2, 0, 0},
            {2, 2, 0},
            {2, 2, 2},
            {2, 0, 2},
            {0, 0, 0},
            {0, 2, 0},
            {0, 2, 2},
            {0, 0, 2}
    };

    private void drawCube(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.argb(255, 247, 246, 238));

        //camera angle: 30, 30
        drawEdge(canvas, paint, 0, 1, 45);
        drawEdge(canvas, paint, 1, 2, 45);
        drawEdge(canvas, paint, 2, 3, 45);
        drawEdge(canvas, paint, 3, 0, 45);

        drawEdge(canvas, paint, 0, 4, 45);
        drawEdge(canvas, paint, 1, 5, 45);
        drawEdge(canvas, paint, 2, 6, 45);
        drawEdge(canvas, paint, 3, 7, 45);

        drawEdge(canvas, paint, 4, 5, 45);
        drawEdge(canvas, paint, 5, 6, 45);
        drawEdge(canvas, paint, 6, 7, 45);
        drawEdge(canvas, paint, 7, 4, 45);
    }

    private void drawEdge(Canvas canvas, Paint paint, int x, int y, int degree)
    {
        double[] xRot = rotateX(cube[x], 90);
        double[] yRot = rotateX(cube[y], 90);

        xRot = rotateY(xRot, degree);
        yRot = rotateY(yRot, degree);

        canvas.drawLine((float) xRot[0] * PIXEL_PER_UNIT + getMeasuredWidth() / 2,
                (float) xRot[1] * PIXEL_PER_UNIT + getMeasuredHeight() / 2,
                (float) yRot[0] * PIXEL_PER_UNIT + getMeasuredWidth() / 2,
                (float) yRot[1] * PIXEL_PER_UNIT + getMeasuredHeight() / 2,
                paint);
    }

    private double[] rotateX(double[] point, int degree)
    {
        double[][] rotationMat = {
                {1, 0, 0},
                {0, Math.cos(degree), -Math.sin(degree)},
                {0, Math.sin(degree), Math.cos(degree)}};

        return reshapePoint(multiplyMat(rotationMat, reshapePoint(point)));
    }

    private double[] rotateY(double[] point, int degree)
    {
        double[][] rotationMat = {
                {Math.cos(degree), 0, Math.sin(degree)},
                {0, 1, 0},
                {-Math.sin(degree), 0, Math.cos(degree)}};

        return reshapePoint(multiplyMat(rotationMat, reshapePoint(point)));
    }

    private double[] rotateZ(double[] point, int degree)
    {
        double[][] rotationMat = {
                {Math.cos(degree), -Math.sin(degree), 0},
                {Math.sin(degree), Math.cos(degree), 0},
                {0, 0, 1}};

        return reshapePoint(multiplyMat(rotationMat, reshapePoint(point)));
    }


    private double[][] reshapePoint(double[] point)
    {
        return new double[][]{{point[0]}, {point[1]}, {point[2]}};
    }

    private double[] reshapePoint(double[][] point)
    {
        return new double[]{point[0][0], point[1][0], point[2][0]};
    }

    public static double[][] multiplyMat(double[][] a, double[][] b)
    {
        int a_first = a.length;
        int a_second = a[0].length;
        int b_first = b.length;
        int b_second = b[0].length;

        if (a_second != b_first)
        {
            throw new RuntimeException(String.format("matrices does not match. [%d, %d] * [%d, %d]",
                    a_first, a_second, b_first, b_second));
        }

        double[][] output = new double[a_first][b_second];

        for (int i = 0; i < a_first; i++)
        {
            for (int j = 0; j < b_second; j++)
            {
                for (int k = 0; k < a_second; k++)
                {
                    output[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return output;
    }
}