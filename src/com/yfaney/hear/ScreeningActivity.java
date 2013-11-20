package com.yfaney.hear;

import java.util.ArrayList;
import java.util.Collections;

import com.yfaney.hear.R;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.Time;
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

public class ScreeningActivity extends Activity implements OnClickListener, OnTouchListener{
	private final static int SEND_THREAD_STOP_MESSAGE = 0;
	private final static int SEND_CHANGE_FREQ_MESSAGE = 1;
	private final static int SEND_CHANGE_EAR_MESSAGE = 2;
	private final static int SEND_CHANGE_DB_MESSAGE = 3;
	
	/* Temporary User Data */
	//SharedPreferences prefs = getPreferences(MODE_PRIVATE);
	/*
	final static String USER_FIRSTNAME = "Younghwan";
	final static String USER_LASTNAME = "Jang";
	final static String USER_USERID = "yfaney";
	*/
	private String firstName = null;
	private String lastName = null;
	private String userID = null;
	
	/* Temporary User Data End */
	int sampleRate = 12000;
    private SendMassgeHandler mMainHandler = null;
    private static ToneThread mToneThread = null;
    int[] freq = null;
    int[] freq2deciBel = null;
    ArrayList<ScreeningTestSet> scrSet = new ArrayList<ScreeningTestSet>();
    ArrayList<TestDataModel> scrTestData = new ArrayList<TestDataModel>();
    ArrayList<ScreeningModel> scrUserData = new ArrayList<ScreeningModel>();
    int testSetIdx = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screening);
        
		/* GUI */
		Button buttonBeginScrng = (Button)findViewById(R.id.buttonBeginScrng);
        Button buttonExitToMain = (Button)findViewById(R.id.buttonExitToMain);

        /* GUI Listener */
        buttonBeginScrng.setOnClickListener(this);
        buttonExitToMain.setOnClickListener(this);
        buttonExitToMain.setVisibility(View.INVISIBLE);

        /* Loading Arrays */
        freq = getResources().getIntArray(R.array.frequency);
        freq2deciBel = getResources().getIntArray(R.array.freq2db);
        /* Make Handler */
        mMainHandler = new SendMassgeHandler();
        
        /* Get user info from preference */
		SharedPreferences prefs = getSharedPreferences("UserInformation", Activity.MODE_PRIVATE);
		firstName = prefs.getString("UserFirstName", "Default:Younghwan");
		lastName = prefs.getString("UserLastName", "Default:Jang");
		userID = prefs.getString("UserID", "Default:yfaney");

		// Moved from R.id.buttonBeginScrng at onClick Start
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout_screening);
		buttonBeginScrng.setVisibility(View.INVISIBLE);
		for(int j=0; j<2;j++){
			for(int i=0;i < freq.length; i++){
				scrSet.add(new ScreeningTestSet(freq[i], (short)freq2deciBel[i], ToneThread.LEFT_EAR));
				scrSet.add(new ScreeningTestSet(freq[i], (short)freq2deciBel[i], ToneThread.RIGHT_EAR));				
			}
		}
		Collections.shuffle(scrSet);
		layout.setBackgroundResource(R.color.red);
		layout.setOnTouchListener(this);
		testSetIdx = 0;
		// Moved from R.id.buttonBeginScrng at onClick End

    
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screening, menu);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonBeginScrng:
	        break;
		case R.id.buttonExitToMain:
    		Intent resultIntent = new Intent();
    		//startActivity(intent);                                    // Activity 실행
    		setResult(Activity.RESULT_OK, resultIntent);
    		finish();
	        break;
		}
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout_screening);
		TextView textViewTesting =(TextView)findViewById(R.id.textViewScrngComplete);
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.layout_screening:
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				// TODO Pressing Screening - Tone Play!
				if (testSetIdx < scrSet.size()){
					layout.setBackgroundResource(R.color.green);
					/* ToneThread Constructor */
					mToneThread = new ToneThread(sampleRate, scrSet.get(testSetIdx).getEarSide(), scrSet.get(testSetIdx).getFrequency(), scrSet.get(testSetIdx).getDeciBel());
					mToneThread.start();
				}
				else{
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				// TODO Releasing Screening - Tone Stop!
				if (testSetIdx < scrSet.size()){
					if(scrSet.get(testSetIdx).getDeciBel() == mToneThread.getdB()){
						scrSet.add(new ScreeningTestSet(scrSet.get(testSetIdx).getFrequency(), (short) (scrSet.get(testSetIdx).getDeciBel() + 5), scrSet.get(testSetIdx).getEarSide()));
			            //Toast.makeText(this, "index increased= "+scrSet.size(), Toast.LENGTH_SHORT).show();
					}
					else{
						scrTestData.add(new TestDataModel(0, testSetIdx, mToneThread.get_ear_side(), (int)mToneThread.getSynth_frequency(), mToneThread.getdB()));
			            //Toast.makeText(this, "idx= "+ testSetIdx +"&frq= "+scrTestData.get(scrTestData.size()-1).getFrequency()+"&dB="+scrTestData.get(scrTestData.size()-1).getDeciBel(), Toast.LENGTH_SHORT).show();
					}
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					layout.setBackgroundResource(R.color.red);
				}
				else{
					/* End Screening */
			    	Time now = new Time();
			    	now.setToNow();
					scrTestData.add(new TestDataModel(0, 0, mToneThread.get_ear_side(), (int)mToneThread.getSynth_frequency(), mToneThread.getdB()));
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					scrUserData.add(new ScreeningModel(0,firstName, lastName, userID, now.format("%Y-%m-%d %H:%M:%S")));
					layout.setBackgroundResource(R.color.black);
					layout.setOnTouchListener(null);
					/* Write data into DB */
					ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
					long setId = dbManager.insertUserData(scrUserData.get(0));
					textViewTesting.setText(getResources().getString(R.string.btn_scrningComplete));
					for(int i=0 ; i < scrTestData.size() ; i++){
						dbManager.insertData(scrTestData.get(i), setId);
					}
			        Button buttonExitToMain = (Button)findViewById(R.id.buttonExitToMain);
			        buttonExitToMain.setVisibility(View.VISIBLE);  // 화면에 안보임
				}
				testSetIdx++;
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
				mToneThread.setdB((Short) msg.obj);
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
        
        // ToneThread Constructor
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
        /* Getter and Setter Start */
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
        public void setdB(short dB){
        	this.dB = dB;
        }
        public void stopThread() {
        	if(isThreadState()){
                isPlay = false;
        	}
        }
        /* Getter and Setter End */
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
				    audioTrack.write(buffer, 0, buffer.length);
    		    }
    		    dB-=2;
			}
		    //Toast.makeText(getApplicationContext(), Integer.toString(dB), Toast.LENGTH_SHORT).show();
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