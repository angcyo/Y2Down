package com.angcyo.y2down;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.angcyo.tools.MyLog;
import com.example.sweetdialoglib.SweetAlertDialog;
import com.example.sweetdialoglib.SweetAlertDialog.OnSweetClickListener;

public class BaseActivity extends FragmentActivity {

	public BaseActivity() {
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.onAttachFragment(fragment);
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			getSupportFragmentManager().popBackStack();
			return;
		}

		SweetAlertDialog dialog = new SweetAlertDialog(this,
				SweetAlertDialog.WARNING_TYPE);

		dialog.setTitleText("确实退出吗 ^_^").setContentText("好好想想...")
				.setCancelText("无..视").setConfirmText("再想想")
				.setCancelClickListener(new OnSweetClickListener() {

					@Override
					public void onClick(final SweetAlertDialog arg0) {
						arg0.setTitleText("正在退出...");
						arg0.setCancelable(false);
						arg0.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
						arg0.show();
						arg0.showContentText(false);
						arg0.showCancelButton(false);

						new CountDownTimer(800, 800) {

							@Override
							public void onTick(long millisUntilFinished) {
							}

							@Override
							public void onFinish() {
								arg0.dismiss();
								BaseActivity.super.onBackPressed();
							}
						}.start();
					}
				}).show();

		MyLog.i("onBackPressed");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyLog.i("onDestroy");
		// getApp().cleanAllData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	public BaseApp getApp() {
		return (BaseApp) getApplication();
	}

}
