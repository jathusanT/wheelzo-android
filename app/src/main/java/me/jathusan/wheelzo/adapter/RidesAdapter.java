package me.jathusan.wheelzo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.framework.Ride;
import me.jathusan.wheelzo.util.FormatUtil;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {
    private ArrayList<Ride> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDescription;
        public TextView mPrice;
        public TextView mDate;

        public ViewHolder(View view) {
            super(view);
            mPrice = (TextView) view.findViewById(R.id.price_text);
            mDescription = (TextView) view.findViewById(R.id.info_text);
            mDate = (TextView) view.findViewById(R.id.date_text);
        }
    }

    public RidesAdapter(ArrayList<Ride> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RidesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ride currentRide = mDataset.get(position);
        holder.mDescription.setText("From:" + currentRide.getOrigin() + "\nTo: " + mDataset.get(position).getDestination());
        holder.mPrice.setText(FormatUtil.formatDollarAmount(currentRide.getPrice()));
        holder.mPrice.setBackgroundColor(currentRide.getColor());
        holder.mDate.setText(currentRide.getStart());
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}