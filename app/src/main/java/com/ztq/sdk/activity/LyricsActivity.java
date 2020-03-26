package com.ztq.sdk.activity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.format.DateUtils;

import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.ztq.sdk.R;
import com.ztq.sdk.entity.LyricsEntity;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;
import com.ztq.sdk.widget.LyricsView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ztq on 2019/10/17.
 * 显示歌词的activity
 */
public class LyricsActivity extends BaseActivity {
    private final String TAG = "noahedu.LyricsActivity";
    private Context mContext;
    private LyricsView mLyricsView;
    private SeekBar mSeekBar;
    private Button mPlayOrPauseBtn;
    private boolean mIsPlay;
    private MediaPlayer mPlayer;
    private int mMediaDuration;    // 音频时长
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        mContext = this;
        initViewAndVariable();
        addListener();
    }

    /**
     * 初始化view和变量
     * @return
     */
    private void initViewAndVariable() {
        mIsPlay = false;
        mLyricsView = findViewById(R.id.lyrics_view);
        mSeekBar = findViewById(R.id.seek_bar);
        mPlayOrPauseBtn = findViewById(R.id.play_or_pause_btn);
        mPlayer = new MediaPlayer();
        loadLyris();
        loadSound();
        startTimer();
    }

    private void startTimer(){
        clearTimer();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Utils.isPlaying(mPlayer)) {
                    int currentTime = mPlayer.getCurrentPosition();
                    if (mMediaDuration > 0) {
                        mSeekBar.setProgress(currentTime * mSeekBar.getMax() / mMediaDuration);
                    }
                    final int currentline = mLyricsView.getCurrentIndex(currentTime);
                    mLyricsView.setCurrentLine(currentline);
                    Log.v(TAG, "currentTime = " + currentTime + "; currentline = " + currentline);
                    MyHandlerThread.postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mLyricsView.updateOffset(currentline);
                        }
                    });
//                    mLyricsView.invalidate();
                }
            }
        }, 0, 1000);
    }

    private void clearTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void loadSound() {
        try {
            //播放 assets/send_it.m4a音乐文件
            AssetFileDescriptor fd = getAssets().openFd("send_it.m4a");
            mPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mPlayer.setLooping(true);
            mPlayer.prepare();
            mMediaDuration = mPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载歌词
     */
    private void loadLyris() {
        String lyricsText = getLyricsText("send_it_en.lrc");
        List<LyricsEntity> lyricsList = parseLyricsListFromLyricsText(lyricsText);
        mLyricsView.setLyricsList(lyricsList);
        MyHandlerThread.postToMainThreadDelayed(new Runnable() {
            @Override
            public void run() {
                mLyricsView.initEntryListStaticLayout();
                mLyricsView.invalidate();
            }
        }, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearTimer();
    }

    /**
     * 从assets中获取到歌词文本
     * @param fileName
     */
    private String getLyricsText(String fileName) {
        if (Utils.isNullOrNil(fileName)) {
            return null;
        }
        String lrcText = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

    /**
     * 从歌词文本转化为歌词列表
     * @param lyricsText
     */
    private List<LyricsEntity> parseLyricsListFromLyricsText(String lyricsText) {
        if (Utils.isNullOrNil(lyricsText)) {
            return null;
        }
        List<LyricsEntity> entryList = new ArrayList<>();
        String[] array = lyricsText.split("\\n");
        for (String line : array) {
            Log.v(TAG, "line = " + line);
            List<LyricsEntity> list = parseLine(line);
            if (list != null && !list.isEmpty()) {
                entryList.addAll(list);
            }
        }

        Collections.sort(entryList);
        for(int i = 0; i < entryList.size(); i++) {
            Log.v(TAG, "lyrics, time = " + entryList.get(i).getStartTime() + "; text = " + entryList.get(i).getContent());
        }
        return entryList;
    }

    private List<LyricsEntity> parseLine(String line) {
        if (Utils.isNullOrNil(line)) {
            return null;
        }
        line = line.trim();
        // [00:17.65]让我掉下眼泪的
        Matcher lineMatcher = Pattern.compile("((\\[\\d\\d:\\d\\d\\.\\d{2,3}\\])+)(.+)").matcher(line);   //(.+)表示贪婪匹配   .匹配除“\n"之外的任何单个字符。要匹配包括"\n"在内的任何字符，请使用像"(.|\n)"的模式。
        if (!lineMatcher.matches()) {
            return null;
        }
        String times = lineMatcher.group(1);   // 匹配第一个括号的内容
        String text = lineMatcher.group(3);    // 匹配第三个括号的内容

        List<LyricsEntity> list = new ArrayList<>();
        // [00:17.65]
        Matcher timeMatcher = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d{2,3})\\]").matcher(times);
        while (timeMatcher.find()) {
            long min = Long.parseLong(timeMatcher.group(1));
            long sec = Long.parseLong(timeMatcher.group(2));
            String milString = timeMatcher.group(3);
            long mil = Long.parseLong(milString);
            // 如果毫秒是两位数，需要乘以10
            if (milString.length() == 2) {
                mil = mil * 10;
            }
            long time = min * DateUtils.MINUTE_IN_MILLIS + sec * DateUtils.SECOND_IN_MILLIS + mil;
            list.add(new LyricsEntity(text, time));
        }
        return list;
    }

    /**
     *添加监听
     */
    private void addListener() {
        mPlayOrPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsPlay) {
                    mPlayer.start();
                    mIsPlay = true;
                    mPlayOrPauseBtn.setText("暂停");
                } else {
                    mPlayer.pause();
                    mIsPlay = false;
                    mPlayOrPauseBtn.setText("播放");
                }
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int currentTime = progress * mMediaDuration / mSeekBar.getMax();
                    mPlayer.seekTo(currentTime);
                    if (mMediaDuration > 0) {
                        mSeekBar.setProgress(currentTime * mSeekBar.getMax() / mMediaDuration);
                    }
                    int currentline = mLyricsView.getCurrentIndex(currentTime);
                    mLyricsView.setCurrentLine(currentline);
                    Log.v(TAG, "currentTime = " + currentTime + "; currentline = " + currentline);
                    mLyricsView.updateOffset(currentline);
                    mLyricsView.invalidate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mIsPlay = false;
                mPlayOrPauseBtn.setText("播放");
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.v(TAG, "onError, what = " + what + "; extra = " + extra);
                return true;
            }
        });

        mLyricsView.setSeekBarListener(new LyricsView.SeekBarListener() {
            @Override
            public void seekTo(long milliseconds) {
                mPlayer.seekTo((int)milliseconds);
                mSeekBar.setProgress((int)milliseconds * mSeekBar.getMax() / mMediaDuration);
            }
        });
    }
}