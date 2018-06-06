package rw.akimana.officels.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import rw.akimana.officels.AppRequest.ContentRequest;
import rw.akimana.officels.Controllers.AppController;
import rw.akimana.officels.Controllers.ItemDivider;
import rw.akimana.officels.Models.Content;
import rw.akimana.officels.R;

public class ContentAdapter  extends RecyclerView.Adapter<ContentAdapter.RecyclerVHolder> {

    Context context;
    List<Content> contentList;
    int currentPosition = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public onItemClickListener mItemClickListener;

    public void setOnItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClickListener(View view, int position, Content content);
    }
    public class RecyclerVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvDetail, tvCreatedAt, tvNo, tvTitle;
        public NetworkImageView imageViewContent;
        public RecyclerVHolder(View v){
            super(v);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();

            tvDetail = v.findViewById(R.id.tv_content_detail);
            tvCreatedAt = v.findViewById(R.id.tv_c_created_at);
            tvNo = v.findViewById(R.id.tv_no);
            tvTitle = v.findViewById(R.id.tv_title);
            imageViewContent = v.findViewById(R.id.img_content);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(view, getAdapterPosition(), contentList.get(getAdapterPosition()));
            }
        }
    }

    public ContentAdapter(Context context, List<Content> contentList){
        this.context = context;
        this.contentList = contentList;
    }
    @Override
    public ContentAdapter.RecyclerVHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_view, parent, false);
        RecyclerVHolder vh = new RecyclerVHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerVHolder holder, int position){
        int no = position + 1;
        final Content thisContent = contentList.get(position);

        holder.tvDetail.setText(thisContent.getDetail());
        holder.tvNo.setText(String.valueOf(no)+". ");
        holder.tvTitle.setText(thisContent.getTitle());
        holder.imageViewContent.setImageUrl(thisContent.getImage(), imageLoader);
        holder.tvCreatedAt.setText(thisContent.getCreated());
    }
    public void clearAdapter(){
        contentList.clear();
        notifyDataSetChanged();
    }
    public void refreshData(){
        notifyDataSetChanged();
    }

    public int numOfItems(){
        return getItemCount();
    }
    @Override
    public int getItemCount(){
        return contentList.size();
    }
}