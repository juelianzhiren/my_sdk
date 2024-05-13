package com.ztq.sdk.widget.question_type_widget;

import android.support.constraint.ConstraintLayout;

import com.ztq.sdk.entity.BeanExercises;

/**
 * 单选题
 */
public class SingleChooseView extends ConstraintLayout implements ViewInterface {
    private BeanExercises.BeanExercise beanExercise;

    @Override
    public void setMaxWidth(float maxWidth) {

    }

    @Override
    public void setData(BeanExercises.BeanExercise beanExercise) {
        this.beanExercise = beanExercise;
    }

    @Override
    public void setEditable(boolean isEditable) {

    }

    @Override
    public void saveUserTrack() {

    }

    @Override
    public void clearUserTrack() {

    }

    @Override
    public void restoreUserTrack() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void submit() {

    }

    @Override
    public Object getUserAnswer() {
        return null;
    }
}