package com.yfaney.hear;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

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