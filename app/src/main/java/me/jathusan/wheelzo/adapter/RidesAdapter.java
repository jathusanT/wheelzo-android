package me.jathusan.wheelzo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.objects.Ride;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {
    private ArrayList<Ride> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDescription;
        public TextView mPrice;
        public TextView mDate;
        public ImageView mImage;

        public ViewHolder(View view) {
            super(view);
            mPrice = (TextView) view.findViewById(R.id.price_text);
            mDescription = (TextView) view.findViewById(R.id.info_text);
            mDate = (TextView) view.findViewById(R.id.date_text);
            mImage = (ImageView) view.findViewById(R.id.card_image);
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
        DecimalFormat df = new DecimalFormat("#.00");
        holder.mDescription.setText(mDataset.get(position).getOrigin() + " to " + mDataset.get(position).getDestination());
        holder.mPrice.setText("$" + df.format(mDataset.get(position).getPrice()));
        holder.mDate.setText(mDataset.get(position).getStart());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}