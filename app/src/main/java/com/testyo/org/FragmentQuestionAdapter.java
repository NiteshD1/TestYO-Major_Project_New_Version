package com.testyo.org;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentQuestionAdapter extends FragmentStatePagerAdapter {

    private Context mCtx;
    public static Boolean paperExtracted = false;

    //public int optionIndex = 0; // to know that option belong to which question

    public List<Fragment> fragments;

    public FragmentQuestionAdapter(FragmentManager fm) {
        super(fm);
        //this.mCtx = mCtx;
        this.fragments = new ArrayList<Fragment>();
        for (int i = 0; i < MainActivity.totalQuestion; i++) {
            fragments.add(new QuestionFragment());


        }
    }

    @Override
    public Fragment getItem(int position) {

        //Log.i("spannew","getcurrentitem "+String.valueOf(position));

        QuestionFragment questionFragment = new QuestionFragment();
        Bundle bundle = new Bundle();


//        if(position >= (fragments.size()-2)){
//            //Fragment newFragment =
//            fragments.add(Fragment.instantiate(mCtx,QuestionFragment.class.getName(),null));
//            notifyDataSetChanged();
//        }

       // bundle.putString("message","Question Number"+position);
        String stringXml = "<p>3. Which is the most abundant gas in the earth's atmosphere?\n" +
                "Nitrogen (78.08%) : "+ String.valueOf(position) + "</p>";

        String stringXml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml-fragment xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\" w:rsidR=\"005470E5\" w:rsidRDefault=\"00600197\" w:rsidP=\"005470E5\"><w:pPr><w:pStyle w:val=\"ListParagraph\"/></w:pPr><w:proofErr w:type=\"gramStart\"/><w:r><w:t>A</w:t></w:r><w:r w:rsidR=\"005470E5\"><w:t>2</w:t></w:r><w:r><w:t xml:space=\"preserve\"> </w:t></w:r><w:r w:rsidR=\"005470E5\"><w:t>.</w:t></w:r><w:proofErr w:type=\"gramEnd\"/><w:r w:rsidR=\"005470E5\"><w:t xml:space=\"preserve\"> </w:t></w:r><w:proofErr w:type=\"spellStart\"/><w:proofErr w:type=\"gramStart\"/><w:r w:rsidR=\"005470E5\"><w:t>shyam</w:t></w:r><w:proofErr w:type=\"spellEnd\"/><w:proofErr w:type=\"gramEnd\"/><w:r w:rsidR=\"00545208\"><w:t xml:space=\"preserve\"> </w:t></w:r><math><mi xmlns=\"http://www.w3.org/1998/Math/MathML\">x</mi><mo xmlns=\"http://www.w3.org/1998/Math/MathML\">=</mo><mfrac xmlns=\"http://www.w3.org/1998/Math/MathML\"><mrow><mo>-</mo><mi>b</mi><mo>±</mo><msqrt><msup><mrow><mi>b</mi></mrow><mrow><mn>2</mn></mrow></msup><mo>-</mo><mn>4</mn><mi>a</mi><mi>c</mi></msqrt></mrow><mrow><mn>2</mn><mi>a</mi></mrow></mfrac></math></xml-fragment>";




                String stringXml1 = "<p>Hi what is this is it you</p>";


//


             int localpos = position;

        if(Extract_paper.solutionModeActive){


                bundle.putString("solutionkey",Extract_paper.getSolutionlist().get(localpos).toString());
                bundle.putString("spanindexright",String.valueOf(Extract_paper.getModifiedanswerlist().get(position).toString()));


        }


            if(paperExtracted && Extract_paper.getOptionlist().get(localpos*4 + 2)!= null){

                bundle.putString("questionkey",Extract_paper.getQuestionlist().get(localpos).toString());
                bundle.putString("optionkey1",Extract_paper.getOptionlist().get(localpos*4 + 0).toString());
                bundle.putString("optionkey2",Extract_paper.getOptionlist().get(localpos*4 + 1).toString());
                bundle.putString("optionkey3",Extract_paper.getOptionlist().get(localpos*4 + 2).toString());
                bundle.putString("optionkey4",Extract_paper.getOptionlist().get(localpos*4 + 3).toString());

            }else {
                bundle.putString("questionkey","Question");
                bundle.putString("optionkey1","option 1");
                bundle.putString("optionkey2","option 2");
                bundle.putString("optionkey3","option 3");
                bundle.putString("optionkey4","option 4");

            }
              bundle.putString("spanindex",String.valueOf(CalculateMarks.selectedRadioList[position]));
             bundle.putString("markforreviewinfo",String.valueOf(CalculateMarks.symbolList[position]));
             bundle.putString("localTimerString",String.valueOf(CalculateMarks.localTimerList[position]));
             if(position == MainActivity.totalQuestion -1){
                 bundle.putString("lastquestioninfo","1");
             }else {
                 bundle.putString("lastquestioninfo","0");
             }


//            position = position+1;
            fragments.get(position).setArguments(bundle);
//        if(position == 2){
//            return fragments.get(fragments.size()-1);
//        }else{

//            if(position == 0){
//             return fragments.get(position);
//            }else {
            return fragments.get(position);
         // }

     //   return questionFragment;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        super.restoreState(state, loader);
    }
//    @Override
//    public int getItemPosition(Object object) {
//        // POSITION_NONE makes it possible to reload the PagerAdapter
//        return POSITION_NONE;
//    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
