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
	// ����ʹ��Time�࣬����
	/**
	 * @return ����HH:mm:ss ����ʱ��
	 */
	public static String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * @return ����yyyy-MM-dd ��ʽ��������
	 */
	public static String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
	}

	/**
	 * @return ����yyyy-MM-dd HH:mm:ss �ĸ�ʽ�������ں�ʱ��
	 */
	public static String getDateAndTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	/**
	 * @param pattern
	 *            ��ʽ
	 * @return ��������
	 */
	public static String getDate(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * @param pattern
	 *            ��ʽ
	 * @return ����ָ����ʽ����ʱ��
	 */
	public static String getTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * @param pattern
	 *            ָ���ĸ�ʽ
	 * @return ����ָ����ʽ�������ں�ʱ��
	 */
	public static String getDateAndTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * ��ҪȨ��<uses-permission android:name="android.permission.READ_PHONE_STATE" >
	 * 
	 * @param context
	 *            ������
	 * @return �����ֻ�����
	 */
	public static String getTelNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return tm.getLine1Number();
	}

	//
	/**
	 * ȡ��device��IP address
	 * 
	 * @param context
	 * @return ����ip
	 */
	public static String getIp(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();

		// ��ʽ��IP address�����磺��ʽ��ǰ��1828825280����ʽ����192.168.1.109
		String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),
				(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
		return ip;

	}

	/**
	 * @return ��ȡdevice��os version
	 */
	public static String getOsVersion() {
		String string = android.os.Build.VERSION.RELEASE;
		return string;
	}

	/**
	 * @return �����豸�ͺ�
	 */
	public static String getDeviceName() {
		String string = android.os.Build.MODEL;
		return string;
	}

	/**
	 * @return �����豸sdk�汾
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
	 * ��ȡ�����
	 * 
	 * @param ���Χ
	 * @return
	 */
	public static int getRandom(int n) {
		Random random = new Random();
		return random.nextInt(n);
	}

	/**
	 * ��ȡ�ַ�������������ַ���
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
	 * ����Դid��ȡ�ַ���
	 * 
	 * @param context
	 *            ������
	 * @param id
	 *            ��Դid
	 * @return �ַ���
	 */
	public static String getStringForRes(Context context, int id) {
		if (context == null) {
			return "";
		}
		return context.getResources().getString(id);
	}

	/**
	 * ����app�İ汾����.
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		// Log.i("�汾����:", version);
		return version;
	}

	/**
	 * ����app�İ汾����.
	 * 
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int code = packInfo.versionCode;
		// Log.i("�汾����:", version);
		return code;
	}

	/**
	 * �ж������Ƿ����
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
	 * �ж�wifi�Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // ����WIFI
																	// ������info
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * ��ȡ��Ļ�Ŀ�ȸ߶�
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
	 * ��ʽ���ֽ�
	 * 
	 * @param size
	 * @return
	 * @date 2014��12��3��
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
	 * �Ƿ�ָ�kb��λ
	 * 
	 * @param size
	 * @param isNewLine
	 * @return
	 * @date 2014��12��3��
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
