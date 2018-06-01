package rw.akimana.officels.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rw.akimana.officels.Models.Mark;
import rw.akimana.officels.R;

public class MarksVAdapter extends RecyclerView.Adapter<MarksVAdapter.RecyclerVHolder> {

    Context context;
    List<Mark> markList;
    int currentPosition = 0;

    public class RecyclerVHolder extends RecyclerView.ViewHolder{
        public TextView tvCourse, tvChapiter, tvExam, tvMarks, tvWeight;
        public RecyclerVHolder(View v){
            super(v);
            tvCourse = v.findViewById(R.id.tv_course);
            tvChapiter = v.findViewById(R.id.tv_chapiter);
            tvExam = v.findViewById(R.id.tv_exam);
            tvMarks = v.findViewById(R.id.tv_mark);
            tvWeight = v.findViewById(R.id.tv_weight);
        }
    }

    public MarksVAdapter(Context context, List<Mark> markList){
        this.context = context;
        this.markList = markList;
    }
    @Override
    public MarksVAdapter.RecyclerVHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mark_item_view, parent, false);
        RecyclerVHolder vh = new RecyclerVHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerVHolder holder, int position){
        Mark current = markList.get(position);
        holder.tvCourse.setText(current.getCourse());
        holder.tvChapiter.setText(current.getChapiter());
        holder.tvExam.setText(current.getExam());
        holder.tvMarks.setText(current.getUserMarks());
        holder.tvWeight.setText(current.getMarksWeight());
    }
    public void clearAdapter(){
        markList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return markList.size();
    }
}