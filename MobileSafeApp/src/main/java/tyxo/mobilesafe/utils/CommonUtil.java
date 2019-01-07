package tyxo.mobilesafe.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import tyxo.functions.prettygirls.app.MyApplication;


public class CommonUtil {
	/**
	 * 在主线程执行任务
	 * 
	 * @param r
	 */
	public static void runOnUIThread(Runnable r) {
		 MyApplication.getMainHandler().post(r);
	}

	/**
	 * 获取Resource对象
	 * 
	 * @return
	 */
	public static Resources getResources() {
		return MyApplication.getIntstance().getResources();
	}

	/**
	 * 获取drawable资源
	 * 
	 * @param resId
	 * @return
	 */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/**
	 * 获取字符串资源
	 * 
	 * @param resId
	 * @return
	 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/**
	 * 获取color资源
	 * 
	 * @param resId
	 * @return
	 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/**
	 * 获取dimens资源,就是dp值
	 * 
	 * @param resId
	 * @return
	 */
	public static float getDimens(int resId) {
		return getResources().getDimension(resId);
	}
	/**
	 * 获取字符串数组资源
	 * @param resId
	 * @return
	 */
	public static String[] getStringArray(int resId){
		return getResources().getStringArray(resId);
	}
	/**
	 * 将指定的child从它 的父View中移除
	 * @param child
	 */
	public static void removeSelfFromParent(View child){
		if(child!=null){
			ViewParent parent = child.getParent();
			if(parent instanceof ViewGroup){
				ViewGroup group = (ViewGroup) parent;
				group.removeView(child);//将子View从父View中移除
			}
		}
	}
}
