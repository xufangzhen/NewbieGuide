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
    private BlurMaskFilter bmf;
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
        bmf = new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBgColor);
        setClickable(true);
        setWillNotDraw(false);
    }

    public void setDate(List<HoleBean> holeList) {
        mHoleList = holeList;
        if (mHoleList != null && mHoleList.size() > 0) {
            mBitmapRect = mHoleList.get(0).getRectF();
            for (HoleBean hole : mHoleList) {
                mBitmapRect.left = Math.min(mBitmapRect.left, hole.getRectF().left);
                mBitmapRect.top = Math.min(mBitmapRect.top, hole.getRectF().top);
                mBitmapRect.right = Math.max(mBitmapRect.right, hole.getRectF().right);
                mBitmapRect.bottom = Math.max(mBitmapRect.bottom, hole.getRectF().bottom);
            }
        }
        mStrokeWidth = Math.max(Math.max(mBitmapRect.left, mBitmapRect.top), Math.max(ScreenUtils.getScreenWidth(getContext()) -
                mBitmapRect.left -
                mBitmapRect.width(), ScreenUtils.getScreenHeight(getContext()) - mBitmapRect.top - mBitmapRect.height())) + 1;
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
            mPaint.setMaskFilter(bmf);
            mPaint.setStyle(Paint.Style.FILL);
            for (HoleBean hole : mHoleList) {
                switch (hole.getType()) {
                    case HoleBean.TYPE_CIRCLE:
                        mCanvas.drawCircle(hole.getCenterX() - mBitmapRect.left, hole.getCenterY() - mBitmapRect.top, hole.getRadius(),
                                mPaint);
                        break;
                    case HoleBean.TYPE_RECTANGLE:
                        mCanvas.drawRect(modifyRect(hole.getRectF()), mPaint);
                        break;
                    case HoleBean.TYPE_OVAL:
                        mCanvas.drawOval(modifyRect(hole.getRectF()), mPaint);
                        break;
                }
            }
            canvas.drawBitmap(mBitmap, mBitmapRect.left, mBitmapRect.top, null);

            mPaint.setXfermode(null);
            mPaint.setMaskFilter(null);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth + 0.1f);
            canvas.drawRect(fillRect(mBitmapRect), mPaint);
        }
    }

    private RectF modifyRect(RectF rectF) {
        rectF.left -= mBitmapRect.left;
        rectF.right -= mBitmapRect.left;
        rectF.top -= mBitmapRect.top;
        rectF.bottom -= mBitmapRect.top;
        return rectF;
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