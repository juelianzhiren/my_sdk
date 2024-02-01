package com.ztq.sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * 声音工具
 */
public class SoundUtil {
    private static final String TAG = "noahedu.SoundUtil";
    private static final Integer SYNC_SAM = Integer.valueOf(30012);

    private WeakReference<Context> contextRef;
    private MediaPlayer player;
    private OnSoundStateListener onSoundStateListener;

    public SoundUtil(Context context) {
        this.contextRef = new WeakReference<>(context);
        player = new MediaPlayer();
    }

    public interface OnSoundFinishListener {
        void onSoundFinish();

        void onSoundException(Exception e);
    }

    public interface OnSoundStateListener {
        int PLAYING = 1;
        int PAUSE = 2;
        int STOP = 3;

        void onSoundStateChange(int state);
    }

    public void setOnSoundStateListener(OnSoundStateListener l) {
        this.onSoundStateListener = l;
    }

    @SuppressLint("StaticFieldLeak")
    public void playLoop(final int resId) {
        try {
            player.reset();
            player.setLooping(true);
            AssetFileDescriptor fileDescriptor = contextRef.get().getResources().openRawResourceFd(resId);
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            fileDescriptor.close();
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.v(TAG, "onError, what = " + what + ", extra = " + extra);
                    return true;
                }
            });

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (onSoundStateListener != null) {
                        onSoundStateListener.onSoundStateChange(OnSoundStateListener.STOP);
                    }
                }
            });
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void play(final int resId, final OnSoundFinishListener l) {
        try {
            player.reset();
            AssetFileDescriptor fileDescriptor = contextRef.get().getResources().openRawResourceFd(resId);
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            fileDescriptor.close();
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.v(TAG, "onError, what = " + what + ", extra = " + extra);
                    if (extra == MediaPlayer.MEDIA_ERROR_IO) {
                        if (l != null) {
                            l.onSoundException(new IOException());
                        }
                    }
                    return true;
                }
            });

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (onSoundStateListener != null) {
                        onSoundStateListener.onSoundStateChange(OnSoundStateListener.STOP);
                    }
                    if (l != null) {
                        l.onSoundFinish();
                    }
                }
            });
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
            if (l != null) {
                l.onSoundException(e);
            }
        }
    }

    public void play(final String path, final OnSoundFinishListener l) {
        if (path == null) {
            Log.e(TAG, "~path is null");
            return;
        }
        try {
            player.reset();
            player.setDataSource(path);
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.v(TAG, "onError, what = " + what + ", extra = " + extra);
                    if (extra == MediaPlayer.MEDIA_ERROR_IO) {
                        if (l != null) {
                            l.onSoundException(new IOException());
                        }
                    }
                    return true;
                }
            });

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (l != null) {
                        l.onSoundFinish();
                    }
                    if (onSoundStateListener != null) {
                        onSoundStateListener.onSoundStateChange(OnSoundStateListener.STOP);
                    }
                }
            });
            player.prepare();
            startPlayInner();
        } catch (Exception e) {
            e.printStackTrace();
            if (l != null) {
                l.onSoundException(e);
            }
        }
    }

    public boolean isPlaying() {
        if (player == null) {
            return false;
        }
        return player.isPlaying();
    }

    public void pause() {
        synchronized (SYNC_SAM) {
            if (player == null) {
                return;
            }
            if (player.isPlaying()) {
                player.pause();
                if (onSoundStateListener != null) {
                    onSoundStateListener.onSoundStateChange(OnSoundStateListener.PAUSE);
                }
            }
        }
    }

    public void stop() {
        synchronized (SYNC_SAM) {
            if (player == null) {
                return;
            }
            if (onSoundStateListener != null) {
                onSoundStateListener.onSoundStateChange(OnSoundStateListener.STOP);
            }
            player.stop();
        }
    }

    public void release() {
        stop();
        player.release();
        player = null;
    }

    private boolean startPlayInner() {
        try {
            player.start();
            if (onSoundStateListener != null) {
                onSoundStateListener.onSoundStateChange(OnSoundStateListener.PLAYING);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void initPlayer(int resId) {
        if (contextRef.get() == null) {
            return;
        }
        synchronized (SYNC_SAM) {
            player.reset();
            AssetFileDescriptor fileDescriptor = contextRef.get().getResources().openRawResourceFd(resId);
            try {
                player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getFilePath(Context context, String url) {
        if (url == null) {
            return null;
        }

        int hashCode = url.hashCode();
        int lastIndexDot = url.lastIndexOf(".");
        int lastIndexAsk = url.lastIndexOf("?");
        lastIndexAsk = lastIndexAsk < lastIndexDot ? url.length() : lastIndexAsk;
        String suffix = lastIndexDot == -1 ? ".mp3" : url.substring(lastIndexDot, lastIndexAsk);
        String parentPath = context.getCacheDir() + "/sound/";
        if (!new File(parentPath).exists()) {
            new File(parentPath).mkdirs();
        }
        String filePath = context.getCacheDir() + "/sound/" + hashCode + suffix;
        return filePath;
    }
}