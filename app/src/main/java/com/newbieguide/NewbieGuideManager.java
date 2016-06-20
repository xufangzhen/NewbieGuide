package com.newbieguide;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;

/**
 * Created by xufangzhen on 2016/6/15.
 */
public class NewbieGuideManager {

    private static final String TAG = "newbie_guide";

    public static final int TYPE_LIST = 0;//收藏
    public static final int TYPE_COLLECT = 1;//list

    private Activity mActivity;
    private SharedPreferences sp;
    private NewbieGuide mNewbieGuide;
    private int mType;

    public NewbieGuideManager(Activity activity, int type) {
        mNewbieGuide = new NewbieGuide(activity);
        sp = activity.getSharedPreferences(TAG, Activity.MODE_PRIVATE);
        mActivity = activity;
        mType = type;
    }

    public NewbieGuideManager addView(View view, int shape) {
        mNewbieGuide.addHighLightView(view, shape);
        return this;
    }

    public void show() {
        show(0);
    }

    public void show(int delayTime) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(TAG + mType, false);
        editor.apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_LIST:
                        mNewbieGuide.setEveryWhereTouchable(false).addIndicateImg(R.drawable.left_arrow, ScreenUtils.dpToPx(mActivity,
                                60), ScreenUtils.dpToPx(mActivity, 110)).addMsgAndKnowTv("这个listview滚动到item6后出现新手引导浮层，\n只有点击我知道啦才会想消失",
                                -ScreenUtils.dpToPx(mActivity, 250)).show();
                        break;
                    case TYPE_COLLECT:
                        mNewbieGuide.addIndicateImg(R.drawable.right, ScreenUtils.dpToPx(mActivity, -70), ScreenUtils.dpToPx(mActivity,
                                50)).addMsgAndKnowTv("写了一个测试的，随便点击哪里都可以\n废话就不那么多了，你们看看吧", ScreenUtils.dpToPx(mActivity, 150)).show();
                        break;
                }
            }
        }, delayTime);
    }

    public void showWithListener(int delayTime, NewbieGuide.OnGuideChangedListener onGuideChangedListener) {
        mNewbieGuide.setOnGuideChangedListener(onGuideChangedListener);
        show(delayTime);
    }

    /**
     * 判断新手引导也是否已经显示了
     */
    public static boolean isNeverShowed(Activity activity, int type) {
        return activity.getSharedPreferences(TAG, Activity.MODE_PRIVATE).getBoolean(TAG + type, true);
    }


}
