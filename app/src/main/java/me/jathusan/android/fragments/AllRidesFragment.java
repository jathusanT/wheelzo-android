package me.jathusan.android.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import me.jathusan.android.R;
import me.jathusan.android.activities.RideInfoActivity;
import me.jathusan.android.adapter.RecyclerItemClickListener;
import me.jathusan.android.adapter.RidesAdapter;
import me.jathusan.android.model.Ride;
import me.jathusan.android.http.WheelzoHttpApi;

public class AllRidesFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "AllRidesFragment";
    private static final String RIDE_PARCELABLE_KEY = "me.jathusan.wheelzo.Ride";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private ArrayList<Ride> mAvailableRides = new ArrayList<Ride>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mNoResults;
    private ProgressBar mSpinner;

    public AllRidesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_rides, container, false);

        mNoResults = (TextView) rootView.findViewById(R.id.noResults);
        mNoResults.setVisibility(View.GONE);
        mSpinner = (ProgressBar) rootView.findViewById(R.id.my_spinner);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.pink_accent_dark),
                getResources().getColor(R.color.yellow_accent_dark),
                getResources().getColor(R.color.blue_accent_dark),
                getResources().getColor(R.color.green_accent_dark));
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rides_recycler_view);
        mRecyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerViewAdapter = new RidesAdapter(mAvailableRides);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent rideInfo = new Intent(getActivity(), RideInfoActivity.class);
                rideInfo.putExtra(RIDE_PARCELABLE_KEY, mAvailableRides.get(position));
                startActivity(rideInfo);
            }
        }));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mRecyclerView.canScrollVertically(-1)) {
                    mSwipeRefreshLayout.setEnabled(false);
                } else {
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                new FetchRidesJob().execute();
            }
        });
        new FetchRidesJob().execute();
    }

    private int getColorForPrice(int price) {
        if (price >= 0 && price < 11) {
            return R.color.very_low_price;
        } else if (price >= 11 && price < 16) {
            return R.color.low_price;
        } else if (price >= 16 && price < 21) {
            return R.color.average_price;
        } else if (price >= 21 && price < 26) {
            return R.color.high_price;
        } else {
            return R.color.very_high_price;
        }
    }

    private class FetchRidesJob extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAvailableRides.clear();
            mNoResults.setVisibility(View.GONE);
            mRecyclerViewAdapter.notifyDataSetChanged();
            if (!mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Response response = WheelzoHttpApi.getRides();

                if (!response.isSuccessful()) {
                    return null;
                }

                JSONArray jsonArray = new JSONArray(response.body().string());

                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Ride ride = gson.fromJson(jsonArray.get(i).toString(), Ride.class);
                    ride.setColor(getResources().getColor(getColorForPrice(ride.getPrice())));
                    mAvailableRides.add(ride);
                }

            } catch (IOException e) {
                Log.e(TAG, "IOException occured while getting rides", e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException occured while parsing get rides request", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mSwipeRefreshLayout.setRefreshing(false);
            mSpinner.setVisibility(View.GONE);
            if (mAvailableRides == null || mAvailableRides.isEmpty()) {
                mNoResults.setText("No Rides Were Found");
                mNoResults.setVisibility(View.VISIBLE);
            } else {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}