package com.testyo.org;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.transform.stream.StreamSource;

import androidx.annotation.Nullable;

public class FileDownloadHelper {

    // character for question extraction


    // download paper from server

    public static Boolean neterrordetected = false;

    String fileForExtraction;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    String fileused = null;
    public static String[] filetoDownloadNeterror = {"0","0"};


    private static Extract_paper extract_paper;

   // private String demofilename = "mydemo.docx";
    private String ommlFile = "OMML2MML.XSL";

    //private String demolink = "https://firebasestorage.googleapis.com/v0/b/myapplicationnav-9397b.appspot.com/o/Previous%20Year%20Paper%202030.docx?alt=media&token=28121a2e-cc99-4f56-b50e-0dd85e77dae0";
    private String ommlLink = "https://firebasestorage.googleapis.com/v0/b/myapplicationnav-9397b.appspot.com/o/OMML2MML.XSL?alt=media&token=85438d50-4167-4e8e-9469-4799a328825f";
    private static StreamSource stylesource;
    private Context mContext;
    private boolean firstTime;

    MainActivity mainActivity;
    ShowMarksActivity showMarksActivity;

    public FileDownloadHelper(Context mContext) {

        this.mContext = mContext;

    }

    public FileDownloadHelper() {

    }


    public void download(){

    }

    public void downloadFromFirebase() {
        String ss = "ram ram jay raja ram";

        //textView.setText(Html.fromHtml(ss));
        //context = this;
        //  webView = (WebView) findViewById(R.id.webView);
        //    webView.getSettings().setJavaScriptEnabled(true);

        String stringXml = "<p>hi what is this is it you</p>";
        //      webView.loadDataWithBaseURL("", stringXml, mimeType, encoding, "");


        stringXml = "<p>hi what is this rrrr</p>";

//        webView.loadDataWithBaseURL("", stringXml, mimeType, encoding, "");
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);



        //downloadPdf(filename,link);
    }


