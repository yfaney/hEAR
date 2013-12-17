package com.yfaney.hear;

import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ScreenerActivity extends Activity {
	public static final int ADDNEWSCREENER = 3;
	public static final int ADMINLOGIN = 4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_main);
		Button buttonSubjList = (Button)findViewById(R.id.buttonSubjList);
		Button buttonManageScrnr = (Button)findViewById(R.id.buttonManageScrnr);
		buttonSubjList.setOnClickListener( new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(ScreenerActivity.this, SubjListActivity.class); // 평범한 Intent 생성
        		//startActivity(intent);                                    // Activity 실행
        		startActivityForResult(intent, MainActivity.SCREENERACTION);        	}
        });
		buttonManageScrnr.setOnClickListener( new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(ScreenerActivity.this, ScreenerMgtActivity.class); // 평범한 Intent 생성
        		//startActivity(intent);                                    // Activity 실행
        		startActivityForResult(intent, ADDNEWSCREENER);
        	}
        });
		SharedPreferences prefs = getSharedPreferences("UserInformation", Activity.MODE_PRIVATE);
		String firstName = prefs.getString("UserFirstName", "ERROR:NOTLOGIN");
		if(firstName.equals("ERROR:NOTLOGIN")){
    		Intent intent = new Intent(ScreenerActivity.this, ScreenerLoginActivity.class); // 평범한 Intent 생성
    		//startActivity(intent);                                    // Activity 실행
    		startActivityForResult(intent, ADMINLOGIN);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screener, menu);
		return true;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) { 
		case (MainActivity.SCREENERACTION) : { 
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
		case (ADDNEWSCREENER) : { 
			if (resultCode == Activity.RESULT_OK) { 
			//int tabIndex = data.getIntExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
			// TODO Switch tabs using the index.
			//    		Intent resultIntent = new Intent();
			//    		//resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, tabIndexValue);
			//    		setResult(Activity.RESULT_OK, resultIntent);
			//    		finish();
			}
			break; 
		}
		case (ADMINLOGIN) : {
			if (resultCode == Activity.RESULT_OK) { 
			}
			else{
				finish();
			}
		}
		}
	}
}