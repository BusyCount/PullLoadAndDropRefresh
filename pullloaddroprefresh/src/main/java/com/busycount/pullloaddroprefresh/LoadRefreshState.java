package com.busycount.pullloaddroprefresh;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * 项目名称：PullLoadAndDropRefresh
 * 类描述：上拉加载下拉刷新状态
 * 创建人：Chen.h
 * 创建时间：2017/4/11 10:22
 * 修改人：Chen.h
 * 修改时间：2017/4/11 10:22
 * 修改备注：
 */
public class LoadRefreshState {
    /**
     * 下拉默认
     */
    static final int TOP_DEFAULT = 10;
    /**
     * 下拉中
     */
    static final int TOP_DROPPING = 11;
    /**
     * 下拉释放
     */
    static final int TOP_RELEASE = 12;
    /**
     * 刷新
     */
    static final int TOP_REFRESHING = 13;
    /**
     * 上拉默认
     */
    static final int BOTTOM_DEFAULT = 20;
    /**
     * 上拉中
     */
    static final int BOTTOM_PULLING = 21;
    /**
     * 上拉释放
     */
    static final int BOTTOM_RELEASE = 22;
    /**
     * 加载
     */
    static final int BOTTOM_LOADING = 23;

    private ListView parent;
    private ViewOffset headerView;
    private ViewOffset footerView;
    private DropRefreshListener dropRefreshListener;
    private PullLoadListener pullLoadListener;
    private int currentState;
    /**
     * 是否滑动
     */
    private boolean isScroll;
    /**
     * 到达顶部
     */
    private boolean isTop;
    /**
     * 到达底部
     */
    private boolean isBottom;
    private float y1;
    private float y2;

    public LoadRefreshState(ListView parent) {
        this.parent = parent;
    }

    void onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int distance;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isTop || isBottom()) {
                    y1 = event.getY();
                } else {
                    y1 = -1f;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScroll) {
                    break;
                }
                if (isTop) {
                    y2 = event.getY();
                    if (y1 < 0) {
                        y1 = y2;
                    }
                    distance = (int) (y2 - y1);
                    if (distance > headerView.getHeight()) {
                        handlerState(TOP_RELEASE, distance);
                    } else if (distance > 0) {
                        handlerState(TOP_DROPPING, distance);
                    } else {
                        handlerState(TOP_DEFAULT, distance);
                    }
                } else if (isBottom) {
                    y2 = event.getY();
                    if (y1 == -1f) {
                        y1 = y2;
                    }
                    distance = (int) (y1 - y2);
                    if (distance > footerView.getHeight()) {
                        handlerState(BOTTOM_RELEASE, distance);
                    } else if (distance > 0) {
                        handlerState(BOTTOM_PULLING, distance);
                    } else {
                        handlerState(BOTTOM_DEFAULT, distance);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isTop) {
                    if (currentState == TOP_RELEASE) {
                        handlerState(TOP_REFRESHING, 0);
                    } else {
                        handlerState(TOP_DEFAULT, 0);
                    }
                } else if (isBottom) {
                    if (currentState == BOTTOM_RELEASE) {
                        handlerState(BOTTOM_LOADING, 0);
                    } else {
                        handlerState(BOTTOM_DEFAULT, 0);
                    }
                }
                break;
            default:
                break;
        }
    }


    private void handlerState(int state, int distance) {
        currentState = state;
        switch (currentState) {
            case TOP_DEFAULT:
                headerView.offsetTop(-headerView.getHeight());
                parent.smoothScrollToPosition(0);
                break;
            case TOP_DROPPING:
                headerView.offsetTop(distance);
                onDropping();
                break;
            case TOP_RELEASE:
                headerView.offsetTop(distance);
                onDropRelease();
                break;
            case TOP_REFRESHING:
                headerView.offsetTop(0);
                onDropRefresh();
                break;
            case BOTTOM_DEFAULT:
                footerView.offsetBottom(-footerView.getHeight());
                parent.smoothScrollToPosition(parent.getCount());
                break;
            case BOTTOM_PULLING:
                footerView.offsetBottom(distance);
                onPulling();
                break;
            case BOTTOM_RELEASE:
                footerView.offsetBottom(distance);
                onPullRelease();
                break;
            case BOTTOM_LOADING:
                footerView.offsetBottom(0);
                onPullLoading();
                break;
            default:
                break;
        }
    }

    private void onDropDefault() {
        if (dropRefreshListener == null) {
            return;
        }
        dropRefreshListener.onDefault(headerView.getView());
    }

    private void onDropping() {
        if (dropRefreshListener == null) {
            return;
        }
        dropRefreshListener.onDropping();
    }

    private void onDropRelease() {
        if (dropRefreshListener == null) {
            return;
        }
        dropRefreshListener.onRelease();
    }

    private void onDropRefresh() {
        if (dropRefreshListener == null) {
            return;
        }
        dropRefreshListener.onRefreshing();
    }

    private void onPullDefault() {
        if (pullLoadListener == null) {
            return;
        }
        pullLoadListener.onDefault(footerView.getView());
    }

    private void onPulling() {
        if (pullLoadListener == null) {
            return;
        }
        pullLoadListener.onPulling();
    }

    private void onPullRelease() {
        if (pullLoadListener == null) {
            return;
        }
        pullLoadListener.onRelease();
    }

    private void onPullLoading() {
        if (pullLoadListener == null) {
            return;
        }
        pullLoadListener.onLoading();
    }

    /**
     * 恢复下拉默认
     */
    public void restoreDrop() {
        handlerState(TOP_DEFAULT, 0);
    }

    /**
     * 恢复上拉默认
     */
    public void restorePull() {
        handlerState(BOTTOM_DEFAULT, 0);
    }


    public void setDropRefreshListener(DropRefreshListener dropRefreshListener) {
        this.dropRefreshListener = dropRefreshListener;
        onDropDefault();
    }

    public void setPullLoadListener(PullLoadListener pullLoadListener) {
        this.pullLoadListener = pullLoadListener;
        onPullDefault();
    }

    public void addHeaderView(View header) {
        headerView = new ViewOffset(header);
        headerView.offsetTop(-headerView.getHeight());
    }

    public void addFooterView(View footer) {
        footerView = new ViewOffset(footer);
        footerView.offsetBottom(-footerView.getHeight());
    }

    public boolean isScroll() {
        return isScroll;
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public boolean isBottom() {
        return isBottom;
    }

    public void setBottom(boolean bottom) {
        isBottom = bottom;
    }
}
