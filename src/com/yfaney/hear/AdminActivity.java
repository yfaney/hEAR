package com.yfaney.hear;

import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends Activity implements OnClickListener{
	EditText editTextAdmName1;
	EditText editTextAdmName2;
	EditText editTextAdmOrg;
	EditText editTextAdmEmail;
	EditText editTextAdmUserId;
	EditText editTextAdmPasswd;
	Button buttonSubjNext1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		editTextAdmName1 = (EditText) findViewById(R.id.editTextAdmName1);
		editTextAdmName2 = (EditText) findViewById(R.id.editTextAdmName2);
		editTextAdmOrg = (EditText) findViewById(R.id.editTextAdmOrg);
		editTextAdmEmail = (EditText) findViewById(R.id.editTextAdmEmail);
		editTextAdmUserId = (EditText) findViewById(R.id.editTextAdmUserId);
		editTextAdmPasswd = (EditText) findViewById(R.id.editTextAdmPasswd);
		buttonSubjNext1 = (Button) findViewById(R.id.buttonSubjNext1);
		buttonSubjNext1.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy(){
		Intent resultIntent = new Intent();
		setResult(Activity.RESULT_CANCELED, resultIntent);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonSubjNext1:
			// Validation
			editTextAdmName1.setError(null);
			editTextAdmName2.setError(null);
			editTextAdmUserId.setError(null);
			editTextAdmPasswd.setError(null);
			editTextAdmOrg.setError(null);
			editTextAdmEmail.setError(null);
			String mFirstName = editTextAdmName1.getText().toString();
			String mLastName = editTextAdmName2.getText().toString();
			String mUserId = editTextAdmUserId.getText().toString();
			String mPassWd = editTextAdmPasswd.getText().toString();
			String mOrgan = editTextAdmOrg.getText().toString();
			String mEmail = editTextAdmEmail.getText().toString();

			boolean cancel = false;
			View focusView = null;

			if(TextUtils.isEmpty(mFirstName)){
				editTextAdmName1.setError(getString(R.string.error_field_required));
				focusView = editTextAdmName1;
				cancel = true;
			}
			if(TextUtils.isEmpty(mLastName)){
				editTextAdmName2.setError(getString(R.string.error_field_required));
				focusView = editTextAdmName2;
				cancel = true;
			}
			if(TextUtils.isEmpty(mUserId)){
				editTextAdmUserId.setError(getString(R.string.error_field_required));
				focusView = editTextAdmUserId;
				cancel = true;
			}
			if(TextUtils.isEmpty(mPassWd)){
				editTextAdmPasswd.setError(getString(R.string.error_field_required));
				focusView = editTextAdmPasswd;
				cancel = true;
			}
			if(TextUtils.isEmpty(mOrgan)){
				editTextAdmOrg.setError(getString(R.string.error_field_required));
				focusView = editTextAdmOrg;
				cancel = true;
			}
			if (TextUtils.isEmpty(mEmail)) {
				editTextAdmEmail.setError(getString(R.string.error_field_required));
				focusView = editTextAdmEmail;
				cancel = true;
			} else if (!mEmail.contains("@")) {
				editTextAdmEmail.setError(getString(R.string.error_invalid_email));
				focusView = editTextAdmEmail;
				cancel = true;
			}
			if (cancel) {
				// There was an error; don't attempt login and focus the first
				// form field with an error.
				focusView.requestFocus();
			}
			else{
				// Register Admin
				UserInformationDBManager dbManager = new UserInformationDBManager(this);
		    	Time now = new Time();
		    	now.setToNow();
				UserAdminInfoModel adminModel = new UserAdminInfoModel(0,
						editTextAdmName1.getText().toString(),
						editTextAdmName2.getText().toString(),
						editTextAdmUserId.getText().toString(),
						editTextAdmPasswd.getText().toString(),
						now.format("%Y-%m-%d %H:%M:%S"),
						editTextAdmOrg.getText().toString(),
						editTextAdmEmail.getText().toString()
						);
				if(dbManager.isAdminDBIn() == 0){
					dbManager.insertAdminData(adminModel);
				}
				else{
					int adminId = dbManager.selectAdminData().getID();
					dbManager.updateAdminData(adminModel, adminId);
				}
				Toast.makeText(this, "Administrator information successfully updated.", Toast.LENGTH_SHORT).show();
	    		Intent resultIntent = new Intent();
	    		setResult(Activity.RESULT_OK, resultIntent);
	    		finish();
			}
			break;
		}
	}

}
