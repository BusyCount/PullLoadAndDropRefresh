package com.busycount.pullloaddroprefresh;

import android.view.View;

/**
 * 项目名称：PullLoadAndDropRefresh
 * 类描述：上拉加载更多监听
 * 创建人：Chen.h
 * 创建时间：2017/4/11 10:10
 * 修改人：Chen.h
 * 修改时间：2017/4/11 10:10
 * 修改备注：
 */
public interface PullLoadListener {

    void onDefault(View footer);

    void onPulling();

    void onRelease();

    void onLoading();
}
