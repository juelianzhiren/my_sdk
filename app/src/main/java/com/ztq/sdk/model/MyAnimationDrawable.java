package com.ztq.sdk.model;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * @author ztq
 * 自定义会实时回收的帧动画（每显示完一帧就回收该帧的内存）
 ***/
public class MyAnimationDrawable {
    private static final String TAG = "pmc.MyAnimationDrawable";

    private final String TAG_ITEM = "item";
    private final String ATTRIBUTE_DRAWABLE = "drawable";
    private final String ATTRIBUTE_DURATION = "duration";
    private final String TAG_ANIMATION_LIST = "animation-list";
    private final String ATTRIBUTE_ONE_SHOT = "oneshot";
    private boolean mIsOneShot = true;
    private boolean mIsRunning;
    private boolean mIsDestroyed;

    private Handler mHander = new Handler();
    private List<MyFrame> mImageFrames = new ArrayList<>();

    private Bitmap mCurrentBitmap;

    public static class MyFrame {
        byte[] bytes;
        int duration;
        Drawable drawable;
    }

    public interface OnDrawableLoadedListener {
        public void onDrawableLoaded(List<MyFrame> myFrames);
    }

    // 1
    public void animateRawManuallyFromXML(int resourceId, final ImageView imageView, final Runnable onStart, final Runnable onComplete) {
        if (resourceId <= 0 || imageView == null) {
            return;
        }
        mImageFrames = new ArrayList<>();
        mIsRunning = true;
        loadRaw(resourceId, imageView.getContext(), new OnDrawableLoadedListener() {
            @Override
            public void onDrawableLoaded(List<MyFrame> myFrames) {
                if (onStart != null) {
                    onStart.run();
                }
                animateRawManually(myFrames, imageView, onComplete);
            }
        });
    }

    // 2
    private void loadRaw(final int resourceId, final Context context, final OnDrawableLoadedListener onDrawableLoadedListener) {
        if (resourceId <= 0 || context == null) {
            return;
        }
        loadFromXml(resourceId, context, onDrawableLoadedListener);
    }

