package com.angcyo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * ���ݿ�Ĺ�����,�ṩ�����ݿ����ɾ�Ĳ�
 * 
 * @author angcyo
 *
 */
public class DBManage {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManage(Context context) {
		helper = new DBHelper(context, DBConstant.DATABASE_NAME, null,
				DBConstant.DATABASE_VERSION);
		// ��ΪgetWritableDatabase�ڲ�������mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// ����Ҫȷ��context�ѳ�ʼ��,���ǿ��԰�ʵ����DBManager�Ĳ������Activity��onCreate��
		db = helper.getWritableDatabase();
	}

	public static final String SQL_INSERT = ("INSERT INTO apk_info "
			+ "(apk_name, apk_ver, apk_ico, apk_des, apk_url, apk_size, apk_time) VALUES"
			+ "( ?,       ?,       ?,       ?,       ?,       ?,        ?)");// �����sql���

	public void add(List<ApkInfo> infos) {
		db.beginTransaction(); // ��ʼ����
		try {
			for (ApkInfo info : infos) {
				db.execSQL(
						SQL_INSERT,
						new Object[] { info.getStrApkName(),
								info.getStrApkVer(), info.getStrApkIco(),
								info.getStrApkDes(), info.getStrApkUrl(),
								info.getStrApkSize(), info.getStrApkTime() });
			}
			db.setTransactionSuccessful(); // ��������ɹ����
		} finally {
			db.endTransaction(); // ��������
		}
	}

	/**
	 * 
	 * ���ز���һ�е�ID,���������,����-1
	 * 
	 * @param cv
	 * @return
	 */
	public long add(ContentValues cv) {
		return db.insert(DBConstant.TABLE_NAME, null, cv);// ִ�в������
	}

	/**
	 * ����һ������
	 * 
	 * @param info
	 * @date 2014��11��25��
	 */
	public void add(ApkInfo info) {
		db.execSQL(
				SQL_INSERT,
				new Object[] { info.getStrApkName(), info.getStrApkVer(),
						info.getStrApkIco(), info.getStrApkDes(),
						info.getStrApkUrl(), info.getStrApkSize(),
						info.getStrApkTime() });

	}

	/**
	 * ����_id ��������
	 * 
	 * @param note
	 */
	public void updateAge(ApkInfo note) {
		// ContentValues cv = new ContentValues();
		// cv.put("title", note.getTitle());
		// cv.put("content", note.getContent());
		// cv.put("time", note.getTime());
		// db.update("note", cv, "_id = ?",
		// new String[] { note.getId().toString() });
	}

	/**
	 * ����_idɾ����¼
	 * 
	 * @param note
	 */
	public void deleteOldNote(ApkInfo note) {
		// db.delete("note", "_id = ?", new String[] { note.getId().toString()
		// });
		// db.execSQL("delete from note where _id=2");//ִ��ɾ������
	}

	public void deleteAll() {
		// db.delete("note", "_id = ?", new String[] { note.getId().toString()
		// });
		db.execSQL(String.format("DELETE FROM %s ", DBConstant.TABLE_NAME));// ִ��ɾ������
	}

	/**
	 * ��ѯ���м�¼
	 * 
	 * @return
	 */
	public List<ApkInfo> query() {
		List<ApkInfo> infos = new ArrayList<ApkInfo>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			ApkInfo note = new ApkInfo();

			//�����

			infos.add(note);
		}
		c.close();
		return infos;
	}

	public List<ApkInfo> query(long limit) {
		ArrayList<ApkInfo> notes = new ArrayList<ApkInfo>();
		Cursor c = queryTheCursor(limit);
		while (c.moveToNext()) {
			ApkInfo note = new ApkInfo();


			notes.add(note);
		}
		c.close();
		return notes;
	}

	/**
	 * ����_id,��ѯ��¼
	 * 
	 * @param id
	 * @return
	 */
	public ApkInfo query(int id) {
		ApkInfo note = new ApkInfo();
		Cursor c = db.rawQuery("select * from note where _id=?",
				new String[] { String.valueOf(id) });
		if (c.moveToFirst()) {

		}
		c.close();

		return note;
	}

	public int getLines() {
		Cursor c = db.rawQuery("select * from note", null);
		int ret = c.getCount();
		c.close();
		return ret;
	}

	/**
	 * ����_id�����ѯ
	 * 
	 * @return
	 */
	private Cursor queryTheCursor() {
		Cursor c = db.rawQuery(String.format(
				"SELECT * FROM %s order by _id DESC", DBConstant.TABLE_NAME),
				null);
		return c;
	}

	/**
	 * ָ�����ص�����
	 * 
	 * @param limit
	 * @return
	 */
	private Cursor queryTheCursor(long limit) {
		Cursor c = db.rawQuery(String.format(
				"SELECT * FROM %s order by _id DESC limit ?",
				DBConstant.TABLE_NAME), new String[] { String.valueOf(limit) });
		return c;
	}

	public void closeDB() {
		db.close();
	}
}
