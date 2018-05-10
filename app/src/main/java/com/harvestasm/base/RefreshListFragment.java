package com.harvestasm.base;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Collections;
import java.util.List;

/***
 * 带标准下拉Refresh刷新控件的Fragment抽象基类，实现view和activity创建时的回调流程。
 * 管理维护 @SwipeRefreshLayout 下拉控件和用于显示列表的list/recycler控件，开始加载
 * 数据并监听回调，刷新用户显示
 *
 * @param <T> T是数据item的类型
 * @param <V> V是ListView或者RecyclerView之类的用于接收
 */
abstract public class RefreshListFragment<T, V extends View> extends RefreshBaseFragment<List<T>, V> {
    private static final String TAG = RefreshListFragment.class.getSimpleName();

    private @Nullable Typeface typeface;  // lazy instance and only accessed by getTypeface()

    @Override
    protected void refreshWithoutData(@NonNull V childView) {
        refreshChangedData(childView, Collections.<T>emptyList());
    }

    protected final Typeface getTypeface() {
        if (null == typeface) {
            typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        }
        return typeface;
    }
}
