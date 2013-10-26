package com.yfaney.hear;

import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class BriefInstActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brief_inst);
        Button buttonBeginTrial = (Button)findViewById(R.id.buttonBeginTrial);
        buttonBeginTrial.setOnClickListener( new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent(BriefInstActivity.this, TrialRunActivity.class); // 평범한 Intent 생성
        		//startActivity(intent);                                    // Activity 실행
        		startActivityForResult(intent, MainActivity.SUBJECTACTION);
        	}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.brief_inst, menu);
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
