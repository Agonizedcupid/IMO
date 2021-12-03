package com.aariyan.imo_template.Model;

public class AnsweredQuestionModel {
    private String id,questionId;

    public AnsweredQuestionModel(){}

    public AnsweredQuestionModel(String id, String questionId) {
        this.id = id;
        this.questionId = questionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
