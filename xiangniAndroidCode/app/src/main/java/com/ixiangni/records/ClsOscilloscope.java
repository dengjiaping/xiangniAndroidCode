package com.ixiangni.records;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.view.SurfaceView;


import com.handongkeji.common.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


/**
 * 录音和写入文件使用了两个不同的线程，以免造成卡机现象
 * @author Li Shaoqing
 *
 */
public class ClsOscilloscope {
	
    private ArrayList<Short> inBuf = new ArrayList<Short>();
    private ArrayList<byte[]> write_data = new ArrayList<byte[]>();
    private boolean isRecording = false;// 录音线程控制标记
    private boolean isWriting = false;// 录音线程控制标记
    
    /** 
     * X轴缩小的比例 
     */  
    public int rateX = 352; 
    
    /** 
     * Y轴缩小的比例 
     */  
    public int rateY = 0;  
    
    /** 
     * Y轴基线 
     */  
    public int baseLine = 0;
    
    private AudioRecord audioRecord;
    
    private String audioName;
    
    int recBufSize;
    
    int sampleRateInHz = 44100;
    
    /**
     * 两次绘图间隔的时间
     */
    int draw_time = 1000 /10;
    
    /**
     * 为了节约绘画时间，每三个像素画一个数据
     */
    int divider = 2;
    
    long c_time;
    
    private String tempName = Constants.CACHE_DIR + "/voice/temp.pcm";
    
    /**
     * 开始 
     * @param audioRecord
     * @param recBufSize
     * @param sfv0
     * @param mPaint
     */
    public void Start(AudioRecord audioRecord, int recBufSize, SurfaceView sfv0,  SurfaceView sfv1,
            Paint mPaint, String audioName) { 
    	File f = new File(Constants.CACHE_DIR+"/voice/");
    		f.mkdirs();
    	this.audioRecord = audioRecord;
        isRecording = true;
        isWriting = true;
        this.audioName = audioName;
        this.recBufSize = recBufSize;
        new Thread(new WriteRunnable()).start();
        new RecordTask(audioRecord, recBufSize, sfv0 , sfv1, mPaint).execute();
    } 
    
    /** 
     * 停止 
     */  
    public void Stop() {  
        isRecording = false;  
        inBuf.clear();// 清除  
    }
    
    /**
     * 异步录音程序
     * @author Li Shaoqing
     *
     */
    class RecordTask extends AsyncTask<Object, Object, Object> {

    	private int recBufSize;  
        private AudioRecord audioRecord;  
        private SurfaceView sfv0;// 画板  
        private SurfaceView sfv1;// 画板  
        private Paint mPaint;// 画笔  
        
        public RecordTask(AudioRecord audioRecord, int recBufSize,
        		SurfaceView sfv0,SurfaceView sfv1, Paint mPaint) {  
            this.audioRecord = audioRecord;  
            this.recBufSize = recBufSize;  
            this.sfv0 = sfv0;
            this.sfv1 = sfv1;
            this.mPaint = mPaint;
        }
    	
		@Override
		protected Object doInBackground(Object... params) {
			try {
                byte[] buffer = new byte[recBufSize];
                audioRecord.startRecording(); // 开始录制
                while (isRecording) {
                    // 从MIC保存数据到缓冲区  
                    int readsize = audioRecord.read(buffer, 0,  
                            recBufSize);
                    synchronized (inBuf) {
	                    // 添加数据
	                	int len = readsize / rateX;
	                    for (int i = 0; i < len; i += rateX) {
	                    	inBuf.add((short)((0x0000 | buffer[i + 1]) << 8 | buffer[i]));
	                    }
                    }
                    publishProgress();
                    if (AudioRecord.ERROR_INVALID_OPERATION != readsize) {
                    	synchronized (write_data) {
                    		write_data.add(buffer);
                        }
        			}
                }
    			isWriting = false;
                audioRecord.stop();
            } catch (Throwable t) {
            }
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
            long time = new Date().getTime();
            if(time - c_time >= draw_time){
            	ArrayList<Short> buf = new ArrayList<Short>();
    			synchronized (inBuf) {
    				if (inBuf.size() == 0)  
    	                return;
    	            while(inBuf.size() > sfv0.getWidth() / divider){
    	            	inBuf.remove(0);
    	            }
    	            buf = (ArrayList<Short>) inBuf.clone();// 保存  
    			}
            	SimpleDraw(buf, baseLine);// 把缓冲区数据画出来
            	c_time = new Date().getTime();
            }
			super.onProgressUpdate(values);
		}
		
