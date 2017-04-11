package com.busycount.pullloaddroprefresh;

import android.view.View;

/**
 * 项目名称：PullLoadAndDropRefresh
 * 类描述：下拉刷新监听
 * 创建人：Chen.h
 * 创建时间：2017/4/11 10:15
 * 修改人：Chen.h
 * 修改时间：2017/4/11 10:15
 * 修改备注：
 */
public interface DropRefreshListener {

    void onDefault(View header);

    void onDropping();

    void onRelease();

    void onRefreshing();
}
