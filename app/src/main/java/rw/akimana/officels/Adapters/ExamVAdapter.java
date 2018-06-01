package rw.akimana.officels.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rw.akimana.officels.Models.Exam;
import rw.akimana.officels.R;

public class ExamVAdapter extends RecyclerView.Adapter<ExamVAdapter.RecyclerVHolder> {

    Context context;
    List<Exam> examList;
    int currentPosition = 0;
    public onItemClickListener mItemClickListener;

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, int position, Exam exam);
    }
    public class RecyclerVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvTitle, tvMArks, tvCreatedAt;
        public RecyclerVHolder(View v){
            super(v);
            tvTitle = v.findViewById(R.id.tv_exam_title);
            tvMArks = v.findViewById(R.id.tv_exam_marks);
            tvCreatedAt = v.findViewById(R.id.tv_exam_created_at);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(view, getAdapterPosition(), examList.get(getAdapterPosition()));
            }
        }
    }

    public ExamVAdapter(Context context, List<Exam> examList){
        this.context = context;
        this.examList = examList;
    }
    @Override
    public ExamVAdapter.RecyclerVHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_item_view, parent, false);
        RecyclerVHolder vh = new RecyclerVHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerVHolder holder, int position){
        Exam current = examList.get(position);
        holder.tvTitle.setText(current.getTitle());
        holder.tvMArks.setText(current.getMarks());
        holder.tvCreatedAt.setText(current.getCreated());
    }
    public void clearAdapter(){
        examList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return examList.size();
    }
}
