package rw.akimana.officels.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import rw.akimana.officels.Models.Course;
import rw.akimana.officels.R;

public class CourseVAdapter extends RecyclerView.Adapter<CourseVAdapter.RecyclerVHolder> {

    Context context;
    List<Course> listCourse;
    int currentPosition = 0;
    public onItemClickListener mItemClickListener;

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, int position, Course myCourse);
    }
    public class RecyclerVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvTitle, tvAssessms, tvChapiters, tvCreatedAt, tvNameShort;
        public RecyclerVHolder(View v){
            super(v);
            tvTitle = v.findViewById(R.id.tv_c_title);
            tvAssessms = v.findViewById(R.id.tv_assignemt);
            tvChapiters = v.findViewById(R.id.tv_chapiters);
            tvCreatedAt = v.findViewById(R.id.tv_created_at);
            tvNameShort = v.findViewById(R.id.tv_name_short);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(view, getAdapterPosition(), listCourse.get(getAdapterPosition()));
            }
        }
    }

    public CourseVAdapter(Context context, List<Course> listCourse){
        this.context = context;
        this.listCourse = listCourse;
    }
    @Override
    public CourseVAdapter.RecyclerVHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card_view, parent, false);
        RecyclerVHolder vh = new RecyclerVHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerVHolder holder, int position){
        Course current = listCourse.get(position);
        String[] fullName = current.getName().split(" ");
        String titleAbrev = "";
        for (int i = 0; i < fullName.length; i++){
//            String firstChar = String.valueOf(fullName[i].charAt(0));
            String firstChar = fullName[i].substring(0,1);
            titleAbrev += firstChar;
        }
        holder.tvTitle.setText(current.getName());
        holder.tvAssessms.setText(current.getAssNum());
        holder.tvChapiters.setText(current.getChapNum());
        holder.tvCreatedAt.setText(current.getCreated());
        holder.tvNameShort.setText(titleAbrev.toUpperCase());
    }
    public void clearAdapter(){
        listCourse.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return listCourse.size();
    }
}
