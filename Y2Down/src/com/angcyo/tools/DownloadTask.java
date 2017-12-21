package com.angcyo.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.AsyncTask;

public class DownloadTask extends AsyncTask<DownloadJob, Long, String> {

	private long fileSize = 0;
	private OnDownloadChange listener;
	private DownloadJob job;

	@Override
	protected String doInBackground(DownloadJob... params) {// ��������к�ʱ����
		this.job = params[0];
		URL url;
		try {
			// ���������ӽ��д���ո�
			String strUrl = Uri.encode(this.job.getDownUrl())
					.replaceAll("%3A", ":").replaceAll("%2F", "/");
			MyLog.i("׼������:" + strUrl);
			url = new URL(strUrl);

			MyLog.i("׼����ʼ����:" + strUrl);
			URLConnection connection = url.openConnection();
			fileSize = connection.getContentLength();
			if (fileSize == -1) {
				return null;
			}

			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					connection.getInputStream());

			MyLog.i("׼�������ļ�:" + job.getFilePath());
			File file = new File(this.job.getFilePath());
			file.createNewFile();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(file));

			byte[] buffer = new byte[1024];
			int readLen = 0;
			long num = 0;
			while ((readLen = bufferedInputStream.read(buffer)) != -1) {
				num += readLen;
				publishProgress(num * 100 / fileSize);
				bufferedOutputStream.write(buffer);
				if (isCancelled())
					break;
			}

			MyLog.i("�ļ�������:" + file.getAbsolutePath());

			if (listener != null) {
				listener.onDownComplete(this.job.getFilePath());
			}

			bufferedInputStream.close();
			bufferedOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPreExecute() {// �ڶ�doInBackground֮ǰִ��,on UI
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {// �ڶ�doInBackground֮��ִ��,on UI
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Long... values) {// �ڵ���publishProgress֮��ִ��,onUI
		super.onProgressUpdate(values);

		if (job.button != null) {
			job.button.setProgress((int) values[0].longValue());
		}

		if (listener != null) {
			listener.onDownProgressUpdate(values[0], fileSize);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	public void setOnDownloadChange(OnDownloadChange listener) {
		this.listener = listener;
	}

	public interface OnDownloadChange {
		void onDownProgressUpdate(long progre, long size);// ���ؽ���

		void onDownComplete(String filePath);// �������
		// void onDownCancelled();//����ȡ��

		void onDownFail();// ����ʧ��
	}
}
