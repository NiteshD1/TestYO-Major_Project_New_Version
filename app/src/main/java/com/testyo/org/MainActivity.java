package com.testyo.org;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView showFrontMessage; // message about paper


    NavAdapter adapter;

    FirebaseFirestore db;
    Toolbar toolbar;

    FileDownloadHelper fileDownloadHelper;

    static MainActivity instance;

    //right nav upper part button
    TextView upperAnswered;
    TextView upperNotVisited;
    TextView upperUnattempted;
    TextView upperMarkForReview;

    int fillRightnavForSolution = 0;


    // symbols for question
    public static final int NOTVISITED = 0;
    public static final int  UNATTEMPTED = 1;
    public static final int ANSWERED = 2;
    public static final int MARKFORREVIEW = 3;

    public static Boolean readyToreview = false;
    public static Boolean readyToSubmit = false;
    public static Boolean enterThread = false;
    // Main timer stuff
    static int totalQuestionFromFirestore = 0;
    static int totalPaperTime = 20; // time in minutes
    static int mainTimerhour;
    static int mainTimerminute;
    static int mainTimersecond;
    static TextView mainTimerTextview;
    static ProgressBar mainTimerProgressBar;
    static float progressBarValueperSecond;
    static float progressBarValue;
    static int submitAlertno = 3;


    // to know previous page in fragments
    int previousPosition = 10000;

    // local question timer stuff



    static final String SOLUTION = " solution";
    static final String ANSWER = " answer";

    static int totalQuestion = 1;// 6 for testing only

    // progress bar

    private static ContentLoadingProgressBar horizontalProgressBar;


    private Boolean sidenaveBtnactive = false;  /// side navigation button shhould work after start button

    static String answerLink = null;

    int newcurrentItem = 0;    /// helpes to find current
    static Boolean currentItemFirstTime = true; ///  item in question fragment

    public static Boolean firstTime = true;
    //screen pixel
    static float dpWidth;

    private Button submitBtn;

    private Button startBtn;
    //private static String cachePathString = getBaseContext().getCacheDir().getPath();

    private Context context = getBaseContext();
    List<NavQuestion> navQuestionList;
    RecyclerView recyclerView;
    NavQuestion navQuestion;
    static DrawerLayout drawer;

    //public static ViewPager viewPager;
    public static CustomViewPager viewPager;
    private static FragmentQuestionAdapter fadapter;


    TextView introTextView;
    // fragment adapter

    public static FragmentQuestionAdapter getFadapter() {
        return fadapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        currentItemFirstTime = true;  // helps in question fragment in solution mode
        totalPaperTime = CalculateMarks.totalPaperTime;



        // set front message
        readyShowFront();


        // initialize upper button
        upperAnswered = (TextView) findViewById(R.id.qAttempted);
        upperNotVisited = (TextView) findViewById(R.id.qNotVisited);
        upperUnattempted = (TextView) findViewById(R.id.qUnattempted);
        upperMarkForReview = (TextView) findViewById(R.id.qMarkForReview);

        // set main Timer

        Drawable drawable = getResources().getDrawable(R.drawable.paper_timer_circle);
        mainTimerTextview = findViewById(R.id.paper_timer);
        mainTimerProgressBar = findViewById(R.id.main_timer_progress_bar);
        mainTimerProgressBar.setProgress(0);   // Main Progress
        mainTimerProgressBar.setSecondaryProgress(100); // Secondary Progress
        mainTimerProgressBar.setMax(100); // Maximum Progress
        mainTimerProgressBar.setProgressDrawable(drawable);

        //set toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.getOverflowIcon().setTint(ContextCompat.getColor(this, R.color.colorBlack));

        horizontalProgressBar = findViewById(R.id.startprogress);

        //getting screen width in dp

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        dpWidth  = outMetrics.widthPixels / density;



        startBtn = findViewById(R.id.startBttn);
        introTextView = findViewById(R.id.introTextview);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FragmentQuestionAdapter.paperExtracted){
//                    for(int i=0;i<totalQuestion;i++){
//                        String wholeText = load(MainBeforeActivity.selectedPaperName+String.valueOf(i)+".xml");
//                        Extract_paper.questionlist.get(i).append(wholeText);
//                    }

                    //totalPaperTime = 20;
                    sidenaveBtnactive = true;

                    startBtn.setVisibility(View.GONE);
                    introTextView.setVisibility(View.GONE);
                    // for big timer
                    setInitialPaperTimer();


                    submitAlertno = 3;
                    enterThread = true;


                    if(CalculateMarks.firstTimeInApp){
                        submitAlertno = 3;
                        enterThread = true;
                        CalculateMarks calculateMarks = new CalculateMarks();
                        calculateMarks.startPaperTimer();
                        CalculateMarks.firstTimeInApp = false;
                    }else {

//                        final Handler handler2 = new Handler();
//                        handler2.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                submitAlertno = 3;
//                                enterThread = true;
//                            }
//                        }, 100);

                    }
                    setCustomViewPager();

                    setRightNav();

                    //now download answer and solution


                    downloadAnsandSol();


                    // for local timer

                }
            }
        });

        //View pager

        Log.i("enter", String.valueOf(Resources.getSystem().getDisplayMetrics().widthPixels));
        viewPager =  findViewById(R.id.customViewPager);
        //viewPager.setOffscreenPageLimit(0);

        //fadapter = new FragmentQuestionAdapter(getSupportFragmentManager());

        // prepare for solution mode

        if(Extract_paper.solutionModeActive){
            prepareForeSolutionMode();
            mainTimerTextview.setText(CalculateMarks.showremainingTimeinSolution);
            mainTimerProgressBar.setProgress( 100 - (int)CalculateMarks.showprogressInsolution);
        }
        //setCustomViewPager();
        // for adding fragment dynamically while swiping

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

              //  Log.i("spannew","current new on"+String.valueOf(position));

