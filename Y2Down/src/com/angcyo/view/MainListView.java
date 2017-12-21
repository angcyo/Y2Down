package com.angcyo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class MainListView extends ListView implements OnScrollListener,
		OnTouchListener {

	// list的下拉刷新,上拉加载
	// private int scrollState;// 下拉刷新,保存滚动的状态
	private int firstVisibleItem;// 第一个可见item的索引
	private boolean isFirstItem = false; // 是否是第一个item
	private float downY;// list按下的位置
	private float pullFreshStep = 50f;// 下拉多少之后可以刷新
	private boolean canFresh = false;// 是否放开可以刷新
	private int totalItemCount;// 列表item的总数量
	private int lastVisibleItem; // 最后一个可见item的索引
	private boolean isLoading = false;// 是否正在装载
	private IScrollListener iScrollListener;
	private IShowItemListener iShowItemListener;

	private float startY;// 用于判断向上或向下滚动的其实坐标
	private float scrollStep = 30;// 滚动距离,之后开始判断

	public MainListView(Context context) {
		super(context);
		initView(context);
	}

	public MainListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public MainListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		// TODO Auto-generated method stub
		this.setOnScrollListener(this);// 自己监听自己的滚动事件
		this.setOnTouchListener(this);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// this.scrollState = scrollState;
		// 滚动完成,并且是最后一个item
		if (totalItemCount == lastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE) {
			if (!isLoading && iScrollListener != null) {
				isLoading = true;
				iScrollListener.onLoad();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;

		if (iShowItemListener != null && visibleItemCount != 0) {
			iShowItemListener.onShowItem(firstVisibleItem, visibleItemCount);
		}
	}

	public void setScrollListener(IScrollListener listener) {
		this.iScrollListener = listener;
	}

	public void setShowItemListener(IShowItemListener listener) {
		this.iShowItemListener = listener;
	}

	/**
	 * 装载完成
	 */
	public void loadComplete() {
		isLoading = false;
	}

	/**
	 * 刷新完成
	 */
	public void freshComplete() {
		isFirstItem = false;
		canFresh = false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = event.getY();

			// 第一个可见的item == 0 ,说明是列表的首
			if (firstVisibleItem == 0) {
				isFirstItem = true;
				downY = event.getY();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			float y = event.getY();
			if (startY < y && Math.abs(startY - y) > scrollStep) {
				if (iScrollListener != null) {
					iScrollListener.onScrollDown();
				}

				startY = y;
			} else if (startY > y && Math.abs(startY - y) > scrollStep) {
				if (iScrollListener != null) {
					iScrollListener.onScrollUp();
				}
				startY = y;
			}
			// 下拉刷新
			if (y - downY > pullFreshStep) {
				canFresh = true;
			} else {
				canFresh = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			if (isFirstItem && canFresh && this.getFirstVisiblePosition() == 0
					&& getTop() <= 0) {
				// iScrollListener.onFresh();
			}
			break;
		default:
			break;
		}
		return false;// 是否销毁这个事件,
	}

	/**
	 * 
	 * List滚动的回调,向上 向下滚动,装载刷新数据
	 * 
	 * @author angcyo
	 *
	 */
	public interface IScrollListener {
		public void onScrollUp();

		public void onScrollDown();

		public void onLoad();

		public void onFresh();

	}

	/**
	 * List当前显示的item的回调,用于当前显示item加载数据,其他item不加载数据
	 * 
	 * @author angcyo
	 *
	 */
	public interface IShowItemListener {
		/**
		 * @param indexItem
		 *            第一个可见item的索引
		 * @param countItem
		 *            总共可见item的数量
		 */
		public void onShowItem(int indexItem, int countItem);
	}
}
