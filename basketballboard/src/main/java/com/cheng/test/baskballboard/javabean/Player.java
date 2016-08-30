package com.cheng.test.baskballboard.javabean;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * 项目名称：BasketballBoard
 * 类描述：封装球员对象
 * 创建人：Cheng
 * 创建时间：2016/8/22 10:41
 * 修改人：Cheng
 * 修改时间：2016/8/22 10:41
 * 修改备注：
 */
public class Player {

    private ArrayList<Point> mPathList;
    public Player() {
        mPathList =  new ArrayList();
    }

    public void addPoint(Point point){
        mPathList.add(point);
    }

    public Point getPoint(int position){

        return mPathList.get(position);
    }

    public int size(){

        return  mPathList.size();
    }

}
