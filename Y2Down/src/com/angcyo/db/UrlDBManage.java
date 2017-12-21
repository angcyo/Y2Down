package com.angcyo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UrlDBManage {
	private DBHelper helper;
	private SQLiteDatabase db;

	private String DATABASE_NAME = "url.sqlite";
	private String TABLE_NAME = "add_url";

	public UrlDBManage(Context context) {
		helper = new DBHelper(context, DATABASE_NAME, null,
				DBConstant.DATABASE_VERSION);
		db = helper.getWritableDatabase();
	}

	public long add(String url) {
		ContentValues cv = new ContentValues();
		cv.put("url", url);
		return db.insert(TABLE_NAME, null, cv);// 执行插入操作
	}

	public void delete(int id) {
		db.delete(TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });
	}

	public void close() {
		db.close();
	}

	/**
	 * 逆序查询所有结果
	 * @return
	 * @date 2014年12月1日
	 */
	public List<AddUrlItem> query() {
		List<AddUrlItem> infos = new ArrayList<AddUrlItem>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			AddUrlItem item = new AddUrlItem();
			item.setId(c.getInt(c.getColumnIndex("_id")));
			item.setUrl(c.getString(c.getColumnIndex("url")));
			infos.add(item);
		}
		c.close();
		return infos;
	}

	/**
	 * 逆序查询
	 * 
	 * @return
	 * @date 2014年12月1日
	 */
	private Cursor queryTheCursor() {
		Cursor c = db
				.rawQuery(String.format("SELECT * FROM %s order by _id DESC",
						TABLE_NAME), null);
		return c;
	}
}
