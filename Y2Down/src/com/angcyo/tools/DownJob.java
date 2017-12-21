package com.angcyo.tools;

import java.io.File;
import java.text.DecimalFormat;

import android.os.Environment;

public class DownJob {

	public String downUrl;// 下载地址
	public String filePath;// 保存的文件路径,全路径
	public int fileSize = -1;// 在下载开始之后,才会赋值.
	public boolean isComplete = false;// 任务是否完成,不管成功与否

	public String strMd5;

	public DownJob(String downUrl, String md5) {
		this.downUrl = downUrl;
		this.strMd5 = md5;

		this.filePath = getFullFilePath(getFileNameFromUrl(downUrl,
				getDefaultFileName()));

	}

	@Override
	public boolean equals(Object o) {
		return strMd5 == ((DownJob) o).strMd5;
	}

	public static String getSDPath() {
		return isExternalStorageWritable() ? Environment
				.getExternalStorageDirectory().getPath() : Environment
				.getDownloadCacheDirectory().getPath();
	}

	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		MyLog.i("没有可用SD卡");
		return false;
	}

	public static String getFullFilePath(String fileName) {
		return getSDPath() + File.separator + fileName;
	}

	/**
	 * 
	 * 从url中获取文件的名称,可以设置默认名称
	 * 
	 * @param url
	 *            需要获取名称的url
	 * @param defName
	 *            没有找到默认的名称
	 * @return
	 * @date 2014年11月27日
	 */
	public static String getFileNameFromUrl(String url, String defName) {
		if (url == null) {
			return null;
		}

		if (defName == null) {
			defName = getDefaultFileName();
		}

		// 从url中,返回文件的名称
		String fileName;
		int index = -1;
		for (int i = url.length() - 1; i >= 0; i--) {
			if (url.charAt(i) == '/') {
				index = i + 1;
				break;
			}
		}

		if (index != -1) {
			fileName = url.substring(index);
		} else {
			fileName = defName;
		}
		return fileName;
	}

	/**
	 * 得到一个MD5的默认文件名,默认的扩展名为.APK
	 * 
	 * @return
	 * @date 2014年11月27日
	 */
	public static String getDefaultFileName() {
		return MD5.getMd5(String.valueOf(System.currentTimeMillis())
				+ ".apk");
	}

	/**
	 * 得到一个MD5的默认文件名,默认的扩展名为.APK
	 * 
	 * @param exten
	 *            扩展名
	 * @return
	 * @date 2014年11月27日
	 */
	public static String getDefaultFileName(String exten) {
		return MD5.getMd5(String.valueOf(System.currentTimeMillis())
				+ exten);
	}

}
