package com.busycount.pullloaddroprefresh;

import android.view.View;

/**
 * 项目名称：PullLoadAndDropRefresh
 * 类描述：View 偏移
 * 创建人：Chen.h
 * 创建时间：2017/4/11 11:14
 * 修改人：Chen.h
 * 修改时间：2017/4/11 11:14
 * 修改备注：
 */
public class ViewOffset {
    private View view;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private int height;

    public ViewOffset(View view) {
        this.view = view;
        left = view.getPaddingLeft();
        top = view.getPaddingTop();
        right = view.getPaddingRight();
        bottom = view.getPaddingBottom();
        height = view.getMeasuredHeight();
    }

    public void offsetTop(int top) {
        view.setPadding(left, top, right, bottom);
    }

    public void offsetBottom(int bottom) {
        view.setPadding(left, top, right, bottom);
    }

    public View getView() {
        return view;
    }

    public int getHeight() {
        return height;
    }

}
