package com.angcyo.fragment;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.angcyo.adapter.DownListAdapter;
import com.angcyo.adapter.DownListItem;
import com.angcyo.db.ApkInfo;
import com.angcyo.service.DataService;
import com.angcyo.tools.DownJob;
import com.angcyo.tools.DownManage;
import com.angcyo.tools.DownloadThread;
import com.angcyo.tools.MD5;
import com.angcyo.tools.MyLog;
import com.angcyo.tools.UnitTool;
import com.angcyo.view.GenerateProcessButton;
import com.angcyo.view.MainListView;
import com.angcyo.view.RippleBackground;
import com.angcyo.y2down.BaseApp;
import com.angcyo.y2down.R;
import com.easyandroidanimations.library.BlinkAnimation;
import com.easyandroidanimations.library.FadeInAnimation;
import com.easyandroidanimations.library.RotationAnimation;
import com.easyandroidanimations.library.ShakeAnimation;
import com.example.niftynotificationlib.Configuration;
import com.example.niftynotificationlib.Effects;
import com.example.niftynotificationlib.NiftyNotificationView;

public class DownFragment extends BaseFragment {
	private MainListView listView;
	private ImageButton imgbtRefresh;
	private ImageButton imgbtMore;
	private TextView txHeadTitle;
	private RippleBackground background;
	private RelativeLayout bg;

	private ServiceGetDataReceiver dataReceiver;
	// private List<ApkInfo> list;
	private DownListAdapter listAdapter;
	// private List<DownListItem> listItem;

	public static final String TAG = "FRAG_DOWN";

	private PopupWindow mPopupWindow;

