package com.angcyo.adapter;

import com.angcyo.db.ApkInfo;

public class DownListItem {
	public ApkInfo apkInfo;
	public boolean showExLayout = false;// �Ƿ���ʾ��չ��ͼ
	public int btProcess = 0; // ��ť�Ľ���
	public int btState = BT_STATE_DEFALUT;// ��ť��״̬
	public boolean isDowning = false;// �Ƿ���������
	public String fileSavePath;// �ļ��ı���·��
	public long speed;// �����ٶ�

	/**
	 * �ȴ���״̬
	 */
	public static final int BT_STATE_DEFALUT = 1000;//

	/**
	 * ��������
	 */
	public static final int BT_STATE_DOWN = 1001;//
	/**
	 * �ļ��Ѿ�����
	 */
	public static final int BT_STATE_ISEXIST = 1002;//
	/**
	 * ����������
	 */
	public static final int BT_STATE_DOWNLOADING = 1003;//
	/**
	 * �������
	 */
	public static final int BT_STATE_DOWNED = 1004;//
	/**
	 * ����ʧ��
	 */
	public static final int BT_STATE_DOWNFAIL = 1005;//

	/**
	 * �ȴ���
	 */
	public static final int BT_STATE_WAITING = 1006;//

	public DownListItem() {
	}

}
