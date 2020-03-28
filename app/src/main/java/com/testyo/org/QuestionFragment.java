package com.testyo.org;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {


     String questionkey;
     String optionkey1;
     String optionkey2;
     String optionkey3;
     String optionkey4;
     String solutionkey;
     String timerkey;
     String spanindex;
     String markforreviewinfo;
     String localTimerString;
     String lastquestioninfo;
     String spanindexright;

     int currentPosition;

     float localprogressBarValue = 0;
    ProgressBar localTimerProgressBar;
    MainActivity mainActivity;
    // web view of fragments

    public WebView questionWebview ;

    //local timer stuff

    public int localTimerint = 0;
    public float localprogressChangeperSecond;
    TextView localTimerTextView;
    Button finalSubmitBtn;

    final String mimeType = "text/html";
    final String encoding = "UTF-8";

    Timer localTimer;

    Button markForReviewBtn;




    public QuestionFragment() {



        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        float timetakenbyQuestion =  (CalculateMarks.totalPaperTime*60)/MainActivity.totalQuestion; // in minute

        localprogressChangeperSecond = 100f/(timetakenbyQuestion);

        Log.i("Enterrrrr","timer localprogressChangeperSecond "+localprogressChangeperSecond);
        mainActivity = MainActivity.getInstance();


        localTimerTextView = view.findViewById(R.id.local_timer_textview);
        //localTimer.scheduleAtFixedRate(task,1000,1000);

        // set final submit button

        finalSubmitBtn = view.findViewById(R.id.final_submit_btn);
        finalSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.callSubmitPaper();
            }
        });




        //setTimerOn();



        // initialize webview

        questionWebview = view.findViewById(R.id.webViewQuestion);
        questionWebview.setWebChromeClient(new WebChromeClient() {});

        questionWebview.getSettings().setJavaScriptEnabled(true);
        questionWebview.getSettings().setAppCacheEnabled(true);

        questionWebview.getSettings().setAppCachePath(((MainActivity)getActivity()).getBaseContext().getCacheDir().getPath());
        questionWebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        questionWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

