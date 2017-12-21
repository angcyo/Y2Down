package com.angcyo.tools;

import java.io.File;

import android.os.Environment;

import com.angcyo.db.ApkInfo;
import com.angcyo.view.GenerateProcessButton;

public class DownloadJob {

	private String downUrl;// 下载地址
	private String filePath;// 保存的文件路径,全路径
	public GenerateProcessButton button;// 保存用于显示进度条的按钮对象
	public ApkInfo info;

	public DownloadJob(GenerateProcessButton button, ApkInfo info) {
		this.button = button;
		this.info = info;

		this.filePath = getSDPath()
				+ File.separator
				+ getFileNameFromUrl(this.info.getStrApkUrl(),
						this.info.getStrApkName() + ".apk");
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
	private String getFileNameFromUrl(String url, String defName) {
		if (url == null || defName == null) {
			return null;
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

	public String getDownUrl() {
		downUrl = info.getStrApkUrl();
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getFilePath() {
		if (filePath == null || filePath.length() == 0) {
			filePath = getSDPath()
					+ File.separator
					+ MD5.getMd5(String.valueOf(System.currentTimeMillis()))
					+ ".apk";
		}
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
}
