package com.newbieguide;

import android.graphics.RectF;
import android.view.View;

/**
 * Created by xufangzhen on 2016/6/17.
 */
public class HoleBean {

    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_RECTANGLE = TYPE_CIRCLE + 1;
    public static final int TYPE_OVAL = TYPE_RECTANGLE + 1;

    private View mHole;
    private int mType;
    private int[] mLocation = new int[2];

    public HoleBean(View hole, int type) {
        this.mHole = hole;
        this.mType = type;
    }

    public int getCenterX() {
        int centerX = 0;
        if (mHole != null) {
            mHole.getLocationOnScreen(mLocation);
            centerX = mLocation[0] + mHole.getWidth() / 2;
        }
        return centerX;
    }

    public int getCenterY() {
        int centerY = 0;
        if (mHole != null) {
            mHole.getLocationOnScreen(mLocation);
            centerY = mLocation[1] + mHole.getHeight() / 2;
        }
        return centerY;
    }

    public int getRadius() {
        return mHole != null ? Math.min(mHole.getWidth(), mHole.getHeight()) / 2 : 0;
    }

    public RectF getRectF() {
        RectF rectF = new RectF();
        if (mHole != null) {
            mHole.getLocationOnScreen(mLocation);
            rectF.left = mLocation[0];
            rectF.top = mLocation[1];
            rectF.right = mLocation[0] + mHole.getWidth();
            rectF.bottom = mLocation[1] + mHole.getHeight();
        }
        return rectF;
    }

    public int getType() {
        return mType;
    }

}