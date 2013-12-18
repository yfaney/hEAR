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
                    + "UserID text, "
                    + "Password text, "
                    + "Organization text, "
                    + "EmailAddress text, "
                    + "CreatedOn text)";
            arg0.execSQL(createSql);
            createSql = "create table " + tableName2 + " ("
                    + "id integer primary key autoincrement, "
            		+ "FirstName text, "
                    + "LastName text, "
                    + "UserID text, "
                    + "Password text, "
                    + "CreatedOn text)";
            arg0.execSQL(createSql);
            //Toast.makeText(context, "DB is opened", Toast.LENGTH_SHORT).show();
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
        }
    }
    // 데이터 추가
    public long insertUserData(UserInformationModel model) {
    	ContentValues newValues = new ContentValues();
    	newValues.put("FirstName", model.getFirstName());
    	newValues.put("LastName", model.getLastName());
    	newValues.put("UserID", model.getUserId());
    	newValues.put("Password", model.getPassWord());
    	newValues.put("CreatedOn", model.getCreatedOn());
    	return db.insert(tableName2, null, newValues);
    }
    public int insertUserData(ArrayList<UserInformationModel> modelList) {
    	int count = 0;
    	for(UserInformationModel model : modelList){
        	ContentValues newValues = new ContentValues();
        	newValues.put("FirstName", model.getFirstName());
        	newValues.put("LastName", model.getLastName());
        	newValues.put("UserID", model.getUserId());
        	newValues.put("Password", model.getPassWord());
        	newValues.put("CreatedOn", model.getCreatedOn());
        	long rowId = db.insert(tableName2, null, newValues); 
        	if(rowId > 0){
            	count++;
        	}else if(rowId == -1){
        		return -1;
        	}
    	}
    	return count;
    }
    public long insertAdminData(UserAdminInfoModel model) {
    	ContentValues newValues = new ContentValues();
    	newValues.put("FirstName", model.getFirstName());
    	newValues.put("LastName", model.getLastName());
    	newValues.put("UserID", model.getUserId());
    	newValues.put("Password", model.getPassWord());
    	newValues.put("Organization", model.getOrganization());
    	newValues.put("EmailAddress", model.getEmailAddress());
    	newValues.put("CreatedOn", model.getCreatedOn());
    	return db.insert(tableName, null, newValues);
    }
    
    // 데이터 갱신
    public void updateUserData(UserInformationModel model, int index) {
        String sql = "update " + tableName2 + " set "
        		+ "FirstName = '" + model.getFirstName()
                + "', LastName = '" + model.getLastName()
                + ", UserID = '" + model.getUserId()
                + ", Password = '" + model.getPassWord()
                + ", CreatedOn = '" + model.getCreatedOn()
                + "' where id = " + index + ";";
        db.execSQL(sql);
    }
    public void updateAdminData(UserAdminInfoModel model, int index) {
        String sql = "update " + tableName + " set "
        		+ "FirstName = '" + model.getFirstName()
                + "', LastName = '" + model.getLastName()
                + "', Organization = '" + model.getOrganization()
                + "', EmailAddress = '" + model.getEmailAddress()
                + ", UserID = '" + model.getUserId()
                + ", Password = '" + model.getPassWord()
                + ", CreatedOn = '" + model.getCreatedOn()
                + "' where id = " + index + ";";
        db.execSQL(sql);
    }

    // 데이터 삭제
    public void removeUserData(int index) {
        String sql = "delete from " + tableName2 + " where id = " + index + ";";
        db.execSQL(sql);
    }
    public void removeAll() {
        String sql = "delete from " + tableName2 + ";";
        db.execSQL(sql);
    }
    // 단일 데이터 검색
    public UserInformationModel selectUserData(int index) {
        String sql = "select * from " + tableName2 + " where id = " + index
                + ";";
        Cursor result = db.rawQuery(sql, null);
 
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
        	UserInformationModel info = new UserInformationModel(result.getInt(0) , result.getString(1),
        			result.getString(2),
        			result.getString(3),
        			result.getString(4),
        			result.getString(5));
            result.close();
            return info;
        }
        result.close();
        return null;
    }

    // 단일 데이터 검색
    public UserAdminInfoModel selectAdminData() {
        String sql = "select * from " + tableName + ";";
        Cursor result = db.rawQuery(sql, null);
 
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
        	UserAdminInfoModel info = new UserAdminInfoModel(result.getInt(0) , result.getString(1),
        			result.getString(2),
        			result.getString(3),
        			result.getString(4),
        			result.getString(5),
        			result.getString(6),
        			result.getString(7));
            result.close();
            return info;
        }
        result.close();
        return null;
    }
    
    public int isAdminDBIn(){
    	String sql = "select * from " + tableName + ";";
    	Cursor result = db.rawQuery(sql, null);
    	return result.getCount();
    }
    
    /**
     *  데이터세트 전체 검색
     * 
     * @return User List
     */
    public ArrayList<UserInformationModel> selectUserData() {
        String sql = "select * from " + tableName2 + ";";
        Cursor results = db.rawQuery(sql, null);
 
        results.moveToFirst();
        ArrayList<UserInformationModel> userSets = new ArrayList<UserInformationModel>();
 
        while (!results.isAfterLast()) {
        	UserInformationModel info = new UserInformationModel(results.getInt(0), results.getString(1),
        			results.getString(2),
        			results.getString(3),
        			results.getString(4),
        			results.getString(5));
        	userSets.add(info);
            results.moveToNext();
        }
        results.close();
        return userSets;
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
     * Save all set data into csv file
     * @param filePath File Path(in the External Storage)
     * @param fileName File Name. should be '.csv'
     * @return 'true' if succeed, 'false' if failed.
     */
    public boolean exportDataIntoCSV(String filePath, String fileName){
	    ArrayList<UserInformationModel> userSet = selectUserData();
	    return saveIntoCSV(userSet, filePath, fileName);
	}
    
    /**
     * Save the list into csv file. Should be used in private.
     * @param userSet User set List
     * @param filePath File Path(in the External Storage)
     * @param fileName File Name. should be '.csv'
     * @return 'true' if succeed, 'false' if failed.
     */
    private boolean saveIntoCSV(ArrayList<UserInformationModel> userSet, String filePath, String fileName){
    	File exportDir = new File(Environment.getExternalStorageDirectory(), filePath);
	    exportDir.mkdirs();
	    File file = new File(exportDir, fileName);
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			bw.write("\"ID\",\"First Name\",\"Last Name\",\"User ID\",\"Created On\"");
            bw.newLine();
			for(UserInformationModel setModel : userSet){
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
				bw.write(oneLine.toString());
                bw.newLine();
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