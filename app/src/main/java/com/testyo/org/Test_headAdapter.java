package com.testyo.org;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Test_headAdapter extends RecyclerView.Adapter<Test_headAdapter.Test_headViewHolder> {

    private FileDownloadHelper fileDownloadHelper;
    //this context we use to inflate the layout
    private Context mCtx;


    // List to show test head

    private List<Test_head> Test_headList;

    // firebase objects


    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    StorageReference ref;
    StorageReference ref1;


    ///   creating private folder to store downloaded file

    private File cacheDir = null;


    //getting the test head list and context with constructor


    public Test_headAdapter(Context mCtx, List<Test_head> test_headList) {
        this.mCtx = mCtx;
        Test_headList = test_headList;
    }

    @NonNull
    @Override
    public Test_headViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.test, null);

        return new Test_headViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Test_headViewHolder holder, final int position) {
        final Test_head test_head = Test_headList.get(position);

        //binding the data with view holder view
        holder.textViewTitle.setText(test_head.getName());
        holder.textViewdesc.setText(MainBeforeActivity.selectedExamName);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mCtx, MainActivity.class);
                intent.putExtra("paperNumber", String.valueOf(position + 1));
                fileDownloadHelper = new FileDownloadHelper(mCtx);

                // update question, answer and solution start with

                String answerOrSolutionIncluded = test_head.getAnswerorSolutionIncluded();
                // set Boolean , answer or solution icluded
                if(answerOrSolutionIncluded.equals("11")){
                    Extract_paper.answerIncluded = true;
                    Extract_paper.solutionIncluded = true;
                    MainActivity.readyToreview = true;
                    MainActivity.readyToSubmit = true;
                }else if(answerOrSolutionIncluded.equals("10")){
                    Extract_paper.answerIncluded = true;
                    Extract_paper.solutionIncluded = false;
                    MainActivity.readyToreview = true;
                    MainActivity.readyToSubmit = false;
                }else if(answerOrSolutionIncluded.equals("01")){
                    Extract_paper.answerIncluded = false;
                    Extract_paper.solutionIncluded = true;
                    MainActivity.readyToreview = false;
                    MainActivity.readyToSubmit = true;
                }else if(answerOrSolutionIncluded.equals("00")){
                    Extract_paper.answerIncluded = false;
                    Extract_paper.solutionIncluded = false;
                    MainActivity.readyToreview = false;
                    MainActivity.readyToSubmit = false;
                }


                Extract_paper.questionStartsWith = test_head.getqStartWith();
                Extract_paper.optionStartsWith = test_head.getoStartWith();
                Extract_paper.option2StartsWith = test_head.geto2StartWith();
                Extract_paper.option3StartsWith = test_head.geto3StartWith();
                Extract_paper.option4StartsWith = test_head.geto4StartWith();
                Extract_paper.answerStartsWith = test_head.getaStartWith();
                Extract_paper.solutionStartsWith = test_head.getsStartWith();
                CalculateMarks.totalQuestionFromFirestore = Integer.parseInt(test_head.getTotalQuestion());
                CalculateMarks.totalPaperTime = Integer.parseInt(test_head.getTotalTime());

                CalculateMarks.marksForCorrectAnswer = Float.parseFloat(test_head.getMarks());
                CalculateMarks.negativeMark = Float.parseFloat(test_head.getMinusMarks());


                fileDownloadHelper.downloadPdf(test_head.getName(),test_head.getLink());
                MainBeforeActivity.selectedPaperName = test_head.getName();

                Log.i("Enter","name here" + MainBeforeActivity.selectedPaperName);
                mCtx.startActivity(intent);

            }
        });

//        holder.download_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Log.i(TAG, "onClick: download button ");
//                Context context;
//
//
//
//
////                File mydir = mCtx.getDir("mydir", Context.MODE_PRIVATE);
////
////                File file =  new File(mydir,"demoFile");
////
////                DownloadFile(test_head.getLink(),mydir);
//
//                //downloadFileInternally(mCtx, test_head.getLink(), mydir);
//
//                //downloadFile(test_head.getTitle(),".docx",mydir.getAbsolutePath(),test_head.getLink());
//
//            }
//        });


    }



    @Override
    public int getItemCount() {

        return Test_headList.size();
    }

    public class Test_headViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewdesc;
        CardView cardView;
        //ImageButton download_btn;

        public Test_headViewHolder(@NonNull View itemView) {

            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewdesc = itemView.findViewById(R.id.textViewdesc);
            cardView = itemView.findViewById(R.id.card_view);
          //  download_btn = itemView.findViewById(R.id.download_btn);
        }

    }


    //////// download function to download docx file from firebase


//    public void downloadFile( String fileName, String fileExtension, String destinationDirectory, String url) {
//
//
//        DownloadManager downloadmanager = (DownloadManager) mCtx.
//                getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        //request.set
//        request.setDestinationInExternalFilesDir(mCtx, destinationDirectory, fileName + fileExtension);
//
//        downloadmanager.enqueue(request);
//        Log.i("ddddd","download successsssssssssssss");
//    }
//
}