//        if(MainActivity.firstTime){
//            //MainActivity.firstTime = false;
//            questionWebview.setVisibility(View.GONE);
//        }


        if (Build.VERSION.SDK_INT >= 19) {
// chromium, enable hardware acceleration
            questionWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            questionWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //questionWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        // getting context

        Context context = getActivity();

        if(context != null){
            questionWebview.addJavascriptInterface(new JavaScriptInterface(context), "Android");
        }

        //questionTextView = view.findViewById(R.id.question_text);
        questionkey = getArguments().getString("questionkey");
        optionkey1 = getArguments().getString("optionkey1");
        optionkey2 = getArguments().getString("optionkey2");
        optionkey3 = getArguments().getString("optionkey3");
        optionkey4 = getArguments().getString("optionkey4");
        solutionkey = getArguments().getString("solutionkey");
        spanindex = getArguments().getString("spanindex");
        markforreviewinfo = getArguments().getString("markforreviewinfo");
        localTimerString = getArguments().getString("localTimerString");
        lastquestioninfo = getArguments().getString("lastquestioninfo");
        spanindexright = getArguments().getString("spanindexright");




        String htmltext = getHtmlTextDifferently();
        questionWebview.loadDataWithBaseURL("file:///android_asset/", readAssetFileAsString("webpart/quiz1.html")+htmltext, "text/html", "UTF-8", null);


        if(!Extract_paper.solutionModeActive && Integer.parseInt(lastquestioninfo) == 1){
            finalSubmitBtn.setVisibility(View.VISIBLE);
        }else {
            finalSubmitBtn.setVisibility(View.GONE);
        }

        // set mark for review
        //MainActivity mainActivity = MainActivity.getInstance();
        markForReviewBtn = view.findViewById(R.id.markForReview);
        if(Integer.parseInt(markforreviewinfo) == mainActivity.MARKFORREVIEW ){
            Drawable myicon = getResources().getDrawable( R.drawable.circlemarkforreview);
            markForReviewBtn.setBackground(myicon);
        }

        markForReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Extract_paper.solutionModeActive){
                    currentPosition = mainActivity.getCurrentFragment();

                    if(CalculateMarks.symbolList[currentPosition] == mainActivity.MARKFORREVIEW){
                        Drawable myicon = getResources().getDrawable( R.drawable.circlenotvisited);
                        markForReviewBtn.setBackground(myicon);
                        int previousSymbol = CalculateMarks.tempSymbolList[currentPosition];
                        if(previousSymbol == mainActivity.ANSWERED){
                            CalculateMarks.upperBtnArray[mainActivity.ANSWERED]++;
                            CalculateMarks.upperBtnArray[CalculateMarks.symbolList[currentPosition]]--;
                            CalculateMarks.symbolList[currentPosition] = mainActivity.ANSWERED;

                        }else {
                            CalculateMarks.upperBtnArray[mainActivity.UNATTEMPTED]++;
                            CalculateMarks.upperBtnArray[CalculateMarks.symbolList[currentPosition]]--;
                            CalculateMarks.symbolList[currentPosition] = mainActivity.UNATTEMPTED;
                        }

                    }else {
                        Drawable myicon = getResources().getDrawable( R.drawable.circlemarkforreview);
                        markForReviewBtn.setBackground(myicon);
                        CalculateMarks.upperBtnArray[mainActivity.MARKFORREVIEW]++;
                        CalculateMarks.upperBtnArray[CalculateMarks.symbolList[currentPosition]]--;
                        CalculateMarks.tempSymbolList[currentPosition] = CalculateMarks.symbolList[currentPosition];
                        CalculateMarks.symbolList[currentPosition] = mainActivity.MARKFORREVIEW;
                    }

                    mainActivity.getrecycleitem(currentPosition);
                    mainActivity.setUpperButton();
                }
                }

        });

        // set main Timer

        Drawable drawable = getResources().getDrawable(R.drawable.fragment_time_cirecle);

        localTimerProgressBar = view.findViewById(R.id.fragment_timer_progress_bar);
        localTimerProgressBar.setProgress(0);   // Main Progress
        localTimerProgressBar.setSecondaryProgress(100); // Secondary Progress
        localTimerProgressBar.setMax(100); // Maximum Progress
        localTimerProgressBar.setProgressDrawable(drawable);

        if(Extract_paper.solutionModeActive){
            setLocalTimerInsolution();
        }

       // questionWebview.loadUrl("file:///android_asset/webpart/quiz1.html");

       // questionWebview.loadDataWithBaseURL("", questionkey, mimeType, encoding, "");




        //questionTextView.setText(message);
        return view;
    }

    private String readAssetFileAsString(String sourceHtmlLocation) {

        InputStream is;
        try
        {
            is = getContext().getAssets().open(sourceHtmlLocation);
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            return new String(buffer, "UTF-8");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return "";
    }


    // it is the function which provide html according to question mode or solution mode

    public String getHtmlTextDifferently(){

//        if(MainActivity.currentItemFirstTime){
//            MainActivity.currentItemFirstTime = false;
//            MainActivity.currentItem = MainActivity.viewPager.getCurrentItem();
//        }else {
//            MainActivity.currentItem = MainActivity.viewPager.getCurrentItem() +1 ;
//        }
        // index for submitted answer by user

        //          Extract_paper.solutionModeActive = true;
        // solutionModeActive says that this is the time for review
        if(Extract_paper.solutionModeActive){
            String[] spanForOptions = { "spannn", "spannn", "spannn", "spannn" };


            int spanIndex = Integer.parseInt(spanindex);
            int spanIndexright = Integer.parseInt(spanindexright);

            Log.i("spann","current item "+ mainActivity.getCurrentFragment());
            Log.i("spann","span endex "+spanIndex);

            spanForOptions[spanIndexright - 1] = "spann";

            if(spanIndex>0 && spanIndex != spanIndexright){
                spanForOptions[spanIndex - 1] = "spannw";
            }




            String solutionString = " Here is the solution present";  // place solution key for this solution string



            String htmltext = "transform: translate(-50%,-50%) scale(1);\n" +
                    "\tborder-radius: 50%;\n" +
                    "}\n" +
                    ".radio spannn{\n" +
                    "    height: 20px;\n" +
                    "    width: 20px;\n" +
                    "    margin-top: 16px;\n" +
                    "    margin-left: 5px;\n" +
                    "    border-radius: 50%;\n" +
                    "    border: 3px solid #64dd17;\n" +
                    "    display: block;\n" +
                    "    position: absolute;\n" +
                    "}\n" +
                    "</style>"+"<div id=\"quiz-holder\" class=\"quiz-holder\">\n" +
                    "    <div id=\"qcontainer\" class=\"qcontainer\">\n" +
                    "        <div class=\"myqxml\" id=\"myqxml\">\n" +
                    "            <p id=\"q1\">\n" +
                    "              " + questionkey  +
                    "            </p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "    <div class=\"radio-group\">\n" +
                    "        <label class=\"radio\">\n" +
                    "           <input type=\"radio\" value=\"1\" name=\"marks\" onclick=\"recordAnswer()\">\n" +
                    "            <"+spanForOptions[0]+"></"+spanForOptions[0]+">\n" +
                    "            <div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myoxml\" id=\"myoxml\">\n" +
                    "                    <p id=\"o1\">\n" +
                    "                       " + optionkey1  +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </label>\n" +
                    "        <label class=\"radio\">\n" +
                    "            <input type=\"radio\" value=\"2\" name=\"marks\" onclick=\"recordAnswer()\">\n" +
                    "            <"+spanForOptions[1]+"></"+spanForOptions[1]+">\n" +
                    "            <div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myoxml\" id=\"myoxml\">\n" +
                    "                    <p id=\"o2\">\n" +
                    "                        " + optionkey2  +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </label>\n" +
                    "        <label class=\"radio\">\n" +
                    "            <input type=\"radio\" value=\"3\" name=\"marks\" onclick=\"recordAnswer()\">\n" +
                    "           <"+spanForOptions[2]+"></"+spanForOptions[2]+">\n" +
                    "            <div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myoxml\" id=\"myoxml\">\n" +
                    "                    <p id=\"o3\">\n" +
                    "                       " + optionkey3  +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </label>\n" +
                    "        <label class=\"radio\">\n" +
                    "            <input type=\"radio\" value=\"4\" name=\"marks\" onclick=\"recordAnswer()\">\n" +
                    "            <"+spanForOptions[3]+"></"+spanForOptions[3]+">\n" +
                    "            <div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myoxml\" id=\"myoxml\">\n" +
                    "                    <p id=\"o4\">\n" +
                    "                       " + optionkey4  +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </label>\n" + "<div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myqxml\" id=\"myqxml\">\n" +
                    "                    <p id=\"sol\">\n" + solutionkey +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>" +
                    "    </div>\n" +
                    "    <script>\n" +
                    "       \twindow.onload = getFromAndroid();\n" +
                    "       \tvar device_width;\n" +
                    "            function getFromAndroid() {\n" +
                    "                  device_width = Android.getScreenWidth()-16 + 'px';\n" +
                    "                  document.getElementById('quiz-holder').style.width = device_width;\n" +
                    "                 }\n" +
                    "function recordAnswer(){\n" +
                    "                \n" +
                    "                      var selectedRadio = document.querySelector('input[name = \"marks\"]:checked').value;\n" +
                    "                      \n" +
                    "                      Android.getRadioSelectedList(selectedRadio);\n" +
                    "                 \n" +
                    "                   }   "+
                    "  </script>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            return htmltext;


        }else {

            //int spanIndex = CalculateMarks.selectedRadioList[MainActivity.viewPager.getCurrentItem()];
            Log.i("spann","current item "+ MainActivity.viewPager.getCurrentItem());
            Log.i("spann","current item "+ mainActivity.getCurrentFragment());
            //Log.i("spann","span endex "+spanIndex);


            int spanIndex = Integer.parseInt(spanindex);

            String[] defaultCheckArray = { "", "", "", "" };

            if(spanIndex>0){
                defaultCheckArray[spanIndex - 1] = "checked=\"checked\"";
            }

            //defaultCheckArray[0] = "checked=\"checked\"";

            String htmltext = "\ttransform: translate(-50%,-50%) scale(0);\n" +
                    "\tborder-radius: 50%;\n" +
                    "\ttransition: 100ms ease-in-out 0s;\n" +
                    "}\n" +
                    "\n" +
                    ".radio input[type=\"radio\"]:checked ~ spann:after{\n" +
                    "\ttransform: translate(-50%,-50%) scale(1);\n" +
                    "}\n" +
                    "\n" +
                    "</style><div id=\"quiz-holder\" class=\"quiz-holder\">\n" +
                    "    <div id=\"qcontainer\" class=\"qcontainer\">\n" +
                    "        <div class=\"myqxml\" id=\"myqxml\">\n" +
                    "            <p id=\"q1\">\n" +
                    "              " + questionkey  +
                    "            </p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "    <div class=\"radio-group\">\n" +
                    "        <label class=\"radio\">\n" +
                    "           <input type=\"radio\" id=\"1\" value=\"1\" name=\"marks\" "+defaultCheckArray[0]+" onclick=\"recordAnswer()\">\n" +
                    "            <spann></spann>\n" +
                    "            <div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myoxml\" id=\"myoxml\">\n" +
                    "                    <p id=\"o1\">\n" +
                    "                       " + optionkey1  +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </label>\n" +
                    "        <label class=\"radio\">\n" +
                    "            <input type=\"radio\" id=\"2\" value=\"2\" name=\"marks\" "+ defaultCheckArray[1]+ " onclick=\"recordAnswer()\">\n" +
                    "            <spann></spann>\n" +
                    "            <div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myoxml\" id=\"myoxml\">\n" +
                    "                    <p id=\"o2\">\n" +
                    "                        " + optionkey2  +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </label>\n" +
                    "        <label class=\"radio\">\n" +
                    "            <input type=\"radio\" id=\"3\" value=\"3\" name=\"marks\" "+ defaultCheckArray[2]+ " onclick=\"recordAnswer()\">\n" +
                    "            <spann></spann>\n" +
                    "            <div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myoxml\" id=\"myoxml\">\n" +
                    "                    <p id=\"o3\">\n" +
                    "                       " + optionkey3  +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </label>\n" +
                    "        <label class=\"radio\">\n" +
                    "            <input type=\"radio\" id=\"4\" value=\"4\" name=\"marks\" "+ defaultCheckArray[3]+ " onclick=\"recordAnswer()\">\n" +
                    "            <spann></spann>\n" +
                    "            <div id=\"qcontainer\" class=\"qcontainer\" >\n" +
                    "                <div class=\"myoxml\" id=\"myoxml\">\n" +
                    "                    <p id=\"o4\">\n" +
                    "                       " + optionkey4  +
                    "                    </p>\n" +
                    "                </div>\n" +
                    "            </div>\n" +
                    "        </label>\n" +
                    "    </div>\n" +
                    "    <script>\n" +
                    "       \twindow.onload = getFromAndroid();\n" +
                    "       \tvar device_width;\n" +
                    "            function getFromAndroid() {\n" +
                    "                  device_width = Android.getScreenWidth()-16 + 'px';\n" +
                    "                  document.getElementById('quiz-holder').style.width = device_width;\n" +
                    "                 }\n" +
                    "function recordAnswer(){\n" +
                    "var selectedRadio = document.querySelector('input[name = \"marks\"]:checked').value;\n" +
                    "                      var radioandroid = Android.sendRadiofromandroid();\n" +
                    "                      if(selectedRadio == radioandroid){\n" +
                    "                          document.getElementById(selectedRadio).checked = false;\n" +
                    "                          Android.getRadioSelectedList('0');   \n" +
                    "                      }else{\n" +
                    "                           Android.getRadioSelectedList(selectedRadio);\n" +
                    "                       }" +
                    "                   }   "+
                    "  </script>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            return htmltext;
        }


    }






    public void setTimerOn(){
        localTimerint++;
        int minute = localTimerint/60;
        int second = localTimerint % 60;

        if(localprogressBarValue <= 100){
            localprogressBarValue = localprogressBarValue + localprogressChangeperSecond;
            localTimerProgressBar.setProgress( (int)localprogressBarValue);
        }

        CalculateMarks.localTimerList[MainActivity.viewPager.getCurrentItem()] = localTimerint;
        localTimerTextView.setText(String.valueOf(minute) + ":" + String.valueOf(second) );

        Log.i("Enterrrrr","timer "+localTimerint);
    }

    public void setLocalTimerInsolution(){
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                int time = Integer.parseInt(localTimerString);
                int minute = time/60;
                int second = time % 60;
                localprogressBarValue = time*localprogressChangeperSecond;
                if(localprogressBarValue <= 100){
                    localTimerProgressBar.setProgress( (int)localprogressBarValue);
                }else {
                    localTimerProgressBar.setProgress(100);
                }

                localTimerTextView.setText(String.valueOf(minute) + ":" + String.valueOf(second) );

            }
        }, 100);

    }




}
