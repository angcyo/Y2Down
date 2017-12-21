package com.angcyo.adapter;

import com.angcyo.db.ApkInfo;

public class DownListItem {
	public ApkInfo apkInfo;
	public boolean showExLayout = false;// 是否显示扩展视图
	public int btProcess = 0; // 按钮的进度
	public int btState = BT_STATE_DEFALUT;// 按钮的状态
	public boolean isDowning = false;// 是否正在下载
	public String fileSavePath;// 文件的保存路径
	public long speed;// 下载速度

	/**
	 * 等待的状态
	 */
	public static final int BT_STATE_DEFALUT = 1000;//

	/**
	 * 可以下载
	 */
	public static final int BT_STATE_DOWN = 1001;//
	/**
	 * 文件已经存在
	 */
	public static final int BT_STATE_ISEXIST = 1002;//
	/**
	 * 正在下载中
	 */
	public static final int BT_STATE_DOWNLOADING = 1003;//
	/**
	 * 下载完成
	 */
	public static final int BT_STATE_DOWNED = 1004;//
	/**
	 * 下载失败
	 */
	public static final int BT_STATE_DOWNFAIL = 1005;//

	/**
	 * 等待中
	 */
	public static final int BT_STATE_WAITING = 1006;//

	public DownListItem() {
	}

}
