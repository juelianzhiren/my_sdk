package com.demo.audiovideorelated.activity;

import android.Manifest;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.demo.audiovideorelated.R;
import com.noahedu.noah_permissions.OnPermissionCallback;
import com.noahedu.noah_permissions.Permission;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MediaExtractorMuxerActivity extends BaseActivity {
    private static final String TAG = "noahedu.MediaExtractorMuxerActivity";
    private static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();

    private MediaExtractor mMediaExtractor;
    private MediaMuxer mMediaMuxer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_media_exractor_muxer);
        checkPermissions();
    }

    private void checkPermissions() {
        // Marshmallow开始才用申请运行时权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permission.request(this, new OnPermissionCallback() {
                @Override
                public List<String> permissions() {
                    List<String> pers = new ArrayList<>();
                    pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    return pers;
                }

                @Override
                public void permissionsDenied() {
                    finish();
                }

                @Override
                public void allPermissionsGranted() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                process();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                @Override
                public void onNoPermissions() {

                }
            });
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        process();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private boolean process() throws IOException {
        mMediaExtractor = new MediaExtractor();
        mMediaExtractor.setDataSource(SDCARD_PATH + "/ss.mp4");

        int mVideoTrackIndex = -1;
        int framerate = 0;
        for (int i = 0; i < mMediaExtractor.getTrackCount(); i++) {
            MediaFormat format = mMediaExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            Log.v(TAG, "mime " + i + " = " + mime);
            if (!mime.startsWith("video/")) {
                continue;
            }
            framerate = format.getInteger(MediaFormat.KEY_FRAME_RATE);
            mMediaExtractor.selectTrack(i);
            mMediaMuxer = new MediaMuxer(SDCARD_PATH + "/ouput.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mVideoTrackIndex = mMediaMuxer.addTrack(format);
            Log.v(TAG, "framerate = " + framerate + ", mVideoTrackIndex = " + mVideoTrackIndex);
            mMediaMuxer.start();
        }

        if (mMediaMuxer == null) {
            return false;
        }

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        info.presentationTimeUs = 0;
        ByteBuffer buffer = ByteBuffer.allocate(500 * 1024);
        int sampleSize = 0;
        while ((sampleSize = mMediaExtractor.readSampleData(buffer, 0)) > 0) {
            Log.v(TAG, "sampleSize = " + sampleSize);
            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
            info.presentationTimeUs += 1000 * 1000 / framerate;
            mMediaMuxer.writeSampleData(mVideoTrackIndex, buffer, info);
            mMediaExtractor.advance();
        }

        mMediaExtractor.release();

        mMediaMuxer.stop();
        mMediaMuxer.release();

        return true;
    }
}