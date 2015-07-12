package me.jathusan.wheelzo.fragments;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.activities.RideInfoActivity;
import me.jathusan.wheelzo.adapter.RecyclerItemClickListener;
import me.jathusan.wheelzo.adapter.RidesAdapter;
import me.jathusan.wheelzo.framework.Ride;
import me.jathusan.wheelzo.http.WheelzoHttpClient;
import me.jathusan.wheelzo.util.FormatUtil;

public class AllRidesFragment extends android.support.v4.app.Fragment {

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
            String bufferResponse = WheelzoHttpClient.getBufferResponse("rides", false);
            Log.d("Test", bufferResponse);
            if (bufferResponse != null) {
                try {
                    JSONArray JSONRides = new JSONArray(bufferResponse);
                    for (int i = 0; i < JSONRides.length(); i++) {
                        JSONObject JSONRide = JSONRides.getJSONObject(i);
                        Ride ride = new Ride();
                        ride.setId(JSONRide.getInt("id"));
                        ride.setDriverId(JSONRide.getInt("driver_id"));
                        ride.setOrigin(JSONRide.getString("origin"));
                        ride.setDestination(JSONRide.getString("destination"));
                        ride.setCapacity(JSONRide.getInt("capacity"));
                        ride.setPrice(JSONRide.getDouble("price"));
                        ride.setStart(FormatUtil.formatDate(JSONRide.getString("start")));
                        ride.setLastUpdated(JSONRide.getString("last_updated"));
                        ride.setDriverFacebookid(JSONRide.getString("driver_facebook_id"));
                        ride.setPersonal(JSONRide.getBoolean("is_personal"));
                        ride.setColor(getResources().getColor(getColorForPrice(ride.getPrice())));
                        mAvailableRides.add(ride);
                    }
                } catch (Exception e) {
                    Log.e("FetchRidesJob", "Error Creating JSONArray");
                }
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

    private int getColorForPrice(double price) {
        int intPrice = (int) price;
        Log.i("Price", "" + intPrice);
        if (intPrice >= 0 && intPrice < 5) {
            return R.color.price0_5;
        } else if (intPrice >= 5 && intPrice < 10) {
            return R.color.price5_10;
        } else if (intPrice >= 10 && intPrice < 15) {
            return R.color.price10_15;
        } else if (intPrice >= 15 && intPrice < 20) {
            return R.color.price15_20;
        } else if (intPrice >= 20 && intPrice < 25) {
            return R.color.price20_25;
        } else if (intPrice >= 25 && intPrice < 30) {
            return R.color.price25_30;
        } else {
            return R.color.price30_35;
        }
    }
}
