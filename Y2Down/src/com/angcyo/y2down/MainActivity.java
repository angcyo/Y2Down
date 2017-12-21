package com.angcyo.y2down;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.angcyo.fragment.DownFragment;
import com.angcyo.fragment.AddUrlFragment.onAddUrlListener;

public class MainActivity extends BaseActivity  implements onAddUrlListener{

	private FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fm = getSupportFragmentManager();

		showDownFragment();
	}

	protected void showDownFragment() {
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.frag_content, new DownFragment(), DownFragment.TAG);
		overridePendingTransition(R.anim.scale_btl, 0);
		ft.commit();
	}

	@Override
	public void onAddUrl(String url) {
		DownFragment fragment = (DownFragment) fm.findFragmentByTag(DownFragment.TAG);
		if (fragment!=null) {
			fragment.addUrl(url);
		}
		
	}
}
