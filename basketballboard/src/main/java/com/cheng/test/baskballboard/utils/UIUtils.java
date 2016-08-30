package com.cheng.test.baskballboard.utils;

import android.content.Context;

/**
 * 项目名称：BasketballBoard
 * 类描述：
 * 创建人：Cheng
 * 创建时间：2016/8/21 19:49
 * 修改人：Cheng
 * 修改时间：2016/8/21 19:49
 * 修改备注：工具类
 */
public class UIUtils {

    /**
     * dp转px
     */
    public static int dip2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5);
    }

    /**
     * px转dp
     */
    public static float px2dip(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return px / density;
    }
}
