package com.angcyo.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * ���ع�����,Ŀǰֻ֧�ֵ��߳�,����������,��֧�ֶϵ�����
 * 
 * @author angcyo
 * @date 2014��12��2��
 *
 */
public class DownManage {
	private static DownManage downManage;// ����ģʽ,�ô�����,Activity����֮��,�����Ի�ȡ���˶���.���û�����ٵĻ�

	private HashMap<String, DownThread> downMap;// ���浱ǰ���ڽ��е���������
	private Set<DownJob> setJob;// ���ڱ�������������б�
	private DownJob jobCurrent;// ��ǰ���ص�����

	private Handler mHandler;// ���ڴ�������

	private DownThread thread;//

	private DownManage() {
	}

	private DownManage(Handler handler) {
		this.mHandler = handler;
	}

	public static DownManage getDownloadManage() {
		if (downManage == null) {
			downManage = new DownManage();
		}
		return downManage;
	}

	public static DownManage getDownloadManage(Handler handler) {
		if (downManage == null) {
			downManage = new DownManage(handler);
		}
		downManage.mHandler = handler;
		return downManage;
	}

	/**
	 * ������������,��һ����
	 * 
	 * @date 2014��12��2��
	 */
	private void startDown() {
		if (setJob != null && setJob.size() != 0) {
			MyLog.i("��������:" + setJob.size());

			if (downMap == null) {
				downMap = new HashMap<String, DownThread>();
			}
			//
			// if (jobCurrent == null) {// û��������ִ��
			//
			// } else if (downMap.get(jobCurrent.strMd5) != null) {//
			// ���������ִ��,�ͷ���
			// return;
			// }

			// �����ǰû����ִ�е�����,���� ִ�е����������,��ִ����һ������
			if (jobCurrent == null || jobCurrent.isComplete) {
				Iterator<DownJob> iterable = setJob.iterator();
				jobCurrent = iterable.next();

				thread = new DownThread(jobCurrent);
				downMap.put(jobCurrent.strMd5, thread);
				thread.start();
				MyLog.i("��ʼһ���µ�����:" + jobCurrent.strMd5);
			}
			MyLog.i("��ǰ������:" + jobCurrent.strMd5);

		}
	}

	/**
	 * 
	 * ������������б�
	 * 
	 * @param job
	 * @date 2014��12��2��
	 */
	public void addDownJob(DownJob job) {
		if (setJob == null) {
			setJob = new HashSet<DownJob>();
		}
		setJob.add(job);
		startDown();// ��ʼ����
	}

	/**
	 * ɾ��һ�����ڵȴ�������
	 * 
	 * @param job
	 * @return �ɹ�����true
	 * @date 2014��12��2��
	 */
	public boolean delDownJob(DownJob job) {
		if (setJob != null) {
			setJob.remove(job);
			return true;
		}
		return false;
	}

	/**
	 * ��ֹһ�����ڽ��е������߳�
	 * 
	 * @param md5
	 * @return
	 * @date 2014��12��2��
	 */
	public boolean abortDownThread(String md5) {
		if (downMap == null) {
			return true;
		}
		DownThread thread = downMap.get(md5);
		thread.isExit = true;
		startDown();// ��ʼ����Ƿ�������������Ҫ����
		try {
			thread.join(2000);
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void sendMessage(int what, String strMd5, String strMsg) {
		sendMessage(what, strMd5, strMsg, -1);
	}

	public void sendMessage(int what, String strMd5, String strMsg, long speed) {
		// ����ʧ��,�������سɹ�֮��,��ʼ������һ������
		if (what == DownThread.INT_STATE_SUCCEED
				|| what == DownThread.INT_STATE_FAIL) {
			setJob.remove(jobCurrent);
			// jobCurrent.isComplete = true;
			startDown();
		}

		if (mHandler == null) {
			return;
		}

		Message message = mHandler.obtainMessage();

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
}
