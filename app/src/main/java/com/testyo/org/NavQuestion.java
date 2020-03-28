package com.testyo.org;

public class NavQuestion {

    private String qNumber ;
    private String answered;
    private String unAttempted;
    private String notVisited;
    private String markForReview;
    private int totalQuestion;

    public NavQuestion() {


    }

    public NavQuestion(String qNumber) {
        this.qNumber = qNumber;

    }

    public void setqNumber(String qNumber) {
        this.qNumber = qNumber;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public void setUnAttempted(String unAttempted) {
        this.unAttempted = unAttempted;
    }

    public void setNotVisited(String notVisited) {
        this.notVisited = notVisited;
    }

    public void setMarkForReview(String markForReview) {
        this.markForReview = markForReview;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public String getqNumber() {
        return qNumber;
    }

    public String getAnswered() {
        return answered;
    }

    public String getUnAttempted() {
        return unAttempted;
    }

    public String getNotVisited() {
        return notVisited;
    }

    public String getMarkForReview() {
        return markForReview;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }
}
