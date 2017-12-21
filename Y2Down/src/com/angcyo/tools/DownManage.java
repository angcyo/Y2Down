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
 * 下载管理器,目前只支持单线程,单任务下载,不支持断点下载
 * 
 * @author angcyo
 * @date 2014年12月2日
 *
 */
public class DownManage {
	private static DownManage downManage;// 单例模式,好处在于,Activity结束之后,还可以获取到此对象.如果没有销毁的话

	private HashMap<String, DownThread> downMap;// 保存当前正在进行的下载任务
	private Set<DownJob> setJob;// 用于保存下载任务的列表
	private DownJob jobCurrent;// 当前下载的任务

	private Handler mHandler;// 用于传输数据

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
	 * 遍历下载任务,足一下载
	 * 
	 * @date 2014年12月2日
	 */
	private void startDown() {
		if (setJob != null && setJob.size() != 0) {
			MyLog.i("任务总数:" + setJob.size());

			if (downMap == null) {
				downMap = new HashMap<String, DownThread>();
			}
			//
			// if (jobCurrent == null) {// 没有任务在执行
			//
			// } else if (downMap.get(jobCurrent.strMd5) != null) {//
			// 如果任务还在执行,就返回
			// return;
			// }

			// 如果当前没有在执行的任务,或者 执行的任务完成了,就执行下一个任务
			if (jobCurrent == null || jobCurrent.isComplete) {
				Iterator<DownJob> iterable = setJob.iterator();
				jobCurrent = iterable.next();

				thread = new DownThread(jobCurrent);
				downMap.put(jobCurrent.strMd5, thread);
				thread.start();
				MyLog.i("开始一个新的任务:" + jobCurrent.strMd5);
			}
			MyLog.i("当前的任务:" + jobCurrent.strMd5);

		}
	}

	/**
	 * 
	 * 添加任务到下载列表
	 * 
	 * @param job
	 * @date 2014年12月2日
	 */
	public void addDownJob(DownJob job) {
		if (setJob == null) {
			setJob = new HashSet<DownJob>();
		}
		setJob.add(job);
		startDown();// 开始下载
	}

	/**
	 * 删除一个正在等待的任务
	 * 
	 * @param job
	 * @return 成功返回true
	 * @date 2014年12月2日
	 */
	public boolean delDownJob(DownJob job) {
		if (setJob != null) {
			setJob.remove(job);
			return true;
		}
		return false;
	}

	/**
	 * 终止一个正在进行的下载线程
	 * 
	 * @param md5
	 * @return
	 * @date 2014年12月2日
	 */
	public boolean abortDownThread(String md5) {
		if (downMap == null) {
			return true;
		}
		DownThread thread = downMap.get(md5);
		thread.isExit = true;
		startDown();// 开始检测是否有其他任务需要下载
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
		// 下载失败,或者下载成功之后,开始下载下一个任务
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
