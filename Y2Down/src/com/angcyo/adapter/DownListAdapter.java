package com.angcyo.adapter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

import libcore.io.DiskLruCache;
import libcore.io.DiskLruCache.Snapshot;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angcyo.fragment.DownFragment;
import com.angcyo.tools.DownJob;
import com.angcyo.tools.DownloadThread;
import com.angcyo.tools.MD5;
import com.angcyo.tools.UnitTool;
import com.angcyo.view.GenerateProcessButton;
import com.angcyo.view.MainListView;
import com.angcyo.y2down.R;
import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams")
public class DownListAdapter extends BaseAdapter {

	private List<DownListItem> list;
	private Context context;
	private MainListView listView;
	private DownFragment fragment;

	/**
	 * ��¼�����������ػ�ȴ����ص�����
	 */
	// private Set<BitmapWorkerTask> taskCollection;
	private Set<LoadBitmapTask> taskCollection;

	/**
	 * ͼƬ���漼���ĺ����࣬���ڻ����������غõ�ͼƬ���ڳ����ڴ�ﵽ�趨ֵʱ�Ὣ�������ʹ�õ�ͼƬ�Ƴ�����
	 */
	// private LruCache<String, Bitmap> mMemoryCache;

	/**
	 * ͼƬӲ�̻�������ࡣ
	 */
	private DiskLruCache mDiskLruCache;

	@SuppressLint("NewApi")
	public DownListAdapter(Context context, List<DownListItem> list,
			MainListView listView, DownFragment fragment) {
		this.list = list;
		this.context = context;
		this.listView = listView;
		this.fragment = fragment;

		// taskCollection = new HashSet<BitmapWorkerTask>();// �������е���������
		// taskCollection = new HashSet<LoadBitmapTask>();// �������е���������
		// ��ȡӦ�ó����������ڴ�
		// int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// MyLog.i("����ڴ�:" + maxMemory);
		//
		// int cacheSize = maxMemory / 8;
		// // ����ͼƬ�����СΪ�����������ڴ��1/8
		// mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
		// @Override
		// protected int sizeOf(String key, Bitmap bitmap) {
		// MyLog.i("ͼƬ��С:" + bitmap.getByteCount());
		//
		// return bitmap.getByteCount();// Ҫ��Ҫ����1024��?
		// }
		// };
		// try {
		// // ��ȡͼƬ����·��
		// File cacheDir = getDiskCacheDir(context, "bitmap");
		// if (!cacheDir.exists()) {
		// cacheDir.mkdirs();
		// }
		// // ����DiskLruCacheʵ������ʼ����������,���һ����������Ĵ�С10MB
		// mDiskLruCache = DiskLruCache.open(cacheDir,
		// UnitTool.getAppVersionCode(context), 1, 10 * 1024 * 1024);
		// MyLog.i("���̻���·��:" + mDiskLruCache.getDirectory().toString());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * ���ݴ����uniqueName��ȡӲ�̻����·����ַ��
	 */
	@SuppressLint("NewApi")
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
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

	public void setDataChanged(List<DownListItem> list) {
		if (this.list != null) {
			this.list = list;
			this.notifyDataSetChanged();

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DownListItem info = (DownListItem) getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_list_item, null);
			holder.apkIco = (RoundedImageView) convertView
					.findViewById(R.id.img_apk_ico);
			holder.apkName = (TextView) convertView
					.findViewById(R.id.tx_apk_name);
			holder.apkDes = (TextView) convertView
					.findViewById(R.id.tx_apk_des);
			holder.apkSize = (TextView) convertView
					.findViewById(R.id.tx_apk_size);
			holder.apkTime = (TextView) convertView
					.findViewById(R.id.tx_apk_time);
			holder.apkVer = (TextView) convertView
					.findViewById(R.id.tx_apk_ver);
			holder.btMain = (GenerateProcessButton) convertView
					.findViewById(R.id.bt_main);
			holder.itemLayout = (LinearLayout) convertView
					.findViewById(R.id.lay_item);
			holder.exLayout = (LinearLayout) convertView
					.findViewById(R.id.lay_item_ex);
			holder.apkBvTextView = (TextView) convertView.findViewById(R.id.id_apk_bv);
			holder.position = position;

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.apkIco.setTag(MD5.getMd5(info.apkInfo.getStrApkIco()));// ����·����Ϊmd5��Ψһ��ʶ
		holder.apkName.setText(info.apkInfo.getStrApkName());
		holder.apkDes.setText(info.apkInfo.getStrApkDes().replaceAll("<br>",
				"\n"));
		holder.apkSize
				.setText(UnitTool.formatSize(info.apkInfo.getStrApkSize()));// ��С
		holder.apkTime.setText(info.apkInfo.getStrApkTime());
		holder.apkVer.setText(info.apkInfo.getStrApkVer());
		holder.btMain.setOnClickListener(new onViewClick());
		holder.btMain.setProgress(info.btProcess);
		String filePath = DownloadThread.getFullFilePath(DownloadThread
				.getFileNameFromUrl(info.apkInfo.getStrApkUrl(),
						DownloadThread.getDefaultFileName()));
		File file = new File(filePath);
		if (file.exists()) {
			info.btState = DownListItem.BT_STATE_ISEXIST;
			holder.apkSize.setText(UnitTool.formatSize(file.length()));
			info.fileSavePath = filePath;
		}

		setProcessButtonText(holder.btMain, info.btState, info.speed);
		holder.itemLayout.setOnClickListener(new onViewClick());
		holder.btMain.setTag(R.id.bt_main, String.valueOf(position));// ����item��λ��
		holder.btMain.setTag(info.apkInfo.getStrMd5());
		holder.itemLayout.setTag(String.valueOf(position));// ����item��λ��
		holder.exLayout.setTag(info.apkInfo.getStrMd5() + position);
		// holder.exLayout.setTag(info.apkInfo.getStrMd5());// Ψһ��ʶ��ͼ,����������ʾ����.
		if (info.showExLayout) {
			holder.exLayout.setVisibility(View.VISIBLE);
		} else {
			holder.exLayout.setVisibility(View.GONE);
		}

//		BadgeView badgeView = new BadgeView(context, holder.apkIco);
//		badgeView.setText("n");
//		badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		if (info.apkInfo.getStrApkIsNew().equalsIgnoreCase("1")) {
			holder.apkBvTextView.setVisibility(View.VISIBLE);
		} else {
			holder.apkBvTextView.setVisibility(View.INVISIBLE);
		}

		if (info.apkInfo.getStrApkIco() == null) {
			holder.apkIco.setImageResource(R.drawable.ico48);
		} else {
			Picasso picasso = Picasso.with(context);
			picasso.setIndicatorsEnabled(true);
			String strUrl = Uri.encode(info.apkInfo.getStrApkIco(), "utf-8")
					.replaceAll("%3A", ":").replaceAll("%2F", "/");

			picasso.load(strUrl).placeholder(R.drawable.ico48)
					.resizeDimen(R.dimen.ds_ico_width, R.dimen.ds_ico_height)
					.error(R.drawable.ico48).into(holder.apkIco);
		}

		// ��ʼ��...�����ļ���·��...Ϊ�˽��BUG,���������޸�
		info.fileSavePath = DownJob.getFullFilePath(DownJob.getFileNameFromUrl(
				info.apkInfo.getStrApkUrl(), DownJob.getDefaultFileName()));
		return convertView;
	}

