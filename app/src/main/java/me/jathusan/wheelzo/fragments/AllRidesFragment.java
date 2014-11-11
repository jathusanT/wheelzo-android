package me.jathusan.wheelzo.fragments;

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.adapter.RidesAdapter;
import me.jathusan.wheelzo.http.WheelzoHttpClient;
import me.jathusan.wheelzo.objects.Ride;

public class AllRidesFragment extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private ArrayList<Ride> mAvailableRides = new ArrayList<Ride>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public AllRidesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.pink_accent, R.color.yellow_accent, R.color.blue_accent, R.color.green_accent);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rides_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerViewAdapter = new RidesAdapter(mAvailableRides);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

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

    private class FetchRidesJob extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAvailableRides.clear();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String bufferResponse = WheelzoHttpClient.getBufferResponse(getString(R.string.base_url) + "rides");
            if (bufferResponse != null){
                try {
                    JSONArray JSONRides = new JSONArray(bufferResponse);
                    for (int i = 0; i < JSONRides.length(); i++){
                        JSONObject JSONRide = JSONRides.getJSONObject(i);
                        Ride ride = new Ride();
                        ride.setId(JSONRide.getInt("id"));
                        ride.setDriverId(JSONRide.getInt("driver_id"));
                        ride.setOrigin(JSONRide.getString("origin"));
                        ride.setDestination(JSONRide.getString("destination"));
                        ride.setCapacity(JSONRide.getInt("capacity"));
                        ride.setPrice(JSONRide.getLong("price"));
                        ride.setStart(JSONRide.getString("start"));
                        ride.setLastUpdated(JSONRide.getString("last_updated"));
                        ride.setPersonal(JSONRide.getBoolean("is_personal"));
                        // TODO: Dropoffs, Comments and Passengers
                        mAvailableRides.add(ride);
                    }
                } catch (Exception e){
                    Log.e("FetchRidesJob", "Error Creating JSONArray");
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mAvailableRides == null || mAvailableRides.isEmpty()){
                // update UI that no data was found
            } else {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }

            if (mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}