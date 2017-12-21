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

	public volatile boolean isExit = false;// �Ƿ��˳�

	public int intDownState = INT_STATE_WAIT;// ���ص�״̬

	public static final int INT_STATE_SUCCEED = 200;// �������
	public static final int INT_STATE_FAIL = 201;// ����ʧ��
	public static final int INT_STATE_ING = 202;// ������
	public static final int INT_STATE_WAIT = 203;// ���صȴ���
	public static final int INT_STATE_START = 204;// ׼������

	public DownThread(DownJob job) {
		this.job = job;
	}

	@Override
	public void run() {
		if (job.downUrl == null || job.downUrl.length() == 0) {
			downFail("��Ч�����ص�ַ");
			return;
		}

		URL url;
		try {
			// ���������ӽ��д���ո�
			String strUrl = Uri.encode(job.downUrl, "utf-8")
					.replaceAll("%3A", ":").replaceAll("%2F", "/");
			MyLog.i("׼������:" + strUrl);
			url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			job.fileSize = connection.getContentLength();
			if (job.fileSize == -1) {
				downFail("�ļ�����Ϊ-1");
				return;
			}

			downStart(String.valueOf(job.fileSize));

			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					connection.getInputStream());

			MyLog.i("׼�������ļ�:" + job.filePath);

			File file = new File(job.filePath);
			if (file.exists()) {
				file.delete();
			}
			File tempFile = new File(DownJob.getFullFilePath(DownJob
					.getDefaultFileName()));
			tempFile.createNewFile();

			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(tempFile));

			// file.createNewFile();// �����µ��ļ�
			// BufferedOutputStream bufferedOutputStream = new
			// BufferedOutputStream(
			// new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int readLen = 0;
			long num = 0;
			long startTime = System.currentTimeMillis();// �������ʵĿ�ʼʱ��,��λ����
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
			} while (!isExit);// ���ȡ����ֹͣ����.

			if (isExit) {
				bufferedInputStream.close();
				bufferedOutputStream.close();
				downFail("���ر���ֹ...");
				return;
			}

			// MyLog.i("��ʱ�ļ�--�ļ�������:" + tempFile.getAbsolutePath());
			
			tempFile.renameTo(file);
			// MyLog.i("���ƺ�--�ļ�������:" + file.getAbsolutePath());
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
