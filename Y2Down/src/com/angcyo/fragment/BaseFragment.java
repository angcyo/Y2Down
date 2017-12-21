package com.angcyo.fragment;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angcyo.adapter.DownListItem;
import com.angcyo.db.ApkInfo;
import com.angcyo.fragment.AddUrlFragment.onAddUrlListener;
import com.angcyo.tools.ToastTool;
import com.angcyo.y2down.BaseActivity;
import com.easyandroidanimations.library.ExplodeAnimation;

public class BaseFragment extends Fragment {

	protected BaseActivity context;
	AddUrlFragment.onAddUrlListener addUrlListener;

	public BaseFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		context = (BaseActivity) activity;
		addUrlListener = (onAddUrlListener) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// Animation animation = AnimationUtils.loadAnimation(BaseApp.app,
		// R.anim.scale_exit);
		// this.getView().startAnimation(animation);

		new ExplodeAnimation(getView()).animate();
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public List<DownListItem> getAppListItem() {
		return context.getApp().getListItem();
	}

	public void setAppListItem(List<DownListItem> listItem) {
		context.getApp().setListItem(listItem);
	}

	public List<ApkInfo> getAppList() {
		return context.getApp().getList();
	}

	public void setAppList(List<ApkInfo> list) {
		context.getApp().setList(list);
	}

	public long getAppDataNum() {
		return context.getApp().dataNum;
	}

	public void setAppDataNum(long num) {
		context.getApp().dataNum = num;
	}

	// public Map<String, DownloadThread> getAppDownMap() {
	// return context.getApp().downMap;
	// }

	/**
	 * 
	 * 根据文件路径安装Apk
	 * 
	 * @param context
	 * @param filePath
	 * @date 2014年12月1日
	 */
	public static void installAPK(final Context context, final String filePath) {
		if (context == null || filePath == null || filePath.length() == 0) {
			ToastTool.getRandomCentertColToast(context, "安装文件丢失 -.-,建议先'删除文件',再重新下载安装");	
			return;
		}
		ToastTool.getRandomCentertColToast(context, "准备开始安装...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(Intent.ACTION_VIEW);

				intent.setDataAndType(Uri.fromFile(new File(filePath)),
						"application/vnd.android.package-archive");
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				intent.setClassName("com.android.packageinstaller",
						"com.android.packageinstaller.PackageInstallerActivity");
				context.startActivity(intent);
			}
		}).start();

		// File apkfile = new File(filePath);
		// if (!apkfile.exists()) {
		// return;
		// }
		// Intent i = new Intent(Intent.ACTION_VIEW);
		// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
		// "application/vnd.android.package-archive");
		// context.startActivity(i);
	}

	protected void onBackPress() {
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
			return;
		}
	}

}
