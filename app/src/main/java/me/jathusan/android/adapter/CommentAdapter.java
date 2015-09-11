package me.jathusan.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.jathusan.android.R;
import me.jathusan.android.model.Comment;
import me.jathusan.android.model.Ride;
import me.jathusan.android.model.RoundedImageView;
import me.jathusan.android.util.FormatUtil;
import me.jathusan.android.util.ImageUtil;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> mDataset;
    private Context mContext;

    public CommentAdapter(Context context, ArrayList<Comment> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mDataset.get(position);
        holder.mComment.setText(comment.getComment());
        holder.mUserName.setText(comment.getUserName());
        holder.mTimestamp.setText(convertDate(comment.getLastUpdated()));
        ImageUtil.loadFacebookImageIntoView(mContext, comment.getFacebookId(), holder.mUserImage);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mUserName, mComment, mTimestamp;
        public RoundedImageView mUserImage;

        public ViewHolder(View view) {
            super(view);
            mTimestamp = (TextView) view.findViewById(R.id.timestamp);
            mUserName = (TextView) view.findViewById(R.id.user_name);
            mComment = (TextView) view.findViewById(R.id.comment_text);
            mUserImage = (RoundedImageView) view.findViewById(R.id.rounded_profile);
        }
    }

    private String convertDate(String start) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
            return new SimpleDateFormat("EEE, MMM d yyyy, hh:mm aaa").format(date);
        } catch (Exception e) {
            return start;
        }
    }


}
