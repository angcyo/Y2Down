package com.angcyo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.angcyo.tools.UnitTool;
import com.angcyo.y2down.BaseApp;
import com.angcyo.y2down.R;
import com.easyandroidanimations.library.FlipHorizontalAnimation;

public class AboutFragment extends BaseFragment {
	public static String TAG = "FRAG_ABOUT";

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about, container, false);
		view.findViewById(R.id.lay_about_bg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						onBackPress();
					}
				});

		Animation animation = AnimationUtils.loadAnimation(BaseApp.app,
				R.anim.tran_toptobottom);
		view.findViewById(R.id.tx_about_tip).startAnimation(animation);

		final TextView  tvVer=(TextView) view.findViewById(R.id.tx_about_ver);
		Animation animation2 = AnimationUtils.loadAnimation(BaseApp.app,
				R.anim.tran_bottomtotop);
		animation2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				new FlipHorizontalAnimation(tvVer).animate();
			}
		});
		
		tvVer.startAnimation(animation2);
		((TextView) view.findViewById(R.id.tx_about_ver)).setText(UnitTool
				.getAppVersionName(BaseApp.app));

		// view.setFocusable(true);
		// view.setFocusableInTouchMode(true);

		return view;
	}
}
