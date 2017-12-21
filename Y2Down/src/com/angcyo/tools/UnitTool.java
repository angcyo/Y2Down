package com.angcyo.tools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

/**
 * @author angcyo
 *
 */
/**
 * @author angcyo
 *
 */
@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class UnitTool {
	// 可以使用Time类，更简单
	/**
	 * @return 按照HH:mm:ss 返回时间
	 */
	public static String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * @return 按照yyyy-MM-dd 格式返回日期
	 */
	public static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
	}

	/**
	 * @return 按照yyyy-MM-dd HH:mm:ss 的格式返回日期和时间
	 */
	public static String getDateAndTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * @param pattern
	 *            格式
	 * @return 返回日期
	 */
	public static String getDate(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * @param pattern
	 *            格式
	 * @return 按照指定格式返回时间
	 */
	public static String getTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * @param pattern
	 *            指定的格式
	 * @return 按照指定格式返回日期和时间
	 */
	public static String getDateAndTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * 需要权限<uses-permission android:name="android.permission.READ_PHONE_STATE" >
	 * 
	 * @param context
	 *            上下文
	 * @return 返回手机号码
	 */
	public static String getTelNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return tm.getLine1Number();
	}

	//
	/**
	 * 取得device的IP address
	 * 
	 * @param context
	 * @return 返回ip
	 */
	public static String getIp(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();

		// 格式化IP address，例如：格式化前：1828825280，格式化后：192.168.1.109
		String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),
				(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
		return ip;

	}

	/**
	 * @return 获取device的os version
	 */
	public static String getOsVersion() {
		String string = android.os.Build.VERSION.RELEASE;
		return string;
	}

	/**
	 * @return 返回设备型号
	 */
	public static String getDeviceName() {
		String string = android.os.Build.MODEL;
		return string;
	}

	/**
	 * @return 返回设备sdk版本
	 */
	public static String getOsSdk() {
		int sdk = android.os.Build.VERSION.SDK_INT;
		return String.valueOf(sdk);
	}

	public static int getRandom() {
		Random random = new Random();
		return random.nextInt();
	}

	/**
	 * 获取随机数
	 * 
	 * @param 最大范围
	 * @return
	 */
	public static int getRandom(int n) {
		Random random = new Random();
		return random.nextInt(n);
	}

	/**
	 * 获取字符数组中随机的字符串
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static String getRandomString(Context context, int resId) {
		String[] strings;
		strings = context.getResources().getStringArray(resId);

		return strings[getRandom(strings.length)];
	}

	/**
	 * 从资源id获取字符串
	 * 
	 * @param context
	 *            上下文
	 * @param id
	 *            资源id
	 * @return 字符串
	 */
	public static String getStringForRes(Context context, int id) {
		if (context == null) {
			return "";
		}
		return context.getResources().getString(id);
	}

	/**
	 * 返回app的版本名称.
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		// Log.i("版本名称:", version);
		return version;
	}

	/**
	 * 返回app的版本代码.
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int code = packInfo.versionCode;
		// Log.i("版本名称:", version);
		return code;
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkOK(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = cm.getActiveNetworkInfo();

		if (info != null && info.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断wifi是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // 返回WIFI
																	// 的连接info
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取屏幕的宽度高度
	 * 
	 * @param context
	 * @param size
	 */
	public static void getDisplay(Context context, Point size) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getSize(size);
		// DisplayMetrics outMetrics = new DisplayMetrics();
		// windowManager.getDefaultDisplay().getMetrics(outMetrics);
		return;
	}

	/**
	 * 格式化字节
	 * 
	 * @param size
	 * @return
	 * @date 2014年12月3日
	 */
	public static String formatSize(long size) {
		if (size / (1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "MB";
		} else if (size / 1024 > 0) {
			return "" + (size / (1024)) + "KB";
		} else
			return "" + size + "B";
	}

	public static String formatSize(String size) {

		if (size.substring(size.length() - 1).equalsIgnoreCase("B")
				|| size.endsWith("-")) {
			return size;
		} else {
			return formatSize(Long.valueOf(size));
		}

	}

	/**
	 * 是否分割kb单位
	 * 
	 * @param size
	 * @param isNewLine
	 * @return
	 * @date 2014年12月3日
	 */
	public static String formatSize(long size, boolean isNewLine) {
		if (isNewLine == false) {
			return formatSize(size);
		}
		if (size / (1024 * 1024) > 0) {
			float tmpSize = (float) (size) / (float) (1024 * 1024);
			DecimalFormat df = new DecimalFormat("#.##");
			return "" + df.format(tmpSize) + "\nMB";
		} else if (size / 1024 > 0) {
			return "" + (size / (1024)) + "\nKB";
		} else
			return "" + size + "\nB";
	}
}
