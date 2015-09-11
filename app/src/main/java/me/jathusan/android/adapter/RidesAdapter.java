package me.jathusan.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.jathusan.android.R;
import me.jathusan.android.model.Ride;
import me.jathusan.android.util.FormatUtil;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {
    private ArrayList<Ride> mDataset;

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
        holder.mDesintation.setText(currentRide.getDestination());
        holder.mOrigin.setText("Departing from " + currentRide.getOrigin());
        holder.mPrice.setText(FormatUtil.formatDollarAmount(currentRide.getPrice()));
        holder.mPrice.setBackgroundColor(currentRide.getColor());
        holder.mDate.setText(currentRide.getDepartureDate());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDesintation, mOrigin;
        public TextView mPrice;
        public TextView mDate;

        public ViewHolder(View view) {
            super(view);
            mPrice = (TextView) view.findViewById(R.id.price_text);
            mDesintation = (TextView) view.findViewById(R.id.dest_text);
            mOrigin = (TextView) view.findViewById(R.id.origin_text);
            mDate = (TextView) view.findViewById(R.id.date_text);
        }
    }


}