	public DownFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_down, container, false);

		// background = (RippleBackground) view.findViewById(R.id.down_bg);
		bg = (RelativeLayout) view.findViewById(R.id.layout_bg);

		listView = (MainListView) view.findViewById(R.id.list_down);
		imgbtRefresh = (ImageButton) view.findViewById(R.id.imgbt_head_refresh);
		imgbtMore = (ImageButton) view.findViewById(R.id.imgbt_head_more);
		txHeadTitle = (TextView) view.findViewById(R.id.tx_head_title);

		imgbtRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onRefresh(v);
			}
		});

		imgbtMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				onMore(v);
			}
		});

		if (getAppDataNum() == -1) {
			startGetData();
		} else if (getAppListItem().size() > 0) {
			setListDownData();

			new CountDownTimer(1800, 1800) {

				@Override
				public void onTick(long millisUntilFinished) {

				}

				@Override
				public void onFinish() {
					removeRipple();
				}
			}.start();
		}
		regGetDataReceiver();

		showRipple();
		new FadeInAnimation(view).animate();
		return view;
	}

	private void showRipple() {
		if (background == null) {
			background = (RippleBackground) LayoutInflater.from(context)
					.inflate(R.layout.layout_ripplebg, null);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			bg.addView(background, params);
			background.startRippleAnimation();
		}

	}

	private void removeRipple() {
		if (background != null) {
			background.stopRippleAnimation();
			bg.removeView(background);
			background = null;
		}
	}

	protected void onMore(View v) {

		new RotationAnimation(v).setPivot(RotationAnimation.PIVOT_CENTER)
				.animate();

		if (null != mPopupWindow) {

			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			} else
				mPopupWindow.showAsDropDown(v);
		} else {
			initPopuptWindow();
			mPopupWindow.showAsDropDown(v);
		}

	}

	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View popupWindow = layoutInflater
				.inflate(R.layout.popwindow_more, null);

		// 添加url事件
		popupWindow.findViewById(R.id.tx_item_add).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						mPopupWindow.dismiss();
						mPopupWindow = null;

						FragmentTransaction ft = context
								.getSupportFragmentManager().beginTransaction();
						ft.setCustomAnimations(R.anim.scale_btl,
								R.anim.scale_exit);

						ft.add(R.id.frag_content, new AddUrlFragment(),
								AddUrlFragment.TAG);
						ft.addToBackStack(AddUrlFragment.TAG);

						ft.commit();
					}
				});

		// 删除文件事件
		popupWindow.findViewById(R.id.tx_item_del).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mPopupWindow.dismiss();
						mPopupWindow = null;

						ProgressDialog dialog = new ProgressDialog(context);
						dialog.setIndeterminate(true);
						dialog.setTitle("请稍等...");
						dialog.setMessage("正在删除所有已下载的文件...");
						dialog.setCancelable(false);
						dialog.show();
						for (int i = 0; i < getAppListItem().size(); i++) {

							String filePath = BaseApp.app.getListItem().get(i).fileSavePath;
							if (filePath != null) {
								File file = new File(filePath);
								if (file.exists()) {
									file.delete();
									BaseApp.app.getListItem().get(i).btState = DownListItem.BT_STATE_DEFALUT;
									// BaseApp.app.getListItem().get(i).apkInfo
									// .setStrApkSize("--");
								}
							}

						}
						dialog.dismiss();
						listAdapter.notifyDataSetChanged();
						Toast.makeText(context, "已清空", 1).show();
					}
				});

		// 关于app事件
		popupWindow.findViewById(R.id.tx_item_about).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mPopupWindow.dismiss();
						mPopupWindow = null;

						FragmentTransaction ft = context
								.getSupportFragmentManager().beginTransaction();
						ft.setCustomAnimations(R.anim.scale_btl,
								R.anim.scale_exit);

						ft.add(R.id.frag_content, new AboutFragment(),
								AboutFragment.TAG);
						ft.addToBackStack(AboutFragment.TAG);

						ft.commit();
					}
				});

		// 完全退出
		popupWindow.findViewById(R.id.tx_item_exit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mPopupWindow.dismiss();
						mPopupWindow = null;

						System.exit(0);
					}
				});

		mPopupWindow = new PopupWindow(popupWindow, getResources()
				.getDimensionPixelSize(R.dimen.ds_popup_item_width), -2);

		mPopupWindow.setTouchable(true);

		mPopupWindow.setBackgroundDrawable(new ColorDrawable());
		mPopupWindow.setOutsideTouchable(true);

		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (imgbtMore != null) {
					imgbtMore.setEnabled(true);
				}

			}
		});

	}

	/**
	 * 刷新
	 * 
	 * @date 2014年11月26日
	 */
	protected void onRefresh(View v) {
		setAppDataNum(-1);
		// Animation animation = AnimationUtils.loadAnimation(context,
		// R.anim.rotate_anim);
		// v.startAnimation(animation);
		showRipple();
		new ShakeAnimation(v).setDuration(
				com.easyandroidanimations.library.Animation.DURATION_SHORT)
				.animate();
		if (CheckNet()) {
			startGetData();
		} else {
			setHeadTitle("没有可用网络", true);
		}

	}

	private Boolean CheckNet() {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	private void startGetData() {
		MyLog.i("调用开始服务");
		txHeadTitle.setText("等待服务器返回...");
		Intent service = new Intent(DataService.STR_GETDATE_SERVICE);
		service.putExtra("id", getAppDataNum());
		context.startService(service);
	}

	@Override
	public void onDestroyView() {
		if (dataReceiver != null) {
			context.unregisterReceiver(dataReceiver);
		}

		super.onDestroyView();
	}

	private void regGetDataReceiver() {
		// 动态注册广播接收器
		dataReceiver = new ServiceGetDataReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DataService.STR_GETDATE_RECEIVER);
		context.registerReceiver(dataReceiver, intentFilter);

	}

	private class ServiceGetDataReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					DataService.STR_GETDATE_RECEIVER)) {
				if (intent.getStringExtra("ret").equalsIgnoreCase("ok")) {
					setAppList(DataService.getApkInfos());//
					setAppDataNum(getAppList().size());//
					setListData();
				} else if (intent.getStringExtra("ret").equalsIgnoreCase(
						"empty")) {
					if (getAppListItem().size() == 0) {
						setHeadTitle("没有可用数据", true);
					} else {
						setListDownData();
					}

				} else {
					setHeadTitle("连接失败", true);
				}

			}

		}

	}

	private void setListData() {
		if (getAppList() == null) {
			return;
		}

		setDefaultListItem();// 转换数据

		if (listAdapter == null) {

			listAdapter = new DownListAdapter(context, getAppListItem(),
					listView, this);
			listView.setAdapter(listAdapter);
		} else {
			listAdapter.setDataChanged(getAppListItem());
		}

		setHeadTitle(String.format("共 %s 条数据.", getAppListItem().size()), true);
	}

	private void setListDownData() {

		if (listAdapter == null) {
			listAdapter = new DownListAdapter(context, getAppListItem(),
					listView, this);
			listView.setAdapter(listAdapter);
		} else {
			listAdapter.setDataChanged(getAppListItem());
		}
		setHeadTitle(String.format("共 %s 条数据.", getAppListItem().size()), true);
	}

	private void setHeadTitle(String title, boolean anim) {
		txHeadTitle.setText(title + "--" + getNetTypeName());
		removeRipple();

		if (anim) {
			new BlinkAnimation(txHeadTitle).animate();
		}

	}

	public String getNetTypeName() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo activeNetworkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo == null) {
				return "无网络";
			}
			if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return activeNetworkInfo.getTypeName();
			} else {
				String typeName = activeNetworkInfo.getSubtypeName();
				MyLog.i("网络名称:", typeName);
				if (typeName == null || typeName.length() == 0) {
					return "未知网络";
				} else if (typeName.length() > 3) {
					return activeNetworkInfo.getSubtypeName().substring(0, 4);
				} else {
					return activeNetworkInfo.getSubtypeName().substring(0,
							typeName.length());
				}

			}

		} else {
			return "无网络";
		}
	}

	private void setDefaultListItem() {
		if (getAppList() == null) {
			return;
		}

		for (int i = 0; i < getAppList().size(); i++) {
			if (i < getAppListItem().size()) {
				getAppListItem().get(i).apkInfo = getAppList().get(i);
			} else {
				DownListItem item = new DownListItem();
				item.apkInfo = getAppList().get(i);
				getAppListItem().add(item);
			}
		}

	}

	public void onButtonClick(View v) {
		int clickPostion = Integer.valueOf((String) v.getTag(R.id.bt_main));
		MyLog.i("点击的按钮 :" + String.valueOf(clickPostion));

		DownListItem item = getAppListItem().get(clickPostion);
		//String md5 = item.apkInfo.getStrMd5();// 首先判断任务是否存在

		DownManage downManage = DownManage.getDownloadManage(mHandler);//
		DownJob job = new DownJob(item.apkInfo.getStrApkUrl(),
				item.apkInfo.getStrMd5());

		switch (item.btState) {
		case DownListItem.BT_STATE_DEFALUT:
			item.btState = DownListItem.BT_STATE_WAITING;// 设置等待状态
			downManage.addDownJob(job);
			listAdapter.notifyDataSetChanged();
			break;
		case DownListItem.BT_STATE_DOWN:
			break;
		case DownListItem.BT_STATE_DOWNED:
			break;
		case DownListItem.BT_STATE_DOWNFAIL:
			item.btState = DownListItem.BT_STATE_WAITING;// 设置等待状态
			downManage.addDownJob(job);
			listAdapter.notifyDataSetChanged();
			break;
		case DownListItem.BT_STATE_DOWNLOADING:
			downManage.abortDownThread(job.strMd5);
			Toast.makeText(context, "不支持断点,任务已取消", Toast.LENGTH_LONG).show();
			break;
		case DownListItem.BT_STATE_ISEXIST:
			MyLog.i("文件位置:" + item.fileSavePath);
			installAPK(context, item.fileSavePath);
			break;

		case DownListItem.BT_STATE_WAITING:
			item.btState = DownListItem.BT_STATE_DEFALUT;// 取消等待状态
			downManage.delDownJob(job);
			listAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	private int getPosForMd5(String md5) {
		int size = getAppListItem().size();
		for (int i = 0; i < size; i++) {
			if (md5.equals(getAppListItem().get(i).apkInfo.getStrMd5())) {
				return i;
			}
		}
		return -1;
	}

	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String strMd5 = bundle.getString("md5");
			String strMsg = bundle.getString("msg");
			long speed = bundle.getLong("speed");
			int pos = getPosForMd5(strMd5);

			DownListItem item = getAppListItem().get(pos);

			super.handleMessage(msg);
			switch (msg.what) {
			case DownloadThread.INT_STATE_SUCCEED:
				MyLog.i(strMd5 + "-线程完成-开始安装" + strMsg);
				MyLog.i(strMd5 + "-线程完成-开始安装-文件路径"
						+ getAppListItem().get(pos).fileSavePath);
				// getAppDownMap().remove(strMd5);
				getAppListItem().get(pos).btState = DownListItem.BT_STATE_ISEXIST;
				// installAPK(context, strMsg);// 启动安装
				item.btProcess = 100;
				// listAdapter.notifyDataSetChanged();
				GenerateProcessButton button_ = ((GenerateProcessButton) listView
						.findViewWithTag(strMd5));
				if (button_ != null && button_ instanceof GenerateProcessButton) {
					button_.setProgress(100);
					button_.setText("安装");
				}

				break;
			case DownloadThread.INT_STATE_FAIL:
				MyLog.i(strMd5 + "-线程失败-" + strMsg);
				item.btState = DownListItem.BT_STATE_DOWNFAIL;// 下载失败
				// listAdapter.notifyDataSetChanged();
				((GenerateProcessButton) listView.findViewWithTag(strMd5))
						.setText("下载失败");
				break;
			case DownloadThread.INT_STATE_ING:
				// MyLog.i(md5 + "-线程进行中-" + msg);
				item.btProcess = Integer.valueOf(strMsg);
				item.speed = speed;
				item.btState = DownListItem.BT_STATE_DOWNLOADING;
				GenerateProcessButton button = ((GenerateProcessButton) listView
						.findViewWithTag(strMd5));

				if (button != null && button instanceof GenerateProcessButton) {
					button.setProgress(item.btProcess);
					button.setText(UnitTool.formatSize(item.speed, true) + "/s");
				}

				// listAdapter.notifyDataSetChanged();
				break;
			case DownloadThread.INT_STATE_WAIT:
				MyLog.i(strMd5 + "-线程等待-" + strMsg);
				break;
			case DownloadThread.INT_STATE_START:
				// MyLog.i(md5 + "-线程开始-" + msg);
				getAppListItem().get(pos).apkInfo.setStrApkSize(UnitTool
						.formatSize(Long.valueOf(strMsg)));
				listAdapter.notifyDataSetChanged();
				listView.invalidate();

				break;

			default:
				break;

			}
		}

	};

	private void showNiftyNotify(String msg) {
		Configuration configuration = new Configuration.Builder()
				.setIconBackgroundColor("#84478D").setTextColor("white")
				.setBackgroundColor("#84478D").build();
		NiftyNotificationView
				.build(context, msg, Effects.flip, R.id.layout_notify,
						configuration).setIcon(R.drawable.ic_launcher).show();
	}

	public void addUrl(String url) {
		MyLog.i("收到url:" + url);

		showNiftyNotify(url + "添加成功");

		DownListItem item = new DownListItem();
		ApkInfo info = new ApkInfo();

		info.setStrApkDes("本地自定义的URL,没有可用描述");
		info.setStrApkIco(null);
		info.setStrApkName(DownJob.getFileNameFromUrl(url, null));
		info.setStrApkSize("- -");
		info.setStrApkTime(UnitTool.getDateAndTime());
		info.setStrApkUrl(url);
		info.setStrApkVer("未知版本");
		info.setStrApkIsNew("0");

		info.setStrMd5(MD5.getMd5(info.toString()));

		item.apkInfo = info;

		BaseApp.app.getListItem().add(item);
		listAdapter.setDataChanged(getAppListItem());
		listView.smoothScrollToPosition(getAppListItem().size() - 1);// 滚动到末尾
	}

}
