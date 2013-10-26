package com.yfaney.hear;

import com.yfaney.hear_draft.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class NewScreenerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_new_screener);
        Button buttonNewUserReg = (Button)findViewById(R.id.buttonNewUserReg);
        buttonNewUserReg.setOnClickListener( new Button.OnClickListener(){
        	@Override
			public void onClick(View v) {
        		//Intent intent = new Intent(ScreeningActivity.this, MainActivity.class); // 평범한 Intent 생성
        		//startActivity(intent);                                    // Activity 실행
        		Intent resultIntent = new Intent();
        		//resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, tabIndexValue);
        		setResult(Activity.RESULT_OK, resultIntent);
        		finish();
        	}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_screener, menu);
		return true;
	}

}
