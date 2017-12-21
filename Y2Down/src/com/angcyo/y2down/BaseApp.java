package com.angcyo.y2down;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;

import com.angcyo.adapter.DownListItem;
import com.angcyo.db.ApkInfo;
import com.angcyo.tools.MyLog;

public class BaseApp extends Application {
	public long dataNum = -1;// �Ѿ���ʾ���ݵ���Ŀ
	private List<DownListItem> listItem;
	private List<ApkInfo> list;

	// public Map<String, DownloadThread> downMap;

	public static BaseApp app;

	public BaseApp() {

	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;

		listItem = new ArrayList<DownListItem>();
		list = new ArrayList<ApkInfo>();
		// downMap = new HashMap<String, DownloadThread>();

	}

	public List<DownListItem> getListItem() {
		return listItem;
	}

	public void setListItem(List<DownListItem> listItem) {
		this.listItem = listItem;
	}

	public List<ApkInfo> getList() {
		return list;
	}

	public void setList(List<ApkInfo> list) {
		this.list = list;
	}

	// public void startDownThread(String md5) {
	// if (!downMap.containsKey(md5)) {
	// return;
	// }
	//
	// downMap.get(md5).start();
	// }
	//
	// public void stoptDownThread(String md5) {
	// if (!downMap.containsKey(md5)) {
	// return;
	// }
	//
	// downMap.get(md5).isExit = true;
	// }
	//
	// public void removeDownThread(String md5) {
	// downMap.remove(md5);
	//
	// }

	public void cleanAllData() {

		dataNum = -1;

		listItem.clear();
		list.clear();

	}

	/**
	 * @param fromFile
	 *            ���ļ�����
	 * @param toFile
	 *            ���Ƶ�
	 * @param rewrite
	 *            ����д��,Ҳ�����Ƿ�ɾ������˼
	 * @return
	 * @date 2014��12��1��
	 */
	public static boolean copyfile(File fromFile, File toFile, Boolean rewrite) {
		if (!fromFile.exists()) {
			return false;
		}
		if (!fromFile.isFile()) {
			return false;
		}
		if (!fromFile.canRead()) {
			return false;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists() && rewrite) {
			toFile.delete();
		}

		try {
			toFile.createNewFile();
			BufferedInputStream fosfrom = new BufferedInputStream(
					new FileInputStream(fromFile));
			BufferedOutputStream fosto = new BufferedOutputStream(
					new FileOutputStream(toFile));
			byte bt[] = new byte[1024];
			int c;
			do {
				c = fosfrom.read(bt);
				if (c <= 0) {
					break;
				}
				fosto.write(bt, 0, c);
			} while (true);

			// while ((c = fosfrom.read(bt)) > 0) {
			// fosto.write(bt, 0, c); // ������д�����ļ�����
			// }
			fosfrom.close();
			fosto.close();
		} catch (Exception ex) {
			Log.e("readfile", ex.getMessage());
			return false;
		}
		return true;
	}
}
