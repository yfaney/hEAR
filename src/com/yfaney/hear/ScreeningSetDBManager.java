package com.yfaney.hear;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.Time;
import android.widget.Toast;

public class ScreeningSetDBManager {
	// DB관련 상수 선언
    private static final String dbName = "ScreeningData.db";
    private static final String tableName = "ScreeningSet";
    private static final String tableName2 = "ResultData";
    public static final int dbVersion = 1;
 
    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller
 
    // 부가적인 객체들
    private Context context;
 
    // 생성자
    public ScreeningSetDBManager(Context context) {
        this.context = context;
        this.opener = new OpenHelper(context, dbName, null, dbVersion);
        db = opener.getWritableDatabase();
    }
 
    // Opener of DB and Table
    private class OpenHelper extends SQLiteOpenHelper {
 
        public OpenHelper(Context context, String name, CursorFactory factory,
                int version) {
            super(context, name, null, version);
            // TODO Auto-generated constructor stub
        }
 
        // 생성된 DB가 없을 경우에 한번만 호출됨
        @Override
        public void onCreate(SQLiteDatabase arg0) {
            // String dropSql = "drop table if exists " + tableName;
            // db.execSQL(dropSql);
 
            String createSql = "create table " + tableName + " ("
                    + "id integer primary key autoincrement, "
            		+ "FirstName text, "
                    + "LastName text, "
                    + "UserID text, "
                    + "CreatedOn text)";
            arg0.execSQL(createSql);
            createSql = "create table " + tableName2 + " ("
                    + "id integer primary key autoincrement, "
            		+ "setID integer, "
                    + "Frequency integer, "
                    + "deciBel integer, "
                    + "earSide integer)";
            arg0.execSQL(createSql);
            Toast.makeText(context, "DB is opened", Toast.LENGTH_SHORT).show();
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
        }
    }
    // 데이터 추가
    public long insertUserData(ScreeningModel model) {
    	ContentValues newValues = new ContentValues();
    	newValues.put("FirstName", model.getFirstName());
    	newValues.put("LastName", model.getLastName());
    	newValues.put("UserID", model.getUserId());
    	newValues.put("CreatedOn", model.getCreatedOn());
    	return db.insert(tableName, null, newValues);
    }
    // 데이터 추가
    public long insertData(TestDataModel testset, long setId) {
    	ContentValues newValues = new ContentValues();
    	newValues.put("setID", setId);
    	newValues.put("Frequency", testset.getFrequency());
    	newValues.put("deciBel", testset.getDeciBel());
    	newValues.put("earSide", testset.getEarSide());
    	return db.insert(tableName2, null, newValues);
    }
 
    // 데이터 갱신
    public void updateUserData(ScreeningModel model, int index) {
        String sql = "update " + tableName + " set "
        		+ "FirstName = '" + model.getFirstName()
                + "', LastName = " + model.getLastName()
                + ", UserID = '" + model.getUserId()
                + ", CreatedOn = '" + model.getCreatedOn()
                + "' where id = " + index + ";";
        db.execSQL(sql);
    }
    public void updateData(TestDataModel testset, int index) {
        String sql = "update " + tableName2 + " set "
        		+ "Frequency = '" + testset.getFrequency()
                + "', deciBel = " + testset.getDeciBel()
                + ", earSide = '" + testset.getEarSide()
                + "' where id = " + index
                + ";";
        db.execSQL(sql);
    }

    // 데이터 삭제
    public void removeUserData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }
    public void removeDatas(int setID) {
        String sql = "delete from " + tableName2 + " where setID = " + setID + ";";
        db.execSQL(sql);
    }
 
    // 데이터 검색
    public TestDataModel selectTestData(int index) {
        String sql = "select * from " + tableName2 + " where id = " + index
                + ";";
        Cursor result = db.rawQuery(sql, null);
 
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
        	TestDataModel info = new TestDataModel(result.getInt(0), result.getInt(1), result.getInt(2), result.getInt(3),
                    result.getShort(4));
            result.close();
            return info;
        }
        result.close();
        return null;
    }
    // 데이터 범위 검색
    public ArrayList<TestDataModel> selectTestDatas(int setID) {
        String sql = "select * from " + tableName2 + " where setID = " + setID
                + ";";
        Cursor results = db.rawQuery(sql, null);
 
        results.moveToFirst();
        ArrayList<TestDataModel> testsets = new ArrayList<TestDataModel>();
 
        while (!results.isAfterLast()) {
        	TestDataModel testset = new TestDataModel(results.getInt(0), results.getInt(1), results.getInt(4), results.getInt(2), results.getShort(3));
        	testsets.add(testset);
            results.moveToNext();
        }
        results.close();
        return testsets;
    }
    // 데이터 범위 검색
    public ArrayList<TestDataModel> selectTestDatas(int setID, int earSide) {
        String sql = "select * from " + tableName2 + " where setID = " + setID
                + " and earSide = " + earSide + ";";
        Cursor results = db.rawQuery(sql, null);
 
        results.moveToFirst();
        ArrayList<TestDataModel> testsets = new ArrayList<TestDataModel>();
 
        while (!results.isAfterLast()) {
        	TestDataModel testset = new TestDataModel(results.getInt(0), results.getInt(1), results.getInt(4), results.getInt(2), results.getShort(3));
        	testsets.add(testset);
            results.moveToNext();
        }
        results.close();
        return testsets;
    }
    // 데이터 전체 검색
    public ArrayList<ScreeningModel> selectSetAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
 
        results.moveToFirst();
        ArrayList<ScreeningModel> testsets = new ArrayList<ScreeningModel>();
 
        while (!results.isAfterLast()) {
        	ScreeningModel testset = new ScreeningModel(results.getInt(0), results.getString(1), results.getString(2),
                    results.getString(3), results.getString(4));
        	testsets.add(testset);
            results.moveToNext();
        }
        results.close();
        return testsets;
    }
    // 데이터 전체 검색
    public ArrayList<TestDataModel> selectDataAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
 
        results.moveToFirst();
        ArrayList<TestDataModel> testsets = new ArrayList<TestDataModel>();
 
        while (!results.isAfterLast()) {
        	TestDataModel testset = new TestDataModel(results.getInt(0), results.getInt(1), results.getInt(2), results.getInt(3),
                    results.getShort(4));
        	testsets.add(testset);
            results.moveToNext();
        }
        results.close();
        return testsets;
    }
}