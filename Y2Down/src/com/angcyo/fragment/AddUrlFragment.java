package com.angcyo.fragment;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.angcyo.adapter.AddUrlListAdapter;
import com.angcyo.db.AddUrlItem;
import com.angcyo.db.UrlDBManage;
import com.angcyo.y2down.MainActivity;
import com.angcyo.y2down.R;

public class AddUrlFragment extends BaseFragment implements OnClickListener {

	public static final String TAG = "FRAG_ADDURL";
	private EditText etAddUrl;
	private Button btAddUrl;
	private ListView listView;
	private UrlDBManage urlDb;

	private List<AddUrlItem> list;
	private AddUrlListAdapter adapter;

	public AddUrlFragment() {
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		urlDb.close();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		urlDb = new UrlDBManage(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_addurl, container, false);
		etAddUrl = (EditText) view.findViewById(R.id.et_addurl);
		btAddUrl = (Button) view.findViewById(R.id.bt_addurl);
		listView = (ListView) view.findViewById(R.id.list_addurl);
		etAddUrl.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					String str = etAddUrl.getText().toString();
					if (str.length() == 0) {
						etAddUrl.setText("http://");
						etAddUrl.setSelection(7);
					}
				}
			}
		});

		setFouce(etAddUrl);

		btAddUrl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onAddUrl();
			}
		});

		initListView();
		return view;
	}

	private void initListView() {
		setListData();
	}

	protected void onAddUrl() {
		String url = etAddUrl.getText().toString();

		if (url.length() == 0) {
			etAddUrl.setError("不能为空哦!");
			setFouce(etAddUrl);
		} else {

			if (!isUrlExist(url)) {// 避免重复添加
				urlDb.add(url);
			}

			// 隐藏键盘
			View view = ((MainActivity) context).getCurrentFocus();
			if (view != null) {
				((InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(view.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
			}

			// 通知跳转
			goBack();
			addUrlListener.onAddUrl(url);

			// setListData();
			// listView.smoothScrollToPosition(0);
		}

	}

	private void goBack() {
		FragmentManager fm = context.getSupportFragmentManager();
		fm.popBackStack();
	}

	private boolean isUrlExist(String url) {
		for (AddUrlItem addUrlItem : list) {
			if (addUrlItem.getUrl().equals(url))
				return true;
		}
		return false;
	}

	private void getDateFromDb() {
		list = urlDb.query();
	}

	private void setListData() {
		if (list != null) {
			list.clear();
			getDateFromDb();
		} else {
			getDateFromDb();
		}

		if (adapter == null) {
			adapter = new AddUrlListAdapter(this, list);
			listView.setAdapter(adapter);
		} else {
			adapter.setDataChanged(list);
		}

	}

	private void setFouce(View v) {
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		v.requestFocusFromTouch();
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		AddUrlItem item = (AddUrlItem) adapter.getItem(pos);

		switch (v.getId()) {
		case R.id.bt_item_del:
			urlDb.delete(item.getId());
			setListData();
			break;

		case R.id.tx_item_url:
			//显示键盘
			etAddUrl.setText(item.getUrl());
			etAddUrl.setSelection(item.getUrl().length());
			((InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.showSoftInput(etAddUrl, 0);
			break;

		default:
			break;
		}
	}

	public interface onAddUrlListener {
		void onAddUrl(String url);
	}

}
