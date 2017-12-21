package com.angcyo.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.IBinder;

import com.angcyo.db.ApkInfo;
import com.angcyo.tools.HttpUtil;
import com.angcyo.tools.MD5;
import com.angcyo.tools.MyLog;
import com.angcyo.tools.PhpUrl;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DataService extends BaseService {

	private long dataNum;// ���շ��������ص���������
	private static List<ApkInfo> list;// ���շ������õ�������

	public static final String STR_GETDATE_SERVICE = "com.angcyo.y2down.getdata.service";
	public static final String STR_GETDATE_RECEIVER = "com.angcyo.y2down.getdata.receiver";

	public DataService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		MyLog.i("���񴴽�...");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		MyLog.i("���ݷ�����...");

		if (intent != null) {
			startGetDataFromService(intent.getLongExtra("id", -1));
		}

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * �ӷ������л�ȡ����
	 * 
	 * @date 2014��11��26��
	 */
	private void startGetDataFromService(long index) {
		if (index == -1) {
			// 1:���ȵõ���������
			getDataNum();// ���óɹ�֮��,�Զ����õ�2��
		} else {
			getDataInfo(index);
		}

	}

	/**
	 * �õ����������ݵ�����
	 * 
	 * @date 2014��11��26��
	 */
	protected void getDataNum() {
		HttpUtil.get(PhpUrl.STR_GET_NUM, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dataNum = Long.parseLong(new String(arg2));
				MyLog.i(String.format("�õ��������� %s ��...", dataNum));
				// 2:�õ�����
				getDataInfo();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				serviceFail(arg2);
			}
		});
	}

	/**
	 * ��ȡ����������
	 * 
	 * @date 2014��11��26��
	 */
	protected void getDataInfo() {
		RequestParams params = new RequestParams();
		params.put("id", dataNum);// �õ�_id<=dataNum֮ǰ������

		HttpUtil.get(PhpUrl.STR_GET_INFO, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
						serviceFail(responseString);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						super.onSuccess(statusCode, headers, response);

						parseJSONArray(response);// ��������
						serviceSuccess();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						super.onSuccess(statusCode, headers, responseString);
						if (responseString.equalsIgnoreCase("fail")) {// �������Զ���ķ��ش�����
							serviceFail(responseString);
						}
					}

				});
	}

	protected void getDataInfo(long id) {
		RequestParams params = new RequestParams();
		if (dataNum - id <= 0) {
			Intent intent = new Intent(STR_GETDATE_RECEIVER);
			intent.putExtra("ret", "empty");
			sendBroadcast(intent);
			return;
		}

		params.put("id", dataNum - id);// �õ�_id<=dataNum֮ǰ������

		HttpUtil.get(PhpUrl.STR_GET_INFO, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseString, Throwable throwable) {
						super.onFailure(statusCode, headers, responseString,
								throwable);
						serviceFail(responseString);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONArray response) {
						super.onSuccess(statusCode, headers, response);

						parseJSONArray(response);// ��������
						serviceSuccess();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						super.onSuccess(statusCode, headers, responseString);
						if (responseString.equalsIgnoreCase("fail")) {// �������Զ���ķ��ش�����
							serviceFail(responseString);
						}
					}

				});
	}

	/**
	 * ��JSon�����н������ݱ��浽list��
	 * 
	 * @param response
	 * @date 2014��11��26��
	 */
	protected void parseJSONArray(JSONArray response) {
		if (list == null) {
			list = new ArrayList<ApkInfo>();
		}
		else {
			list.clear();
		}

		int leng = response.length();
		JSONObject json;
		for (int i = 0; i < leng; i++) {
			ApkInfo info = new ApkInfo();
			try {
				json = response.getJSONObject(i);
				info.setStrApkDes(json.getString(ApkInfo.APK_DES));
				info.setStrApkIco(json.getString(ApkInfo.APK_ICO));
				info.setStrApkName(json.getString(ApkInfo.APK_NAME));
				info.setStrApkSize(json.getString(ApkInfo.APK_SIZE));
				info.setStrApkTime(json.getString(ApkInfo.APK_TIME));
				info.setStrApkUrl(json.getString(ApkInfo.APK_URL));
				info.setStrApkVer(json.getString(ApkInfo.APK_VER));
				info.setStrApkIsNew(json.getString(ApkInfo.APK_ISNEW));

				info.setStrMd5(MD5.getMd5(info.toString()));
				// MyLog.i(String.format("ApkName = %s", info.getStrApkName()));

				list.add(info);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	protected void serviceFail(byte[] arg) {
		// ����������ʧ��
		Intent intent = new Intent(STR_GETDATE_RECEIVER);
		intent.putExtra("ret", "fail");
		sendBroadcast(intent);
	}

	protected void serviceFail(String arg) {
		// ����������ʧ��
		Intent intent = new Intent(STR_GETDATE_RECEIVER);
		intent.putExtra("ret", "fail");
		sendBroadcast(intent);
	}

	public static List<ApkInfo> getApkInfos() {
		return list;
	}

	protected void serviceSuccess() {
		// �ӷ������������ݳɹ�,���͹㲥,���ݻ�ȡ�ɹ�
		MyLog.i(String.format("�������õ�����  %s ��.", list.size()));
		Intent intent = new Intent(STR_GETDATE_RECEIVER);
		intent.putExtra("ret", "ok");
		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
