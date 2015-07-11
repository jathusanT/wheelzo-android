package me.jathusan.wheelzo.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.activities.CreateRideActivity;
import me.jathusan.wheelzo.activities.RideInfoActivity;
import me.jathusan.wheelzo.adapter.RecyclerItemClickListener;
import me.jathusan.wheelzo.adapter.RidesAdapter;
import me.jathusan.wheelzo.framework.Ride;
import me.jathusan.wheelzo.framework.RoundedImageView;
import me.jathusan.wheelzo.http.WheelzoHttpClient;
import me.jathusan.wheelzo.util.FormatUtil;

public class MyAccountFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MyRidesFragment";
    private LoginButton mLoginButton;
    private UiLifecycleHelper mUiHelper;
    private TextView mUserAccount;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private ArrayList<Ride> mAvailableRides = new ArrayList<Ride>();
    private static final int REAUTH_ACTIVITY_CODE = 100;
    private LoginButton mFacebookLoginButton;
    private Button mCreateRideButton;
    private String mFacebookId = null;
    private RoundedImageView mFacebookPicture;
    private ProgressBar mSpinner;
    private boolean imageLoaded, modifyingDataSet = false;

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

        mSpinner = (ProgressBar) rootView.findViewById(R.id.my_spinner);
        mLoginButton = (LoginButton) rootView.findViewById(R.id.authButton);
        mLoginButton.setFragment(this);
        mLoginButton.setReadPermissions(Arrays.asList("public_profile"));
        mUserAccount = (TextView) rootView.findViewById(R.id.user_account);
        mFacebookPicture = (RoundedImageView) rootView.findViewById(R.id.rounded_profile);
        mFacebookLoginButton = (LoginButton) rootView.findViewById(R.id.authButton);
        mCreateRideButton = (Button) rootView.findViewById(R.id.create_button);
        mCreateRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyingDataSet = true;
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
            mSpinner.setVisibility(View.VISIBLE);
            new FetchRidesJob().execute();
            if (!imageLoaded && mFacebookId != null) {
                new FetchFacebookImage("http://graph.facebook.com/" + mFacebookId + "/picture?width=150&height=150", mFacebookPicture).execute();
            }
        } else {
            mFacebookPicture.setVisibility(View.GONE);
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
        if (modifyingDataSet && session != null && (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
            modifyingDataSet = false;
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
                                //mProfilePicture.setProfileId(user.getId());
                                mUserAccount.setText(user.getName());
                                mFacebookId = user.getId();

                                Log.i(TAG, "Facebook ID = " + mFacebookId);
                                new FetchFacebookImage("https://graph.facebook.com/" + mFacebookId + "/picture?width=200&height=200", mFacebookPicture).execute();
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
            mFacebookPicture.setVisibility(View.VISIBLE);
            mUserAccount.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.VISIBLE);
            new FetchRidesJob().execute();
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            mFacebookPicture.setVisibility(View.GONE);
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
                        ride.setColor(getResources().getColor(R.color.green_accent_dark));
                        // TODO: Dropoffs
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
            mSpinner.setVisibility(View.GONE);
            if (mAvailableRides == null || mAvailableRides.isEmpty()) {
                // Update UI For No Rides
            } else {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private class FetchFacebookImage extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private RoundedImageView imageView;

        private FetchFacebookImage(String url, RoundedImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                Log.e("Facebook Image Load", "Exception while downloading Faceobok image", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                imageLoaded = true;
            }
        }
    }

}