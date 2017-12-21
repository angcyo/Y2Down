package com.angcyo.tools;

import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastTool {

	/**
	 * ����ָ����С��Html�ַ�
	 * 
	 * @param source
	 * @param size
	 * @return
	 */
	public static CharSequence getHtml(String source, int size) {
		String strItem = String.format(Locale.CHINA, "<font size=%d>%s</font>",
				size, // ��С
				source); // �ַ���
		return Html.fromHtml(strItem);
	}

	/**
	 * ����ָ����ɫ��Html�ַ�
	 * 
	 * @param source
	 * @param col
	 * @return
	 */
	public static CharSequence getHtml(String source, String col) {
		String strItem = String.format(Locale.CHINA,
				"<font color=\"%s\">%s</font>", col, // ��ɫ
				source); // �ַ���
		return Html.fromHtml(strItem);
	}

	/**
	 * ����ָ����ɫ��ǰ��Html�ַ�
	 * 
	 * @param source
	 *            ��Ҫ��ɫ���ַ�
	 * @param string
	 *            ����Ҫ��ɫ���ַ�
	 * @param col
	 *            ��ɫ
	 * @return
	 */
	public static CharSequence getHtml(String source, String string, String col) {
		String strItem = String.format(Locale.CHINA,
				"<font color=\"%s\">%s</font><font>%s</font>", col, // ��ɫ
				source, string); // �ַ���
		return Html.fromHtml(strItem);
	}

	/**
	 * ������ɫ�ں��Html�ַ�
	 * 
	 * @param string
	 * @param source
	 * @param col
	 * @return
	 */
	public static CharSequence getHtml2(String string, String source, String col) {
		String strItem = String.format(Locale.CHINA,
				"<font>%s</font><font color=\"%s\">%s</font>", string, // ��ɫ
				col, source); // �ַ���
		return Html.fromHtml(strItem);
	}

	/**
	 * ����ָ����ɫ��С��ǰ��Html�ַ�
	 * 
	 * @param source
	 * @param string
	 * @param col
	 * @param fSize
	 * @return
	 */
	public static CharSequence getHtml(String source, String string,
			String col, int fSize) {
		String strItem = String.format(
				"<font size=%d color=\"%s\">%s</font><font>%s</font>", fSize, // �����С
				col, // ��ɫ
				source, string); // �ַ���

		return Html.fromHtml(strItem);
	}

	/**
	 * ��ȡһ��������ʾ��Toast,Ĭ������ʾʱ��Long
	 * 
	 * @param context
	 * @param charSequence
	 * @return
	 */
	public static Toast getCentertToast(Context context,
			CharSequence charSequence) {
		Toast toast = Toast.makeText(context, charSequence, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return toast;
	}

	/**
	 * ��ȡһ��������ʾ��Toast,����������ʾ��ʱ��
	 * 
	 * @param context
	 * @param charSequence
	 * @param lg
	 * @return
	 */
	public static Toast getCentertToast(Context context,
			CharSequence charSequence, int lg) {
		Toast toast = Toast.makeText(context, charSequence, lg);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return toast;
	}

	/**
	 * ��ȡһ������ͼƬ��toast,ʱ��long
	 * 
	 * @param context
	 * @param resId
	 * @param charSequence
	 * @return
	 */
	public static Toast getImageToast(Context context, int resId,
			CharSequence charSequence) {
		Toast toast = Toast.makeText(context, charSequence, Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(context);
		imageCodeProject.setImageResource(resId);
		toastView.addView(imageCodeProject, 0);
		toast.show();
		return toast;
	}

	/**
	 * ��ȡһ��Ĭ��ʱ��Ϊlong��toast
	 * 
	 * @param context
	 * @param charSequence
	 * @return
	 */
	public static Toast getToast(Context context, CharSequence charSequence) {
		Toast toast = Toast.makeText(context, charSequence, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}

	/**
	 * ��ȡһ��������ɫ��Toast,ʱ��Ϊlong
	 * 
	 * @param context
	 * @param charSequence
	 * @param col
	 * @return
	 */
	public static Toast getColToast(Context context, CharSequence charSequence,
			String col) {
		CharSequence sequence = ToastTool.getHtml(charSequence.toString(), "",
				col);
		Toast toast = Toast.makeText(context, sequence, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}

	/**
	 * ��ȡһ�������ɫ��Toast
	 * 
	 * @param context
	 * @param charSequence
	 * @return
	 */
	public static Toast getRandomColToast(Context context,
			CharSequence charSequence) {
		CharSequence sequence = ToastTool.getHtml(charSequence.toString(), "",
				ColorTool.getRandomColorString2());
		Toast toast = Toast.makeText(context, sequence, Toast.LENGTH_LONG);
		toast.show();
		return toast;
	}

	/**
	 * �����ɫ,���Ҿ��е�toast,Ĭ��ʱ��long
	 * 
	 * @param context
	 * @param charSequence
	 * @return
	 */
	public static Toast getRandomCentertColToast(Context context,
			CharSequence charSequence) {
		CharSequence sequence = ToastTool.getHtml(charSequence.toString(), "",
				ColorTool.getRandomColorString2());
		Toast toast = Toast.makeText(context, sequence, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return toast;
	}

	/**
	 * ��ȡһ���Զ�����ͼ��toast
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	public static Toast getCustomToast(Context context, View view) {
		// LayoutInflater inflater = getLayoutInflater();
		// View layout = inflater.inflate(R.layout.custom,
		// (ViewGroup) findViewById(R.id.llToast));
		// ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
		// image.setImageResource(R.drawable.icon);
		// TextView title = (TextView) layout.findViewById(R.id.tvTitleToast);
		// title.setText("Attention");
		// TextView text = (TextView) layout.findViewById(R.id.tvTextToast);
		// text.setText("��ȫ�Զ���Toast");

		Toast toast = new Toast(context);
		// toast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(view);
		return toast;

	}

}
