package com.yfaney.hear;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.Time;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ScreenerMgtActivity extends Activity implements OnClickListener, OnItemClickListener, DialogInterface.OnClickListener{
	public final static int SELECT_CSV_DIALOG = 1;
	static int selectedItem = -1;
    private TableAdapter adapter;
	private ArrayList<TableItem> list;
	ListView userList = null;
	TextView txtEmpty = null;
	UserInformationDBManager dbManager = null;
    ArrayList<UserInformationModel> userData = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_mgt);
		txtEmpty = (TextView)findViewById(R.id.textEmptyView);
		userList = (ListView)findViewById(R.id.listViewUserInfo);
		list = new ArrayList<TableItem>();
		dbManager = new UserInformationDBManager(this);
	    userData = dbManager.selectUserData();
	    for(int i=0 ; i < userData.size() ; i++){
	    	list.add(new TableItem(userData.get(i).getUserId(), userData.get(i).getFirstName() + " " + userData.get(i).getLastName(), userData.get(i).getID(), null));
	    }
	    if(list.isEmpty()){
			txtEmpty.setVisibility(TextView.VISIBLE);	    	
	    }
	    else{
	    	txtEmpty.setVisibility(TextView.INVISIBLE);
	    }
		adapter = new TableAdapter(this, R.layout.row, list);
		userList.setAdapter(adapter);
		userList.setOnItemClickListener(this);
		registerForContextMenu(userList);
		/* Button Event */
	    Button buttonNewScreener = (Button)findViewById(R.id.buttonNewScreener);
	    Button buttonImportUser = (Button)findViewById(R.id.buttonImportUser);
	    buttonNewScreener.setOnClickListener(this);
	    buttonImportUser.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		refreshView();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screener_mgt, menu);
		return true;
	}
	/**
	 * Menu from clicking menu button
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.action_export_csv:
	    	exportFile();
	    	return true;
	    case R.id.action_import_csv:
	    	// Call File Chooser and callback to activity result
			Intent mIntent = new Intent();
			mIntent.setType("file/csv");
			mIntent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(mIntent, "Select CSV File"), SELECT_CSV_DIALOG);
	    	return true;
	    case R.id.action_usermailto_adm:
	    	// Call File Chooser and callback to activity result
	    	sendToAdmin();
	    	return true;
	    case R.id.action_clear_all:
	    	// Delete Confirm dialogue
            Builder dlg= new AlertDialog.Builder(ScreenerMgtActivity.this);
            dlg.setTitle("Delete All Data")
            .setMessage(R.string.txt_delete_confirm)
            .setIcon(R.drawable.delete)
            .setPositiveButton(R.string.btn_Delete, this)
            .setNegativeButton(R.string.btn_Cancel, this)
            .show();
            return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	/**
	 * Menu for Long Click
	 * */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, view, menuInfo);
		if (view == userList){
			menu.setHeaderTitle(getResources().getString(R.string.menu_action));
			menu.add(0, Menu.FIRST, 0, getResources().getString(R.string.menu_delete));
//			menu.add(0, Menu.FIRST + 1, 0, getResources().getString(R.string.menu_delete));						
		}
	}
	/*	Button Event Method Start	*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonNewScreener:
			Intent intent = new Intent(this, NewScreenerActivity.class); // 평범한 Intent 생성
			intent.putExtra("ActionType", "New");
    		startActivity(intent);                                    // Activity 실행
			//mToneThread = new ToneThread(sampleRate, ToneThread.LEFT_EAR, frequency, (short)50);
			//tonePlayer.playSound();
	        break;
		case R.id.buttonImportUser:
			// Import userdata from csv file
			Intent mIntent = new Intent();
			mIntent.setType("file/csv");
			mIntent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(mIntent, "Select CSV File"), SELECT_CSV_DIALOG);
	        break;
    	}
    }
	public void onActivityResult(int requestCode, int resultCode, Intent result) 
	 {
	     if (resultCode == RESULT_OK) 
	     {
	         if (requestCode == SELECT_CSV_DIALOG) 
	         {
	             Uri data = result.getData();
	             if(data.getLastPathSegment().endsWith("csv"))
	             {
	                String csvPath = data.getPath();
		        	importFile(csvPath);
	             }
	             else 
	             {
	            	 Toast.makeText(this, "Invalid file type", Toast.LENGTH_SHORT).show();
	                 //CommonMethods.ShowMessageBox(CraneTrackActivity.this, "Invalid file type");   
	             }               
	         }
	      }
	 }

	/*	List Event Method Start	*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		// TODO Auto-generated method stub
		if(parent == userList){
			//Intent mainIntent = getIntent();
			Intent intent = new Intent(this, NewScreenerActivity.class); // 평범한 Intent 생성
			intent.putExtra("ActionType", "Edit");
			intent.putExtra("selectedItem", list.get(position).getFieldId());
    		startActivity(intent);                                    // Activity 실행
		}
	}

	/*	List Event Method End.	*/
	@Override
	public boolean onContextItemSelected(MenuItem item){
		super.onContextItemSelected(item);
		Intent mainIntent = getIntent();
		selectedItem = mainIntent.getIntExtra("selectedItem", -1);
		AdapterView.AdapterContextMenuInfo menuInfo;
		switch(item.getItemId()){
		case 1:
			menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			dbManager.removeUserData((list.get(menuInfo.position).getFieldId()));
			list.remove(menuInfo.position);
			refreshView();
			return true;
		}
		return false;
	}
	/* Dialog Click Listener */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which) {
		case DialogInterface.BUTTON_POSITIVE:
			// TODO Auto-generated method stub
			dbManager.removeAll();
			refreshView();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
		}
	}
	/**
	 * Export the user list into CSV file
	 * */
	private boolean exportFile(){
		Time now = new Time();
		now.setToNow();
		String fileName = "hEAR-Users-" + now.format("%Y-%m-%d %H:%M:%S") + ".csv";
		String filePath = "/hEARData";
		if(dbManager.exportDataIntoCSV(filePath, fileName)){
			Toast.makeText(this, "Data was exported into " + "<SD Card Path>/hEARData/" + fileName, Toast.LENGTH_SHORT).show();
			return true;
		}
		else{
			Toast.makeText(this, "Data was not saved. Please try again.", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	private boolean sendToAdmin(){
		Time now = new Time();
		now.setToNow();
		String fileName = "hEAR-Users-" + now.format("%Y-%m-%d %H:%M:%S") + ".csv";
		String filePath = "/hEARData";
		if(dbManager.exportDataIntoCSV(filePath, fileName)){
			// Send e-Mail Routine
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			//String userID = getIntent().getStringExtra("UserID");
			String adminEmail = dbManager.selectAdminData().getEmailAddress();
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { adminEmail });
			intent.putExtra(Intent.EXTRA_SUBJECT, "hEAR User Data - " + now.format("%Y-%m-%d %H:%M:%S"));
			intent.putExtra(Intent.EXTRA_TEXT, "Sent by hEAR for Android");
			File root = Environment.getExternalStorageDirectory();
			File path = new File(root, filePath);
			if(path.exists()){
				File file = new File(path, fileName);
				if (!file.exists() || !file.canRead()) {
				    Toast.makeText(this, "Attachment Error", Toast.LENGTH_SHORT).show();
				    return false;
				}
				else{
					Uri uri = Uri.parse("file://" + file);
					intent.putExtra(Intent.EXTRA_STREAM, uri);
					startActivity(Intent.createChooser(intent, "Send email..."));
					Toast.makeText(this, "Data was exported into " + "<SD Card Path>/hEARData/" + fileName, Toast.LENGTH_SHORT).show();
				    return true;
				}
			}
			else{
			    Toast.makeText(this, "Attachment Error", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			Toast.makeText(this, "Data was not saved. Please try again.", Toast.LENGTH_SHORT).show();
		    return false;
		}
	}

	/**
	 * Import the user list from CSV file
	 * */
	public void importFile(String csvPath){
       BufferedReader br = null;
       String line;
       String csvSplitBy = ",";
       String csvQuoteBy = "\"";
       try {
			br = new BufferedReader(new FileReader(csvPath));
			ArrayList<UserInformationModel> importedData = new ArrayList<UserInformationModel>();
			while((line = br.readLine()) != null){
				String[] rowData = line.split(csvSplitBy);
				deleteRegex(rowData, csvQuoteBy);
				if (rowData.length < 5){
					continue;
				}
				else{
					importedData.add(new UserInformationModel(0,
							rowData[1],
							rowData[2],
							rowData[3],
							null,
							rowData[4]));
				}
			}
			br.close();
			if(importedData.get(0).getUserId().equals("ID")){
				importedData.remove(0);
			}
			if (importedData.size() > 0){
				int count = dbManager.insertUserData(importedData);
				if(count > 0){
					Toast.makeText(this, Integer.toString(count) + " Users successfully imported.", Toast.LENGTH_SHORT).show();
					refreshView();
				}
				else{
					Toast.makeText(this, "Error occured to write DB.", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(this, "No data imported. Please check the csv file.", Toast.LENGTH_SHORT).show();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "File " + csvPath + " Not Found", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	private void deleteRegex(String[] quotedString, String regex){
		for (int i=0; i<quotedString.length ; i++){
			if(quotedString[i].startsWith(regex) && quotedString[i].endsWith(regex)){
				quotedString[i] = quotedString[i].substring(1, quotedString[i].length()-1); 
			}
		}
	}
	private void refreshView(){
		//startManagingCursor(cursor);
	    userData = dbManager.selectUserData();
	    list.clear();
	    for(int i=0 ; i < userData.size() ; i++){
	    	list.add(new TableItem(userData.get(i).getUserId(), userData.get(i).getFirstName() + " " + userData.get(i).getLastName(), userData.get(i).getID(), null));
	    }
		adapter = new TableAdapter(this, R.layout.row, list);
		userList.setAdapter(adapter);
	    if(list.isEmpty()){
			txtEmpty.setVisibility(TextView.VISIBLE);	    	
	    }
	    else{
	    	txtEmpty.setVisibility(TextView.INVISIBLE);
	    }
	}
}
