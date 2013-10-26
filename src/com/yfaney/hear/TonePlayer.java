package com.yfaney.hear;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class TonePlayer {
    boolean play = false;
    
    public boolean isPlay() {
		return play;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

	void playSound(float synth_frequency, int sample_Rate){
        int minSize = AudioTrack.getMinBufferSize(sample_Rate,
				AudioFormat.CHANNEL_OUT_STEREO,
				AudioFormat.ENCODING_PCM_16BIT);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
								sample_Rate,
								AudioFormat.CHANNEL_OUT_MONO,
								AudioFormat.ENCODING_PCM_16BIT,
								minSize,
								AudioTrack.MODE_STREAM);
		audioTrack.play();
		short[] buffer = new short[minSize];
		float angle = 0;
		while (true){
			if (play){
			    for (int i = 0; i < buffer.length; i++){
			        float angular_frequency =
			        (float)(2*Math.PI) * synth_frequency / sample_Rate;
			        buffer[i] = (short)(Short.MAX_VALUE * ((float) Math.sin(angle)));
			        angle += angular_frequency;
			    }
			    audioTrack.write(buffer, 0, buffer.length);
			}
		}
    }
}
//    private int duration = 3; // seconds
//    private int sampleRate = 8000;
//    private int numSamples = duration * sampleRate;
//    private double sample[] = null;
//    private double freqOfTone = 440; // hz
//    AudioTrack audioTrack = null;
//
//    private byte generatedSnd[] = null;
//    
//    Handler handler = null;
//    Thread thread = null;
//
//    public TonePlayer(int duration, int sampleRate, double freqOfTone) {
//		super();
//		this.duration = duration;
//		this.sampleRate = sampleRate;
//		this.numSamples = duration * sampleRate;
//		this.freqOfTone = freqOfTone;
//		this.sample = new double[this.numSamples];
//		this.generatedSnd = new byte[2 * this.numSamples];
//		this.handler = new Handler();
//		this.thread = new Thread(new Runnable() {
//	        public void run() {
//	        	genTone();
//	            handler.post(new Runnable() {
//
//	                public void run() {
//	                	playSound();
//	                }
//	            });
//	        }
//	    });
//	}
//    void genTone(){
//        // fill out the array
//        for (int i = 0; i < numSamples; ++i) {
//            sample[i] = (Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone)));
//        }
//
//        // convert to 16 bit pcm sound array
//        // assumes the sample buffer is normalised.
//        int idx = 0;
//        for (final double dVal : sample) {
//            // scale to maximum amplitude
//            final short val = (short) ((dVal * 32767));
//            // in 16 bit wav PCM, first byte is the low order byte
//            generatedSnd[idx++] = (byte) (val & 0x00ff);
//            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
//        }
//    }
//
//    void playSound(){
//		new AudioTrack(AudioManager.STREAM_MUSIC,
//	            sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
//	            AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
//	            AudioTrack.MODE_STATIC);
//        audioTrack.write(generatedSnd, 0, generatedSnd.length);
//        audioTrack.play();
//    }
//    void startSound(){
//    	this.thread.start();
//    }
