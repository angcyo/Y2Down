package com.angcyo.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import com.angcyo.fragment.DownFragment;
import com.angcyo.y2down.BaseApp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;

public class DownloadThread extends Thread {

	public String downUrl;// 下载地址
	public String filePath;// 保存的文件路径,全路径
	public volatile boolean isExit = false;// 是否退出
	public int intDownState = INT_STATE_WAIT;// 下载的状态
	public int fileSize = -1;// 在下载开始之后,才会赋值.

	public String strMd5;
	public DownFragment fragment;

	public static final int INT_STATE_SUCCEED = 200;// 下载完成
	public static final int INT_STATE_FAIL = 201;// 下载失败
	public static final int INT_STATE_ING = 202;// 下载中
	public static final int INT_STATE_WAIT = 203;// 下载等待中
	public static final int INT_STATE_START = 204;// 准备下载

	public DownloadThread(String url, String filePath) {
		this.downUrl = url;

		if (filePath == null || filePath.length() == 0) {
			this.filePath = getFullFilePath(getFileNameFromUrl(url,
					getDefaultFileName()));
		} else {
			this.filePath = filePath;
		}

	}

	public DownloadThread(Runnable runnable) {
		super(runnable);
	}

	public DownloadThread(String threadName) {
		super(threadName);
	}

	@Override
	public void run() {
		if (downUrl == null || downUrl.length() == 0) {
			return;
		}

		URL url;
		try {
			// 对下载链接进行处理空格
			String strUrl = Uri.encode(downUrl).replaceAll("%3A", ":")
					.replaceAll("%2F", "/");
			MyLog.i("准备下载:" + strUrl);
			url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			fileSize = connection.getContentLength();
			if (fileSize == -1) {
				downFail("文件长度为-1");
				return;
			}

			downStart(String.valueOf(fileSize));

			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					connection.getInputStream());

			MyLog.i("准备创建文件:" + filePath);

			File file = new File(filePath);
			File tempFile = File.createTempFile("y2downtemp", ".apk");

			// file.createNewFile();

			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(tempFile));

			byte[] buffer = new byte[1024];
			int readLen = 0;
			long num = 0;
			long startTime = System.currentTimeMillis();// 计算速率的开始时间,单位毫秒
			long startSize = 0;
			while ((readLen = bufferedInputStream.read(buffer)) != -1) {
				num += readLen;

				long time = System.currentTimeMillis();
				int s = (int) ((time - startTime) / 1000);// 转换成秒
				if (s > 1) {// 大于1秒,开始计算速率
					downIng(String.valueOf(num * 100 / fileSize),
							(num - startSize) / s);
					
					startSize = num;
				}

				Thread.yield();

				bufferedOutputStream.write(buffer);
				if (isExit) {
					bufferedInputStream.close();
					bufferedOutputStream.close();
					downFail("下载被终止...");
					return;
				}
			}

			MyLog.i("文件保存至:" + tempFile.getAbsolutePath());
			BaseApp.copyfile(tempFile, file, true);
			downSucceed(filePath);

			bufferedInputStream.close();
			bufferedOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			downFail(downUrl + " " + e.getMessage());
		}
	}

	private void sendMessage(int what, String strMd5, String strMsg) {
		sendMessage(what, strMd5, strMsg, -1);
	}

	private void sendMessage(int what, String strMd5, String strMsg, long speed) {
		Message message = fragment.mHandler.obtainMessage();

		Bundle bundle = new Bundle();
		message.what = what;
		bundle.putString("md5", strMd5);
		bundle.putString("msg", strMsg);

		if (speed != -1) {
			bundle.putLong("speed", speed);
		}

		message.setData(bundle);
		message.sendToTarget();
	}

	private void downFail(String msg) {
		sendMessage(INT_STATE_FAIL, strMd5, msg);
	}

	private void downIng(String msg, long speed) {
		sendMessage(INT_STATE_ING, strMd5, msg, speed);
	}

	private void downSucceed(String msg) {
		sendMessage(INT_STATE_SUCCEED, strMd5, msg);
	}

	@SuppressWarnings("unused")
	private void downWait(String msg) {
		sendMessage(INT_STATE_WAIT, strMd5, msg);
	}

	private void downStart(String msg) {
		sendMessage(INT_STATE_START, strMd5, msg);
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
