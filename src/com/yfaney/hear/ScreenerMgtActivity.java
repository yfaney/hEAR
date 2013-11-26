package com.yfaney.hear;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
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

public class ScreenerMgtActivity extends Activity implements OnClickListener, OnItemClickListener, DialogInterface.OnClickListener{
	static int selectedItem = -1;
    private TableAdapter adapter;
	private ArrayList<TableItem> list;
	ListView userList = null;
	TextView txtEmpty = null;
	
	ArrayList<UserInformationModel> userInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_mgt);
		txtEmpty = (TextView)findViewById(R.id.textEmptyView);
		userList = (ListView)findViewById(R.id.listViewUserInfo);
		list = new ArrayList<TableItem>();
		UserInformationDBManager dbManager = new UserInformationDBManager(this);
	    ArrayList<UserInformationModel> userData = dbManager.selectUserData();
	    for(int i=0 ; i < userData.size() ; i++){
	    	list.add(new TableItem(userData.get(i).getCreatedOn(), userData.get(i).getFirstName() + " " + userData.get(i).getLastName(), userData.get(i).getID(), null));
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.action_export_csv:
	        return true;
	    case R.id.action_import_csv:
	        return true;
	    case R.id.action_clear_all:
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
	        break;
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
			UserInformationDBManager dbManager = new UserInformationDBManager(this);
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
			UserInformationDBManager dbManager = new UserInformationDBManager(this);
			dbManager.removeAll();
			refreshView();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
		}
	}

	private void refreshView(){
		//startManagingCursor(cursor);
		UserInformationDBManager dbManager = new UserInformationDBManager(this);
	    ArrayList<UserInformationModel> userData = dbManager.selectUserData();
	    list.clear();
	    for(int i=0 ; i < userData.size() ; i++){
	    	list.add(new TableItem(userData.get(i).getCreatedOn(), userData.get(i).getFirstName() + " " + userData.get(i).getLastName(), userData.get(i).getID(), null));
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
