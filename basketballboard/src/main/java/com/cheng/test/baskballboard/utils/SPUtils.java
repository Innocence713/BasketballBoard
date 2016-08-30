package com.cheng.test.baskballboard.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {

	private static SharedPreferences sp;


	/**
	 * 写入boolean变量到SharedPreferences对象sp中
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param value
	 *            存储节点的值
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 读取SharedPreferences对象sp中boolean变量值
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param defValue
	 *            没有存储节点的默认值
	 * @return 存储节点对应值或默认值
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		return sp.getBoolean(key, defValue);
	}

	/**
	 * 写入String变量到SharedPreferences对象sp中
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param value
	 *            存储节点的值
	 */
	public static void putString(Context context, String key, String value) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		sp.edit().putString(key, value).commit();
	}

	/**
	 * 读取SharedPreferences对象sp中String变量值
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param defValue
	 *            没有存储节点的默认值
	 * @return 存储节点对应值或默认值
	 */
	public static String getString(Context context, String key, String defValue) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		return sp.getString(key, defValue);
	}

	/**
	 * 写入int变量到SharedPreferences对象sp中
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param value
	 *            存储节点的值
	 */
	public static void putInt(Context context, String key, int value) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		sp.edit().putInt(key, value).commit();
	}

	// ��
	/**
	 * 读取SharedPreferences对象sp中int变量值
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param defValue
	 *            没有存储节点的默认值
	 * @return 存储节点对应值或默认值
	 */
	public static int getInt(Context context, String key, int defValue) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		return sp.getInt(key, defValue);
	}

	public static void remove(Context context, String key) {
		if (sp == null) {
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}

		sp.edit().remove(key).commit();

	}


	/**
	 * 写入String变量到SharedPreferences对象sp中
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param value
	 *            存储节点的值
	 */
	public static void putPathString(Context context, String key, String value) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("path", Context.MODE_PRIVATE);
		}

		sp.edit().putString(key, value).commit();
	}

	/**
	 * 读取SharedPreferences对象sp中String变量值
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param defValue
	 *            没有存储节点的默认值
	 * @return 存储节点对应值或默认值
	 */
	public static String getPathString(Context context, String key, String defValue) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("path", Context.MODE_PRIVATE);
		}

		return sp.getString(key, defValue);
	}


	/**
	 * 写入int变量到SharedPreferences对象sp中
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param value
	 *            存储节点的值
	 */
	public static void putPathInt(Context context, String key, int value) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("path", Context.MODE_PRIVATE);
		}

		sp.edit().putInt(key, value).commit();
	}

	// ��
	/**
	 * 读取SharedPreferences对象sp中int变量值
	 *
	 * @param context
	 *            上下文环境
	 * @param key
	 *            存储节点
	 * @param defValue
	 *            没有存储节点的默认值
	 * @return 存储节点对应值或默认值
	 */
	public static int getPathInt(Context context, String key, int defValue) {
		// (存储节点文件名称， 读写方式)
		if (sp == null) {
			sp = context.getSharedPreferences("path", Context.MODE_PRIVATE);
		}

		return sp.getInt(key, defValue);
	}

	public static void removePath(Context context, String key) {
		if (sp == null) {
			sp = context.getSharedPreferences("path", Context.MODE_PRIVATE);
		}

		sp.edit().remove(key).commit();

	}

}
