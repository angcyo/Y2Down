package com.angcyo.db;

import com.angcyo.tools.MD5;

public class ApkInfo {

	// ���ݿ���е��ֶ���
	public final static String APK_ICO = "apk_ico";
	public final static String APK_NAME = "apk_name";
	public final static String APK_VER = "apk_ver";
	public final static String APK_DES = "apk_des";
	public final static String APK_URL = "apk_url";
	public final static String APK_SIZE = "apk_size";
	public final static String APK_TIME = "apk_time";
	public final static String APK_ISNEW = "apk_isnew";

	private String strApkIco = "apk_ico";// ͼ��
	private String strApkName = "apk_name";// ����
	private String strApkVer = "apk_ver";// �汾
	private String strApkDes = "apk_des";// ����
	private String strApkUrl = "apk_url";// ���ص�ַ
	private String strApkSize = "apk_size";// ��С,������Կ����������ļ���ʱ������,����Ҫ�ӷ�������ȡ
	private String strApkTime = "apk_time";// ����ʱ��
	private String strApkIsNew = "apk_isnew";// �Ƿ��и���

	private String StrMd5 = MD5.getMd5(String.valueOf(System
			.currentTimeMillis()));// Ψһ��ʶ��

	public String getStrMd5() {
		return StrMd5;
	}

	public void setStrMd5(String strMd5) {
		StrMd5 = strMd5;
	}

	public ApkInfo() {

	}

	public String getStrApkIco() {
		return strApkIco;
	}

	public void setStrApkIco(String strApkIco) {
		this.strApkIco = strApkIco;
	}

	public String getStrApkName() {
		return strApkName;
	}

	public void setStrApkName(String strApkName) {
		this.strApkName = strApkName;
	}

	public String getStrApkVer() {
		return strApkVer;
	}

	public void setStrApkVer(String strApkVer) {
		this.strApkVer = strApkVer;
	}

	public String getStrApkDes() {
		return strApkDes;
	}

	public void setStrApkDes(String strApkDes) {
		this.strApkDes = strApkDes;
	}

	public String getStrApkUrl() {
		return strApkUrl;
	}

	public void setStrApkUrl(String strApkUrl) {
		this.strApkUrl = strApkUrl;
	}

	public String getStrApkSize() {
		return strApkSize;
	}

	public void setStrApkSize(String strApkSize) {
		this.strApkSize = strApkSize;
	}

	public String getStrApkTime() {
		return strApkTime;
	}

	public void setStrApkTime(String strApkTime) {
		this.strApkTime = strApkTime;
	}
	
	public String getStrApkIsNew() {
		return strApkIsNew;
	}

	public void setStrApkIsNew(String strApkIsNew) {
		this.strApkIsNew = strApkIsNew;
	}

	@Override
	public String toString() {
		return strApkIco + strApkName + strApkVer + strApkDes + strApkUrl
				+ strApkSize + strApkTime + strApkIsNew;
	}
}
