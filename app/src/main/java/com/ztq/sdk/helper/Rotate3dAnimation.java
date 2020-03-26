package com.ztq.sdk.helper;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.ztq.sdk.log.Log;

/**
 * Created by ztq on 2019/11/23.
 */
public class Rotate3dAnimation extends Animation {
    private static final String TAG = "noahedu.Rotate3dAnimation";
    private float mFromDegrees;
    private float mToDegress;
    private float mCenterX;
    private float mCenterY;
    private float mDepthZ;
    private boolean mReverse;
    private Camera mCamera;

    public Rotate3dAnimation(float fromDegress, float toDegrees, float centerX, float centerY, float depthZ, boolean reverse) {
        mFromDegrees = fromDegress;
        mToDegress = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
//        super.applyTransformation(interpolatedTime, t);
        Log.v(TAG, "applyTransformation");
        float fromDegrees = mFromDegrees;
        float degress = fromDegrees + (mToDegress - fromDegrees) * interpolatedTime;
        float centerX = mCenterX;
        float centerY = mCenterY;
        Matrix matrix = t.getMatrix();
        mCamera.save();
        if (mReverse) {
            mCamera.translate(0f, 0f, mDepthZ * interpolatedTime);
        } else {
            mCamera.translate(0, 0, mDepthZ * (1 - interpolatedTime));
        }
        mCamera.rotateY(degress);
        mCamera.getMatrix(matrix);
        mCamera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}