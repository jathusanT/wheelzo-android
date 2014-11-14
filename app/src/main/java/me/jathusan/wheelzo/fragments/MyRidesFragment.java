package me.jathusan.wheelzo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import org.w3c.dom.Text;

import java.util.Arrays;

import me.jathusan.wheelzo.R;

public class MyRidesFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "MyRidesFragment";
    private LoginButton mLoginButton;
    private UiLifecycleHelper mUiHelper;
    private TextView mSubtitleText, mTitleText, mUserAccount;
    private ProfilePictureView mProfilePicture;
    private static final int REAUTH_ACTIVITY_CODE = 100;

    public MyRidesFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_my_rides, container, false);

        mLoginButton = (LoginButton) rootView.findViewById(R.id.authButton);
        mLoginButton.setFragment(this);
        mLoginButton.setReadPermissions(Arrays.asList("public_profile"));
        mSubtitleText = (TextView) rootView.findViewById(R.id.subtitle);
        mTitleText = (TextView) rootView.findViewById(R.id.title);
        mUserAccount = (TextView) rootView.findViewById(R.id.user_account);
        mProfilePicture = (ProfilePictureView) rootView.findViewById(R.id.selection_profile_pic);
        mProfilePicture.setCropped(true);
        return rootView;
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
        if (requestCode == REAUTH_ACTIVITY_CODE){
            mUiHelper.onActivityResult(requestCode, resultCode, data);
        }
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void makeMeRequest(final Session session){
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
        if (session != null && session.isOpened()){
            makeMeRequest(session);
        }

        if (state.isOpened()) {
            mTitleText.setVisibility(View.GONE);
            mSubtitleText.setVisibility(View.GONE);
            mProfilePicture.setVisibility(View.VISIBLE);
            mUserAccount.setVisibility(View.VISIBLE);
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            mTitleText.setVisibility(View.VISIBLE);
            mSubtitleText.setVisibility(View.VISIBLE);
            mProfilePicture.setVisibility(View.GONE);
            mUserAccount.setVisibility(View.GONE);
            Log.i(TAG, "Logged out...");
        }
    }
}