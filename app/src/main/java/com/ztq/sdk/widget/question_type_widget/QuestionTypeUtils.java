package com.ztq.sdk.widget.question_type_widget;

import com.ztq.sdk.entity.BeanExercises;

public class QuestionTypeUtils {
    public static int getQuestionType(BeanExercises.BeanExercise beanExercise) {
        if (beanExercise == null) {
            return Define.SUB_TYPE_NONE;
        }
        return beanExercise.getType();
    }

    public static ViewInterface getViewInterface(BeanExercises.BeanExercise beanExercise) {
        if (beanExercise == null) {
            return null;
        }
        int questionType = getQuestionType(beanExercise);
        ViewInterface viewInterface = null;
        switch (questionType) {
            case Define.SUB_TYPE_CHOOSE:
                viewInterface = new SingleChooseView();
                break;
            case Define.SUB_TYPE_MULCHOOSE:
                viewInterface = new MultiChooseView();
                break;
        }
        return viewInterface;
    }
}