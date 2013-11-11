package com.yfaney.hear;

import com.yfaney.hear.R;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        Button buttonBeginTrial = (Button)findViewById(R.id.buttonBeginTrial);
        Button buttonBeginScrning = (Button)findViewById(R.id.buttonBeginScrning);
        buttonBeginScrning.setVisibility(View.INVISIBLE);  // 화면에 안보임
        buttonBeginTrial.setOnClickListener(this);
        buttonBeginScrning.setOnClickListener(this);
        mMainHandler = new SendMassgeHandler();
        
        /* Volume Max! */
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) , 0);
        
		// Moved from at R.id.buttonBeginTrial - onClick() Start
		TextView textViewTesting =(TextView)findViewById(R.id.textViewTrialComplete);
		// TODO Trial Starting
		textViewTesting.setText(getResources().getString(R.string.btn_trialTesting));
		buttonBeginTrial.setVisibility(View.INVISIBLE);
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout_trial_run);
		layout.setBackgroundResource(R.color.red);
		layout.setOnTouchListener(this);
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
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout_trial_run);
		TextView textViewTesting =(TextView)findViewById(R.id.textViewTrialComplete);
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.layout_trial_run:
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				if (toneTestSound > 0 && frequency > 0){
					layout.setBackgroundResource(R.color.green);
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
					layout.setBackgroundResource(R.color.black);
					layout.setOnTouchListener(null);
					textViewTesting.setText(getResources().getString(R.string.btn_scrningComplete));
			        Button buttonBeginScrning = (Button)findViewById(R.id.buttonBeginScrning);
			        buttonBeginScrning.setVisibility(View.VISIBLE);  // 화면에 안보임
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				if (toneTestSound > 0){
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					layout.setBackgroundResource(R.color.red);
				}
				else{
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					layout.setBackgroundResource(R.color.black);
					layout.setOnTouchListener(null);
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
	// Thread 클래스
    class ToneThread extends Thread implements Runnable {
    	final static int LEFT_EAR = 0;
    	final static int RIGHT_EAR = 1;
    	final static int MONO = 1;
    	final static int STEREO = 2;
    	int sample_Rate = 8000;
    	int ear_side = LEFT_EAR;
		float synth_frequency = 1000;
		short amplitute = 10000;	//Starts from 50db = 20*log(amp/1)
		short dB = 50;
        private boolean isPlay = false;

        public ToneThread(int sample_Rate) {
        	this.sample_Rate = sample_Rate;
            isPlay = true;
        }
        public ToneThread(int sample_Rate, int ear_side, float synth_frequency, short dB) {
        	this.sample_Rate = sample_Rate;
        	this.ear_side = ear_side;
        	this.synth_frequency = synth_frequency;
        	this.dB = dB;
        	this.amplitute = (short) getAmplitute(dB);
            isPlay = true;
        }
    	public float getSynth_frequency() {
			return synth_frequency;
		}
    	public void set_ear_side(int ear_side){
    		this.ear_side = ear_side;
    	}
		public void setSynth_frequency(float synth_frequency) {
			this.synth_frequency = synth_frequency;
		}
        public boolean isThreadState() {
            return this.isPlay;
        }
    	public int get_ear_side(){
    		return this.ear_side;
    	}
        public short getdB(){
        	return this.dB;
        }
        public void setdB(int dB){
        	this.dB = (short)dB;
        	this.amplitute = (short) getAmplitute((short)dB);
        }
        public void stopThread() {
        	if(isThreadState()){
                isPlay = false;
        	}
        }
        @Override
        public void run() {
            super.run();
            int minSize = AudioTrack.getMinBufferSize(sample_Rate,
    				AudioFormat.CHANNEL_OUT_STEREO,
    				AudioFormat.ENCODING_PCM_16BIT);
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
    								sample_Rate,
    								AudioFormat.CHANNEL_OUT_STEREO,
    								AudioFormat.ENCODING_PCM_16BIT,
    								minSize,
    								AudioTrack.MODE_STREAM);
    		audioTrack.play();
    		short[] buffer = new short[minSize];
    		float angle = 0;
    		while (isPlay && dB>-10){
    		    amplitute = (short)getAmplitute(dB);
    		    for(int j=0;(j<5) && isPlay;j++){
	    			switch(ear_side){
	    			case LEFT_EAR:
	    			    for (int i = 0; i < buffer.length; i+=2){
	    			        float angular_frequency =
	    			        (float)(2*Math.PI) * synth_frequency / sample_Rate;
	    			        buffer[i] = (short)(amplitute * ((float) Math.sin(angle)));
	    			        buffer[i+1] = 0;
	    			        angle += angular_frequency;
	    			    }
	    			    break;
	    			case RIGHT_EAR:
	    			    for (int i = 0; i < buffer.length; i+=2){
	    			        float angular_frequency =
	    			        (float)(2*Math.PI) * synth_frequency / sample_Rate;
	    			        buffer[i] = 0;
	    			        buffer[i+1] = (short)(amplitute * ((float) Math.sin(angle)));
	    			        angle += angular_frequency;
	    			    }
	    			    break;
	    			case STEREO:
	    			    for (int i = 0; i < buffer.length; i+=2){
	    			        float angular_frequency =
	    			        (float)(2*Math.PI) * synth_frequency / sample_Rate;
	    			        buffer[i] = (short)(amplitute * ((float) Math.sin(angle)));
	    			        buffer[i+1] = (short)(amplitute * ((float) Math.sin(angle)));
	    			        angle += angular_frequency;
	    			    }
	    			    break;
				    default:
				    	break;
	    			}
	    			audioTrack.flush();
				    audioTrack.write(buffer, 0, buffer.length);
    		    }
    		    dB-=2;
			}
    		audioTrack.release();
        }
    	double getAmplitute(short dB){
    		return Math.pow(10, (double)dB/20);
    	}
    	double getdB(short amplitute){
    		//To avoid divided by zeros
    		if (amplitute > 1){
    			return 20*Math.log10(amplitute);
    		}
    		else{
    			return 0;
    		}
    	}
    }
}