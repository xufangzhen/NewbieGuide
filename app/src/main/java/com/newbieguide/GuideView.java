package com.newbieguide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by xufangzhen on 2016/6/16.
 */
public class GuideView extends RelativeLayout {

	private int mBgColor = 0xb2000000;
	private float mStrokeWidth;
	private Paint mPaint;
	private Bitmap mBitmap;
	private RectF mBitmapRect;
	private Canvas mCanvas;
	private List<HoleBean> mHoleList;
	private PorterDuffXfermode pdf;

	public GuideView(Context context) {
		super(context);
		init();
	}

	public GuideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		pdf = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mBgColor);
		mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
		mBitmapRect = new RectF();
		setClickable(true);
		setWillNotDraw(false);
	}

	public void setDate(List<HoleBean> holeList) {
		mHoleList = holeList;
		if (mHoleList != null && !mHoleList.isEmpty()) {
			for (HoleBean hole : mHoleList) {
				mBitmapRect.union(hole.getRectF());
			}
		}
		mStrokeWidth = Math.max(Math.max(mBitmapRect.left, mBitmapRect.top), Math.max(ScreenUtils.getScreenWidth(getContext()) -
				mBitmapRect.right, ScreenUtils.getScreenHeight(getContext()) - mBitmapRect.bottom));
		if (mBitmapRect.width() > 0 && mBitmapRect.height() > 0) {
			mBitmap = Bitmap.createBitmap((int) mBitmapRect.width(), (int) mBitmapRect.height(), Bitmap.Config.ARGB_8888);
		} else {
			mBitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
			throw new UnsupportedOperationException("需要高亮的view尚未加载完，请调整适当的时机或者延迟");
		}
		mCanvas = new Canvas(mBitmap);
		mCanvas.drawColor(mBgColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mHoleList != null && mHoleList.size() > 0) {
			mPaint.setXfermode(pdf);
			mPaint.setStyle(Paint.Style.FILL);
			for (HoleBean hole : mHoleList) {
				RectF rectF = hole.getRectF();
				rectF.offset(-mBitmapRect.left, -mBitmapRect.top);
				switch (hole.getType()) {
					case HoleBean.TYPE_CIRCLE:
						mCanvas.drawCircle(rectF.centerX(), rectF.centerY(), hole.getRadius(), mPaint);
						break;
					case HoleBean.TYPE_RECTANGLE:
						mCanvas.drawRect(rectF, mPaint);
						break;
					case HoleBean.TYPE_OVAL:
						mCanvas.drawOval(rectF, mPaint);
						break;
				}
			}
			canvas.drawBitmap(mBitmap, mBitmapRect.left, mBitmapRect.top, null);
			//绘制剩余空间的矩形
			mPaint.setXfermode(null);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setStrokeWidth(mStrokeWidth + 0.1f);
			canvas.drawRect(fillRect(mBitmapRect), mPaint);
		}
	}

	private RectF fillRect(RectF rectF) {
		RectF fillRect = new RectF();
		fillRect.left = rectF.left - mStrokeWidth / 2;
		fillRect.top = rectF.top - mStrokeWidth / 2;
		fillRect.right = rectF.right + mStrokeWidth / 2;
		fillRect.bottom = rectF.bottom + mStrokeWidth / 2;
		return fillRect;
	}

	public void recycler() {
		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}

}