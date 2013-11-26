package com.yfaney.hear;

import com.yfaney.hear.R;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BriefInstActivity extends Activity implements OnClickListener, DialogInterface.OnClickListener{

	Intent intent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brief_inst);
        Button buttonBeginTrial = (Button)findViewById(R.id.buttonBeginTrial);
        Button buttonSkipTrial = (Button)findViewById(R.id.buttonSkipTrial);
        buttonBeginTrial.setOnClickListener(this);
        buttonSkipTrial.setOnClickListener(this);
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

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which) {
		case DialogInterface.BUTTON_POSITIVE:
			// TODO Auto-generated method stub
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			Toast.makeText(this, getResources().getString(R.string.txt_warning_unplug), Toast.LENGTH_SHORT).show();
			if(intent != null){
	    		startActivityForResult(intent, MainActivity.SUBJECTACTION);
			}
    		break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonBeginTrial:
    		intent = new Intent(BriefInstActivity.this, TrialRunActivity.class); // 평범한 Intent 생성
            if(ifEarphoneConnectedOrAlert()){
        		//startActivity(intent);                                    // Activity 실행
    			//mToneThread = new ToneThread(sampleRate, ToneThread.LEFT_EAR, frequency, (short)50);
    			//tonePlayer.playSound();
        		startActivityForResult(intent, MainActivity.SUBJECTACTION);
            }
            else{
            }
	        break;
		case R.id.buttonSkipTrial:
    		intent = new Intent(BriefInstActivity.this, ScreeningActivity.class); // 평범한 Intent 생성
            if(ifEarphoneConnectedOrAlert()){
        		//startActivity(intent);                                    // Activity 실행
        		startActivityForResult(intent, MainActivity.SUBJECTACTION);
            }
            else{
            }
	        break;
    	}
    }
	
	@SuppressWarnings("deprecation")
	public boolean ifEarphoneConnectedOrAlert(){
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.isWiredHeadsetOn()){
        	return true;
        }
        else{
            Builder dlg= new AlertDialog.Builder(BriefInstActivity.this);
            dlg.setTitle("Earphone is not detected")
            .setMessage(R.string.txt_unplug_confirm)
            .setIcon(R.drawable.headphone)
            .setPositiveButton(R.string.btn_Back, this)
            .setNegativeButton(R.string.btn_ProceedAnyway, this)
            .show();
    		return false;
        }
	}
}
