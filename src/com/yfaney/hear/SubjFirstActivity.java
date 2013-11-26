package com.yfaney.hear;

import java.util.ArrayList;

import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SubjFirstActivity extends Activity implements OnClickListener, OnItemClickListener{
	static int selectedItem = -1;
    private TableAdapter adapter;
	private ArrayList<TableItem> list;
	ListView userList = null;
	TextView txtEmpty = null;

	ArrayList<UserInformationModel> userInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subj_first);
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

        Button buttonSubjBack = (Button)findViewById(R.id.buttonSubjBack);
        buttonSubjBack.setOnClickListener(this);
	}
	/*	Button Event Method Start	*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonSubjBack:
			finish();
	        break;
    	}
    }

	/*	List Event Method Start	*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		// TODO Auto-generated method stub
		if(parent == userList){
			//Intent mainIntent = getIntent();
			UserInformationDBManager dbManager = new UserInformationDBManager(this);
		    UserInformationModel userData = dbManager.selectUserData(list.get(position).getFieldId());

    		SharedPreferences prefs = getSharedPreferences("UserInformation", Activity.MODE_PRIVATE);
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.putString("UserFirstName", userData.getFirstName());
    		editor.putString("UserLastName", userData.getLastName());
    		editor.putString("UserID", userData.getUserId());
    		editor.commit();
    		Intent intent = new Intent(SubjFirstActivity.this, BriefInstActivity.class); // 평범한 Intent 생성
    		//startActivity(intent);                                    // Activity 실행
    		startActivityForResult(intent, MainActivity.SUBJECTACTION);        	}
	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (MainActivity.SUBJECTACTION) : { 
	      if (resultCode == Activity.RESULT_OK) { 
	      //int tabIndex = data.getIntExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
	      // TODO Switch tabs using the index.
      		Intent resultIntent = new Intent();
      		//resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, tabIndexValue);
      		setResult(Activity.RESULT_OK, resultIntent);
      		finish();
	      } 
	      break; 
	    } 
	  } 
	}
}
