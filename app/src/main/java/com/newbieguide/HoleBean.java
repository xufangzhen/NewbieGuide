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

	public HoleBean(View hole, int type) {
		this.mHole = hole;
		this.mType = type;
	}

	public int getRadius() {
		return mHole != null ? Math.min(mHole.getWidth(), mHole.getHeight()) / 2 : 0;
	}

	public RectF getRectF() {
		RectF rectF = new RectF();
		if (mHole != null) {
			int[] location = new int[2];
			mHole.getLocationOnScreen(location);
			rectF.left = location[0];
			rectF.top = location[1];
			rectF.right = location[0] + mHole.getWidth();
			rectF.bottom = location[1] + mHole.getHeight();
		}
		return rectF;
	}

	public int getType() {
		return mType;
	}

}