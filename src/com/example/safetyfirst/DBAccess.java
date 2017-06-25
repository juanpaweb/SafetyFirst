package com.example.safetyfirst;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAccess {

	public static void createDB(SQLiteDatabase myDB) {
		myDB.execSQL("create table if not exists safetyfirst("
				+ "contactNum varchar," + "contactName varchar,"
				+ "contactPhone varchar" + ");");

		Log.i("DBAcess", "create DB");
	}

	public static void insertDB(SQLiteDatabase myDB, String contactNum,
			String contactName, String contactPhone) {
		ArrayList<String> dbData = retrieveDB(myDB, null);
		try {
			if (dbData.size() == 0) {
				ContentValues contentValues = new ContentValues();
				contentValues.put("contactNum", contactNum);
				contentValues.put("contactName", contactName);
				contentValues.put("contactPhone", contactPhone);
				myDB.insert("safetyfirst", null, contentValues);
			} else {
				myDB.execSQL("update safetyfirst set contactName='"
						+ contactName + "', contactPhone='" + contactPhone
						+ "' where contactNum='" + contactNum + "';");
			}
		} catch (Exception e) {
			Log.e("DBAccess", e.getMessage());
		}
		Log.i("DBAccess", "insert data - " + contactPhone);
	}

	public static ArrayList<String> retrieveDB(SQLiteDatabase myDB, String num) {
		ArrayList<String> dbData = new ArrayList<String>();
		Cursor rs;
		if (num == null) {
			String sql = "select * from safetyfirst";
			try {
				rs = myDB.rawQuery(sql, null);
				rs.moveToFirst();

				while (!rs.isAfterLast()) {
					String contactNum = rs.getString(rs
							.getColumnIndex("contactNum"));
					String contactName = rs.getString(rs
							.getColumnIndex("contactName"));
					String contactPhone = rs.getString(rs
							.getColumnIndex("contactPhone"));

					String data = contactNum + ";" + contactName + ";"
							+ contactPhone;

					dbData.add(data);

					Log.i("DBAccess", "retrieve - " + data);

					rs.moveToNext();
				}
			} catch (Exception e) {
				Log.e("DBAccess", e.getMessage());
			}

			return dbData;
		} else {
			// String sql = "select * from safetyfirst where trackId=" + num;
			// rs = myDB.rawQuery(sql, null);
			// rs.moveToFirst();
			//
			// while (!rs.isAfterLast()) {
			// String trackId = rs.getString(rs.getColumnIndex("trackId"));
			// String trackName = rs.getString(rs.getColumnIndex("trackName"));
			// String trackCensoredName = rs.getString(rs
			// .getColumnIndex("trackCensoredName"));
			// String primaryGenreName = rs.getString(rs
			// .getColumnIndex("primaryGenreName"));
			// String collectionName = rs.getString(rs
			// .getColumnIndex("collectionName"));
			// String collectionPrice = rs.getString(rs
			// .getColumnIndex("collectionPrice"));
			// String trackPrice = rs.getString(rs
			// .getColumnIndex("trackPrice"));
			//
			// String data = trackId + ";" + trackName + ";"
			// + trackCensoredName + ";" + primaryGenreName + ";"
			// + collectionName + ";" + collectionPrice + ";"
			// + trackPrice;
			//
			// dbData.add(data);
			//
			// Log.i("DBAccess", "retrieve - " + data);
			//
			// rs.moveToNext();
			// }
			//
			// return dbData;

			Log.i("DBAccess", "else cond");
			return null;
		}
	}
}