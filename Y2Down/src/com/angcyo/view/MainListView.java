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

	// list������ˢ��,��������
	// private int scrollState;// ����ˢ��,���������״̬
	private int firstVisibleItem;// ��һ���ɼ�item������
	private boolean isFirstItem = false; // �Ƿ��ǵ�һ��item
	private float downY;// list���µ�λ��
	private float pullFreshStep = 50f;// ��������֮�����ˢ��
	private boolean canFresh = false;// �Ƿ�ſ�����ˢ��
	private int totalItemCount;// �б�item��������
	private int lastVisibleItem; // ���һ���ɼ�item������
	private boolean isLoading = false;// �Ƿ�����װ��
	private IScrollListener iScrollListener;
	private IShowItemListener iShowItemListener;

	private float startY;// �����ж����ϻ����¹�������ʵ����
	private float scrollStep = 30;// ��������,֮��ʼ�ж�

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
		this.setOnScrollListener(this);// �Լ������Լ��Ĺ����¼�
		this.setOnTouchListener(this);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// this.scrollState = scrollState;
		// �������,���������һ��item
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
	 * װ�����
	 */
	public void loadComplete() {
		isLoading = false;
	}

	/**
	 * ˢ�����
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

			// ��һ���ɼ���item == 0 ,˵�����б����
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
			// ����ˢ��
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
		return false;// �Ƿ���������¼�,
	}

	/**
	 * 
	 * List�����Ļص�,���� ���¹���,װ��ˢ������
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
	 * List��ǰ��ʾ��item�Ļص�,���ڵ�ǰ��ʾitem��������,����item����������
	 * 
	 * @author angcyo
	 *
	 */
	public interface IShowItemListener {
		/**
		 * @param indexItem
		 *            ��һ���ɼ�item������
		 * @param countItem
		 *            �ܹ��ɼ�item������
		 */
		public void onShowItem(int indexItem, int countItem);
	}
}
