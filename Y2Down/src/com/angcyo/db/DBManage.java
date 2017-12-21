package com.angcyo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * 数据库的管理类,提供对数据库的增删改查
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
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	public static final String SQL_INSERT = ("INSERT INTO apk_info "
			+ "(apk_name, apk_ver, apk_ico, apk_des, apk_url, apk_size, apk_time) VALUES"
			+ "( ?,       ?,       ?,       ?,       ?,       ?,        ?)");// 插入的sql语句

	public void add(List<ApkInfo> infos) {
		db.beginTransaction(); // 开始事务
		try {
			for (ApkInfo info : infos) {
				db.execSQL(
						SQL_INSERT,
						new Object[] { info.getStrApkName(),
								info.getStrApkVer(), info.getStrApkIco(),
								info.getStrApkDes(), info.getStrApkUrl(),
								info.getStrApkSize(), info.getStrApkTime() });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * 
	 * 返回插入一行的ID,如果错误发生,返回-1
	 * 
	 * @param cv
	 * @return
	 */
	public long add(ContentValues cv) {
		return db.insert(DBConstant.TABLE_NAME, null, cv);// 执行插入操作
	}

	/**
	 * 插入一条数据
	 * 
	 * @param info
	 * @date 2014年11月25日
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
	 * 根据_id 更新数据
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
	 * 根据_id删除记录
	 * 
	 * @param note
	 */
	public void deleteOldNote(ApkInfo note) {
		// db.delete("note", "_id = ?", new String[] { note.getId().toString()
		// });
		// db.execSQL("delete from note where _id=2");//执行删除操作
	}

	public void deleteAll() {
		// db.delete("note", "_id = ?", new String[] { note.getId().toString()
		// });
		db.execSQL(String.format("DELETE FROM %s ", DBConstant.TABLE_NAME));// 执行删除操作
	}

	/**
	 * 查询所有记录
	 * 
	 * @return
	 */
	public List<ApkInfo> query() {
		List<ApkInfo> infos = new ArrayList<ApkInfo>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			ApkInfo note = new ApkInfo();

			//待添加

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
	 * 根据_id,查询记录
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
	 * 按照_id逆序查询
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
	 * 指定返回的条数
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
