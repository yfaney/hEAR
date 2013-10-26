package com.yfaney.hear;

import java.util.ArrayList;

import com.yfaney.hear.TableAdapter;
import com.yfaney.hear.TableItem;
import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SubjListActivity extends Activity implements OnItemClickListener{
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
		list = new ArrayList<TableItem>();
		ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
	    ArrayList<ScreeningModel> scrUserData = dbManager.selectSetAll();
	    for(int i=1 ; i < scrUserData.size() ; i++){
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subj_list, menu);
		return true;
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
	/*	List Event Method Start	*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		// TODO Auto-generated method stub
		if(parent == subjList){
			Intent mainIntent = getIntent();
			mainIntent.putExtra("selectedItem", list.get(position).getFieldId());
			Intent intent = new Intent(this, SubjGraphActivity.class); // 평범한 Intent 생성
    		startActivity(intent);                                    // Activity 실행
//			Intent i = new Intent(Intent.ACTION_GET_CONTENT);   
//			i.setType("vnd.android.cursor.item/phone_v2");  
//			startActivityForResult(i, GET_PHONE_NUMBER);
			//intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		}
	}
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
/*	List Event Method End.	*/

}
