package rw.akimana.officels.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rw.akimana.officels.Models.Comment;
import rw.akimana.officels.R;

public class CommentVAdapter extends RecyclerView.Adapter<CommentVAdapter.RecyclerVHolder> {

    Context context;
    List<Comment> commentList;
    int currentPosition = 0;
    public class RecyclerVHolder extends RecyclerView.ViewHolder{
        public TextView tvComment, tvUser, tvCreatedAt;
        public RecyclerVHolder(View v){
            super(v);
            tvUser = v.findViewById(R.id.tv_username);
            tvComment = v.findViewById(R.id.tv_comment);
            tvCreatedAt = v.findViewById(R.id.tv_com_created_at);

        }
    }

    public CommentVAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.commentList = comments;
    }
    @Override
    public CommentVAdapter.RecyclerVHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_view, parent, false);
        RecyclerVHolder vh = new RecyclerVHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerVHolder holder, int position){
        Comment current = commentList.get(position);
        String user = current.getUserName();
        String username = "";
        if(user.length() > 12) username = user.substring(0, 12);
        else username = user;
        holder.tvUser.setText(username);
        holder.tvComment.setText(current.getComment());
        holder.tvCreatedAt.setText(current.getCreated());
    }
    public void clearAdapter(){
        commentList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return commentList.size();
    }
}