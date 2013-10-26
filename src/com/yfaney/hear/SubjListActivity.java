package com.yfaney.hear;

import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SubjListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_subj_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subj_list, menu);
		return true;
	}

}
