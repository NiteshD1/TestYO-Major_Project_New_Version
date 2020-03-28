package com.testyo.org;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

public class JavaScriptInterface {

    private static String htmltext = "giugi";

    final Handler handler = new Handler();

    private Boolean delay = true;


    Context mContext;



    JavaScriptInterface(Context c) {
        mContext = c;
    }

    public static void setHtmltext(String htmltext1) {
        htmltext = htmltext1;
    }

    //@JavascriptInterface
//    public String getFromAndroid() {
////        return htmltext;
////        String[] questionData = new String[5];
////        int k =
////        questionData[0] = Extract_paper.getQuestionlist().get(k).toString();
////        questionData[1] = Extract_paper.getOptionlist().get(4*k + 0).toString();
//        return Extract_paper.getQuestionlist().get(0).toString();//QuestionFragment.questionkey;
//    }

    @JavascriptInterface
    public float getScreenWidth() {

        return MainActivity.dpWidth;
//        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @JavascriptInterface

    public int sendRadiofromandroid(){
        int k = MainActivity.viewPager.getCurrentItem();
        return CalculateMarks.selectedRadioList[k];

    }
    @JavascriptInterface

    public void getRadioSelectedList(String radiono){
        int k = MainActivity.viewPager.getCurrentItem();
        CalculateMarks.selectedRadioList[k] = Integer.parseInt(radiono);
        MainActivity mainActivity = MainActivity.getInstance();
        if(Integer.parseInt(radiono) == 0 && CalculateMarks.symbolList[k] != mainActivity.MARKFORREVIEW){
            CalculateMarks.upperBtnArray[mainActivity.UNATTEMPTED]++;
            CalculateMarks.upperBtnArray[CalculateMarks.symbolList[k]]--;
//            mainActivity.setUpperButton();
            CalculateMarks.symbolList[k] = mainActivity.UNATTEMPTED;
        }else if(CalculateMarks.symbolList[k]!= mainActivity.MARKFORREVIEW){
            CalculateMarks.upperBtnArray[mainActivity.ANSWERED]++;
            CalculateMarks.upperBtnArray[CalculateMarks.symbolList[k]]--;
//            mainActivity.setUpperButton();
            CalculateMarks.symbolList[k] = mainActivity.ANSWERED;
        }else if(Integer.parseInt(radiono)!= 0 && CalculateMarks.symbolList[k] == mainActivity.MARKFORREVIEW){
            CalculateMarks.tempSymbolList[k] = mainActivity.ANSWERED;
        }else if(Integer.parseInt(radiono) == 0 && CalculateMarks.symbolList[k] == mainActivity.MARKFORREVIEW){
            CalculateMarks.tempSymbolList[k] = mainActivity.UNATTEMPTED;
        }
        //mainActivity.getrecycleitem(k);
        mainActivity.runUithreadforUpperBtn();

        // set upper text view right nav

//        mainActivity.upperAnswered.setText(String.valueOf(CalculateMarks.upperBtnArray[mainActivity.ANSWERED]));
//        mainActivity.upperNotVisited.setText(String.valueOf(CalculateMarks.upperBtnArray[mainActivity.NOTVISITED]));
//        mainActivity.upperUnattempted.setText(String.valueOf(CalculateMarks.upperBtnArray[mainActivity.UNATTEMPTED]));
//        mainActivity.upperMarkForReview.setText(String.valueOf(CalculateMarks.upperBtnArray[mainActivity.MARKFORREVIEW]));

//        Log.i("Enterrr", "I got that  " + String.valueOf(k) );
//        Log.i("Enterrr", "I got that radio no " + String.valueOf(radiono) );
//        Log.i("Enterrr", "I got that  " + String.valueOf(CalculateMarks.selectedRadioList[k]) );
    }

//    int i = 3;
//    @JavascriptInterface
//    public Boolean get_delay(){
//        if(i > 0){
//            i--;
//            delay = false;
//            Log.i("Enter","true returened");
//            return true;
//        }else {
//            Log.i("Enter","false returened");
//            return  false;
//        }
//
//    }

}
