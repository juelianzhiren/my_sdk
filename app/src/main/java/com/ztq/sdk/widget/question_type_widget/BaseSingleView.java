package com.ztq.sdk.widget.question_type_widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ztq.sdk.R;
import com.ztq.sdk.entity.SingleEntity;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.utils.AnimDrawableUtil;
import com.ztq.sdk.utils.QuestionUtil;
import com.ztq.sdk.utils.SoundUtil;
import com.ztq.sdk.utils.StringUtil;
import com.ztq.sdk.utils.ToastUtils;
import com.ztq.sdk.widget.MyTextView;
import com.ztq.sdk.widget.SingleTextWithPinyinView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 显示题目信息中，针对题干、题目、选项、答案、解析的基础view
 * 这个类在慢慢完善，因为还不能兼容很多种情况
 */
public class BaseSingleView extends LinearLayout {
    private static final String TAG = "noahedu.BaseSingleView";
    private Context context;
    private boolean autoPlaySound; // 是否需要自动播放
    private boolean isAutoPlayed; // 是否已自动播放
    private float normalTextSize;
    private int normalTextColor;
    private float pinyinSize;
    private int pinyinColor;
    private float editTextSize;
    private int editTextColor;
    private int editTextWidth;
    private float maxLength;
    private android.graphics.drawable.Drawable soundPicDrawable;
    private int soundAnimPicDrawableId;
    private android.graphics.drawable.Drawable videoPicDrawable;
    private int maxPicWidth;  // 标签为<img的图片最大显示宽度
    private int maxPicHeight; // 标签为<img的图片最大显示高度
    private int userAnswerAreaWidth;
    private int userAnswerTextColor;

    private AnimDrawableUtil animDrawableUtil;
    private SoundUtil soundUtil;

    private android.view.View animIv;
    private TextPaint textPaint;
    private OnElementClickListener onElementClickListener;

    private java.util.List<java.util.List<SingleEntity>> list;

