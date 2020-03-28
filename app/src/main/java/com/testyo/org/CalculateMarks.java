package com.testyo.org;

import android.util.Log;

public class CalculateMarks {


    // show after test end

    static int totalQuestionFromFirestore=0;
    static int totalPaperTime = 20;
    static Boolean firstTimeInApp = true;
    static float calculatedFinalTotalMarks = 0;
    static String showremainingTimeinSolution;
    static float showprogressInsolution;
    static float finalMarksObtained = 0;
    static float marksForCorrectAnswer = 4;
    static float negativeMark = 1;
    MainActivity mainActivity;

    public static int[] selectedRadioList = new int[200];   // 200 max number of question
    public static int[] symbolList = new int[200];
    public static int[] tempSymbolList = new int[200];
    public static int[] localTimerList = new int[200];
    public static int[] upperBtnArray = new int[4];
    //
    // public static List<Integer> selectedRadiolist = new ArrayList<Integer>(200);


    // show this marks when submit button pressed

    public static void calculateFinalMarks(){
        int localTotalQuestion = MainActivity.totalQuestion;


        ShowMarksActivity.correct =0;// number of correct answer

        calculatedFinalTotalMarks = localTotalQuestion * marksForCorrectAnswer;


        for (int i=0;i< MainActivity.totalQuestion;i++){


            Log.i("Enterrrr" ,"sizeeee" +Extract_paper.getModifiedanswerlist().size()+ " "+selectedRadioList.length);

            if(selectedRadioList[i] == Integer.parseInt(Extract_paper.getModifiedanswerlist().get(i).toString())) {
                finalMarksObtained = finalMarksObtained + marksForCorrectAnswer;
                ShowMarksActivity.correct++;
            }else if(selectedRadioList[i] != 0){
                finalMarksObtained = finalMarksObtained - negativeMark;
            }

        }

        Log.i("Enterrrrrrrrr" ,"final marks obtained " + finalMarksObtained + "from  "+ calculatedFinalTotalMarks);

    }


    public void startPaperTimer() {
        mainActivity = MainActivity.getInstance();
        Thread t = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {

                        Thread.sleep(1000);
                        Log.i("Thread","Thread is on ");
                        mainActivity.runOnuithread();
                    }
                    catch (InterruptedException e)
                    {
                        // ooops
                    }
            }
        }); // the while thread will start in BG thread
        t.start();


    }



}
