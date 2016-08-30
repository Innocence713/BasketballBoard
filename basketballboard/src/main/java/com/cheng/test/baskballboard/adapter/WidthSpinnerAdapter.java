package com.cheng.test.baskballboard.adapter;

/**
 * 项目名称：BasketballBoard
 * 类描述：
 * 创建人：Cheng
 * 创建时间：2016/8/25 14:43
 * 修改人：Cheng
 * 修改时间：2016/8/25 14:43
 * 修改备注：
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheng.test.baskballboard.R;
import com.cheng.test.baskballboard.javabean.PaintWidth;

import java.util.List;

/**
 * 自定义适配器类
 *
 * @author jiangqq  <a href=http://blog.csdn.net/jiangqq781931404></a>
 */

public class WidthSpinnerAdapter extends BaseAdapter {

    private List<PaintWidth> mList;

    private Context mContext;


    public WidthSpinnerAdapter(Context pContext, List<PaintWidth> pList) {

        this.mContext = pContext;

        this.mList = pList;

    }


    @Override

    public int getCount() {

        return mList.size();

    }


    @Override

    public Object getItem(int position) {

        return mList.get(position);

    }


    @Override

    public long getItemId(int position) {

        return position;

    }

    /**
     * 下面是重要代码
     */

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.spinner_item, null);

        }
        TextView textView = (TextView) convertView.findViewById(R.id.item_text);
        textView.setText(mList.get(position).mWidthName);
        textView.setTextColor(0xFF000000);


        return convertView;

    }

}

