package com.ztq.sdk.utils;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.ztq.sdk.R;

/**
 * Author: zhengjf
 * Date: 2020/12/16 17:41
 * Description: 连续执行多个动画工具
 */
public class AnimDrawableUtil {

    private static final String TAG = "noahedu." + AnimDrawableUtil.class.getSimpleName();

    private static final int TAG_TASK = R.id.anim_drawable_util_task;

    public interface OnAnimationListener {
        void onAnimEnd(int resId, boolean isFinal);
    }

    /**
     * 连续通过setBackground播放多个动画
     * 如果内存不够直接执行onAnimationEnd
     */
    public void startBgAnims(View view, OnAnimationListener onAnimationListener, int... resIds) {
        startBgAnims(view, onAnimationListener, false, resIds);
    }

    public void startBgAnims(View view, OnAnimationListener onAnimationListener, boolean isAsyncLoad, int... resIds) {

        stopAnim(view);
        TaskData taskData = new TaskData();
        taskData.view = view;
        taskData.resIds = resIds;
        taskData.playingIndex = 0;
        taskData.onAnimationListener = onAnimationListener;
        if (resIds == null || resIds.length == 0) {
            return;
        }
        view.setTag(TAG_TASK, taskData);
        if (isAsyncLoad && taskData.currentDrawable== null) {

            AnimationDrawable drawable = taskData.currentDrawable;
            if (drawable == null) {
                new Thread() {
                    @Override
                    public void run() {
                        taskData.initCurrentDrawable();
                        HandlerUtil.getMainH().post(new Runnable() {
                            @Override
                            public void run() {
                                if (!ContextUtil.isContextAlive(view.getContext())) {
                                    return;
                                }
                                startTask(taskData);
                            }
                        });
                    }
                }.start();
            }
        } else {
            startTask(taskData);
        }
    }

    public void stopAnim(View view) {
        Drawable drawble = view.getBackground();
        if (drawble != null && drawble instanceof AnimationDrawable) {
            AnimationDrawable animationDrawable = (AnimationDrawable)drawble;
            animationDrawable.stop();
        }
        Object tag = view.getTag(TAG_TASK);
        if (tag != null && tag instanceof TaskData) {
            TaskData taskData = (TaskData) tag;
            taskData.isStop = true;
            view.setTag(TAG_TASK, null);
        }
    }

    private void startTask(TaskData taskData) {
        if (taskData.isStop) {
            return;
        }
        AnimationDrawable drawable = taskData.currentDrawable;
        if (drawable == null) {
            taskData.initCurrentDrawable();
        }

        drawable = taskData.currentDrawable;

        if (drawable == null) {
            invokeAnimListener(taskData);
            if (taskData.isLastTask()) {
                return;
            } else {
                Log.e(TAG, "~drawable is null");
            }
            taskData.initNextDrawble();
            taskData.nextData();
            startTask(taskData);
            return;
        }
        taskData.view.setBackground(drawable);
        drawable.start();
        int time = getAnimDrawableDuration(drawable);
        if (taskData.onAnimationListener == null && taskData.isLastTask()) {
            return;
        }
        HandlerUtil.getMainH().postDelayed(new Runnable() {
            @Override
            public void run() {
                invokeAnimListener(taskData);
                if (taskData.isStop) {
                    return;
                }
                taskData.nextData();
                startTask(taskData);
            }
        }, time);

        new Thread() {
            @Override
            public void run() {
                super.run();
                taskData.initNextDrawble();
            }
        }.start();
    }

    private void invokeAnimListener(TaskData taskData) {
        if (taskData.onAnimationListener == null) {
            return;
        }
        int index = taskData.playingIndex;
        if (index < taskData.resIds.length) {
            taskData.onAnimationListener.onAnimEnd(taskData.resIds[index], taskData.isLastTask());
        }
    }

    private int getAnimDrawableDuration(AnimationDrawable animationDrawable) {
        if (animationDrawable.getNumberOfFrames() > 0) {
            return animationDrawable.getNumberOfFrames() * animationDrawable.getDuration(0);
        }
        return 0;
    }

    private class TaskData {
        private View view;
        private int[] resIds;
        private int playingIndex;
        private AnimationDrawable currentDrawable;
        private AnimationDrawable nextDrawable;
        private OnAnimationListener onAnimationListener;
        private volatile boolean isStop;

        private boolean isLastTask() {
            return playingIndex + 1 >= resIds.length;
        }

        private void nextData() {
            playingIndex++;
            currentDrawable = nextDrawable;
            nextDrawable = null;
        }

        private void initCurrentDrawable() {
            if (playingIndex < resIds.length) {
                currentDrawable = createDrawable(playingIndex);
            }
        }

        private void initNextDrawble() {
            if (!isLastTask()) {
                nextDrawable = createDrawable(playingIndex + 1);
            }
        }

        private synchronized AnimationDrawable createDrawable(int index) {
            Log.d(TAG, "~createDrawable " + index);
            Context context = view.getContext();
            int resId = resIds[index];
            try {
                return (AnimationDrawable) context.getResources().getDrawable(resId);
            } catch (Error e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}