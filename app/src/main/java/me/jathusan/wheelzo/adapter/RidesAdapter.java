package me.jathusan.wheelzo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.mDescription.setText(currentRide.getOrigin() + " to " + mDataset.get(position).getDestination());
        holder.mPrice.setText(FormatUtil.formatDollarAmount(currentRide.getPrice()));
        //holder.mPrice.setBackgroundColor(getColorForPrice(currentRide.getPrice()));
        holder.mDate.setText(currentRide.getStart());
    }

    private int getColorForPrice(double price) {
        Log.i("Price", price + "");
        if (price >= 0.0 && price < 5.0) {
            return R.color.price0_5;
        } else if (price >= 5.0 && price < 10.0) {
            return R.color.price5_10;
        } else if (price >= 10.0 && price < 15.0) {
            return R.color.price10_15;
        } else if (price >= 15.0 && price < 20.0) {
            return R.color.price15_20;
        } else if (price >= 20.0 && price < 25.0) {
            return R.color.price20_25;
        } else if (price >= 25.0 && price < 30.0) {
            return R.color.price25_30;
        } else {
            return R.color.price30_35;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}