package com.testyo.org;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowMarksActivity extends AppCompatActivity {

    public static TextView totalMarks;
    public static TextView marksObtained;
    public static TextView attemptedQuestion;
    public static TextView incorrectAnswers;
    public static TextView correctAnswers;
    private Button reviewBtn;

    static ShowMarksActivity instance;

    static int correct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_marks);
        instance = this;
        totalMarks = findViewById(R.id.total_marks);
        marksObtained = findViewById(R.id.marks_obtained);
        reviewBtn = findViewById(R.id.review_btn);
        attemptedQuestion = findViewById(R.id.question_attempted);
        incorrectAnswers = findViewById(R.id.incorrect_answer);
        correctAnswers = findViewById(R.id.correct_answer);

        // change text of
        setTitle("TestYO");


        // change review btn color if ready
        if(MainActivity.readyToreview){

            setReviewBtnColor();
        }



        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Extract_paper.solutionModeActive = true;
                if (MainActivity.readyToreview) {
                    Log.i("Enterr", "review button clicked");
                    Intent intent = new Intent(ShowMarksActivity.this, MainActivity.class);

                    startActivity(intent);
                }
            }
        });

        fillMarksInTextView();

    }

    public void setReviewBtnColor() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI

                reviewBtn.setBackground(getResources().getDrawable(R.drawable.mygreenbtn));

            }
        });
    }


    @Override
    public void onBackPressed() {
        MainActivity mainActivity = MainActivity.getInstance();
        mainActivity.enterThread = false;
        Extract_paper.solutionModeActive = false;
        startActivity(new Intent(ShowMarksActivity.this, MainBeforeActivity.class));
        finish();
    }

    public static void fillMarksInTextView(){

        int attempted = CalculateMarks.upperBtnArray[MainActivity.ANSWERED];

        totalMarks.setText(String.valueOf(CalculateMarks.calculatedFinalTotalMarks));
        marksObtained.setText(String.valueOf(CalculateMarks.finalMarksObtained));
        attemptedQuestion.setText(String.valueOf(attempted));
        correctAnswers.setText(String.valueOf(correct));
        incorrectAnswers.setText(String.valueOf(attempted - correct));

    }


    public static ShowMarksActivity getinstance(){
        return instance;
    }

}
