package com.yfaney.hear;

import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewScreenerActivity extends Activity implements OnClickListener {
	int dbId = -1;
	Intent intent = null;   // 값을 받기 위한 Intent 생성

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_new_screener);
		intent = getIntent();
        Button buttonNewUserReg = (Button)findViewById(R.id.buttonNewUserReg);
		if(intent.getStringExtra("ActionType").equals("Edit")){
			UserInformationDBManager dbManager = new UserInformationDBManager(this);
			dbId = intent.getIntExtra("selectedItem", -1);
			UserInformationModel info = dbManager.selectUserData(dbId);
			if(info != null){
				EditText editTextNewScrnrFirstName = (EditText)findViewById(R.id.editTextNewScrnrFirstName);
				EditText editTextNewLastName = (EditText)findViewById(R.id.editTextNewLastName);
				EditText editTextNewUserId = (EditText)findViewById(R.id.editTextNewUserId);
				editTextNewScrnrFirstName.setText(info.getFirstName());
				editTextNewLastName.setText(info.getLastName());
				editTextNewUserId.setText(info.getUserId());
			}
			buttonNewUserReg.setText(R.string.btn_Edit);
		}
        buttonNewUserReg.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_screener, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonNewUserReg:
	    	Time now = new Time();
	    	now.setToNow();
			EditText editTextNewScrnrFirstName = (EditText)findViewById(R.id.editTextNewScrnrFirstName);
			EditText editTextNewLastName = (EditText)findViewById(R.id.editTextNewLastName);
			EditText editTextNewUserId = (EditText)findViewById(R.id.editTextNewUserId);
			UserInformationModel info = new UserInformationModel(dbId,
					editTextNewScrnrFirstName.getText().toString(),
					editTextNewLastName.getText().toString(),
					editTextNewUserId.getText().toString(),
					null,
					now.format("%Y-%m-%d %H:%M:%S")
					);
			UserInformationDBManager dbManager = new UserInformationDBManager(this);
			if(intent.getStringExtra("ActionType").equals("Edit")){
				dbId = intent.getIntExtra("selectedItem", -1);
				dbManager.updateUserData(info, dbId);
			}
			else{
				dbManager.insertUserData(info);
			}
			finish();
	        break;
		}
	}

}
