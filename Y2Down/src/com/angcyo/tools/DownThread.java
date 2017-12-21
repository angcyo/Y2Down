package com.angcyo.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;

public class DownThread extends Thread {

	private DownJob job;

	public volatile boolean isExit = false;// 是否退出

	public int intDownState = INT_STATE_WAIT;// 下载的状态

	public static final int INT_STATE_SUCCEED = 200;// 下载完成
	public static final int INT_STATE_FAIL = 201;// 下载失败
	public static final int INT_STATE_ING = 202;// 下载中
	public static final int INT_STATE_WAIT = 203;// 下载等待中
	public static final int INT_STATE_START = 204;// 准备下载

	public DownThread(DownJob job) {
		this.job = job;
	}

	@Override
	public void run() {
		if (job.downUrl == null || job.downUrl.length() == 0) {
			downFail("无效的下载地址");
			return;
		}

		URL url;
		try {
			// 对下载链接进行处理空格
			String strUrl = Uri.encode(job.downUrl, "utf-8")
					.replaceAll("%3A", ":").replaceAll("%2F", "/");
			MyLog.i("准备下载:" + strUrl);
			url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			job.fileSize = connection.getContentLength();
			if (job.fileSize == -1) {
				downFail("文件长度为-1");
				return;
			}

			downStart(String.valueOf(job.fileSize));

			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					connection.getInputStream());

			MyLog.i("准备创建文件:" + job.filePath);

			File file = new File(job.filePath);
			if (file.exists()) {
				file.delete();
			}
			File tempFile = new File(DownJob.getFullFilePath(DownJob
					.getDefaultFileName()));
			tempFile.createNewFile();

			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(tempFile));

			// file.createNewFile();// 创建新的文件
			// BufferedOutputStream bufferedOutputStream = new
			// BufferedOutputStream(
			// new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int readLen = 0;
			long num = 0;
			long startTime = System.currentTimeMillis();// 计算速率的开始时间,单位毫秒
			long readSize = 0;
			long speed = 0;

			do {
				readLen = bufferedInputStream.read(buffer);
				if (readLen <= 0) {
					break;
				}
				num += readLen;
				long time = System.currentTimeMillis();
				if ((time - startTime) <= 300) {
					readSize += readLen;
					if (speed == 0) {
						downIng(String.valueOf(num * 100 / job.fileSize),
								readLen);
					}
				} else {
					speed = (readSize * 1000) / (time - startTime);
					readSize = 0;
					startTime = time;

					downIng(String.valueOf(num * 100 / job.fileSize), speed);
				}
				bufferedOutputStream.write(buffer, 0, readLen);
				Thread.yield();
			} while (!isExit);// 点击取消就停止下载.

			if (isExit) {
				bufferedInputStream.close();
				bufferedOutputStream.close();
				downFail("下载被终止...");
				return;
			}

			// MyLog.i("临时文件--文件保存至:" + tempFile.getAbsolutePath());
			
			tempFile.renameTo(file);
			// MyLog.i("复制后--文件保存至:" + file.getAbsolutePath());
			downSucceed(job.filePath);

			bufferedInputStream.close();
			bufferedOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			downFail(job.downUrl + " " + e.getMessage());
		}

		setJobComplete(true);
	}

	private void setJobComplete(boolean b) {
		job.isComplete = b;
	}

	private void downFail(String msg) {
		setJobComplete(true);
		DownManage.getDownloadManage().sendMessage(INT_STATE_FAIL, job.strMd5,
				msg);
	}

	private void downIng(String msg, long speed) {
		setJobComplete(false);
		DownManage.getDownloadManage().sendMessage(INT_STATE_ING, job.strMd5,
				msg, speed);
	}

	private void downSucceed(String msg) {
		setJobComplete(true);
		DownManage.getDownloadManage().sendMessage(INT_STATE_SUCCEED,
				job.strMd5, msg);
	}

	@SuppressWarnings("unused")
	private void downWait(String msg) {
		setJobComplete(false);
		DownManage.getDownloadManage().sendMessage(INT_STATE_WAIT, job.strMd5,
				msg);
	}

	private void downStart(String msg) {
		setJobComplete(false);
		DownManage.getDownloadManage().sendMessage(INT_STATE_START, job.strMd5,
				msg);
	}
}
