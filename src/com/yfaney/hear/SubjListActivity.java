package com.yfaney.hear;

import java.io.File;
import java.util.ArrayList;

import com.yfaney.hear.TableAdapter;
import com.yfaney.hear.TableItem;
import com.yfaney.hear.R;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SubjListActivity extends Activity implements OnClickListener, OnItemClickListener, DialogInterface.OnClickListener{
	static int selectedItem = -1;
    private TableAdapter adapter;
	private ArrayList<TableItem> list;
	ListView subjList = null;
	TextView txtEmpty = null;
	
	ArrayList<ScreeningModel> subject = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_subj_list);
		txtEmpty = (TextView)findViewById(R.id.textEmptyView);
		subjList = (ListView)findViewById(R.id.listViewSubject);
		list = new ArrayList<TableItem>();
		ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
	    ArrayList<ScreeningModel> scrUserData = dbManager.selectSetAll();
	    for(int i=0 ; i < scrUserData.size() ; i++){
	    	list.add(new TableItem(scrUserData.get(i).getCreatedOn(), scrUserData.get(i).getFirstName(), scrUserData.get(i).getID(), null));
	    }
	    if(list.isEmpty()){
			txtEmpty.setVisibility(TextView.VISIBLE);	    	
	    }
	    else{
	    	txtEmpty.setVisibility(TextView.INVISIBLE);
	    }
		adapter = new TableAdapter(this, R.layout.row, list);
		subjList.setAdapter(adapter);
		subjList.setOnItemClickListener(this);
		registerForContextMenu(subjList);
		/* Button Event */
	    Button buttonSubjExport = (Button)findViewById(R.id.buttonSubjExport);
	    Button buttonEmailToAdmin = (Button)findViewById(R.id.buttonEmailToAdmin);
	    buttonSubjExport.setOnClickListener(this);
	    buttonEmailToAdmin.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subj_list, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.action_export_csv:
			exportFile();
	        return true;
	    case R.id.action_mailto_adm:
			sendToAdmin();
	        return true;
	    case R.id.action_clear_all:
            Builder dlg= new AlertDialog.Builder(SubjListActivity.this);
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
		if (view == subjList){
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
		case R.id.buttonSubjExport:
			exportFile();
			//mToneThread = new ToneThread(sampleRate, ToneThread.LEFT_EAR, frequency, (short)50);
			//tonePlayer.playSound();
	        break;
		case R.id.buttonEmailToAdmin:
			sendToAdmin();
	        break;
    	}
    }
	/*	List Event Method Start	*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		// TODO Auto-generated method stub
		if(parent == subjList){
			//Intent mainIntent = getIntent();
			Intent intent = new Intent(this, SubjGraphActivity.class); // 평범한 Intent 생성
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
			ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
			dbManager.removeUserData((list.get(menuInfo.position).getFieldId()));
			dbManager.removeDatas((list.get(menuInfo.position).getFieldId()));
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
			ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
			dbManager.removeAll();
			refreshView();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
		}
	}
	private void refreshView(){
		//startManagingCursor(cursor);
		ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
	    ArrayList<ScreeningModel> scrUserData = dbManager.selectSetAll();
	    list.clear();
	    for(int i=1 ; i < scrUserData.size() ; i++){
	    	list.add(new TableItem(scrUserData.get(i).getCreatedOn(), scrUserData.get(i).getFirstName(), scrUserData.get(i).getID(), null));
	    }
		adapter = new TableAdapter(this, R.layout.row, list);
		subjList.setAdapter(adapter);
	    if(list.isEmpty()){
			txtEmpty.setVisibility(TextView.VISIBLE);	    	
	    }
	    else{
	    	txtEmpty.setVisibility(TextView.INVISIBLE);
	    }
	}
	private boolean exportFile(){
		Time now = new Time();
		now.setToNow();
		String fileName = "hEAR-" + now.format("%Y-%m-%d %H:%M:%S") + ".csv";
		String filePath = "/hEARData";
		ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
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
		String fileName = "hEAR-" + now.format("%Y-%m-%d %H:%M:%S") + ".csv";
		String filePath = "/hEARData";
		ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
		if(dbManager.exportDataIntoCSV(filePath, fileName)){
			// Send e-Mail Routine
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			//String userID = getIntent().getStringExtra("UserID");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
			intent.putExtra(Intent.EXTRA_SUBJECT, "hEAR Test Data - " + now.format("%Y-%m-%d %H:%M:%S"));
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
}
