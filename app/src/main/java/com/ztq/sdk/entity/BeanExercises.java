package com.ztq.sdk.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class BeanExercises {

    private String status;

    private String msg;

    private List<BeanExercise> value;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setValue(List<BeanExercise> value) {
        this.value = value;
    }

    public List<BeanExercise> getValue() {
        return this.value;
    }

    public static class BeanExercise implements Serializable {
        private static final long serialVersionUID = 1250368949691896750L;

        private String summary;

        private String analyse;

        private String answer;

        private String answerShowSign;

        private List<BeanExercise> childQuestions;

        private int childs;

        private String codetype;

        private int difficultyid;

        private String dispAnswer;

        private long id;

        private String intro;

        private String kpids;

        private String kpnames;

        private String kpphaseids;

        private String kpsubjectids;

        private String options;

        private String optionsbak;

        private String qtypename;

        private String question;

        private List<QuestionVideo> questionVideos;

        private int score;

        private String sourceType;

        private int study_count;

        private int subjectid;

        private int type;

        private int xueduanid;

        private String comment;

        private String trunkImage;

        private List<Position> position;

        private String falliblePoint;
        private String errorAnswer;
        private String errorAnalysis;
        private String point;

        private String language_points;
        private String article_source;
        private String discourse_reading;
        private String analysis_sentences;
        private String text_translation;
        private String words_notes;

        private String result;  //本地需要用的变量
        private String categoryName;
        private String userAnswer;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setAnalyse(String analyse) {
            this.analyse = analyse;
        }

        public String getAnalyse() {
            return this.analyse;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAnswer() {
            return this.answer;
        }

        public String getAnswerShowSign() {
            return answerShowSign;
        }

        public void setAnswerShowSign(String answerShowSign) {
            this.answerShowSign = answerShowSign;
        }

        public void setChildQuestions(List<BeanExercise> childQuestions) {
            this.childQuestions = childQuestions;
        }

        public List<BeanExercise> getChildQuestions() {
            return this.childQuestions;
        }

        public void setChilds(int childs) {
            this.childs = childs;
        }

        public int getChilds() {
            return this.childs;
        }

        public void setCodetype(String codetype) {
            this.codetype = codetype;
        }

        public String getCodetype() {
            return this.codetype;
        }

        public void setDifficultyid(int difficultyid) {
            this.difficultyid = difficultyid;
        }

        public int getDifficultyid() {
            return this.difficultyid;
        }

        public void setDispAnswer(String dispAnswer) {
            this.dispAnswer = dispAnswer;
        }

        public String getDispAnswer() {
            return this.dispAnswer;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getIdLong() {
            return id;
        }

        public int getId() {
            return (int) this.id;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getIntro() {
            return this.intro;
        }

        public void setKpids(String kpids) {
            this.kpids = kpids;
        }

        public String getKpids() {
            return this.kpids;
        }

        public void setKpnames(String kpnames) {
            this.kpnames = kpnames;
        }

        public String getKpnames() {
            return this.kpnames;
        }

        public void setKpphaseids(String kpphaseids) {
            this.kpphaseids = kpphaseids;
        }

        public String getKpphaseids() {
            return this.kpphaseids;
        }

        public void setKpsubjectids(String kpsubjectids) {
            this.kpsubjectids = kpsubjectids;
        }

        public String getKpsubjectids() {
            return this.kpsubjectids;
        }

        public void setOptions(String options) {
            this.options = options;
        }

        public String getOptions() {
            return this.options;
        }

        public void setOptionsbak(String optionsbak) {
            this.optionsbak = optionsbak;
        }

        public String getOptionsbak() {
            return this.optionsbak;
        }

        public void setQtypename(String qtypename) {
            this.qtypename = qtypename;
        }

        public String getQtypename() {
            return this.qtypename;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getQuestion() {
            return this.question;
        }

        public void setQuestionVideos(List<QuestionVideo> questionVideos) {
            this.questionVideos = questionVideos;
        }

        public List<QuestionVideo> getQuestionVideos() {
            return this.questionVideos;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getScore() {
            return this.score;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getSourceType() {
            return this.sourceType;
        }

        public void setStudy_count(int study_count) {
            this.study_count = study_count;
        }

        public int getStudy_count() {
            return this.study_count;
        }

        public void setSubjectid(int subjectid) {
            this.subjectid = subjectid;
        }

        public int getSubjectid() {
            return this.subjectid;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }

        public void setXueduanid(int xueduanid) {
            this.xueduanid = xueduanid;
        }

        public int getXueduanid() {
            return this.xueduanid;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getTrunkImage() {
            return trunkImage;
        }

        public void setTrunkImage(String trunkImage) {
            this.trunkImage = trunkImage;
        }

        public List<Position> getPosition() {
            return position;
        }

        public void setPosition(List<Position> position) {
            this.position = position;
        }

        public String getFalliblePoint() {
            return falliblePoint;
        }

        public void setFalliblePoint(String falliblePoint) {
            this.falliblePoint = falliblePoint;
        }

        public String getErrorAnswer() {
            return errorAnswer;
        }

        public void setErrorAnswer(String errorAnswer) {
            this.errorAnswer = errorAnswer;
        }

        public String getErrorAnalysis() {
            return errorAnalysis;
        }

        public void setErrorAnalysis(String errorAnalysis) {
            this.errorAnalysis = errorAnalysis;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getLanguage_points() {
            return language_points;
        }

        public void setLanguage_points(String language_points) {
            this.language_points = language_points;
        }

        public String getArticle_source() {
            return article_source;
        }

        public void setArticle_source(String article_source) {
            this.article_source = article_source;
        }

        public String getDiscourse_reading() {
            return discourse_reading;
        }

        public void setDiscourse_reading(String discourse_reading) {
            this.discourse_reading = discourse_reading;
        }

        public String getAnalysis_sentences() {
            return analysis_sentences;
        }

        public void setAnalysis_sentences(String analysis_sentences) {
            this.analysis_sentences = analysis_sentences;
        }

        public String getText_translation() {
            return text_translation;
        }

        public void setText_translation(String text_translation) {
            this.text_translation = text_translation;
        }

        public String getWords_notes() {
            return words_notes;
        }

        public void setWords_notes(String words_notes) {
            this.words_notes = words_notes;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        @Override
        public String toString() {
            return "BeanExercise{" +
                    "summary='" + summary + '\'' +
                    "analyse='" + analyse + '\'' +
                    ", answer='" + answer + '\'' +
                    ", childQuestions=" + childQuestions +
                    ", childs=" + childs +
                    ", codetype='" + codetype + '\'' +
                    ", difficultyid=" + difficultyid +
                    ", dispAnswer='" + dispAnswer + '\'' +
                    ", id=" + id +
                    ", intro='" + intro + '\'' +
                    ", kpids='" + kpids + '\'' +
                    ", kpnames='" + kpnames + '\'' +
                    ", kpphaseids='" + kpphaseids + '\'' +
                    ", kpsubjectids='" + kpsubjectids + '\'' +
                    ", options='" + options + '\'' +
                    ", optionsbak='" + optionsbak + '\'' +
                    ", qtypename='" + qtypename + '\'' +
                    ", question='" + question + '\'' +
                    ", questionVideos=" + questionVideos +
                    ", score=" + score +
                    ", sourceType='" + sourceType + '\'' +
                    ", study_count=" + study_count +
                    ", subjectid=" + subjectid +
                    ", type=" + type +
                    ", xueduanid=" + xueduanid +
                    ", comment=" + comment +
                    ", falliblePoint=" + falliblePoint +
                    ", errorAnswer=" + errorAnswer +
                    ", errorAnalysis=" + errorAnalysis +
                    ", point=" + point +
                    '}';
        }
    }

    public static class QuestionVideo implements Serializable {
        private static final long serialVersionUID = 2194486514936933359L;

        private int questionId;

        private String videoId;

        private String videoName;

        private String videoType;

        public void setQuestionId(int questionId) {
            this.questionId = questionId;
        }

        public int getQuestionId() {
            return this.questionId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getVideoId() {
            return this.videoId;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getVideoName() {
            return this.videoName;
        }

        public void setVideoType(String videoType) {
            this.videoType = videoType;
        }

        public String getVideoType() {
            return this.videoType;
        }

        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            try {
                json.put("questionId", questionId);
                json.put("videoId", videoId);
                json.put("videoName", videoName);
                json.put("videoType", videoType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        public void fromJson(JSONObject json) {
            try {
                setQuestionId(json.getInt("questionId"));
                setVideoId(json.getString("videoId"));
                setVideoName(json.getString("videoName"));
                setVideoType(json.getString("videoType"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 挖空的位置
     */
    public static class Position implements Serializable {
        private static final long serialVersionUID = -4170318790673497366L;

        private int height; //挖空的高度
        private int width;//挖空的宽度
        private int left;//挖空与左边的距离
        private int top;//挖空与顶部的距离
        private int type;//空的类型

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    @Override
    public String toString() {
        return "BeanExercises{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", value=" + value +
                '}';
    }
}