	static class ViewHolder {
		RoundedImageView apkIco;
		TextView apkName;
		TextView apkVer;
		TextView apkSize;
		TextView apkDes;
		TextView apkTime;
		GenerateProcessButton btMain;
		LinearLayout exLayout;
		LinearLayout itemLayout;
		/**
		 * ������ͼ
		 */
		TextView apkBvTextView;
		int position;
	}

	private void setProcessButtonText(GenerateProcessButton button, int state,
			long speed) {
		String title;

		switch (state) {
		case DownListItem.BT_STATE_DEFALUT:
			title = "����";
			break;
		case DownListItem.BT_STATE_DOWN:
			title = "����";
			break;
		case DownListItem.BT_STATE_DOWNED:
			title = "���";
			break;
		case DownListItem.BT_STATE_DOWNFAIL:
			title = "����ʧ��";
			break;
		case DownListItem.BT_STATE_DOWNLOADING:
			title = UnitTool.formatSize(speed, true) + "/s";
			break;
		case DownListItem.BT_STATE_ISEXIST:
			title = "��װ";
			break;
		case DownListItem.BT_STATE_WAITING:
			title = "�ȴ���...";
			break;
		default:
			title = "^_^";
			break;
		}

		button.setText(title);

	}

	private class onViewClick implements View.OnClickListener {
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.lay_item:
				int clickPostion = Integer.valueOf((String) v.getTag());
				list.get(clickPostion).showExLayout = !list.get(clickPostion).showExLayout;