//    public void checkdemoPaper(){
//        downloadPdf(demofilename,demolink);
//        Toast.makeText(mContext, "demo file checking...", Toast.LENGTH_LONG).show();
//    }


    public void firstDownloadOmmlFile() {
        downloadPdf(ommlFile,ommlLink);

    }


    // downloading code

    @SuppressLint("StaticFieldLeak")
    public void downloadPdf(final String localfilename , final String localLink) {
             firstTime = true;  // so that file does not extracted twice
         fileForExtraction = localfilename;



//         if(!mainActivity.isOnline()){
//
//
//         }

        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if(aBoolean){
                    //Log.i("Succcceeesssss","doc downloaded");

                    File file = ((MainBeforeActivity)mContext).getFileStreamPath(localfilename);
                    if (file.exists())
                    {
                        //Log.i("path success","path just downloaded is  "+file.getName());
                        if(ommlFile.equals(file.getName())==false){
                            //Log.i("path success","path other than omml  "+file.getAbsolutePath());
                            
                            if(firstTime){
                                firstTime = false;
                                extract_paper(file); 
                            }
                            
                        }else{

                            // delete omml file and redownload if download error


                            if(file.getTotalSpace() != 13375950848L){
                                file.delete();
                                if(file.exists()){
                                    try {
                                        file.getCanonicalFile().delete();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if(file.exists()){
                                        ((MainBeforeActivity)mContext).getApplicationContext().deleteFile(file.getName());
                                    }
                                }
                                firstDownloadOmmlFile();
                            }
                            //Log.i("path success","omml file size   "+String.valueOf(file.getTotalSpace()));
                        }

                    }
                }else{
                    //Log.i("errrrorrrr","doc not downloaded");

                    //
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return downloadPdf();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);

                Log.i("Download Progress",String.valueOf(values[0]));
            }

            @Nullable
            private Boolean downloadPdf() {
                try {
                    File file = (mContext).getFileStreamPath(localfilename);
                    if (file.exists())
                    {
                        //Log.i("path success","path exist is  "+file.getName());
                        if(ommlFile.equals(file.getName())==false){
                            Log.i("path success","path exist is  "+file.getAbsolutePath());
                            if(firstTime){
                                firstTime = false;
                                extract_paper(file);
                            }
                        }

                        return true;
                    }
                    try {
                        FileOutputStream fileOutputStream = ((MainBeforeActivity)mContext).openFileOutput(localfilename, Context.MODE_PRIVATE);
                        URL u = new URL(localLink);
                        URLConnection conn = u.openConnection();
                        int contentlength = conn.getContentLength();
                        InputStream input = new BufferedInputStream(u.openStream());
                        byte data[] = new byte[contentlength];
                        long total = 0;
                        int count;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            publishProgress((int) ((total * 100) / contentlength));
                            fileOutputStream.write(data, 0, count);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        input.close();
                        return true;
                    } catch (final Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return false;


            }





        }.execute();

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) { /* do something with boolean response */
                //Log.i("Error", "No net");
                if(!internet) {
                    //Log.i("Error", "No net inside");


                    String path = ((MainBeforeActivity) mContext).getFilesDir().getAbsolutePath() + "/" + localfilename +".docx";
                    File file = new File(path);

                    if (!file.exists()) {
//                      filetoDownloadNeterror[0] = localfilename;
//                      filetoDownloadNeterror[1] = localLink;
//                      neterrordetected = true;
//                      Log.i("Error", "No net inside again");
                        Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }


    // call extract method from Extract paper

    public void extract_paper(File downloadedFile) {


        File file = (mContext).getFileStreamPath("OMML2MML.XSL");// (MainBeforeActivity)  casting if not work

        //Log.i("Enter","omml path is  "+file.getAbsolutePath());

        stylesource = new StreamSource(file);


        //InputStream in = mgr.open("OMML2MML.XSL", AssetManager.ACCESS_BUFFER);






        //Log.i("Enter","point of call");





        extract_paper = new Extract_paper(downloadedFile,mContext,stylesource);

        //String pathOmml = "android.resource://"+ getPackageName() + "/" +  R.raw.omml2mml;


        //String pathOmml = getResources().openRawResource(R.raw.omml2mml);




        if(stylesource == null){
            Log.i("Enter","null");
        }
        extract_paper.runExtractCode();


//        final String stringXml1 = "<p>hi what is this</p>";
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                webView.loadData(stringXml1,mimeType,encoding);
//            }
//        }, 10) ;


        // get mainactivity instance
        mainActivity = MainActivity.getInstance();

        showMarksActivity = ShowMarksActivity.getinstance();

        //Log.i("sol",fileForExtraction);

        if(fileForExtraction.equals(MainBeforeActivity.selectedPaperName+MainActivity.ANSWER)){
            mainActivity.readyToSubmit = true;
            Extract_paper.fillDataInModifiedList();
            //Log.i("sol",fileForExtraction);
        }else if(fileForExtraction.equals(MainBeforeActivity.selectedPaperName+MainActivity.SOLUTION)){
            MainActivity.readyToreview = true;
            showMarksActivity.setReviewBtnColor();

//            ShowMarksActivity showMarksActivity = new ShowMarksActivity();
//            showMarksActivity.changeReviewBtnColor();
            //Log.i("sol",fileForExtraction);
        }else if(fileForExtraction.equals(MainBeforeActivity.selectedPaperName)){
            // set total number of question after extracting
            mainActivity.setTotalQestion();
            if(FragmentQuestionAdapter.paperExtracted == false){

                FragmentQuestionAdapter.paperExtracted = true;
                //mainActivity.setRightNav();
                mainActivity.hide_horizontal_progressBar();
            }

            if(Extract_paper.answerIncluded){
                Extract_paper.fillDataInModifiedList();
            }
            //Log.i("sol",fileForExtraction);
        }

        Extract_paper.extractAnswers = false; // now it will extract all things


         // to enable viepager stuff to show webview
        //MainActivity.setCustomViewPager();
        Log.i("Enterr","size " +Extract_paper.getQuestionlist().size());

        //MainActivity.viewPager.setCurrentItem(0);
        Log.i("Enter","I am at the end of end");
//        Log.i("Enter",Extract_paper.questionlist.get(0).toString());
//
//        Log.i("Enter",Extract_paper.optionlist.get(1).toString());


//        "<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "<body>"+ "<p>hi what is this</p>"+ Extract_paper.getQuestionlist().get(0).toString() +
//                "</body>\n" ;
        //"</html>";


    }

    static class InternetCheck extends AsyncTask<Void,Void,Boolean> {

        private Consumer mConsumer;
        public  interface Consumer { void accept(Boolean internet); }

        public  InternetCheck(Consumer consumer) { mConsumer = consumer; execute(); }

        @Override protected Boolean doInBackground(Void... voids) { try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            return true;
        } catch (IOException e) { return false; } }

        @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
    }
}
