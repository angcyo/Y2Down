package com.angcyo.tools;

import android.util.Log;

public class MyLog {

	public static String TAG = "MyLog";
	private static boolean isDebug = true;

	public static void i(String message) {
		if (isDebug()) {
			Log.i(TAG, message);
		}
	}

	public static void i(String Tag, String message) {
		if (isDebug()) {
			Log.i(Tag, message);
		}
	}

	public static void i(int message) {
		if (isDebug()) {
			Log.i(TAG, String.valueOf(message));
		}
	}

	public static void i(String tag, int message) {
		if (isDebug()) {
			Log.i(tag, String.valueOf(message));
		}
	}

	public static boolean isDebug() {
		// TODO Auto-generated method stub
		return isDebug;
	}

	public static void setDebug(boolean bool) {
		isDebug = bool;
	}

}
