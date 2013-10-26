package com.yfaney.hear;

import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ScreenerActivity extends Activity {
	public static final int ADDNEWSCREENER = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_main);
		Button buttonScrLogIn = (Button)findViewById(R.id.buttonScrLogIn);
		Button buttonAddNewScrnr = (Button)findViewById(R.id.buttonAddNewScrnr);
		buttonScrLogIn.setOnClickListener( new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(ScreenerActivity.this, ScreenerLoginActivity.class); // 평범한 Intent 생성
        		//startActivity(intent);                                    // Activity 실행
        		startActivityForResult(intent, MainActivity.SCREENERACTION);        	}
        });
        buttonAddNewScrnr.setOnClickListener( new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(ScreenerActivity.this, NewScreenerActivity.class); // 평범한 Intent 생성
        		//startActivity(intent);                                    // Activity 실행
        		startActivityForResult(intent, ADDNEWSCREENER);
        	}
        });
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
	  } 
	}
}
