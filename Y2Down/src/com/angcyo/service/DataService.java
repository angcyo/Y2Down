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

	private long dataNum;// 接收服务器返回的数据总数
	private static List<ApkInfo> list;// 接收服务器得到的数据

	public static final String STR_GETDATE_SERVICE = "com.angcyo.y2down.getdata.service";
	public static final String STR_GETDATE_RECEIVER = "com.angcyo.y2down.getdata.receiver";

	public DataService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();

		MyLog.i("服务创建...");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		MyLog.i("数据服务开启...");

		if (intent != null) {
			startGetDataFromService(intent.getLongExtra("id", -1));
		}

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 从服务器中获取数据
	 * 
	 * @date 2014年11月26日
	 */
	private void startGetDataFromService(long index) {
		if (index == -1) {
			// 1:首先得到数据总数
			getDataNum();// 调用成功之后,自动调用第2步
		} else {
			getDataInfo(index);
		}

	}

	/**
	 * 得到服务器数据的总数
	 * 
	 * @date 2014年11月26日
	 */
	protected void getDataNum() {
		HttpUtil.get(PhpUrl.STR_GET_NUM, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				dataNum = Long.parseLong(new String(arg2));
				MyLog.i(String.format("得到数据总数 %s 条...", dataNum));
				// 2:得到数据
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
	 * 获取服务器数据
	 * 
	 * @date 2014年11月26日
	 */
	protected void getDataInfo() {
		RequestParams params = new RequestParams();
		params.put("id", dataNum);// 得到_id<=dataNum之前的数据

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

						parseJSONArray(response);// 解析数据
						serviceSuccess();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						super.onSuccess(statusCode, headers, responseString);
						if (responseString.equalsIgnoreCase("fail")) {// 服务器自定义的返回错误码
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

		params.put("id", dataNum - id);// 得到_id<=dataNum之前的数据

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

						parseJSONArray(response);// 解析数据
						serviceSuccess();
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseString) {
						super.onSuccess(statusCode, headers, responseString);
						if (responseString.equalsIgnoreCase("fail")) {// 服务器自定义的返回错误码
							serviceFail(responseString);
						}
					}

				});
	}

	/**
	 * 从JSon数组中解析数据保存到list中
	 * 
	 * @param response
	 * @date 2014年11月26日
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
		// 服务器连接失败
		Intent intent = new Intent(STR_GETDATE_RECEIVER);
		intent.putExtra("ret", "fail");
		sendBroadcast(intent);
	}

	protected void serviceFail(String arg) {
		// 服务器连接失败
		Intent intent = new Intent(STR_GETDATE_RECEIVER);
		intent.putExtra("ret", "fail");
		sendBroadcast(intent);
	}

	public static List<ApkInfo> getApkInfos() {
		return list;
	}

	protected void serviceSuccess() {
		// 从服务器返回数据成功,发送广播,数据获取成功
		MyLog.i(String.format("服务器得到数据  %s 条.", list.size()));
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
