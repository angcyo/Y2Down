package com.angcyo.db;

import com.angcyo.tools.MD5;

public class ApkInfo {

	// 数据库表中的字段名
	public final static String APK_ICO = "apk_ico";
	public final static String APK_NAME = "apk_name";
	public final static String APK_VER = "apk_ver";
	public final static String APK_DES = "apk_des";
	public final static String APK_URL = "apk_url";
	public final static String APK_SIZE = "apk_size";
	public final static String APK_TIME = "apk_time";
	public final static String APK_ISNEW = "apk_isnew";

	private String strApkIco = "apk_ico";// 图标
	private String strApkName = "apk_name";// 名称
	private String strApkVer = "apk_ver";// 版本
	private String strApkDes = "apk_des";// 描述
	private String strApkUrl = "apk_url";// 下载地址
	private String strApkSize = "apk_size";// 大小,这个属性可以在下载文件的时候设置,不需要从服务器读取
	private String strApkTime = "apk_time";// 更新时间
	private String strApkIsNew = "apk_isnew";// 是否有更新

	private String StrMd5 = MD5.getMd5(String.valueOf(System
			.currentTimeMillis()));// 唯一标识符

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