    private OnClickListener soundClickListener = new OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            if (v.getTag() == null || !(v.getTag() instanceof SingleEntity)) {
                return;
            }
            SingleEntity entity = (SingleEntity) v.getTag();
            if (TextUtils.isEmpty(entity.getStr())) {
                return;
            }
            soundUtil.stop();
            animIv = v;
            animDrawableUtil.startBgAnims(v, null, soundAnimPicDrawableId);
            MyHandlerThread.postToWorker1(new Runnable() {
                @Override
                public void run() {
                    soundUtil.play(entity.getStr(), new SoundUtil.OnSoundFinishListener() {
                        @Override
                        public void onSoundFinish() {
                            MyHandlerThread.postToMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    animDrawableUtil.stopAnim(v);
                                    v.setBackground(soundPicDrawable);
                                }
                            });
                        }

                        @Override
                        public void onSoundException(Exception e) {
                            MyHandlerThread.postToMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (e instanceof IOException){
                                        ToastUtils.show("声音文件不存在");
                                    }
                                    animDrawableUtil.stopAnim(v);
                                    v.setBackground(soundPicDrawable);
                                }
                            });
                        }
                    });
                }
            });
        }
    };

    private OnClickListener picClickListener = new OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            if (v.getTag(R.id.item) == null || !(v.getTag(R.id.item) instanceof SingleEntity)) {
                return;
            }
            SingleEntity entity = (SingleEntity) v.getTag(R.id.item);
            if (TextUtils.isEmpty(entity.getStr())) {
                return;
            }
            if (onElementClickListener != null) {
                onElementClickListener.onElementClick(v, entity);
            }
        }
    };

    public BaseSingleView(Context context) {
        this(context, null);
    }

    public BaseSingleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSingleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setOnElementClickListener(OnElementClickListener onElementClickListener) {
        this.onElementClickListener = onElementClickListener;
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseSingleView);
        autoPlaySound = ta.getBoolean(R.styleable.BaseSingleView_autoPlaySound, false);
        normalTextSize = ta.getDimension(R.styleable.BaseSingleView_normalTextSize, context.getResources().getDimension(R.dimen.normal_text_size));
        editTextSize = ta.getDimension(R.styleable.BaseSingleView_editTextSize, context.getResources().getDimension(R.dimen.edit_text_size));
        normalTextColor = ta.getColor(R.styleable.BaseSingleView_normalTextColor, context.getResources().getColor(R.color.normal_text_color));
        editTextColor = ta.getColor(R.styleable.BaseSingleView_editTextColor, context.getResources().getColor(R.color.edit_text_color));
        maxLength = ta.getDimension(R.styleable.BaseSingleView_maxLen, Float.MAX_VALUE);
        soundPicDrawable = ta.getDrawable(R.styleable.BaseSingleView_soundPicRes);
        soundAnimPicDrawableId = ta.getResourceId(R.styleable.BaseSingleView_soundAnimPicRes, R.drawable.ns_exercise_sound_playing);
        videoPicDrawable = ta.getDrawable(R.styleable.BaseSingleView_videoPicRes);
        maxPicWidth = (int) ta.getDimension(R.styleable.BaseSingleView_maxPicWidth, context.getResources().getDimension(R.dimen.max_pic_width));
        maxPicHeight = (int) ta.getDimension(R.styleable.BaseSingleView_maxPicHeight, context.getResources().getDimension(R.dimen.max_pic_height));
        userAnswerAreaWidth = (int) ta.getDimension(R.styleable.BaseSingleView_userAnswerAreaWidth, context.getResources().getDimension(R.dimen.user_answer_area_width));
        userAnswerTextColor = (int) ta.getColor(R.styleable.BaseSingleView_userAnswerTextColor, context.getResources().getColor(R.color.user_answer_text_color));
        pinyinSize = ta.getDimension(R.styleable.BaseSingleView_pinyinSize, normalTextSize * 0.4f);
        pinyinColor = ta.getColor(R.styleable.BaseSingleView_pinyinColor, normalTextColor);
        editTextWidth = (int)ta.getDimension(R.styleable.BaseSingleView_editTextWidth, context.getResources().getDimension(R.dimen.edit_text_area_width));

        ta.recycle();
        setOrientation(LinearLayout.VERTICAL);
        soundUtil = new SoundUtil(context);
        animDrawableUtil = new AnimDrawableUtil();
        textPaint = new TextPaint();
    }

    public void setMaxLength(float maxLength) {
        this.maxLength = maxLength;
    }

    public void setData(String str) {
        android.util.Log.v(TAG, "setData, str = " + str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
//        List<SingleEntity> preAssemblyList = StringUtil.parseData(str, normalTextSize, normalTextColor, pinyinSize, pinyinColor);

        java.util.List<SingleEntity> preAssemblyList = StringUtil.parseData2(str, normalTextSize, normalTextColor, pinyinSize, pinyinColor);
        android.util.Log.v(TAG, "preAssemblyList = " + preAssemblyList);

        for (SingleEntity entity : preAssemblyList) {   // 对远程的图片做处理
            if (entity != null) {
                if (entity.getTag() == SingleEntity.TAG_PIC) {
                    queryPicWidth(entity, preAssemblyList);
                }
            }
        }
        android.util.Log.v(TAG, "preAssemblyList list = " + preAssemblyList);
        list = reassemblingData(preAssemblyList, maxLength);
        android.util.Log.v(TAG, "list = " + list);
        updateUI(list);
    }

    public void setUserAnswer(String userAnswer) {
        if (list == null || list.size() == 0) {
            return;
        }
        loop:
        for (java.util.List<SingleEntity> childList : list) {
            if (childList != null) {
                for (SingleEntity entity : childList) {
                    if (entity != null && entity.getTag() == SingleEntity.TAG_USER_ANSWER_AREA_TEXT) {
                        entity.setStr(userAnswer);
                    }
                }
            }
        }
        updateUI(list);
    }

    /**
     * 查询远程图片的宽度
     */
    private void queryPicWidth(SingleEntity entity, java.util.List<SingleEntity> preAssemblyList) {
        if (entity == null || entity.getTag() != SingleEntity.TAG_PIC || preAssemblyList == null) {
            return;
        }
        if (entity.getTag() == SingleEntity.TAG_PIC) {
            Glide.with(context).load(entity.getStr()).override(maxPicWidth, maxPicHeight).fitCenter().into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    Drawable bitmapDrawable = (Drawable) resource;
                    entity.setWidth(bitmapDrawable.getIntrinsicWidth());
                    java.util.List<java.util.List<SingleEntity>> list = reassemblingData(preAssemblyList, maxLength);
                    updateUI(list);
                }
            });
        }
    }

    private java.util.List<java.util.List<SingleEntity>> reassemblingData(java.util.List<SingleEntity> list, float maxLength) {
        removeAllViews();
        java.util.List<java.util.List<SingleEntity>> targetList = new ArrayList<>();
        if (list == null || list.size() == 0) {
            return targetList;
        }
        float aLineWidth = 0;
        java.util.List<SingleEntity> childList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SingleEntity entity = list.get(i);
            if (entity != null) {
                if (entity.getTag() == SingleEntity.TAG_NORMAL_TEXT || entity.getTag() == SingleEntity.TAG_FONT || entity.getTag() == SingleEntity.TAG_PINYIN) {
                    textPaint.setTextSize(entity.getTextSize());
                    textPaint.setColor(entity.getTextColor());
                    textPaint.setFontFeatureSettings("'liga' off");
                    if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_NORMAL) {
                        textPaint.setFakeBoldText(false);
                        textPaint.setTextSkewX(0);
                    } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_BOLD) {
                        textPaint.setFakeBoldText(true);
                        textPaint.setTextSkewX(0);
                    } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_ITALIC) {
                        textPaint.setFakeBoldText(false);
                        textPaint.setTextSkewX(-0.2f);
                    } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_BOLD_AND_ITALIC) {
                        textPaint.setFakeBoldText(true);
                        textPaint.setTextSkewX(-0.2f);
                    }
                    String str = entity.getStr();
                    int start = 0;
                    int end = 1;
                    loop:
                    while (end <= str.length()) {
                        String subStr = str.substring(start, end);
                        if (aLineWidth + textPaint.measureText(subStr) <= maxLength) {
                            // 是否满足 I am a student, I want to go sch
                            //          ool
                            // 即一行的最后一个字符为英文字母，且是一个单词的中间部分， 即一个单词被分隔显示两行了
                            if (StringUtil.isEnglishLetter(str.substring(end - 1, end))) {

//                                try {
//                                    Log.v(TAG, "currentLetter is = " + str.substring(end - 1, end) + ", start = " + start + ", end = " + end + ", aLineWidth = " + aLineWidth + ", textPaint.measureText(str.substring(start, end + 1)) = " + textPaint.measureText(str.substring(start, end + 1)) + ", maxLength = " + maxLength + ", next letter is = " + str.substring(end, end + 1) + ", subStr = " + subStr);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }

                                if (end < str.length()) {
                                    if (StringUtil.isEnglishLetter(str.substring(end, end + 1)) && aLineWidth + textPaint.measureText(str.substring(start, end + 1)) >= maxLength) {
                                        int index1 = end - 1;
                                        int index2 = end + 1;
                                        while (true) {
                                            if (index1 >= 0) {
                                                if (StringUtil.isEnglishLetter(str.substring(index1 - 1, index1))) {
                                                    index1--;
                                                } else {
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        while (true) {
                                            if (index2 < str.length()) {
                                                if (StringUtil.isEnglishLetter(str.substring(index2, index2 + 1))) {
                                                    index2++;
                                                } else {
                                                    break;
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                        if (textPaint.measureText(str.substring(index1, index2)) <= maxLength) {
                                            try {
                                                SingleEntity newEntity = (SingleEntity) entity.clone();
                                                newEntity.setStr(str.substring(start, index1));
                                                android.util.Log.v(TAG, "111 subStr = " + str.substring(start, index1));
                                                childList.add(newEntity);
                                                targetList.add(childList);
                                                childList = new ArrayList<>();
                                            } catch (CloneNotSupportedException e) {
                                                e.printStackTrace();
                                            }
                                            start = index1;
                                            end = start + 1;
                                            aLineWidth = 0;
                                            continue;
                                        }
                                    } else if (!StringUtil.isEnglishLetter(str.substring(end, end + 1)) && aLineWidth + textPaint.measureText(str.substring(start, end + 1)) == maxLength) {
                                        try {
                                            SingleEntity newEntity = (SingleEntity) entity.clone();
                                            newEntity.setStr(str.substring(start, end + 1));
                                            childList.add(newEntity);
                                            targetList.add(childList);
                                            childList = new ArrayList<>();
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                        start = end + 1;
                                        end = start + 1;
                                        aLineWidth = 0;
                                        continue;
                                    }

                                }
                            }
                            subStr = str.substring(start, end);
//                            aLineWidth += textPaint.measureText(subStr);

                            if (end < str.length() && aLineWidth + textPaint.measureText(str.substring(start, end + 1)) > maxLength) {
                                try {
                                    SingleEntity newEntity = (SingleEntity) entity.clone();
                                    newEntity.setStr(subStr);
                                    childList.add(newEntity);
                                    targetList.add(childList);
                                    childList = new ArrayList<>();
                                    start = end;
                                    end = start + 1;
                                    aLineWidth = 0;
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (end == str.length()) {
                                try {
                                    SingleEntity newEntity = (SingleEntity) entity.clone();
                                    newEntity.setStr(subStr);
                                    childList.add(newEntity);
                                    aLineWidth += textPaint.measureText(subStr);
                                    if (i == list.size() - 1) {
                                        targetList.add(childList);
                                    }
                                    break;
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            end++;
                        } else {
                            targetList.add(childList);
                            childList = new ArrayList<>();
                            aLineWidth = 0;
                            if (end == str.length()) {
                                try {
                                    SingleEntity newEntity = (SingleEntity) entity.clone();
                                    newEntity.setStr(subStr);
                                    childList.add(newEntity);
                                    aLineWidth += textPaint.measureText(subStr);
                                    if (i == list.size() - 1) {
                                        targetList.add(childList);
                                    }
                                    break;
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else if (entity.getTag() == SingleEntity.TAG_PIC) {
                    if (aLineWidth + entity.getWidth() > maxLength) {
                        if (aLineWidth == 0) {  // 如果单单放一张图片，宽度都不够，采取的措施：将图片放入这一行，不用管宽度限制
                            childList.add(entity);
                            targetList.add(childList);
                            childList = new ArrayList<>();
                            aLineWidth = 0;
                        } else {
                            targetList.add(childList);
                            childList = new ArrayList<>();
                            childList.add(entity);
                            aLineWidth = entity.getWidth();
                        }
                    } else {
                        childList.add(entity);
                        aLineWidth += entity.getWidth();
                        if (i == list.size() - 1) {
                            targetList.add(childList);
                        }
                    }
                } else if (entity.getTag() == SingleEntity.TAG_SOUND) {
                    if (aLineWidth + entity.getWidth() > maxLength) {
                        if (aLineWidth == 0) {  // 如果单单放一张声音图片，宽度都不够，采取的措施：将图片放入这一行，不用管宽度限制
                            childList.add(entity);
                            targetList.add(childList);
                            childList = new ArrayList<>();
                            aLineWidth = 0;
                        } else {
                            targetList.add(childList);
                            childList = new ArrayList<>();
                            childList.add(entity);
                            aLineWidth = entity.getWidth();
                        }
                    } else {
                        childList.add(entity);
                        aLineWidth += entity.getWidth();
                        if (i == list.size() - 1) {
                            targetList.add(childList);
                        }
                    }
                } else if (entity.getTag() == SingleEntity.TAG_VIDEO) {
                    if (aLineWidth + entity.getWidth() > maxLength) {
                        if (aLineWidth == 0) {  // 如果单单放一张视频图片，宽度都不够，采取的措施：将图片放入这一行，不用管宽度限制
                            childList.add(entity);
                            targetList.add(childList);
                            childList = new ArrayList<>();
                            aLineWidth = 0;
                        } else {
                            targetList.add(childList);
                            childList = new ArrayList<>();
                            childList.add(entity);
                            aLineWidth = entity.getWidth();
                        }
                    } else {
                        childList.add(entity);
                        aLineWidth += entity.getWidth();
                        if (i == list.size() - 1) {
                            targetList.add(childList);
                        }
                    }
                } else if (entity.getTag() == SingleEntity.TAG_NEWLINE) {
                    targetList.add(childList);
                    childList = new ArrayList<>();
                    aLineWidth = 0;
                    if (i == list.size() - 1) {
                        targetList.add(childList);
                    }
                } else if (entity.getTag() == SingleEntity.TAG_USER_ANSWER_AREA_TEXT) {
                    if (aLineWidth + userAnswerAreaWidth > maxLength) {
                        targetList.add(childList);
                        childList = new ArrayList<>();
                        childList.add(entity);
                        aLineWidth = userAnswerAreaWidth;
                    } else {
                        childList.add(entity);
                        aLineWidth += userAnswerAreaWidth;
                        if (i == list.size() - 1) {
                            targetList.add(childList);
                        }
                    }
                } else if (entity.getTag() == SingleEntity.TAG_EDIT_TEXT) {
                    if (aLineWidth + editTextWidth > maxLength) {
                        targetList.add(childList);
                        childList = new ArrayList<>();
                        childList.add(entity);
                        aLineWidth = editTextWidth;
                    } else {
                        childList.add(entity);
                        aLineWidth += editTextWidth;
                        if (i == list.size() - 1) {
                            targetList.add(childList);
                        }
                    }
                }
            }
        }
        return targetList;
    }

    private void updateUI(java.util.List<java.util.List<SingleEntity>> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        android.util.Log.v(TAG, "updateUI");
        removeAllViews();
        for (java.util.List<SingleEntity> childList : list) {
            if (childList != null && childList.size() > 0) {
                LinearLayout ll = createLinearLayout();
                addView(ll);
                boolean isContainPinyin = QuestionUtil.isContainPinyinItem(childList);
                for (SingleEntity entity : childList) {
                    if (entity != null) {
                        if (entity.getTag() == SingleEntity.TAG_NORMAL_TEXT) {
                            if (TextUtils.isEmpty(entity.getPinyin())) {
                                MyTextView tv = new MyTextView(context);
//                                tv.setBackgroundColor(Color.parseColor("#e6e6e6"));
                                tv.setFontFeatureSettings("'liga' off");
                                tv.setText(entity.getStr());
                                tv.setGravity(Gravity.CENTER_VERTICAL);
                                tv.getPaint().setTextSize(entity.getTextSize());
                                if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_NORMAL) {
                                    tv.getPaint().setFakeBoldText(false);
                                    tv.getPaint().setTextSkewX(0);
                                } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_BOLD) {
                                    tv.getPaint().setFakeBoldText(true);
                                    tv.getPaint().setTextSkewX(0);
                                } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_ITALIC) {
                                    tv.getPaint().setFakeBoldText(false);
                                    tv.getPaint().setTextSkewX(-0.2f);
                                } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_BOLD_AND_ITALIC) {
                                    tv.getPaint().setFakeBoldText(true);
                                    tv.getPaint().setTextSkewX(-0.2f);
                                }
                                android.util.Log.v(TAG, "str = " + entity.getStr() + ", textFlag = " + entity.getTextFlag());
                                if (entity.getTextFlag() == SingleEntity.TEXT_FLAG_UNDERLINE) {
                                    tv.setFlag(MyTextView.FLAG_UNDERLINE);
                                } else if (entity.getTextFlag() == SingleEntity.TEXT_FLAG_BOTTOM_DOT) {
                                    tv.setFlag(MyTextView.FLAG_UNDER_DOT);
                                } else if (entity.getTextFlag() == SingleEntity.TEXT_FLAG_BOTTOM_WAVE) {
                                    tv.setFlag(MyTextView.FLAG_UNDER_WAVE);
                                }

                                tv.setTextColor(entity.getTextColor());
                                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                                if (isContainPinyin) {
                                    params.gravity = Gravity.BOTTOM;
                                } else {
                                    params.gravity = Gravity.TOP;
                                }
                                ll.addView(tv, params);
                            } else {
                                SingleTextWithPinyinView tv = new SingleTextWithPinyinView(context);
                                tv.setData(entity);
                                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                                params.gravity = Gravity.BOTTOM;
                                ll.addView(tv, params);
                            }
                        } else if (entity.getTag() == SingleEntity.TAG_PIC) {
                            ImageView iv = new ImageView(context);
                            Glide.with(context).load(entity.getStr()).fitCenter().placeholder(R.drawable.ic_placeholder).override((int) maxPicWidth, (int) maxPicHeight).into(iv);
                            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            if (isContainPinyin) {
                                params.gravity = Gravity.BOTTOM;
                            } else {
                                params.gravity = Gravity.TOP;
                            }
                            ll.addView(iv, params);
                            iv.setTag(R.id.item, entity);
                            iv.setOnClickListener(picClickListener);
                        } else if (entity.getTag() == SingleEntity.TAG_SOUND) {
                            ImageView iv = new ImageView(context);
                            iv.setTag(entity);
                            iv.setOnClickListener(soundClickListener);
                            iv.setBackground(soundPicDrawable);
                            android.util.Log.v(TAG, "sound drawable, width = " + soundPicDrawable.getIntrinsicWidth() + ", height = " + soundPicDrawable.getIntrinsicHeight());
                            entity.setWidth(soundPicDrawable.getIntrinsicWidth());
                            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            if (isContainPinyin) {
                                params.gravity = Gravity.BOTTOM;
                            } else {
                                params.gravity = Gravity.TOP;
                            }
                            ll.addView(iv, params);
                            if (autoPlaySound && !isAutoPlayed) {
                                iv.performClick();
                                isAutoPlayed = true;
                            }
                        } else if (entity.getTag() == SingleEntity.TAG_VIDEO) {
                            ImageView iv = new ImageView(context);
                            iv.setImageDrawable(videoPicDrawable);
                            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            if (isContainPinyin) {
                                params.gravity = Gravity.BOTTOM;
                            } else {
                                params.gravity = Gravity.TOP;
                            }
                            ll.addView(iv, params);
                            entity.setWidth(videoPicDrawable.getIntrinsicWidth());
                            iv.setTag(R.id.item, entity);
                            iv.setOnClickListener(picClickListener);
                        } else if (entity.getTag() == SingleEntity.TAG_FONT) {
                            MyTextView tv = new MyTextView(context);
                            tv.setFontFeatureSettings("'liga' off");
                            tv.setText(entity.getStr());
                            tv.setGravity(Gravity.CENTER_VERTICAL);
                            tv.getPaint().setTextSize(entity.getTextSize());
                            if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_NORMAL) {
                                tv.getPaint().setFakeBoldText(false);
                                tv.getPaint().setTextSkewX(0);
                            } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_BOLD) {
                                tv.getPaint().setFakeBoldText(true);
                                tv.getPaint().setTextSkewX(0);
                            } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_ITALIC) {
                                tv.getPaint().setFakeBoldText(false);
                                tv.getPaint().setTextSkewX(-0.2f);
                            } else if (entity.getTextStyle() == SingleEntity.TEXT_STYLE_BOLD_AND_ITALIC) {
                                tv.getPaint().setFakeBoldText(true);
                                tv.getPaint().setTextSkewX(-0.2f);
                            }
                            android.util.Log.v(TAG, "str = " + entity.getStr() + ", textFlag = " + entity.getTextFlag());
                            if (entity.getTextFlag() == SingleEntity.TEXT_FLAG_UNDERLINE) {
                                tv.setFlag(MyTextView.FLAG_UNDERLINE);
                            } else if (entity.getTextFlag() == SingleEntity.TEXT_FLAG_BOTTOM_DOT) {
                                tv.setFlag(MyTextView.FLAG_UNDER_DOT);
                            } else if (entity.getTextFlag() == SingleEntity.TEXT_FLAG_BOTTOM_WAVE) {
                                tv.setFlag(MyTextView.FLAG_UNDER_WAVE);
                            }

                            tv.setTextColor(entity.getTextColor());
                            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            if (isContainPinyin) {
                                params.gravity = Gravity.BOTTOM;
                            } else {
                                params.gravity = Gravity.TOP;
                            }
                            ll.addView(tv, params);
                        } else if (entity.getTag() == SingleEntity.TAG_USER_ANSWER_AREA_TEXT) {
                            android.widget.TextView tv = new android.widget.TextView(context);
                            tv.setFontFeatureSettings("'liga' off");
                            tv.setGravity(Gravity.CENTER_HORIZONTAL);
                            tv.getPaint().setTextSize(entity.getTextSize());
                            tv.setTextColor(userAnswerTextColor);
                            tv.setWidth(userAnswerAreaWidth);
                            tv.setText(entity.getStr());
                            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            if (isContainPinyin) {
                                params.gravity = Gravity.BOTTOM;
                            } else {
                                params.gravity = Gravity.TOP;
                            }
                            ll.addView(tv, params);
                        } else if (entity.getTag() == SingleEntity.TAG_EDIT_TEXT) {
                            EditText et = new EditText(context);
                            et.setPadding(0, 0, 0, 0);
                            et.setFontFeatureSettings("'liga' off");
                            et.setGravity(Gravity.CENTER_HORIZONTAL);
                            et.getPaint().setTextSize(entity.getTextSize());
//                            et.setBackgroundResource(R.drawable.ic_edittext_normal);
                            et.setTextColor(editTextColor);
                            et.setWidth((int)editTextWidth);
                            et.setText(entity.getStr());
                            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            if (isContainPinyin) {
                                params.gravity = Gravity.BOTTOM;
                            } else {
                                params.gravity = Gravity.TOP;
                            }
                            ll.addView(et, params);
                        } else if (entity.getTag() == SingleEntity.TAG_PINYIN) {
                            SingleTextWithPinyinView tv = new SingleTextWithPinyinView(context);
                            tv.setData(entity);
                            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.BOTTOM;
                            ll.addView(tv, params);
                        }
                    }
                }
            }
        }
    }

    public boolean isAutoPlayed() {
        return isAutoPlayed;
    }

    public void setAutoPlayed(boolean autoPlayed) {
        isAutoPlayed = autoPlayed;
    }

    private LinearLayout createLinearLayout() {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        return ll;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        soundUtil.stop();
        if (animIv != null) {
            animDrawableUtil.stopAnim(animIv);
        }
    }

    interface OnElementClickListener {
        void onElementClick(android.view.View view, SingleEntity entity);
    }
}