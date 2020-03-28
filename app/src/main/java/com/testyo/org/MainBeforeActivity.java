package com.testyo.org;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainBeforeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    // set system property

    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );

    }

    private ProgressBar progressBarRound;

    boolean doubleBackToExitPressedOnce = false;
    static String linkanswedoc;

    private Context context;
    ActionBar actionBar;



    //  exam name

    public static String selectedExamName = "Jee Advanced";
    public static String selectedPaperName = "Previous Year Paper 2025";
    //spinner

    private Spinner spinner;

    // to store all test head

    List<Test_head> test_headList = null;
    RecyclerView recyclerView;

    // realtime database
    FirebaseDatabase database;
    DatabaseReference myRef;

    // firestore object

    static FirebaseFirestore db;

    //helper object

    static FileDownloadHelper fileDownloadHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_before);

        Toolbar   mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        MainActivity.readyToreview = false;
        Extract_paper.solutionModeActive = false;
        FragmentQuestionAdapter.paperExtracted = false;
        Arrays.fill(CalculateMarks.selectedRadioList, 0);
        Arrays.fill(CalculateMarks.symbolList, 0);
        Arrays.fill(CalculateMarks.tempSymbolList, 0);
        Arrays.fill(CalculateMarks.upperBtnArray, 0);

        //round progress for loading firebase content
        progressBarRound = findViewById(R.id.round_progress);

        // realtime database testing

        realtimeDatabaseTesting();

        // sqlite testing

        sqliteTesting();

        // set spinner

        setSpinner();

        //method to set recycler view from xml

        setting_up_recyclerView();



        // drawer layout and navigation view

        prepareDrawerAndNavigation();
        // download paper from server

        fileDownloadHelper = new FileDownloadHelper(MainBeforeActivity.this);

        // check demo file
        //fileDownloadHelper.checkdemoPaper();

        fileDownloadHelper.firstDownloadOmmlFile();



        //downloadFromFirebase();

    }

    private void realtimeDatabaseTesting() {

         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("Jee Main");

         myRef.setValue("4");
    }

    private void sqliteTesting() {

        SqLiteDbHelper sqLiteDbHelper = new SqLiteDbHelper(this);
        SQLiteDatabase db = sqLiteDbHelper.getWritableDatabase();

        // content value is hashmap
        ContentValues values = new ContentValues();
        values.put("sid","1");
        values.put("sname","nitesh");
        values.put("marks","10");

        long row = db.insert("student",null,values);

        Log.i("database","row number is" + row);

        SQLiteDatabase db1 = sqLiteDbHelper.getReadableDatabase();

        String projection[] = {"sid","sname","marks"};
        Cursor cursor = db1.query("student",projection,null,null,null,null,null);
        cursor.moveToPosition(0);

        Log.i("database","name is " + cursor.getString(1));

    }


    private void setSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.examName, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK); /* if you want your item to be white */
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        spinner.setOnItemSelectedListener(this);
    }

    // click events for spinner

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
//        ((TextView) parent.getChildAt(position)).setTextColor(Color.BLACK);
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        selectedExamName = text;

        dataFromFirebaseFirestore();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void prepareDrawerAndNavigation() {

        // preparing toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // time for drawer and navigation view


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }




    private void setting_up_recyclerView() {

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        test_headList = new ArrayList<>();

        //dataFromFirebaseFirestore();





    }

    private void dataFromFirebaseFirestore() {

        //if(test_headList.size()>0)
            test_headList.clear();

        // set progress bar to show progress



        //Dialog.setMessage("Doing something...");
        //Dialog.setIndeterminate(true);



        // set db

        db=FirebaseFirestore.getInstance();

        // get data from db

        db.collection(selectedExamName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i=1;
                        for(DocumentSnapshot documentSnapshot: task.getResult()) {
                            String marks = documentSnapshot.getString("Marks");
                            String minusMarks = documentSnapshot.getString("MinusMarks");
                            String totalquestion = documentSnapshot.getString("TotalQuestion");
                            String totalTime = documentSnapshot.getString("TotalTime");
                            String Name = documentSnapshot.getString("Name");
                            String Link = documentSnapshot.getString("Link");
                            String QuestionStartWith = documentSnapshot.getString("QuestionStartWith");
                            String OptionStartWith = documentSnapshot.getString("OptionStartWith");
                            String Option2StartWith = documentSnapshot.getString("Option2StartWith");
                            String Option3StartWith = documentSnapshot.getString("Option3StartWith");
                            String Option4StartWith = documentSnapshot.getString("Option4StartWith");
                            String AnswerStartWith = documentSnapshot.getString("AnswerStartWith");
                            String SolutionStartWith = documentSnapshot.getString("SolutionStartWith");
                            String AnswerOrSolutionIncluded = documentSnapshot.getString("AnswerOrSolutionIncluded");

                            Test_head tHeadObject = new Test_head(Name, totalquestion,Link,QuestionStartWith,OptionStartWith,Option2StartWith,Option3StartWith,Option4StartWith,AnswerStartWith,SolutionStartWith,marks,minusMarks,totalTime,AnswerOrSolutionIncluded);
                            test_headList.add(tHeadObject);
                            Log.i("Enter","paper added");

                        }
                        // if no item present for particular exam

                        if(test_headList.size() < 1){
                            Toast.makeText(MainBeforeActivity.this,"Coming Soon...",Toast.LENGTH_LONG).show();

                        }


                        Test_headAdapter adapter = new Test_headAdapter(MainBeforeActivity.this,test_headList); //creating recycler view adapter


                        // setting adapter to recycler view

                        recyclerView.setAdapter(adapter);

                        progressBarRound.setVisibility(View.GONE);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainBeforeActivity.this, "Error ;-.-;", Toast.LENGTH_SHORT).show();
                    }
                });



    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }

            this.doubleBackToExitPressedOnce = true;
            //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_before, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share1) {
            share();
            return true;
        }else if (id == R.id.action_rate_us1) {
            rateUs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_share) {

             share();

        } else if (id == R.id.nav_rate_us) {

             rateUs();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Download the TestYo app to practice more and more. App Link - https://play.google.com/store/apps/details?id="+ getPackageName()+ " \"";
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


    public static void getsolutionoranswerLink(final String solOrAns) {
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection(selectedExamName + solOrAns).document(selectedPaperName);
        ((DocumentReference) docRef).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                             MainActivity.answerLink = document.getString("Link");

                             if(solOrAns == MainActivity.ANSWER){
                                 Extract_paper.extractAnswers = true;
                                 MainBeforeActivity.fileDownloadHelper.downloadPdf(selectedPaperName+MainActivity.ANSWER,MainActivity.answerLink);

                             }else{
                                 // write code for solution when submit button pressed

                                 MainBeforeActivity.fileDownloadHelper.downloadPdf(selectedPaperName+MainActivity.SOLUTION,document.getString("Link"));

                             }
                        Log.i("Enter","now i am in link  "+ linkanswedoc);
                        Log.d("Enter", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("Enter", "No such document");
                    }
                } else {
                    Log.d("Enter", "get failed with ", task.getException());
                }
            }
        });
        Log.i("Enter","now i am in link  "+ linkanswedoc);

    }

}





