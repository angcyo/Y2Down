package com.angcyo.tools;

import java.io.File;

import android.os.Environment;

import com.angcyo.db.ApkInfo;
import com.angcyo.view.GenerateProcessButton;

public class DownloadJob {

	private String downUrl;// ���ص�ַ
	private String filePath;// ������ļ�·��,ȫ·��
	public GenerateProcessButton button;// ����������ʾ�������İ�ť����
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
	 * ��url�л�ȡ�ļ�������,��������Ĭ������
	 * 
	 * @param url
	 *            ��Ҫ��ȡ���Ƶ�url
	 * @param defName
	 *            û���ҵ�Ĭ�ϵ�����
	 * @return
	 * @date 2014��11��27��
	 */
	private String getFileNameFromUrl(String url, String defName) {
		if (url == null || defName == null) {
			return null;
		}
		// ��url��,�����ļ�������
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
		MyLog.i("û�п���SD��");
		return false;
	}
}
