package com.angcyo.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.Environment;

public class DownloadRunnable implements Runnable {

	public String downUrl;// ���ص�ַ
	public String filePath;// ������ļ�·��,ȫ·��
	public volatile boolean isExit = false;// �Ƿ��˳�
	public int intDownState = INT_STATE_WAIT;// ���ص�״̬
	public int fileSize = -1;// �����ؿ�ʼ֮��,�Żḳֵ.
	
	public String strMd5;

	public static int INT_STATE_SUCCEED = 200;// �������
	public static int INT_STATE_FAIL = 201;// ����ʧ��
	public static int INT_STATE_ING = 202;// ������
	public static int INT_STATE_WAIT = 203;// ���صȴ���
	public static int INT_STATE_START = 204;// ׼������

	public DownloadRunnable() {
	}

	public DownloadRunnable(String url) {
		this.downUrl = url;
		this.filePath = getFullFilePath(getFileNameFromUrl(url,
				getDefaultFileName()));

	}

	public DownloadRunnable(String url, String filePath) {
		this.downUrl = url;

		if (filePath == null || filePath.length() == 0) {
			this.filePath = getFullFilePath(MD5.getMd5(String
					.valueOf(System.currentTimeMillis())) + ".apk");
		} else {
			this.filePath = filePath;
		}

	}

	@Override
	public void run() {
		if (downUrl == null || downUrl.length() == 0) {
			return;
		}

		URL url;
		try {
			// ���������ӽ��д���ո�
			String strUrl = Uri.encode(downUrl).replaceAll("%3A", ":")
					.replaceAll("%2F", "/");
			MyLog.i("׼������:" + strUrl);
			url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			fileSize = connection.getContentLength();
			if (fileSize == -1) {
				downFail("�ļ�����Ϊ-1");
				return;
			}

			downStart(String.valueOf(fileSize));

			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					connection.getInputStream());

			MyLog.i("׼�������ļ�:" + filePath);

			File file = new File(filePath);
			file.createNewFile();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(file));

			byte[] buffer = new byte[1024];
			int readLen = 0;
			long num = 0;
			while ((readLen = bufferedInputStream.read(buffer)) != -1) {
				num += readLen;

				downIng(String.valueOf(num * 100 / fileSize));

				bufferedOutputStream.write(buffer);
				if (isExit) {
					bufferedInputStream.close();
					bufferedOutputStream.close();
					downFail("���ر���ֹ...");
					return;
				}
			}

			MyLog.i("�ļ�������:" + file.getAbsolutePath());
			downSucceed(filePath);

			bufferedInputStream.close();
			bufferedOutputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			downFail(downUrl + " " + e.getMessage());
		}
	}

	DownloadStateListener downState;

	public interface DownloadStateListener {
		void onDownStateChange(int state, String msg);
	}

	public void setDownloadStateListener(DownloadStateListener listener) {
		downState = listener;
	}

	private void downFail(String msg) {
		if (downState != null) {
			downState.onDownStateChange(INT_STATE_FAIL, msg);
		}
	}

	private void downIng(String msg) {
		if (downState != null) {
			downState.onDownStateChange(INT_STATE_ING, msg);
		}
	}

	private void downSucceed(String msg) {
		if (downState != null) {
			downState.onDownStateChange(INT_STATE_SUCCEED, msg);
		}
	}

	@SuppressWarnings("unused")
	private void downWait(String msg) {
		if (downState != null) {
			downState.onDownStateChange(INT_STATE_WAIT, msg);
		}
	}

	private void downStart(String msg) {
		if (downState != null) {
			downState.onDownStateChange(INT_STATE_WAIT, msg);
		}
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
		if (url == null || defName == null) {
			return null;
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