    // 3
    private void loadFromXml(final int resourceId, final Context context, final OnDrawableLoadedListener onDrawableLoadedListener) {
        if (resourceId <= 0 || context == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                XmlResourceParser parser = context.getResources().getXml(resourceId);
                try {
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        Log.v(TAG, "eventType = " + eventType);
                        if (eventType == XmlPullParser.START_DOCUMENT) {

                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (parser.getName().equals(TAG_ITEM)) {
                                byte[] bytes = null;
                                int duration = 1000;

                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals(ATTRIBUTE_DRAWABLE)) {
                                        int resId = Integer.parseInt(parser.getAttributeValue(i).substring(1));

                                        Log.v(TAG, i + ": resId = " + parser.getAttributeValue(i).substring(1));

                                        bytes = Utils.inputStream2ByteArray(context.getResources().openRawResource(resId));
                                    } else if (parser.getAttributeName(i).equals(ATTRIBUTE_DURATION)) {
                                        duration = parser.getAttributeIntValue(i, 1000);

                                        Log.v(TAG, i + "; duration = " + duration);
                                    }
                                }
                                MyFrame myFrame = new MyFrame();
                                myFrame.bytes = bytes;
                                myFrame.duration = duration;
                                mImageFrames.add(myFrame);
                            } else if (parser.getName().equals(TAG_ANIMATION_LIST)) {
                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals(ATTRIBUTE_ONE_SHOT)) {
                                        mIsOneShot = Boolean.parseBoolean(parser.getAttributeValue(i));
                                        Log.v(TAG, "mIsOneShot = " + mIsOneShot);
                                    }
                                }
                            }
                        } else if (eventType == XmlPullParser.END_TAG) {

                        } else if (eventType == XmlPullParser.TEXT) {

                        }
                        eventType = parser.next();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e2) {
                    e2.printStackTrace();
                }
                // Run on UI Thread
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (onDrawableLoadedListener != null) {
                            onDrawableLoadedListener.onDrawableLoaded(mImageFrames);
                        }
                    }
                });
            }
        }).start();
    }

    // 4
    private void animateRawManually(List<MyFrame> myFrames, ImageView imageView, Runnable onComplete) {
        if (myFrames == null || imageView == null) {
            return;
        }
        animateRawManually(myFrames, imageView, onComplete, 0);
    }

    // 5
    private void animateRawManually(final List<MyFrame> myFrames, final ImageView imageView, final Runnable onComplete, final int frameIndex) {
        if (!mIsRunning || mIsDestroyed || myFrames == null || imageView == null || frameIndex >= myFrames.size()) {
            return;
        }
        final MyFrame thisFrame = myFrames.get(frameIndex);
        if (thisFrame == null) {
            return;
        }

        thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(), BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
        if (!(mIsOneShot && frameIndex == 0)) {
            MyFrame previousFrame = myFrames.get((frameIndex - 1 + myFrames.size()) % myFrames.size());
            if (previousFrame.drawable != null) {
                ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
                previousFrame.drawable = null;
            }
        }

        imageView.setImageDrawable(thisFrame.drawable);
        mCurrentBitmap = ((BitmapDrawable)thisFrame.drawable).getBitmap();
        Log.v(TAG, "bitmap size = " + mCurrentBitmap.getAllocationByteCount() / 1024f / 1024f + "M");
        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "postDelayed, " + frameIndex + "; mIsRunning = " + mIsRunning + "; isOneShot = " + mIsOneShot + "; myFrames size = " + myFrames.size() + "; this = " + MyAnimationDrawable.this);
                if (frameIndex + 1 >= myFrames.size() && mIsOneShot) {
                    if (onComplete != null) {
                        onComplete.run();
                        Log.v(TAG, "onComplete run");
                        mIsRunning = false;
                    }
                } else {
                    animateRawManually(myFrames, imageView, onComplete,(frameIndex + 1) % myFrames.size());
                }
            }
        }, thisFrame.duration);
    }

    /**
     * 加载动画资源
     * @param resourceId
     * @param context
     */
    public void loadAnimationRes(final Context context, final int resourceId) {
        if (resourceId <= 0 || context == null) {
            return;
        }
        mImageFrames.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                XmlResourceParser parser = context.getResources().getXml(resourceId);
                try {
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        Log.v(TAG, "eventType = " + eventType);
                        if (eventType == XmlPullParser.START_DOCUMENT) {

                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (parser.getName().equals(TAG_ITEM)) {
                                byte[] bytes = null;
                                int duration = 1000;

                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals(ATTRIBUTE_DRAWABLE)) {
                                        int resId = Integer.parseInt(parser.getAttributeValue(i).substring(1));

                                        Log.v(TAG, i + ": resId = " + parser.getAttributeValue(i).substring(1));

                                        bytes = Utils.inputStream2ByteArray(context.getResources().openRawResource(resId));
                                    } else if (parser.getAttributeName(i).equals(ATTRIBUTE_DURATION)) {
                                        duration = parser.getAttributeIntValue(i, 1000);

                                        Log.v(TAG, i + "; duration = " + duration);
                                    }
                                }
                                MyFrame myFrame = new MyFrame();
                                myFrame.bytes = bytes;
                                myFrame.duration = duration;
                                mImageFrames.add(myFrame);
                            } else if (parser.getName().equals(TAG_ANIMATION_LIST)) {
                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals(ATTRIBUTE_ONE_SHOT)) {
                                        mIsOneShot = Boolean.parseBoolean(parser.getAttributeValue(i));
                                        Log.v(TAG, "mIsOneShot = " + mIsOneShot);
                                    }
                                }
                            }
                        } else if (eventType == XmlPullParser.END_TAG) {

                        } else if (eventType == XmlPullParser.TEXT) {

                        }
                        eventType = parser.next();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 开始动画
     * @param imageView
     * @param onStart
     * @param onComplete
     */
    public void startAnim(final ImageView imageView, final Runnable onStart, final Runnable onComplete) {
        if (imageView == null || mImageFrames == null || mImageFrames.size() == 0) {
            return;
        }
        MyHandlerThread.postToMainThread(new Runnable() {
            @Override
            public void run() {
                if (onStart != null) {
                    onStart.run();
                }
                mIsRunning = true;
                animateRawManually(mImageFrames, imageView, onComplete);
            }
        });
    }

    /**
     * 动画是否在运行
     * @return
     */
    public boolean isRunning() {
        return mIsRunning;
    }

    public void setIsDestroyed(boolean mIsDestroyed) {
        this.mIsDestroyed = mIsDestroyed;
    }

    /**
     * 停止动画，同时清除内存
     */
    public void stopAnim() {
        mIsRunning = false;
        mHander.removeCallbacksAndMessages(null);
        Log.v(TAG, "mIsRunning = " + mIsRunning + "; this = " + this);
    }

    /**
     * 销毁的操作
     */
    public void destroy() {
        mIsRunning = false;
        mIsDestroyed = true;
        mImageFrames.clear();
        mHander.removeCallbacksAndMessages(null);
        if (mCurrentBitmap != null && !mCurrentBitmap.isRecycled()) {
            mCurrentBitmap.recycle();
        }
    }
}  