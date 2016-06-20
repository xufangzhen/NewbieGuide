package com.newbieguide;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AbsListView.OnScrollListener {

    private ImageView mCollect;
    private TextView mTitleTv;
    private ListView mListView;
    private List<String> mList;
    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mCollect = (ImageView) findViewById(R.id.collect);
        mTitleTv = (TextView) findViewById(R.id.title);
        mListView = (ListView) findViewById(R.id.list);

        mList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mList.add("Item:" + i);
        }
        mListView.setAdapter(new ListAdapter(mList));
        mListView.setOnScrollListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(NewbieGuideManager.isNeverShowed(this, NewbieGuideManager.TYPE_COLLECT)) {
            new NewbieGuideManager(this, NewbieGuideManager.TYPE_COLLECT).addView(mCollect, HoleBean.TYPE_CIRCLE).addView(mTitleTv,
                    HoleBean.TYPE_RECTANGLE).show();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(final AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem >= 6 && NewbieGuideManager.isNeverShowed(this, NewbieGuideManager.TYPE_LIST) && !isShow) {
            isShow = true;
            mListView.smoothScrollToPosition(6);
            mListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mListView.setSelection(6);
                    mListView.post(new Runnable() {
                        @Override
                        public void run() {
                            new NewbieGuideManager(MainActivity.this, NewbieGuideManager.TYPE_LIST).addView(view.getChildAt(0)
                                    .findViewById(R.id.logo), HoleBean.TYPE_RECTANGLE).show();
                        }
                    });
                }
            }, 200);
        }
    }


    class ListAdapter extends BaseAdapter {

        public ListAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList != null ? mList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mList != null ? mList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item, null);
                holder.logo = (ImageView) convertView.findViewById(R.id.logo);
                holder.item = (TextView) convertView.findViewById(R.id.details);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.item.setText(mList.get(position).toString());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView logo;
        TextView item;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //todo 下面代码需要删掉
        SharedPreferences.Editor editor = getSharedPreferences("newbie_guide", MODE_PRIVATE).edit();
        editor.putBoolean("newbie_guide" + NewbieGuideManager.TYPE_LIST, true);
        editor.putBoolean("newbie_guide" + NewbieGuideManager.TYPE_COLLECT, true);
        editor.apply();
    }
}
