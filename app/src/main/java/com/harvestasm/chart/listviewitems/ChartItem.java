package com.harvestasm.chart.listviewitems;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.data.ChartData;
import com.harvestasm.apm.sample.R;

/**
 * baseclass of the chart-listview items
 * @author philipp
 *
 */
public abstract class ChartItem {
    /*
        "内存占用"

        "键盘收起时间"
        "电流"
        "CPU占用"

        "Theme页滑动"
        "emoji页滑动"
        "主键盘与符号键盘切换"
        "主键盘与emoji键盘切换"
        "键盘到设置页面"
        "键盘打字弹泡"

        "设备"
     */
    public static class ID {
        public static final int MEMORY = 0;
        public static final int KEYBOARD_HIDE = 1;
        public static final int BATTARY = 2;
        public static final int CPU = 3;
        public static final int SKIN_SLIP = 4;
        public static final int EMOJI_SLIP = 5;
        public static final int SYMBOL_KB_SWITCH = 6;
        public static final int EMOJI_KB_SWITCH = 7;
        public static final int KB_SETTING = 8;
        public static final int KB_BALLOOM = 9;
        public static final int DEMO_PI = 10;
    }
    
    protected static final int TYPE_BARCHART = 0;
    protected static final int TYPE_LINECHART = 1;
    protected static final int TYPE_PIECHART = 2;
    
    protected ChartData<?> mChartData;

    private final String title;
    private final int id;
    
    public ChartItem(ChartData<?> cd) {
        this(cd, "", 0);
    }

    public ChartData<?> getChartData() {
        return mChartData;
    }

    public ChartItem(ChartData<?> cd, String title, int id) {
        this.mChartData = cd;
        this.title = title;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract int getItemType();
    
    public abstract View getView(int position, View convertView, Context c);

    public static class BaseViewHolder {
         private TextView title;

        protected BaseViewHolder(View convertView) {
            title = convertView.findViewById(R.id.title);
        }

        protected void bind(ChartItem item) {
            if (null != title) {
                title.setText(item.title);
            }
        }
    }
}
