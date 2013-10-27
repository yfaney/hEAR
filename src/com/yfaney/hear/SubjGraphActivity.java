package com.yfaney.hear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.yfaney.hear.R;
import com.yfaney.hear.ScreeningActivity.ToneThread;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

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
	    ArrayList<TestDataModel> scrTestLeftData = dbManager.selectTestDatas(setId, ToneThread.LEFT_EAR);
	    ArrayList<TestDataModel> scrTestRightData = dbManager.selectTestDatas(setId, ToneThread.RIGHT_EAR);
	    Collections.sort(scrTestLeftData, new CustomComparator());
	    Collections.sort(scrTestRightData, new CustomComparator());
	 // init example series data
	    GraphViewData[] graphLeftData = new GraphViewData[scrTestLeftData.size()];
	    GraphViewData[] graphRightData = new GraphViewData[scrTestRightData.size()];
		for (int i=0; i<scrTestLeftData.size(); i++) {
			graphLeftData[i] = new GraphViewData(scrTestLeftData.get(i).getFrequency(), scrTestLeftData.get(i).getDeciBel());
		}
		for (int i=0; i<scrTestRightData.size(); i++) {
			graphRightData[i] = new GraphViewData(scrTestRightData.get(i).getFrequency(), scrTestRightData.get(i).getDeciBel());
		}
	    GraphViewSeriesStyle leftStyle = new GraphViewSeriesStyle(0xFFFF1121,2);
	    GraphViewSeriesStyle rightStyle = new GraphViewSeriesStyle(0xFF00FF21,2);
	    GraphViewSeries leftSeries = new GraphViewSeries("Left Ear", leftStyle, graphLeftData);
	    GraphViewSeries rightSeries = new GraphViewSeries("Right Ear", rightStyle, graphRightData);
	      
	    GraphView graphView = new LineGraphView(  
	          this // context  
	          , "Data" // heading  
	    );  
	    graphView.setGraphViewStyle(new GraphViewStyle(0xFF000055,0xFF000055,0xFF000000));
	    //graphView.setManualYAxisBounds(90.0, -10.0);
	    Display display = getWindowManager().getDefaultDisplay(); 
	    int width = display.getWidth();  // deprecated
	    int height = display.getHeight();  // deprecated
	    graphView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height-400));
        //Toast.makeText(this, Double.toString(graphView.getMaxY()), Toast.LENGTH_SHORT).show();
	    graphView.addSeries(leftSeries); // data  
	    graphView.addSeries(rightSeries); // data
	    graphView.setViewPort(0, 8000);
	    graphView.setScalable(true);
	    graphView.setShowLegend(true);
	    graphView.setLegendWidth(150);
	    RelativeLayout layout = (RelativeLayout) findViewById(R.id.graphLayout);  
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
	public class CustomComparator implements Comparator<TestDataModel> {
	    @Override
	    public int compare(TestDataModel o1, TestDataModel o2) {
	    	if(o1.getFrequency() > o2.getFrequency()){	    		
	    		return 1;
	    	}
	    	else{
	    		return -1;
	    	}
	    }
	}
}
