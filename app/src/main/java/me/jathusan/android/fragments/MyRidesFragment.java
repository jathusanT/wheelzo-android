package me.jathusan.android.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import me.jathusan.android.R;
import me.jathusan.android.activities.CreateRideActivity;
import me.jathusan.android.activities.RideInfoActivity;
import me.jathusan.android.adapter.RecyclerItemClickListener;
import me.jathusan.android.adapter.RidesAdapter;
import me.jathusan.android.model.Ride;
import me.jathusan.android.http.WheelzoHttpApi;

public class MyRidesFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MyRidesFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private ArrayList<Ride> mAvailableRides = new ArrayList<Ride>();

    private ProgressBar mSpinner;
    private FloatingActionButton mFAB;

    /* No Rides */
    private TextView mNoRidesText;
    private ImageView mNoRidesImage;

    public MyRidesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_rides, container, false);
        setHasOptionsMenu(false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_rides_recycler_view);
        mRecyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerViewAdapter = new RidesAdapter(mAvailableRides);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent rideInfo = new Intent(getActivity(), RideInfoActivity.class);
                rideInfo.putExtra(RideInfoActivity.RIDE_PARCELABLE_KEY, mAvailableRides.get(position));
                startActivity(rideInfo);
            }
        }));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mFAB = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createRideActivity = new Intent(getActivity(), CreateRideActivity.class);
                startActivity(createRideActivity);
            }
        });

        mNoRidesText = (TextView) rootView.findViewById(R.id.no_rides_text);
        mNoRidesImage = (ImageView) rootView.findViewById(R.id.no_rides_image);
        mSpinner = (ProgressBar) rootView.findViewById(R.id.my_spinner);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        new FetchRidesJob().execute();
    }

    private class FetchRidesJob extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSpinner.setVisibility(View.VISIBLE);
            mNoRidesText.setVisibility(View.GONE);
            mNoRidesImage.setVisibility(View.GONE);
            mAvailableRides.clear();
            mRecyclerViewAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                com.squareup.okhttp.Response response = WheelzoHttpApi.getMyRides();
                if (!response.isSuccessful()) {
                    return null;
                }

                JSONArray jsonArray = new JSONArray(response.body().string());

                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Ride ride = gson.fromJson(jsonArray.get(i).toString(), Ride.class);
                    ride.setColor(getResources().getColor(R.color.light_purple));
                    mAvailableRides.add(ride);
                }

            } catch (IOException e) {
                Log.e(TAG, "IOException occurred while getting myRides", e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException occurred while getting myRides", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mSpinner.setVisibility(View.GONE);
            if (mAvailableRides == null || mAvailableRides.isEmpty()) {
                mNoRidesText.setVisibility(View.VISIBLE);
                mNoRidesImage.setVisibility(View.VISIBLE);
            } else {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

}
