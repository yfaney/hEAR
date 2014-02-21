package com.yfaney.hear;

import java.util.ArrayList;
import java.util.Collections;

import com.yfaney.hear.R;

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
		firstName = prefs.getString("UserFirstName", "DefaultFirst");
		lastName = prefs.getString("UserLastName", "DefaultLast");
		userID = prefs.getString("UserID", "DefaultID");

		// Moved from R.id.buttonBeginScrng at onClick Start
		//RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout_screening);
		//buttonBeginScrng.setVisibility(View.INVISIBLE);
		for(int j=0; j<2;j++){
			for(int i=0;i < freq.length; i++){
				scrSet.add(new ScreeningTestSet(freq[i], (short)freq2deciBel[i], ToneThread.LEFT_EAR));
				scrSet.add(new ScreeningTestSet(freq[i], (short)freq2deciBel[i], ToneThread.RIGHT_EAR));				
			}
		}
		Collections.shuffle(scrSet);
		buttonBeginScrng.setBackgroundResource(R.color.red);
		buttonBeginScrng.setOnTouchListener(this);
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
		//RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout_screening);
		Button buttonBeginScrng = (Button)findViewById(R.id.buttonBeginScrng);
		TextView textViewTesting =(TextView)findViewById(R.id.textViewScrngComplete);
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonBeginScrng:
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				// TODO Pressing Screening - Tone Play!
				if (testSetIdx < scrSet.size()){
					buttonBeginScrng.setBackgroundResource(R.color.green);
					/* ToneThread Constructor */
					mToneThread = new ToneThread(sampleRate, scrSet.get(testSetIdx).getEarSide(), scrSet.get(testSetIdx).getFrequency(), scrSet.get(testSetIdx).getDeciBel());
					mToneThread.start();
				}
				else{
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				// TODO Releasing Screening - Tone Stop!
				if (testSetIdx+1 < scrSet.size()){
					if(scrSet.get(testSetIdx).getDeciBel() == mToneThread.getdB()){
						scrSet.add(new ScreeningTestSet(scrSet.get(testSetIdx).getFrequency(), (short) (scrSet.get(testSetIdx).getDeciBel() + 5), scrSet.get(testSetIdx).getEarSide()));
			            //Toast.makeText(this, "index increased= "+scrSet.size(), Toast.LENGTH_SHORT).show();
					}
					else{
						scrTestData.add(new TestDataModel(0, testSetIdx, mToneThread.get_ear_side(), (int)mToneThread.getSynth_frequency(), mToneThread.getdB()));
			            //Toast.makeText(this, "idx= "+ testSetIdx +"&frq= "+scrTestData.get(scrTestData.size()-1).getFrequency()+"&dB="+scrTestData.get(scrTestData.size()-1).getDeciBel(), Toast.LENGTH_SHORT).show();
					}
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					buttonBeginScrng.setBackgroundResource(R.color.red);
					TextView textInstrSub3 =(TextView)findViewById(R.id.textInstrSub3);
					textInstrSub3.setText("<Progress : " + Integer.toString(testSetIdx+1) + " of " + Integer.toString(scrSet.size()) + " sets>");
				}
				else{
					/* End Screening */
			    	Time now = new Time();
			    	now.setToNow();
					scrTestData.add(new TestDataModel(0, 0, mToneThread.get_ear_side(), (int)mToneThread.getSynth_frequency(), mToneThread.getdB()));
					mMainHandler.sendEmptyMessage(SEND_THREAD_STOP_MESSAGE);
					scrUserData.add(new ScreeningModel(0,firstName, lastName, userID, now.format("%Y-%m-%d %H:%M:%S")));
					buttonBeginScrng.setBackgroundResource(R.color.black);
					buttonBeginScrng.setOnTouchListener(null);
					buttonBeginScrng.setVisibility(View.INVISIBLE);
					/* Write data into DB */
					ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
					long setId = dbManager.insertUserData(scrUserData.get(0));
					textViewTesting.setText(getResources().getString(R.string.btn_scrningComplete));
					for(int i=0 ; i < scrTestData.size() ; i++){
						dbManager.insertData(scrTestData.get(i), setId);
					}
					TextView textInstrSub1 =(TextView)findViewById(R.id.textInstrSub1);
					TextView textInstrSub2 =(TextView)findViewById(R.id.textInstrSub2);
					TextView textInstrSub3 =(TextView)findViewById(R.id.textInstrSub3);
					textInstrSub1.setVisibility(View.INVISIBLE);
					textInstrSub2.setVisibility(View.INVISIBLE);
					textInstrSub3.setVisibility(View.INVISIBLE);
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
}