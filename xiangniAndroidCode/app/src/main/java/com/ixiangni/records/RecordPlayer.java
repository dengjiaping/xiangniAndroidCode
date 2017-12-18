package com.ixiangni.records;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.util.Log;

import com.ixiangni.app.MyApp;

import java.io.IOException;

/**
 * 
 * 
 * @ClassName:RecordPlayer

 * @PackageName:com.yidianxinxi.shangxuelian.diagnose

 * @Create On 2016-2-25下午6:10:28

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2016-2-25 handongkeji All rights reserved.
 */
public class RecordPlayer {
	
	private int mPlayState ;
	private static final int STATE_PLAYING = 0;
	private static final int STATE_STOP = 1;
	private static final int STATE_PAUSE = 2;
	private String recordPath;
	private MediaPlayer mediaPlayer ;
	private int currentPosition;

	public RecordPlayer(){

		MyApp.getInstance().OpenSpeaker();
		mediaPlayer = new MediaPlayer();
		mPlayState = STATE_STOP;
		currentPosition = 0 ;
	}
	
	public void setResource(String recordPath){
		this.recordPath = recordPath;
	}
	
	public String getResource(){
		return recordPath;
	}
	
	public boolean isMediaPlaying(){
		return mediaPlayer.isPlaying();
	}
	
	public void stopMediaPlayer(){
		mPlayState = STATE_STOP;
		mediaPlayer.stop();
		mediaPlayer.reset();
		if (callback != null) {
			callback.onStop();
		}
	}

	public void playRecord() {
		if (mPlayState == STATE_STOP) {

			try {
				mediaPlayer.setDataSource(recordPath);
				mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						Log.d("aaa", "onPrepared...  "+(mp == mediaPlayer));
						mediaPlayer.start();
						int duration = mediaPlayer.getDuration();
						if (callback != null) {
							callback.onPrepare((int)(duration / 500));
						}
						mPlayState = STATE_PLAYING;
						new Thread(new Runnable() {

							@Override
							public void run() {
								while (mediaPlayer.isPlaying()) {
									int position = mediaPlayer
											.getCurrentPosition() / 500;
									if (callback != null) {
										callback.onPlaying(position);
									}
								}
							}
						}).start();
						if (callback != null) {
							callback.startPlay();
						}
						currentPosition = 0;
					}
				});
				mediaPlayer.prepare();
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						mPlayState = STATE_STOP;
						mediaPlayer.stop();
						mediaPlayer.reset();
						if (callback != null) {
							callback.onStop();
						}
					}
				});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (mPlayState == STATE_PLAYING) {
			if (mediaPlayer != null) {
				currentPosition = mediaPlayer.getCurrentPosition();
				if (currentPosition == mediaPlayer.getDuration()) {
					mPlayState = STATE_STOP;
					if (callback != null) {
						callback.onStop();
					}
				} else {
					mPlayState = STATE_PAUSE;
					mediaPlayer.pause();
				}
				if (callback != null) {
					callback.onPause();
				}
			}
		} else if (mPlayState == STATE_PAUSE) {
			mPlayState = STATE_PLAYING;
			mediaPlayer.seekTo(currentPosition);
			mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {

				@Override
				public void onSeekComplete(MediaPlayer mp) {
					mediaPlayer.start();
					new Thread(new Runnable() {

						@Override
						public void run() {
							while (mediaPlayer.isPlaying()) {
								int position = mediaPlayer.getCurrentPosition() / 500;
								if (callback != null) {
									callback.onPlaying(position);
								}
							}
						}
					}).start();
					if (callback != null) {
						callback.startPlay();
					}
				}
			});
		}
	}
	
	public interface PlayCallback{
		void onPlaying(int position);
		void onStop();
		void startPlay();
		void onPause();
		void onPrepare(int max);
		
	}
	
	private PlayCallback callback;

	public PlayCallback getCallback() {
		return callback;
	}

	public void setCallback(PlayCallback callback) {
		this.callback = callback;
	}
	
}
