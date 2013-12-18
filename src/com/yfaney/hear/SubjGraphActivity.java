package com.yfaney.hear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.yfaney.hear.R;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SubjGraphActivity extends Activity {
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
			graphLeftData[i] = new GraphViewData(scrTestLeftData.get(i).getFrequency(), (short)100-scrTestLeftData.get(i).getDeciBel());
		}
		for (int i=0; i<scrTestRightData.size(); i++) {
			graphRightData[i] = new GraphViewData(scrTestRightData.get(i).getFrequency(), (short)100-scrTestRightData.get(i).getDeciBel());
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
	    int dimension[] = getWidthHeight();
	    int height = dimension[1];  // deprecated
	
	    graphView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height-400));
        //Toast.makeText(this, Double.toString(graphView.getMaxY()), Toast.LENGTH_SHORT).show();
	    graphView.addSeries(leftSeries); // data  
	    graphView.addSeries(rightSeries); // data
	    graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
	    	   @Override
	    	   public String formatLabel(double value, boolean isValueX) {
	    	      if (isValueX) {
	    	         if (value <= 0) {
	    	            return "0Hz";
	    	         } else if (value <= 125) {
	    	            return "125Hz";
	    	         } else if (value <= 250) {
	    	            return "250Hz";
	    	         } else if (value <= 500) {
	    	            return "500Hz";
	    	         } else if (value <= 1000) {
	    	            return "1000Hz";
	    	         } else if (value <= 2000) {
	    	            return "2000Hz";
	    	         } else if (value <= 4000) {
	    	            return "4000Hz";
	    	         } else {
	    	            return "8000Hz";
	    	         }
	    	      }
	    	      else{
	    	         if (value <= 20) {
		    	            return "poor";
		    	     }else if((value >=20) && (value <= 105)){
	    	    		  String retValue = Integer.toString(100-(int)value) + "dB";
	    	    		  return retValue;
	    	    	 }
	    	    	 else{
	    	            return "excellent";
	    	    	 }
	    	    	  /*
		    	         if (value <= 20) {
		    	            return "poor";
		    	         } else if (value <= 25) {
			    	        return "75dB";
		    	         } else if (value <= 30) {
		    	            return "70dB";
		    	         } else if (value <= 35) {
		    	            return "65dB";
		    	         } else if (value <= 40) {
		    	            return "60dB";
		    	         } else if (value <= 45) {
		    	            return "55dB";
		    	         } else if (value <= 50) {
		    	            return "50dB";
		    	         } else if (value <= 55) {
		    	            return "45dB";
		    	         } else if (value <= 60) {
		    	            return "40dB";
		    	         } else if (value <= 65) {
		    	            return "35dB";
		    	         } else if (value <= 70) {
		    	            return "30dB";
		    	         } else if (value <= 75) {
		    	            return "25dB";
		    	         } else if (value <= 80) {
		    	            return "20dB";
		    	         } else if (value <= 85) {
		    	            return "15dB";
		    	         } else if (value <= 90) {
		    	            return "10dB";
		    	         } else if (value <= 95) {
		    	            return "5dB";
		    	         } else if (value <= 100) {
		    	            return "0dB";
		    	         } else if (value <= 105) {
			    	        return "-5dB";
		    	         } else {
		    	            return "excellent";*/
	    	      }
	    	   }
	    	});
	    graphView.setViewPort(0, 8000);
	    graphView.setScalable(true);
	    graphView.setShowLegend(true);
	    graphView.setLegendWidth(150);
	    RelativeLayout layout = (RelativeLayout) findViewById(R.id.graphLayout);  
	    layout.addView(graphView); 
	}
/*	I don't need a menu until now
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subj_graph, menu);
		return true;
	}*/
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public int[] getWidthHeight(){
	    Display display = getWindowManager().getDefaultDisplay();
		int dimension[] = new int[2];
	    if(android.os.Build.VERSION.SDK_INT >= 13){
	    	Point size = new Point();
	    	display.getSize(size);
	    	dimension[0] = size.x;
	    	dimension[1] = size.y;
	    }
	    else{
	    	dimension[0] = display.getWidth();
	    	dimension[1] = display.getHeight();
	    }
		return dimension;
		
	}
	public int getHeight(){
		return 0;
	}
	
	public void getMinMax(ArrayList<TestDataModel> modelSet, int[] graphMinMax){
		if((modelSet == null)||(modelSet.size() == 0)){
		}
		else{
			if(modelSet.size() == 1){
				graphMinMax[0] = graphMinMax[1] = modelSet.get(0).getDeciBel();
			}
			else if(modelSet.size() == 2){
				if(modelSet.get(0).getDeciBel() < modelSet.get(1).getDeciBel()){
					graphMinMax[0] = modelSet.get(0).getDeciBel();
					graphMinMax[1] = modelSet.get(1).getDeciBel();
				}
				else{
					graphMinMax[0] = modelSet.get(1).getDeciBel();
					graphMinMax[1] = modelSet.get(0).getDeciBel();
				}
			}
			else{
				int mid = modelSet.size()/2;
				int[] lMinMax1 = new int[2]; 
				int[] lMinMax2 = new int[2]; 
				getMinMax((ArrayList<TestDataModel>) modelSet.subList(0, mid), lMinMax1);
				getMinMax((ArrayList<TestDataModel>) modelSet.subList(0, mid), lMinMax2);
				if (lMinMax1[0]<lMinMax2[0]){
					graphMinMax[0] = lMinMax1[0];
				}
				else{
					graphMinMax[0] = lMinMax2[0];
				}
				if (lMinMax1[1]>lMinMax2[1]){
					graphMinMax[1] = lMinMax1[1];
				}
				else{
					graphMinMax[1] = lMinMax2[1];
				}
			}
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
