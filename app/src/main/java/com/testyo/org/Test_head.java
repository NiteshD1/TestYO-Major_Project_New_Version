package com.testyo.org;

public class Test_head {


    private String totalQuestion = "20";
    private String name;
    private String link;
    private String Answerlink;
    private String solutionlink;
    private String qStartWith;
    private String oStartWith;
    private String o2StartWith;
    private String o3StartWith;
    private String o4StartWith;
    private String aStartWith;
    private String sStartWith;
    private String marks = "4";
    private String minusMarks ="1";
    private String totalTime ="20";
    public String answerorSolutionIncluded = "00"; // 00 describes that not icluded





    public Test_head(String name, String totalQuestion, String link, String qStartWith, String oStartWith, String o2StartWith, String o3StartWith, String o4StartWith, String aStartWith, String sStartWith, String marks, String minusMarks, String totalTime ,String answerorSolutionIncluded) {

        this.totalQuestion = totalQuestion;
        this.marks = marks;
        this.minusMarks = minusMarks;
        this.totalTime = totalTime;
        this.name = name;
        this.link = link;
        this.qStartWith = qStartWith;
        this.oStartWith = oStartWith;
        this.o2StartWith = o2StartWith;
        this.o3StartWith = o3StartWith;
        this.o4StartWith = o4StartWith;
        this.aStartWith = aStartWith;
        this.sStartWith = sStartWith;
        this.answerorSolutionIncluded = answerorSolutionIncluded;

    }


    public String getAnswerorSolutionIncluded() {
        return answerorSolutionIncluded;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public String getMarks() {
        return marks;
    }

    public String getMinusMarks() {
        return minusMarks;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getName() {
        return name;
    }

    public String getqStartWith() {
        return qStartWith;
    }

    public String getoStartWith() {
        return oStartWith;
    }
    public String geto2StartWith() {
        return o2StartWith;
    }
    public String geto3StartWith() {
        return o3StartWith;
    }
    public String geto4StartWith() {
        return o4StartWith;
    }

    public String getaStartWith() {
        return aStartWith;
    }

    public String getsStartWith() {
        return sStartWith;
    }

    public String getLink() {
        return link;
    }
}
