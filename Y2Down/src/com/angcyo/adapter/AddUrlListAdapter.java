package com.angcyo.adapter;

import java.util.List;

import com.angcyo.db.AddUrlItem;
import com.angcyo.fragment.AddUrlFragment;
import com.angcyo.fragment.BaseFragment;
import com.angcyo.y2down.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AddUrlListAdapter extends BaseAdapter implements OnClickListener {

	private List<AddUrlItem> list;
	private BaseFragment fragment;

	public AddUrlListAdapter(BaseFragment fragment, List<AddUrlItem> list) {
		this.fragment = fragment;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setDataChanged(List<AddUrlItem> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		AddUrlItem item = (AddUrlItem) getItem(position);

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(fragment
					.getActivity());
			convertView = inflater.inflate(R.layout.layout_list_addurl, null);
			holder = new ViewHolder();

			holder.txUrl = (TextView) convertView
					.findViewById(R.id.tx_item_url);
			holder.btDel = (Button) convertView.findViewById(R.id.bt_item_del);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txUrl.setText(item.getUrl());
		holder.txUrl.setTag(position);
		holder.txUrl.setOnClickListener(this);
		holder.btDel.setOnClickListener(this);
		holder.btDel.setTag(position);

		return convertView;
	}

	static class ViewHolder {
		TextView txUrl;
		Button btDel;
	}

	@Override
	public void onClick(View v) {
		((AddUrlFragment) fragment).onClick(v);
	}

}
