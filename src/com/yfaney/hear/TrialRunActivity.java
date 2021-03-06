package com.yfaney.hear;

import com.yfaney.hear.R;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TrialRunActivity extends Activity implements OnClickListener, OnTouchListener{
	private final static int SEND_THREAD_STOP_MESSAGE = 0;
	private final static int SEND_CHANGE_FREQ_MESSAGE = 1;
	private final static int SEND_CHANGE_EAR_MESSAGE = 2;
	private final static int SEND_CHANGE_DB_MESSAGE = 3;
	private final static int SEND_THREAD_START_MESSAGE = 4;
	
	float frequency = 2000;
	int sampleRate = 12000;
	int toneTestSound = 2;
	int toneside = 1;
    private SendMassgeHandler mMainHandler = null;
    private static ToneThread mToneThread = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trial_run);
        ImageButton buttonBeginTrial = (ImageButton)findViewById(R.id.buttonBeginTrial);
        Button buttonBeginScrning = (Button)findViewById(R.id.buttonBeginScrning);
        buttonBeginTrial.setOnTouchListener(this);
        //buttonBeginTrial.setBackgroundResource(R.color.red);
        buttonBeginScrning.setVisibility(View.INVISIBLE);  // 화면에 안보임
        buttonBeginScrning.setOnClickListener(this);
        mMainHandler = new SendMassgeHandler();
        
        /* Volume Max! */
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) , 0);
        
		// Moved from at R.id.buttonBeginTrial - onClick() Start
		//TextView textViewTesting =(TextView)findViewById(R.id.textViewTrialComplete);
		// TODO Trial Starting
		//textViewTesting.setText(getResources().getString(R.string.btn_trialTesting));
		//buttonBeginTrial.setVisibility(View.INVISIBLE);
		//RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout_trial_run);
		//layout.setBackgroundResource(R.color.red);
		//layout.setOnTouchListener(this);
		// Moved from at R.id.buttonBeginTrial - onClick() End
	}
    @Override
    protected void onResume() {
        super.onResume();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trial_run, menu);
		return true;
	}
	public boolean onKeyDown( int KeyCode, KeyEvent event ){
		if( event.getAction() == KeyEvent.ACTION_DOWN ){
			// 이 부분은 특정 키를 눌렀을때 실행 된다.
			// 만약 뒤로 버튼을 눌럿을때 할 행동을 지정하고 싶다면
			if( KeyCode == KeyEvent.KEYCODE_BACK ){
				if(mToneThread != null){
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
				}
				 //여기에 뒤로 버튼을 눌렀을때 해야할 행동을 지정한다
				return super.onKeyDown( KeyCode, event );
				// 여기서 리턴값이 중요한데; 리턴값이 true 이냐 false 이냐에 따라 행동이 달라진다.
				// true 일경우 back 버튼의 기본동작인 종료를 실행하게 된다.
				// 하지만 false 일 경우 back 버튼의 기본동작을 하지 않는다.
				// back 버튼을 눌렀을때 종료되지 않게 하고 싶다면 여기서 false 를 리턴하면 된다.
				// back 버튼의 기본동작을 막으면 어플리케이션을 종료할 방법이 없기때문에
				// 따로 종료하는 방법을 마련해야한다.
			}
		}
		return super.onKeyDown( KeyCode, event );
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
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.buttonBeginTrial:
	        break;
		case R.id.buttonBeginScrning:
			// TODO Move to Real Screening Page
    		Intent intent = new Intent(TrialRunActivity.this, ScreeningActivity.class); // 평범한 Intent 생성
    		//startActivity(intent);                                    // Activity 실행
    		startActivityForResult(intent, MainActivity.SUBJECTACTION);
	        break;
    	}
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		ImageButton buttonBeginTrial = (ImageButton)findViewById(R.id.buttonBeginTrial);
		TextView textViewTesting =(TextView)findViewById(R.id.textViewTrialComplete);
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonBeginTrial:
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				buttonBeginTrial.setImageResource(R.drawable.button_focused);
				if (toneTestSound > 0 && frequency > 0){
					//buttonBeginTrial.setBackgroundResource(R.color.green);
					if(toneside == 1){
						mToneThread = new ToneThread(sampleRate, ToneThread.LEFT_EAR, frequency, (short)50);
						mToneThread.start();
						toneside--;
					}
					else{
						toneside = 1;
						toneTestSound--;
						mToneThread = new ToneThread(sampleRate, ToneThread.RIGHT_EAR, frequency, (short)50);
						mToneThread.start();
						frequency -= 1000;
					}
				}
				else{
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					//buttonBeginTrial.setBackgroundResource(R.color.black);
					buttonBeginTrial.setOnTouchListener(null);
					textViewTesting.setText(getResources().getString(R.string.btn_scrningComplete));
			        Button buttonBeginScrning = (Button)findViewById(R.id.buttonBeginScrning);
			        buttonBeginScrning.setVisibility(View.VISIBLE);  // 화면에 안보임
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				buttonBeginTrial.setImageResource(R.drawable.button_unfocused);
				if (toneTestSound > 0){
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					//buttonBeginTrial.setBackgroundResource(R.color.red);
				}
				else{
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					//buttonBeginTrial.setBackgroundResource(R.color.black);
					buttonBeginTrial.setOnTouchListener(null);
					buttonBeginTrial.setVisibility(View.INVISIBLE);
					TextView textInstrSub1 =(TextView)findViewById(R.id.textInstrSub1);
					TextView textInstrSub2 =(TextView)findViewById(R.id.textInstrSub2);
					textInstrSub1.setVisibility(View.INVISIBLE);
					textInstrSub2.setVisibility(View.INVISIBLE);
					textViewTesting.setText(getResources().getString(R.string.btn_scrningComplete));
			        Button buttonBeginScrning = (Button)findViewById(R.id.buttonBeginScrning);
			        buttonBeginScrning.setVisibility(View.VISIBLE);  // 화면에 안보임
				}
			}
			return true;
		default:
		return false;
		}
	}
	// Handler 클래스
	static class SendMassgeHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SEND_THREAD_STOP_MESSAGE:
				mToneThread.stopThread();
				break;
				case SEND_CHANGE_FREQ_MESSAGE:
				mToneThread.setSynth_frequency((Float) msg.obj);
				break;
				case SEND_CHANGE_EAR_MESSAGE:
				mToneThread.set_ear_side((Integer) msg.obj);
				break;
				case SEND_CHANGE_DB_MESSAGE:
				mToneThread.setdB((Integer) msg.obj);
				break;
				case SEND_THREAD_START_MESSAGE:
				mToneThread.start();
				break;
				default:
				break;
			}
		}
	};
	
}