//                if(position == previousPosition){
//
//                    QuestionFragment frag1 = (QuestionFragment) viewPager
//                            .getAdapter()
//                            .instantiateItem(viewPager, viewPager.getCurrentItem());
//                    frag1.setTimeroff();
//
//
//                }

                // to stop swiping in right direction
//
//                if(position == 1){
//                    viewPager.setAllowedSwipeDirection(SwipeDirection.right);
//                }else if(position == fadapter.fragments.size()-2){
//                    viewPager.setAllowedSwipeDirection(SwipeDirection.left);
//                }else {
//                    viewPager.setAllowedSwipeDirection(SwipeDirection.all);
//                }

                //when fragment 0 arrivied send it to back

//                if(position == 0){
//
//                    viewPager.setCurrentItem(fadapter.fragments.size()-3);
//                }



            }

            @Override
            public void onPageSelected(int position) {

                newcurrentItem = position;

                if(!Extract_paper.solutionModeActive){
                    if(position == 1 && CalculateMarks.symbolList[0]== NOTVISITED){
                        CalculateMarks.upperBtnArray[UNATTEMPTED]++;
                        CalculateMarks.upperBtnArray[CalculateMarks.symbolList[0]]--;
                        CalculateMarks.symbolList[0] = UNATTEMPTED;
                        getrecycleitem(0);

                    }
                    if(CalculateMarks.symbolList[position] == NOTVISITED){
                        CalculateMarks.upperBtnArray[UNATTEMPTED]++;
                        CalculateMarks.upperBtnArray[CalculateMarks.symbolList[position]]--;
                        CalculateMarks.symbolList[position] = UNATTEMPTED;

                    }
                    getrecycleitem(position);
                    setUpperButton();

                }


                //Log.i("spannew","current new "+String.valueOf(newcurrentItem));
//                if(previousPosition!= 10000){
//
//                    QuestionFragment frag2 = (QuestionFragment) viewPager
//                            .getAdapter()
//                            .instantiateItem(viewPager, previousPosition);
//                    frag2.setTimeroff();
//                }
//
//                previousPosition = position;



//                QuestionFragment frag1 = (QuestionFragment) viewPager
//                        .getAdapter()
//                        .instantiateItem(viewPager, viewPager.getCurrentItem());
//                frag1.setTimer();

                // onFragmentAdapterChanged(position);

               // fadapter.notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        // submit button

        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callSubmitPaper();


            }
        });




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.addDrawerListener(toggle);
        //toggle.syncState();


        // set custom nav button
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_right_nav);


        // right navigation click

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(Gravity.RIGHT)){
                    drawer.closeDrawer(Gravity.RIGHT);
                }else if(sidenaveBtnactive){
                    drawer.openDrawer(Gravity.RIGHT);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void downloadAnsandSol() {

        if(!Extract_paper.answerIncluded){
            MainBeforeActivity.getsolutionoranswerLink(ANSWER);
        }

        //MainBeforeActivity.getsolutionoranswerLink(SOLUTION);   // download solution file

        readyToSubmit = true;
    }
    // set upper button individualy

    public void setUpperButton() {
        upperAnswered.setText(String.valueOf(CalculateMarks.upperBtnArray[ANSWERED]));
        upperNotVisited.setText(String.valueOf(CalculateMarks.upperBtnArray[NOTVISITED]));
        upperUnattempted.setText(String.valueOf(CalculateMarks.upperBtnArray[UNATTEMPTED]));
        upperMarkForReview.setText(String.valueOf(CalculateMarks.upperBtnArray[MARKFORREVIEW]));
    }




    private void setRightNavUpperBtn() {



        if(!Extract_paper.solutionModeActive){
            CalculateMarks.upperBtnArray[NOTVISITED] = totalQuestion;
            upperNotVisited.setText(String.valueOf(totalQuestion));
        }

    }


    private void setInitialPaperTimer() {

        mainTimerhour = totalPaperTime/60;
        mainTimerminute = totalPaperTime%60;
        mainTimersecond = 1;
        progressBarValueperSecond = 100f/(totalPaperTime*60f);
        progressBarValue= 100f;

        totalPaperTime = totalPaperTime*60;

    }





    // to show time of exam

    private void displayMainTimerData() {

        mainTimersecond = mainTimersecond-1;

        totalPaperTime = totalPaperTime-1;
        // call submit when time over

        if(totalPaperTime <= 0 ){
            submitAlertno = submitAlertno-1;
            if(submitAlertno == 2){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Time's up")
                        .setMessage("Your Paper Is Submitting...")
                        .show();
            }else if(submitAlertno <= 0){
                callSubmitPaper();
            }

        }else {

            String finalTime = String.valueOf(mainTimerhour)+":"+String.valueOf(mainTimerminute)+":"+String.valueOf(mainTimersecond);
            CalculateMarks.showremainingTimeinSolution = finalTime;
            mainTimerTextview.setText(finalTime);
            progressBarValue = progressBarValue-progressBarValueperSecond;
            CalculateMarks.showprogressInsolution = progressBarValue; // for solution Mode
            mainTimerProgressBar.setProgress( 100 - (int)progressBarValue);
            Log.i("Enterr","now time is "+((int)progressBarValue)+" " +(int)progressBarValueperSecond);
            Log.i("Enterr","now time is "+String.valueOf(mainTimerhour)+":"+String.valueOf(mainTimerminute)+":"+String.valueOf(mainTimersecond));
            if(mainTimersecond <= 0){
                if(mainTimerminute <= 0){
                    mainTimerhour = mainTimerhour -1;
                    mainTimerminute=60;
                }
                mainTimersecond = 60;
                mainTimerminute = mainTimerminute-1;
            }

        }





    }

    public void callSubmitPaper() {
        // get answer link to get correct answer data

        if(readyToSubmit){

            Log.i("Enter","submit button pressed ");
            submitAlertno = 0;
            if(!Extract_paper.solutionIncluded) {
                MainBeforeActivity.getsolutionoranswerLink(SOLUTION);
            }
            Extract_paper.solutionModeActive = true;         // now solution will be extracted and shown

            CalculateMarks.calculateFinalMarks();


            Intent intent = new Intent(MainActivity.this, ShowMarksActivity.class);

            startActivity(intent);
            //fileDownloadHelper = new FileDownloadHelper();

        }

    }



    private void prepareForeSolutionMode() {

        setRightNav();
        horizontalProgressBar.hide();
        sidenaveBtnactive = true;
        startBtn.setVisibility(View.GONE);

        introTextView.setVisibility(View.GONE);


        setCustomViewPager();

        // fill right nav with symbol
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fillRightnavForSolution();
                setUpperButton();
            }
        }, 100);




    }

    private void fillRightnavForSolution() {
        for(int i = 0; i< totalQuestion; i++){
            getrecycleitem(i);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } if(Extract_paper.solutionModeActive){
            startActivity(new Intent(MainActivity.this, ShowMarksActivity.class));
            finish();
        } else {
            enterThread = false;
            Extract_paper.solutionModeActive = false;
            startActivity(new Intent(MainActivity.this, MainBeforeActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            share();
            return true;
        }else if (id == R.id.action_rate_us) {
            rateUs();
            return true;
        }

        if (id == R.id.right_nav_btn){
            if(drawer.isDrawerOpen(Gravity.RIGHT)){
                drawer.closeDrawer(Gravity.RIGHT);
            }else if(sidenaveBtnactive){
                drawer.openDrawer(Gravity.RIGHT);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Download the TestYo app to practice more and more. App Link - https://play.google.com/store/apps/details?id=org.studyquiz.learnenglish";
        String shareSub = "Give Test Paper With TestYo";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    private void rateUs() {
        try{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

        // get value from before activity intent

        //Intent intent = getIntent();

        //String paperNum = intent.getExtras().getString("paperNumber");
    }



    // for adding fragment dynamically while swiping

//    void onFragmentAdapterChanged(int position){
//
//        if(position >= fadapter.fragments.size()-2) {
//            fadapter.fragments.add(Fragment.instantiate(this, QuestionFragment.class.getName(), null));
//            fadapter.notifyDataSetChanged();
//        }
//    }

    public void setCustomViewPager(){

        //fadapter.feedsList = new ArrayList<>();

        // List<QuestionFragment> QuestionFragmentList = new ArrayList<QuestionFragment>(5);
        //fadapter.feedsList = QuestionFragmentList;
        fadapter = new FragmentQuestionAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fadapter);
        //viewPager.setCurrentItem(1);


           //(also call solution here and save that like it

        //viewPager.setCurrentItem(fadapter.fragments.size()-1);
        //viewPager.setVisibility(View.INVISIBLE);

    }

    public void hide_horizontal_progressBar(){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                horizontalProgressBar.hide();

                startBtn.setBackground(getResources().getDrawable(R.drawable.mygreenbtn));

            }
        });

    }


    public static MainActivity getInstance() {
        return instance;
    }

    public void setTotalQestion() {

        totalQuestion = Extract_paper.getQuestionlist().size();
        if(totalQuestion != CalculateMarks.totalQuestionFromFirestore){
            Toast.makeText(MainActivity.this,"Total Question Mismatch",Toast.LENGTH_SHORT).show();
        }

    }



    private void displaylocalTimerData() {
    }


    public int getCurrentFragment() {
        return newcurrentItem;
    }


    public void setRightNav(){
        // recycler view for right navigation

        recyclerView = (RecyclerView)findViewById(R.id.nav_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));

        navQuestion = new NavQuestion();
        navQuestion.setTotalQuestion(totalQuestion);

        navQuestionList = new ArrayList<>();

        for(int i = 0 ;i<navQuestion.getTotalQuestion();i++){

            navQuestionList.add(
                    new NavQuestion(
                            String.valueOf(i+1)
                    )
            );
        }

        adapter = new NavAdapter(this,navQuestionList); //creating recycler view adapter

        // setting adapter to recycler view

        recyclerView.setAdapter(adapter);

        // set raght nav upper button
        setRightNavUpperBtn();



    }

    public void getrecycleitem(int position){

        View view2 = recyclerView.getLayoutManager().findViewByPosition(position);
        Button btn = (Button) view2.findViewById(R.id.nav_Question);
        Log.i("Enterrr","I am in getitemrecycle "+ position);

        int symbol = CalculateMarks.symbolList[position];
        Drawable myIcon;

        if(symbol == NOTVISITED){
            myIcon = getResources().getDrawable( R.drawable.circlenotvisited);
            btn.setBackground(myIcon);
        }else if(symbol == UNATTEMPTED){
            myIcon = getResources().getDrawable( R.drawable.circlered);
            btn.setBackground(myIcon);
        }else if(symbol == ANSWERED){
            myIcon = getResources().getDrawable( R.drawable.circle);
            btn.setBackground(myIcon);
        }else if(symbol == MARKFORREVIEW){
            myIcon = getResources().getDrawable( R.drawable.circlemarkforreview);
            btn.setBackground(myIcon);
        }

        //setUpperButton();

    }


    public void runOnuithread() {
        runOnUiThread(new Runnable() // start actions in UI thread
        {

            @Override
            public void run()
            {
//                Log.i("Thread","Thread is on 1");
//                Log.i("Thread","submitAlertno " +submitAlertno);
//                Log.i("Thread","enterThread " +enterThread);

                // gain net after network error
//                new FileDownloadHelper.InternetCheck(new FileDownloadHelper.InternetCheck.Consumer() {
//                    @Override
//                    public void accept(Boolean internet) { /* do something with boolean response */
//                        Log.i("Error", "No net");
//                        if (FileDownloadHelper.neterrordetected && internet) {
//                            Log.i("Thread", "Gain net after net error ");
//                            FileDownloadHelper.neterrordetected = false;
//                            fileDownloadHelper.downloadPdf(FileDownloadHelper.filetoDownloadNeterror[0], FileDownloadHelper.filetoDownloadNeterror[1]);
//
//                        }
//                    }
//                });

                if(submitAlertno>0 && enterThread){
                    Log.i("Thread","Thread is on 2");
                    displayMainTimerData();
                    QuestionFragment frag1 = (QuestionFragment) viewPager
                            .getAdapter()
                            .instantiateItem(viewPager, viewPager.getCurrentItem());
                    frag1.setTimerOn();
                }

                // this action have to be in UI thread
            }
        });
    }

    public void runUithreadforUpperBtn(){
        runOnUiThread(new Runnable() // start actions in UI thread
        {

            @Override
            public void run()
            {
                Log.i("Thread","Ui thread for upper btn");
               setUpperButton();
               getrecycleitem(viewPager.getCurrentItem());
                // this action have to be in UI thread
            }
        });
    }

    public void save(String text ,String fileName) {
        //String text = mEditText.getText().toString();
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(text.getBytes());

           // mEditText.getText().clear();
//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + fileName,
//                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String load(String fileName) {
        FileInputStream fis = null;
        String text = null;
        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();


            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            //mEditText.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return text;
    }


    public void readyShowFront(){
        //String symbol = getResources().getString(R.string.showInFront);
        int hour = totalPaperTime/60;
        int minute = totalPaperTime%60;

        String time;
        if(minute==0){
            time = String.valueOf(hour) +" Hour";
        }else if(hour == 0) {
            time = String.valueOf(minute) +" Minute";

        }else {
                time = String.valueOf(hour) +" Hour and "+ String.valueOf(minute)+ " Minute";
        }


        String frontMessage = "✰ This Test contains "+ CalculateMarks.totalQuestionFromFirestore +" Question \n" +
                "✰ Each Question contains "+ CalculateMarks.marksForCorrectAnswer+ " marks \n" +
                "✰ -" + CalculateMarks.negativeMark+   " will be deducted for every \n" +
                "      incorrect Answer  \n" +
                "✰ Time duration " +time;



        showFrontMessage = findViewById(R.id.introTextview);
        showFrontMessage.setText(frontMessage);
       // Log.i("Enter","✰"+symbol);
    }




}
