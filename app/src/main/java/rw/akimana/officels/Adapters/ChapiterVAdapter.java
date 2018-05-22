package rw.akimana.officels.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rw.akimana.officels.Models.Chapiter;
import rw.akimana.officels.R;

public class ChapiterVAdapter extends RecyclerView.Adapter<ChapiterVAdapter.RecyclerVHolder> {

    Context context;
    List<Chapiter> chapiterList;
    int currentPosition = 0;
    public onItemClickListener mItemClickListener;

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, int position, Chapiter chapiter);
    }
    public class RecyclerVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvName, tvContents, tvCreatedAt;
        public RecyclerVHolder(View v){
            super(v);

            tvName = (TextView) v.findViewById(R.id.tv_c_name);
            tvContents = (TextView) v.findViewById(R.id.tv_c_contents);
            tvCreatedAt = (TextView) v.findViewById(R.id.tv_c_created_at);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(view, getAdapterPosition(), chapiterList.get(getAdapterPosition()));
            }
        }
    }

    public ChapiterVAdapter(Context context, List<Chapiter> chapiterList){
        this.context = context;
        this.chapiterList = chapiterList;
    }
    @Override
    public ChapiterVAdapter.RecyclerVHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapiter_card_view, parent, false);
        RecyclerVHolder vh = new RecyclerVHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerVHolder holder, int position){
        Chapiter thisChapiter = chapiterList.get(position);
        holder.tvName.setText(thisChapiter.getName());
        holder.tvContents.setText(thisChapiter.getContents());
        holder.tvCreatedAt.setText(thisChapiter.getCreated());
    }
    public void clearAdapter(){
        chapiterList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return chapiterList.size();
    }
}
