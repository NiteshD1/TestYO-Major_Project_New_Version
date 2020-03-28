package com.testyo.org;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NavAdapter extends RecyclerView.Adapter<NavAdapter.NavQuestionViewHolder> {
    //this context we use to inflate the layout
    private Context mCtx;

    private static FragmentQuestionAdapter fragmentQuestionAdapter;


    // List to show test head

    public List<NavQuestion> NavQuestionList;

    public NavAdapter(Context mCtx, List<NavQuestion> navQuestionList) {
        this.mCtx = mCtx;
        NavQuestionList = navQuestionList;

    }

    @NonNull
    @Override
    public NavQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.navquestion,null);
        return new NavQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavQuestionViewHolder navQuestionViewHolder,final int position) {

        final NavQuestion navQuestion = NavQuestionList.get(position);
        navQuestion.setqNumber(String.valueOf(position+1));

        //binding the data with view holder view

        navQuestionViewHolder.navQuestionBtn.setText(navQuestion.getqNumber());

        navQuestionViewHolder.navQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                MainActivity mainActivity = MainActivity.getInstance();
//                mainActivity.getrecycleitem(position);
                //Toast.makeText(mCtx, "Btn pressed"+position+1, Toast.LENGTH_SHORT).show();

                // setting next position

                //int next_pos = MainActivity.viewPager.getCurrentItem();

//                if(position > next_pos){
//                    next_pos = next_pos+1;
//                    // for adding fragment when nav button clicked
//                    addFragment(true,next_pos);
//
//                }else if (position < next_pos){
//                    next_pos = next_pos -1;
//                    addFragment(false,next_pos);
//                }else{
//                    next_pos = position;
//                }



                MainActivity.viewPager.setCurrentItem(position);


                MainActivity.drawer.closeDrawer(Gravity.RIGHT);

                //Toast.makeText("Btn pressed",MainActivity.class,Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(mCtx,MainActivity.class);
                //intent.putExtra("paperNumber", String.valueOf(position +1));



                //mCtx.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return NavQuestionList.size();
    }


    //getting the test head list and context with constructor


    public class NavQuestionViewHolder extends RecyclerView.ViewHolder {

        Button navQuestionBtn;
        public NavQuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            navQuestionBtn = itemView.findViewById(R.id.nav_Question);
        }


    }



//    public void update(){
//        Button n = NavAdapter.this.getItemId(0);
//        n.setBackground(R.drawable.circlered);
//    }
    // for adding fragment when nav button clicked

//    public void addFragment(Boolean addnextFragment,int next_pos){
//        fragmentQuestionAdapter = MainActivity.getFadapter();
//        QuestionFragment newFrag = (QuestionFragment) Fragment.instantiate(mCtx,QuestionFragment.class.getName());
//
//
//
//            if(next_pos >= (fragmentQuestionAdapter.fragments.size()-3)){
//                fragmentQuestionAdapter.fragments.add(newFrag);//Fragment.instantiate(mCtx,QuestionFragment.class.getName()));
//                fragmentQuestionAdapter.notifyDataSetChanged();
//            }
//
//        }else {
//             fragmentQuestionAdapter.fragments.add( newFrag );
//            fragmentQuestionAdapter.notifyDataSetChanged();
//
//            fragmentQuestionAdapter.fragments.remove( fragmentQuestionAdapter.fragments.size() - 1 );
//            fragmentQuestionAdapter.fragments.add( 0, newFrag );
//            fragmentQuestionAdapter.notifyDataSetChanged();
//        }

//    }



}
