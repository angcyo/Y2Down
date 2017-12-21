package com.angcyo.tools;

import java.io.File;
import java.text.DecimalFormat;

import android.os.Environment;

public class DownJob {

	public String downUrl;// ���ص�ַ
	public String filePath;// ������ļ�·��,ȫ·��
	public int fileSize = -1;// �����ؿ�ʼ֮��,�Żḳֵ.
	public boolean isComplete = false;// �����Ƿ����,���ܳɹ����

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
		MyLog.i("û�п���SD��");
		return false;
	}

	public static String getFullFilePath(String fileName) {
		return getSDPath() + File.separator + fileName;
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
	public static String getFileNameFromUrl(String url, String defName) {
		if (url == null) {
			return null;
		}

		if (defName == null) {
			defName = getDefaultFileName();
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

	/**
	 * �õ�һ��MD5��Ĭ���ļ���,Ĭ�ϵ���չ��Ϊ.APK
	 * 
	 * @return
	 * @date 2014��11��27��
	 */
	public static String getDefaultFileName() {
		return MD5.getMd5(String.valueOf(System.currentTimeMillis())
				+ ".apk");
	}

	/**
	 * �õ�һ��MD5��Ĭ���ļ���,Ĭ�ϵ���չ��Ϊ.APK
	 * 
	 * @param exten
	 *            ��չ��
	 * @return
	 * @date 2014��11��27��
	 */
	public static String getDefaultFileName(String exten) {
		return MD5.getMd5(String.valueOf(System.currentTimeMillis())
				+ exten);
	}

}
