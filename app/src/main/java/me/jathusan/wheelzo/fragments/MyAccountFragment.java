package me.jathusan.wheelzo.fragments;

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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.activities.CreateRideActivity;
import me.jathusan.wheelzo.activities.LoginActivity;
import me.jathusan.wheelzo.activities.RideInfoActivity;
import me.jathusan.wheelzo.adapter.RecyclerItemClickListener;
import me.jathusan.wheelzo.adapter.RidesAdapter;
import me.jathusan.wheelzo.framework.Ride;
import me.jathusan.wheelzo.framework.RoundedImageView;
import me.jathusan.wheelzo.http.WheelzoHttpApi;
import me.jathusan.wheelzo.util.ImageUtil;

public class MyAccountFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MyRidesFragment";
    private static final String RIDE_PARCELABLE_KEY = "me.jathusan.wheelzo.Ride";

    private TextView mUserAccount;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private ArrayList<Ride> mAvailableRides = new ArrayList<Ride>();
    private Button mCreateRideButton;
    private String mFacebookId = null;
    private RoundedImageView mFacebookPicture;
    private ProgressBar mSpinner;
    private boolean imageLoaded = false;

    public MyAccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_rides_recycler_view);
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
            }
        });

        mSpinner = (ProgressBar) rootView.findViewById(R.id.my_spinner);
        mUserAccount = (TextView) rootView.findViewById(R.id.user_account);
        mFacebookPicture = (RoundedImageView) rootView.findViewById(R.id.rounded_profile);
        mCreateRideButton = (Button) rootView.findViewById(R.id.create_button);
        mCreateRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateRideActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
            mSpinner.setVisibility(View.VISIBLE);
            makeMeRequest(Session.getActiveSession());
            new FetchRidesJob().execute();
            if (mFacebookId != null) {
                ImageUtil.loadFacebookImageIntoView(getActivity(), mFacebookId, mFacebookPicture);
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void makeMeRequest(final Session session) {
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                //mProfilePicture.setProfileId(user.getId());
                                mUserAccount.setText(user.getName());
                                mFacebookId = user.getId();

                                Log.i(TAG, "Facebook ID = " + mFacebookId);
                                ImageUtil.loadFacebookImageIntoView(getActivity(), mFacebookId, mFacebookPicture);
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                });
        request.executeAsync();
    }

    private class FetchRidesJob extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSpinner.setVisibility(View.VISIBLE);
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
                    ride.setColor(getResources().getColor(R.color.green_accent_dark));
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
                // Update UI For No Rides
            } else {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

}