		/** 
         * 绘制指定区域 
         *  
         * @param buf 
         *            缓冲区 
         * @param baseLine 
         *            Y轴基线 
         */  
        void SimpleDraw(ArrayList<Short> buf, int baseLine) { 
        	if(rateY == 0){
        		rateY = 50000 / sfv0.getHeight();
        		baseLine = sfv0.getHeight() / 2;
        	}
            Canvas canvas0 = sfv0.getHolder().lockCanvas(new Rect(0, 0, sfv0.getWidth(), sfv0.getHeight()));// 关键:获取画布
            Canvas canvas1 = sfv1.getHolder().lockCanvas(new Rect(0, 0, sfv0.getWidth(), sfv0.getHeight()));// 关键:获取画布  
            if (canvas0 == null || canvas1 == null) {
				return;
			}
            canvas0.drawColor(Color.TRANSPARENT,Mode.CLEAR);// 清除背景  
            canvas1.drawColor(Color.TRANSPARENT,Mode.CLEAR);// 清除背景  
            int start0 = sfv0.getWidth() - buf.size() * divider;
            int start1 = sfv1.getWidth() - buf.size() * divider;
            int py = baseLine;
            if(buf.size() > 0)
            	py += buf.get(0) / rateY;
            int y;
            canvas0.drawLine(0, baseLine, start0 - divider, baseLine, mPaint);
            canvas1.drawLine(0, baseLine, start1 - divider, baseLine, mPaint);
            for (int i = 0; i < buf.size(); i++) {
                y = buf.get(i) / rateY + baseLine;// 调节缩小比例，调节基准线  
                canvas0.drawLine(start0 + (i - 1 ) * divider, py, start0 + i * divider, y, mPaint);
                canvas1.drawLine(start1 + i * divider, py,start1 + (i - 1 ) * divider, y, mPaint);
                py = y;
            }  
            sfv0.getHolder().unlockCanvasAndPost(canvas0);// 解锁画布，提交画好的图像  
            sfv1.getHolder().unlockCanvasAndPost(canvas1);// 解锁画布，提交画好的图像  
        }
    }
    
    /**
     * 异步写文件
     * @author Li Shaoqing
     *
     */
    class WriteRunnable implements Runnable {
		@Override
		public void run() {
			try {
                FileOutputStream fos = null;
                File file = null;
        		try {
        			file = new File(tempName);
        			if (file.exists()) {
        				file.delete();
        			}else{
        				file.createNewFile();
        			}
        			fos = new FileOutputStream(file);// 建立一个可存取字节的文件
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
                while (isWriting || write_data.size() > 0) {
                	byte[] buffer = null;
                    synchronized (write_data) {
                    	if(write_data.size() > 0){
                    		buffer = write_data.get(0);
                        	write_data.remove(0);
                    	}
                    }
                    try {
                    	if(buffer != null){
        					fos.write(buffer);
        					fos.flush();
                    	}
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
                }
    			fos.close();// 关闭写入流
                copyWaveFile(tempName, audioName);
            } catch (Throwable t) {
            }
		}
    }
    
    // 这里得到可播放的音频文件
 	private void copyWaveFile(String inFilename, String outFilename) {
 		FileInputStream in = null;
 		FileOutputStream out = null;
 		long totalAudioLen = 0;
 		long totalDataLen = totalAudioLen + 36;
 		long longSampleRate = sampleRateInHz;
 		int channels = 2;
 		long byteRate = 16 * sampleRateInHz * channels / 8;
 		byte[] data = new byte[recBufSize];
 		try {
 			in = new FileInputStream(inFilename);
 			File file = new File(outFilename);
 			if (!file.exists()) {
				file.createNewFile();
			}
 			out = new FileOutputStream(outFilename);
 			totalAudioLen = in.getChannel().size();
 			totalDataLen = totalAudioLen + 36;
 			WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
 					longSampleRate, channels, byteRate);
 			while (in.read(data) != -1) {
 				out.write(data);
 			}
 			in.close();
 			out.close();
 		} catch (FileNotFoundException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 	}
    
    /**
	 * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。
	 * 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav
	 * 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有
	 * 自己特有的头文件。
	 */
	private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
			long totalDataLen, long longSampleRate, int channels, long byteRate)
			throws IOException {
		byte[] header = new byte[44];
		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (2 * 16 / 8); // block align
		header[33] = 0;
		header[34] = 16; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
		out.write(header, 0, 44);
	}
}   
