package com.ztq.sdk.widget.question_type_widget;

import com.ztq.sdk.entity.BeanExercises;

public interface ViewInterface {
    void setMaxWidth(float maxWidth);

    void setData(BeanExercises.BeanExercise beanExercise);

    void setEditable(boolean isEditable);

    void saveUserTrack();

    void clearUserTrack();

    void restoreUserTrack();

    void onResume();

    void onPause();

    void submit();

    Object getUserAnswer();
}