				View exLayout = listView
						.findViewWithTag(list.get(clickPostion).apkInfo
								.getStrMd5() + clickPostion);
				int visibility = exLayout.getVisibility();

				if (visibility == View.GONE) {
					exLayout.setVisibility(View.VISIBLE);
				} else {
					exLayout.setVisibility(View.GONE);
				}
				// notifyDataSetChanged();
				break;
			case R.id.bt_main:

				fragment.onButtonClick(v);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * ��LruCache�л�ȡһ��ͼƬ����������ھͷ���null��
	 * 
	 * @param key
	 *            LruCache�ļ������ﴫ��ͼƬ��URL��ַ��
	 * @return ��Ӧ�������Bitmap���󣬻���null��
	 */
	// public Bitmap getBitmapFromMemoryCache(String key) {
	// return mMemoryCache.get(key);
	// }

	public void loadBitmaps(ImageView imageView, String imageUrl,
			String imgViewTag) {

		LoadBitmapTask task = new LoadBitmapTask();
		taskCollection.add(task);
		task.execute(imageUrl, imgViewTag);

		// try {
		// Bitmap bitmap = getBitmapFromMemoryCache(imgViewTag);
		// if (bitmap == null) {
		// BitmapWorkerTask task = new BitmapWorkerTask();
		// taskCollection.add(task);
		// task.execute(imageUrl, imgViewTag);
		// } else {
		// if (imageView != null && bitmap != null) {
		// imageView.setImageBitmap(bitmap);
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * ȡ�������������ػ�ȴ����ص�����
	 */
	// public void cancelAllTasks() {
	// if (taskCollection != null) {
	// for (BitmapWorkerTask task : taskCollection) {
	// task.cancel(false);
	// }
	// }
	// }

	class LoadBitmapTask extends AsyncTask<String, Void, Bitmap> {
		private String imageUrl;
		private String imageTag;

		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			imageTag = params[1];

			Snapshot snapShot = null;
			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			try {
				snapShot = mDiskLruCache.get(imageTag);
				if (snapShot == null) {
					DiskLruCache.Editor editor = mDiskLruCache.edit(imageTag);
					if (editor != null) {
						OutputStream outputStream = editor.newOutputStream(0);// д�뻺��
						if (downloadUrlToStream(imageUrl, outputStream)) {
							editor.commit();
						} else {
							editor.abort();
						}
					}
					snapShot = mDiskLruCache.get(imageTag);
				}
				if (snapShot != null) {
					fileInputStream = (FileInputStream) snapShot
							.getInputStream(0);// ��������
					fileDescriptor = fileInputStream.getFD();
				}
				// ���������ݽ�����Bitmap����
				Bitmap bitmap = null;
				if (fileDescriptor != null) {
					bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				}

				if (bitmap != null) {
					return bitmap;
				} else {
					return BitmapFactory.decodeResource(context.getResources(),
							R.drawable.ico48);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			// ����Tag�ҵ���Ӧ��ImageView�ؼ��������غõ�ͼƬ��ʾ������
			ImageView imageView = (ImageView) listView
					.findViewWithTag(imageTag);
			if (imageView != null && result != null) {
				imageView.setImageBitmap(result);
			}
			taskCollection.remove(this);
		}

		private boolean downloadUrlToStream(String urlString,
				OutputStream outputStream) {
			HttpURLConnection urlConnection = null;
			BufferedOutputStream out = null;
			BufferedInputStream in = null;
			try {
				// ���������ӽ��д���ո�
				String strUrl = Uri.encode(urlString, "utf-8")
						.replaceAll("%3A", ":").replaceAll("%2F", "/");
				final URL url = new URL(strUrl);
				urlConnection = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(urlConnection.getInputStream());
				out = new BufferedOutputStream(outputStream);
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				return true;
			} catch (final IOException e) {
				e.printStackTrace();
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
			return false;
		}

	}

	/**
	 * �첽����ͼƬ������
	 * 
	 * @author guolin
	 */
	// class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	//
	// /**
	// * ͼƬ��URL��ַ
	// */
	// private String imageUrl;
	// private String imageTag;
	//
	// @Override
	// protected Bitmap doInBackground(String... params) {
	// imageUrl = params[0];
	// imageTag = params[1];
	// FileDescriptor fileDescriptor = null;
	// FileInputStream fileInputStream = null;
	// Snapshot snapShot = null;
	// try {
	// // ����ͼƬURL��Ӧ��key
	// final String key = imageTag;// hashKeyForDisk(imageUrl);
	// // ����key��Ӧ�Ļ���
	// snapShot = mDiskLruCache.get(key);
	// if (snapShot == null) {
	// // ���û���ҵ���Ӧ�Ļ��棬��׼�����������������ݣ���д�뻺��
	// DiskLruCache.Editor editor = mDiskLruCache.edit(key);
	// if (editor != null) {
	// OutputStream outputStream = editor.newOutputStream(0);
	// if (downloadUrlToStream(imageUrl, outputStream)) {
	// editor.commit();
	// } else {
	// editor.abort();
	// }
	// }
	// // ���汻д����ٴβ���key��Ӧ�Ļ���
	// snapShot = mDiskLruCache.get(key);
	// }
	// if (snapShot != null) {
	// fileInputStream = (FileInputStream) snapShot
	// .getInputStream(0);
	// fileDescriptor = fileInputStream.getFD();
	// }
	// // ���������ݽ�����Bitmap����
	// Bitmap bitmap = null;
	// if (fileDescriptor != null) {
	// bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
	// }
	// // if (bitmap != null) {
	// // // ��Bitmap������ӵ��ڴ滺�浱��
	// // addBitmapToMemoryCache(params[1], bitmap);
	// // }
	// return bitmap;
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// if (fileDescriptor == null && fileInputStream != null) {
	// try {
	// fileInputStream.close();
	// } catch (IOException e) {
	// }
	// }
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Bitmap bitmap) {
	// super.onPostExecute(bitmap);
	// // ����Tag�ҵ���Ӧ��ImageView�ؼ��������غõ�ͼƬ��ʾ������
	// ImageView imageView = (ImageView) listView
	// .findViewWithTag(imageTag);
	// if (imageView != null && bitmap != null) {
	// imageView.setImageBitmap(bitmap);
	// }
	// taskCollection.remove(this);
	// }
	//
	// /**
	// * ����HTTP���󣬲���ȡBitmap����
	// *
	// * @param imageUrl
	// * ͼƬ��URL��ַ
	// * @return �������Bitmap����
	// */
	// private boolean downloadUrlToStream(String urlString,
	// OutputStream outputStream) {
	// HttpURLConnection urlConnection = null;
	// BufferedOutputStream out = null;
	// BufferedInputStream in = null;
	// try {
	// // ���������ӽ��д���ո�
	// String strUrl = Uri.encode(urlString, "utf-8")
	// .replaceAll("%3A", ":").replaceAll("%2F", "/");
	// final URL url = new URL(strUrl);
	// urlConnection = (HttpURLConnection) url.openConnection();
	// in = new BufferedInputStream(urlConnection.getInputStream());
	// out = new BufferedOutputStream(outputStream);
	// int b;
	// while ((b = in.read()) != -1) {
	// out.write(b);
	// }
	// return true;
	// } catch (final IOException e) {
	// e.printStackTrace();
	// } finally {
	// if (urlConnection != null) {
	// urlConnection.disconnect();
	// }
	// try {
	// if (out != null) {
	// out.close();
	// }
	// if (in != null) {
	// in.close();
	// }
	// } catch (final IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return false;
	// }
	// }

	/**
	 * ��һ��ͼƬ�洢��LruCache�С�
	 * 
	 * @param key
	 *            LruCache�ļ������ﴫ��ͼƬ��URL��ַ��
	 * @param bitmap
	 *            LruCache�ļ������ﴫ������������ص�Bitmap����
	 */
	// public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	// if (getBitmapFromMemoryCache(key) == null) {
	// mMemoryCache.put(key, bitmap);
	// }
	// }

	// /**
	// * ��byteת����md5ֵ
	// *
	// * @param bytes
	// * @return
	// */
	// public static String bytesToHexString(byte[] bytes) {
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < bytes.length; i++) {
	// String hex = Integer.toHexString(0xFF & bytes[i]);
	// if (hex.length() == 1) {
	// sb.append('0');
	// }
	// sb.append(hex);
	// }
	// return sb.toString();
	// }
	//
	// /**
	// * ʹ��MD5�㷨�Դ����key���м��ܲ����ء�
	// */
	// public static String hashKeyForDisk(String key) {
	// String cacheKey;
	// try {
	// final MessageDigest mDigest = MessageDigest.getInstance("MD5");
	// mDigest.update(key.getBytes());
	// cacheKey = bytesToHexString(mDigest.digest());
	// } catch (NoSuchAlgorithmException e) {
	// cacheKey = String.valueOf(key.hashCode());
	// }
	// return cacheKey;
	// }

}
