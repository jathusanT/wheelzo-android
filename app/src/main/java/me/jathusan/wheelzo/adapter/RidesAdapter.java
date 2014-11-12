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
        holder.mDate.setText(formatDate(mDataset.get(position).getStart()));
    }

    private String formatDate(String date) {
        String[] parts = date.split(" ");
        String newDate = parts[0];
        String newTime = parts[1];

        parts = newDate.split("-");
        String year = parts[0];
        String month = getMonthForNumber(Integer.parseInt(parts[1]));
        String day = parts[2];

        newTime = convertToStandardTime(newTime);

        return month + " " + day + ", " + year + " at " + newTime;
    }

    private String convertToStandardTime(String time) {

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        String minutes = parts[1];

        if (hour > 12) {
            return (24 - hour) + ":" + minutes + " p.m.";
        }
        return hour + ":" + minutes + " a.m.";
    }

    private String getMonthForNumber(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "N/A";
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}