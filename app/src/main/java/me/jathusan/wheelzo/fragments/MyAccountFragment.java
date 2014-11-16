package me.jathusan.wheelzo.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.activities.CreateRideActivity;
import me.jathusan.wheelzo.activities.RideInfoActivity;
import me.jathusan.wheelzo.adapter.RecyclerItemClickListener;
import me.jathusan.wheelzo.adapter.RidesAdapter;
import me.jathusan.wheelzo.framework.Ride;
import me.jathusan.wheelzo.http.WheelzoHttpClient;
import me.jathusan.wheelzo.util.FormatUtil;

public class MyAccountFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MyRidesFragment";
    private LoginButton mLoginButton;
    private UiLifecycleHelper mUiHelper;
    private TextView mUserAccount;
    private ProfilePictureView mProfilePicture;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private ArrayList<Ride> mAvailableRides = new ArrayList<Ride>();
    private static final int REAUTH_ACTIVITY_CODE = 100;
    private LoginButton mFacebookLoginButton;
    private Button mCreateRideButton;

    public MyAccountFragment() {
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState sessionState, Exception e) {
            onSessionStateChange(session, sessionState, e);
        }
    };

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
                startActivity(new Intent(getActivity(), RideInfoActivity.class));
            }
        }));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mLoginButton = (LoginButton) rootView.findViewById(R.id.authButton);
        mLoginButton.setFragment(this);
        mLoginButton.setReadPermissions(Arrays.asList("public_profile"));
        mUserAccount = (TextView) rootView.findViewById(R.id.user_account);
        mProfilePicture = (ProfilePictureView) rootView.findViewById(R.id.selection_profile_pic);
        mProfilePicture.setCropped(true);
        mProfilePicture.setVisibility(View.GONE);
        mFacebookLoginButton = (LoginButton) rootView.findViewById(R.id.authButton);
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
            mFacebookLoginButton.setVisibility(View.GONE);
            new FetchRidesJob().execute();
        } else {
            mProfilePicture.setVisibility(View.GONE);
            mUserAccount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUiHelper = new UiLifecycleHelper(getActivity(), callback);
        mUiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        if (session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        mUiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUiHelper.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REAUTH_ACTIVITY_CODE) {
            mUiHelper.onActivityResult(requestCode, resultCode, data);
        }
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void makeMeRequest(final Session session) {
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                mProfilePicture.setProfileId(user.getId());
                                mUserAccount.setText(user.getName());
                            }
                        }
                        if (response.getError() != null) {
                            // Handle errors, will do so later.
                        }
                    }
                });
        request.executeAsync();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            makeMeRequest(session);
        }

        if (state.isOpened()) {
            mFacebookLoginButton.setVisibility(View.GONE);
            mProfilePicture.setVisibility(View.VISIBLE);
            mUserAccount.setVisibility(View.VISIBLE);
            new FetchRidesJob().execute();
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            mProfilePicture.setVisibility(View.GONE);
            mUserAccount.setVisibility(View.GONE);
            mFacebookLoginButton.setVisibility(View.VISIBLE);
            Log.i(TAG, "Logged out...");
        }
    }

    private class FetchRidesJob extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAvailableRides.clear();
            mRecyclerViewAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String bufferResponse = WheelzoHttpClient.getBufferResponse("rides/me", true);
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
                        ride.setPersonal(JSONRide.getBoolean("is_personal"));
                        ride.setColor(getResources().getColor(R.color.pink_accent));
                        // TODO: Dropoffs, Comments and Passengers
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
            if (mAvailableRides == null || mAvailableRides.isEmpty()) {
                // Update UI For No Rides
            } else {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

}