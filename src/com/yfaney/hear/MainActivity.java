package com.yfaney.hear;

import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int SUBJECTACTION = 1;
	public static final int SCREENERACTION = 2;
	public static final int ADMINACTION = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        Button buttonSubj = (Button)findViewById(R.id.buttonSubj);
        Button buttonScr = (Button)findViewById(R.id.buttonScr);

        buttonSubj.setOnClickListener( new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(MainActivity.this, SubjFirstActivity.class); // 평범한 Intent 생성
        		//startActivity(intent);                                    // Activity 실행
        		startActivityForResult(intent, MainActivity.SUBJECTACTION);        	}
        });
        buttonScr.setOnClickListener( new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(MainActivity.this, ScreenerActivity.class); // 평범한 Intent 생성
        		//startActivity(intent);                                    // Activity 실행
        		startActivityForResult(intent, MainActivity.SCREENERACTION);
        	}
        });
        // Check Admin Status
		UserInformationDBManager dbManager = new UserInformationDBManager(this);
		if(dbManager.isAdminDBIn() <= 0){
			Intent intent = new Intent(MainActivity.this, AdminActivity.class);
    		startActivityForResult(intent, MainActivity.ADMINACTION);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (MainActivity.SUBJECTACTION) : { 
	      if (resultCode == Activity.RESULT_OK) { 
	      //int tabIndex = data.getIntExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
	      // TODO Switch tabs using the index.
//      		Intent resultIntent = new Intent();
//      		//resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, tabIndexValue);
//      		setResult(Activity.RESULT_OK, resultIntent);
//      		finish();
	      } 
	      break; 
	    } 
	    case (MainActivity.SCREENERACTION) : { 
		      if (resultCode == Activity.RESULT_OK) { 
		      //int tabIndex = data.getIntExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
		      // TODO Switch tabs using the index.
//	      		Intent resultIntent = new Intent();
//	      		//resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, tabIndexValue);
//	      		setResult(Activity.RESULT_OK, resultIntent);
//	      		finish();
		      } 
		      break; 
		    } 
	    case (MainActivity.ADMINACTION) : { 
		      if (resultCode == Activity.RESULT_OK) { 
		      //int tabIndex = data.getIntExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
		      // TODO Switch tabs using the index.
//	      		Intent resultIntent = new Intent();
//	      		//resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, tabIndexValue);
//	      		setResult(Activity.RESULT_OK, resultIntent);
//	      		finish();
		      }
		      else{
					Toast.makeText(this, "You need to register Administrator before using.", Toast.LENGTH_SHORT).show();
					finish();
		      }
		      break; 
		    } 
	    }
	  
	}
}
