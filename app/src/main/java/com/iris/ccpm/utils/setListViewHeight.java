package com.iris.ccpm.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

import com.iris.ccpm.adapter.MemberAdapter;

public class setListViewHeight {
    public static void set(ListView listView, Adapter adapter) {
        // 获取ListView对应的Adapter
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1)) + 100;
        listView.setLayoutParams(params);
    }
}
