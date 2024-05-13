package com.ztq.sdk.widget.question_type_widget;

public class Define {
    public static final int SUB_TYPE_NONE = 0X00;
    public static final int SUB_TYPE_CHOOSE = 0x01;// 单项选择题
    public static final int SUB_TYPE_MULCHOOSE = 0x02;// 多项选择(固定顺序)
    public static final int SUB_TYPE_MULCHOOSE_U = 0x03;// 多项选择(顺序不固定)
    public static final int SUB_TYPE_TOF = 0x04;// 判断题
    public static final int SUB_TYPE_T_CHOOSE = 0x05;// 听力单选题
    public static final int SUB_TYPE_T_MULCHOOSE = 0x06;// 听力多选题
    public static final int SUB_TYPE_T_CHOOSE_CLICK = 0x07;// 听力点选
    public static final int SUB_TYPE_CHOOSE_CLICK = 0x08;//点选题
    public static final int SUB_TYPE_FILL = 0x10;//填空题
    public static final int SUB_TYPE_FILL_w = 0x11;//填空题
    public static final int SUB_TYPE_WORD_LATTICE = 0x12;//田字格题
    public static final int SUB_TYPE_T_FILL = 0x13;//听力填空题
    public static final int SUB_TYPE_T_FILL_v = 0x14;//听力填空题
    public static final int SUB_TYPE_LINE_V = 0x20;//连线题
    public static final int SUB_TYPE_LINE_H = 0x21;//连线题
    public static final int SUB_TYPE_SORT_SENT = 0x30;//句子排序
    public static final int SUB_TYPE_DRAG_SORT = 0x31;//连词成句
    public static final int SUB_TYPE_PIC_FILL_COLOR = 0x40;//颜色填充图片
    public static final int SUB_TYPE_PIC_FILL_STRING = 0x41;//字符串填充图片
    public static final int SUB_TYPE_PIC_FILL_PIC = 0x42;//图片填充图片
    public static final int SUB_TYPE_CLOZE = 0x50;// 完形填空
    public static final int SUB_TYPE_LISTEN_COMPOSITE = 0x60;//听力套题
    public static final int SUB_TYPE_READ = 0x70;// 阅读理解套题
    public static final int SUB_TYPE_SJANS = 0x80;// 主观题
    public static final int SUB_TYPE_DRAG_SORT_COMPOSITE = 0x90; //拖拽排序套题;

    public static final int SUB_TYPE_FILL_NO_ORDER = 0x99;// 填空题-无顺序
    public static final int SUB_TYPE_MUL_CLICK_CHOICE = 0x100;// 不定项点选题
    public static final int SUB_TYPE_HOLLOW_FILL = 0x101;// 挖空填空题
    public static final int SUB_TYPE_HOLLOW_CLICK_CHOICE = 0x102;// 挖空点选题
    public static final int SUB_TYPE_HOLLOW_MULTIPLE_CHOICE = 0x103;// 挖空多选题
    public static final int SUB_TYPE_VERTICAL_FILL = 0x104;// 竖式填空题
    public static final int SUB_TYPE_COMPOSITION = 0x108;// 作文拍搜题
    public static final int SUB_TYPE_ARITH = 0x128;// 新题型(四则运算)

    public static final int SUB_TYPE_VOICE_CHOOSE = 0x201;// 听说问答式(口语选择题)
    public static final int SUB_TYPE_VOICE_EVALUATE = 0x202;// 复述题(口语评测题)
    public static final int SUB_TYPE_DRAG = 0x210;// 拖拽题
    public static final int SUB_TYPE_FLOAT_CHOOSE = 0x211;// 视频单选题
    public static final int SUB_TYPE_MUL_SCORE_CHOOSE = 0x212;// 多分制单选题

    //答题的结果
    public static final int SUB_RESULT_NOTDO = 0;/*没有做*/

    public static final int SUB_RESULT_ALLRIGHT = 1;/*全对*/
    public static final int SUB_RESULT_NOTALLRIGHT = 2;/*半对*/
    public static final int SUB_RESULT_ALLERROR = 3;/*全错*/
    public static final int SUB_RESULT_NONE = -1; /*无*/
    public static final int SUB_RESULT_ALLRIGHT_MANUAL = 4;/*主观评分全对*/
    public static final int SUB_RESULT_NOTALLRIGHT_MANUAL = 5;/*主观评分半对*/
    public static final int SUB_RESULT_ALLERROR_MANUAL = 6;/*主观评分全错*/
    public static final int SUB_RESULT_HANDING = 7;/*主观评分进行中*/
    public static final int SUB_RESULT_ZHUGUAN = 8;/*主观题*/

    public static final String SOURCE_TYPE_YYT = "1";// 应用题模型
    public static final String SOURCE_TYPE_TXJSQ = "2";// 图形计算器
    public static final String SOURCE_TYPE_NOAHFLASH = "3";// 诺亚动漫
    public static final String SOURCE_TYPE_CROMATH = "4";// 脱式计算
    public static final String SOURCE_TYPE_VERMATH = "5";// 竖式计算
    public static final String SOURCE_TYPE_FRAME = "6";// 帧动画

    //矩形1;  方框2;   三角形3;   圆形4;  下划线5；  括号6
    public static final int FILL_TYPE_RECTANGLE = 1;
    public static final int FILL_TYPE_SQUARE = 2;
    public static final int FILL_TYPE_TRIANGLE = 3;
    public static final int FILL_TYPE_CIRCLE = 4;
    public static final int FILL_TYPE_UNDERLINE = 5;
    public static final int FILL_TYPE_PARENTHES = 6;


    public static String REPLACE_FOR_UNDERLINE = "________";

    public static final int SUBJECT_CHN = 1;
    public static final int SUBJECT_MATH = 2;
    public static final int SUBJECT_ENG = 3;
}