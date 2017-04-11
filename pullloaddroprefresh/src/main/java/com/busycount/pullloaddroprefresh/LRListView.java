package com.busycount.pullloaddroprefresh;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 项目名称：PullLoadAndDropRefresh
 * 类描述：上拉加载更多，下拉刷新ListView
 * 创建人：Chen.h
 * 创建时间：2017/4/11 10:06
 * 修改人：Chen.h
 * 修改时间：2017/4/11 10:06
 * 修改备注：
 */
public class LRListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = "LRListView";

    private LoadRefreshState state;


    public LRListView(Context context) {
        this(context, null);
    }

    public LRListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LRListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LRListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initAttr(Context context) {
        state = new LoadRefreshState(this);
        LayoutInflater inflater = LayoutInflater.from(context);
        initHeader(inflater, R.layout.lr_header);
        initFooter(inflater, R.layout.lr_footer);
        setOnScrollListener(this);
    }

    private void initHeader(LayoutInflater inflater, int headerLayoutId) {
        View header = inflater.inflate(headerLayoutId, null);
        measureView(header);
        this.addHeaderView(header);
        state.addHeaderView(header);
        state.setDropRefreshListener(new DropRefreshListener() {
            TextView tv;

            @Override
            public void onDefault(View header) {
                tv = (TextView) header.findViewById(R.id.lr_drop_refresh_text);
            }

            @Override
            public void onDropping() {
                tv.setText("下拉刷新");
            }

            @Override
            public void onRelease() {
                tv.setText("松开开始刷新");
            }

            @Override
            public void onRefreshing() {
                tv.setText("刷新中");
            }
        });
    }

    private void initFooter(LayoutInflater inflater, int footerLayoutId) {
        View footer = inflater.inflate(footerLayoutId, null);
        measureView(footer);
        this.addFooterView(footer);
        state.addFooterView(footer);
        state.setPullLoadListener(new PullLoadListener() {
            TextView tv;

            @Override
            public void onDefault(View footer) {
                tv = (TextView) footer.findViewById(R.id.lr_pull_load_text);
            }

            @Override
            public void onPulling() {
                tv.setText("上拉加载更多");
            }

            @Override
            public void onRelease() {
                tv.setText("松开开始加载");
            }

            @Override
            public void onLoading() {
                tv.setText("加载中...");
            }
        });
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height = params.height;
        int childHeightSpec;
        if (height > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        state.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        state.setScroll(scrollState == SCROLL_STATE_TOUCH_SCROLL);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        state.setTop(firstVisibleItem == 0);
        state.setBottom(firstVisibleItem + visibleItemCount == totalItemCount);
    }

    public void setDropRefreshListener(DropRefreshListener dropRefreshListener) {
        state.setDropRefreshListener(dropRefreshListener);
    }

    public void setPullLoadListener(PullLoadListener pullLoadListener) {
        state.setPullLoadListener(pullLoadListener);
    }
}
