package com.cheng.test.baskballboard.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng.test.baskballboard.R;

import java.util.ArrayList;

/**
 * 项目名称：BasketballBoard
 * 类描述：
 * 创建人：Cheng
 * 创建时间：2016/8/25 22:46
 * 修改人：Cheng
 * 修改时间：2016/8/25 22:46
 * 修改备注：
 */
public class ListViewAdapter extends BaseAdapter {
    private ArrayList<String> mFileList;
    private Context mContext;
    private Handler mHandler;

    public ListViewAdapter(Context context, ArrayList<String> fileNameList, Handler handler) {
        mFileList = fileNameList;
        mContext = context;
        mHandler = handler;
    }

    @Override
    public int getCount() {
        return mFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.list_item, null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.tv_file_name);
        textView.setText(mFileList.get(position));

        ImageView iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知MainActivity删除
                Message msg = Message.obtain();
                msg.what = 5;
                msg.arg1 = position;
                mHandler.sendMessage(msg);
            }
        });
        return convertView;
    }
}
