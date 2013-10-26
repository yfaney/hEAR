package com.yfaney.hear;

import java.util.ArrayList;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.yfaney.hear.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SubjGraphActivity extends Activity implements OnClickListener{
    Button buttonSubjExport = null;
    Button buttonEmailToAdmin = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screener_subj_graph);
		Intent intent = getIntent();
		int setId = intent.getIntExtra("selectedItem", -1);
		ScreeningSetDBManager dbManager = new ScreeningSetDBManager(this);
	    ArrayList<TestDataModel> scrTestData = dbManager.selectTestDatas(setId);
	 // init example series data  
	    GraphViewData[] graphData = new GraphViewData[scrTestData.size()];
	    for (int i=0; i<scrTestData.size(); i++) {  
	    	   graphData[i] = new GraphViewData(scrTestData.get(i).getFrequency(), scrTestData.get(i).getDeciBel());
	    }  
	    GraphViewSeries exampleSeries = new GraphViewSeries(graphData);  
	      
	    GraphView graphView = new LineGraphView(  
	          this // context  
	          , "GraphViewDemo" // heading  
	    );  
	    graphView.addSeries(exampleSeries); // data  
	    LinearLayout layout = (LinearLayout) findViewById(R.id.graphLayout);  
	    layout.addView(graphView); 
	    Button buttonSubjExport = (Button)findViewById(R.id.buttonSubjExport);
	    Button buttonEmailToAdmin = (Button)findViewById(R.id.buttonEmailToAdmin);
	    buttonSubjExport.setOnClickListener(this);
	    buttonEmailToAdmin.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subj_graph, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonSubjExport:

			//mToneThread = new ToneThread(sampleRate, ToneThread.LEFT_EAR, frequency, (short)50);
			//tonePlayer.playSound();
	        break;
		case R.id.buttonEmailToAdmin:

	        break;
    	}
    }
}
