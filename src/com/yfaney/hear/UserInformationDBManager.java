package com.yfaney.hear;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;
import android.widget.Toast;

public class UserInformationDBManager {
	// DB관련 상수 선언
    private static final String dbName = "UserData.db";
    private static final String tableName = "Administrator";
    private static final String tableName2 = "UserInformation";
    public static final int dbVersion = 1;
 
    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller
 
    // 부가적인 객체들
    private Context context;
 
    // 생성자
    public UserInformationDBManager(Context context) {
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
                    + "Organization text, "
                    + "EmailAddress text, "
                    + "UserID text, "
                    + "Password text, "
                    + "CreatedOn text)";
            arg0.execSQL(createSql);
            createSql = "create table " + tableName2 + " ("
                    + "id integer primary key autoincrement, "
            		+ "FirstName text, "
                    + "LastName text, "
                    + "UserID text, "
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
    public void removeAll() {
        String sql = "delete from " + tableName2 + ";";
        db.execSQL(sql);
    }
    // 단일 데이터 검색
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
    public ScreeningModel selectSingleUser(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor results = db.rawQuery(sql, null);
 
        results.moveToFirst();
 
        if (results.moveToFirst()) {
        	ScreeningModel testset = new ScreeningModel(results.getInt(0), results.getString(1), results.getString(2),
                    results.getString(3), results.getString(4));
            results.close();
            return testset;
        }
        results.close();
        return null;
    }
    /**
     * 특정 세트의 결과 데이터 선택
     * @param setID
     * @return
     */
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
    /**
     * 특정 세트의 결과 데이터 선택
     * @param setID
     * @param earSide
     * @return
     */
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
    /**
     *  데이터세트 전체 검색
     * @param userID
     * @return User Set List
     */
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
    /**
     *  사용자 데이터세트 검색
     * @param userID
     * @return User Set List
     */
    public ArrayList<ScreeningModel> selectSetUser(String userID) {
        String sql = "select * from " + tableName + " where UserID= " + userID + ";";
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
    /**
     * 결과 데이터 전체 선택
     * @return
     */
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
    /**
     * Save all set data into csv file
     * @param filePath File Path(in the External Storage)
     * @param fileName File Name. should be '.csv'
     * @return 'true' if succeed, 'false' if failed.
     */
    public boolean exportDataIntoCSV(String filePath, String fileName){
	    ArrayList<ScreeningModel> scrUserSet = selectSetAll();
	    return saveIntoCSV(scrUserSet, filePath, fileName);
	}
    /**
     * Save the particular user's set data into csv file
     * @param filePath File Path(in the External Storage)
     * @param fileName File Name. should be '.csv'
     * @param userID User ID
     * @return 'true' if succeed, 'false' if failed.
     */
    public boolean exportDataIntoCSV(String filePath, String fileName, String userID){
	    ArrayList<ScreeningModel> scrUserSet = selectSetUser(userID);
	    return saveIntoCSV(scrUserSet, filePath, fileName);
	}
    
    /**
     * Save the list into csv file. Should be used in private.
     * @param scrUserSet User set List
     * @param filePath File Path(in the External Storage)
     * @param fileName File Name. should be '.csv'
     * @return 'true' if succeed, 'false' if failed.
     */
    private boolean saveIntoCSV(ArrayList<ScreeningModel> scrUserSet, String filePath, String fileName){
    	File exportDir = new File(Environment.getExternalStorageDirectory(), filePath);
	    exportDir.mkdirs();
	    File file = new File(exportDir, fileName);
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			bw.write("\"ID\",\"First Name\",\"Last Name\",\"User ID\",\"Created On\",\"EarSide\",\"Frequency\",\"dB\"");
            bw.newLine();
			for(ScreeningModel setModel : scrUserSet){
				ArrayList<TestDataModel> scrTestSet = selectTestDatas(setModel.getID());
				for(TestDataModel dataModel : scrTestSet){
					StringBuffer oneLine = new StringBuffer();
					oneLine.append("\"");
					oneLine.append(Integer.toString(setModel.getID()));
					oneLine.append("\"");
					oneLine.append(",");
					oneLine.append("\"");
					oneLine.append(setModel.getFirstName());
					oneLine.append("\"");
					oneLine.append(",");
					oneLine.append("\"");
					oneLine.append(setModel.getLastName());
					oneLine.append("\"");
					oneLine.append(",");
					oneLine.append("\"");
					oneLine.append(setModel.getUserId());
					oneLine.append("\"");
					oneLine.append(",");
					oneLine.append("\"");
					oneLine.append(setModel.getCreatedOn());
					oneLine.append("\"");
					oneLine.append(",");
					oneLine.append("\"");
					switch(dataModel.getEarSide()){
					case 0:
						oneLine.append("Left");
						break;
					case 1:
						oneLine.append("Right");
					}
					oneLine.append("\"");
					oneLine.append(",");
					oneLine.append("\"");
					oneLine.append(Integer.toString(dataModel.getFrequency()));
					oneLine.append("\"");
					oneLine.append(",");
					oneLine.append("\"");
					oneLine.append(Integer.toString(dataModel.getDeciBel()));
					oneLine.append("\"");
					bw.write(oneLine.toString());
	                bw.newLine();
				}
			}
            bw.flush();
            bw.close();
            return true;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    }
}