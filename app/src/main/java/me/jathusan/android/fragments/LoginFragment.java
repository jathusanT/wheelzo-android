package me.jathusan.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

import me.jathusan.android.R;
import me.jathusan.android.activities.MainActivity;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final int REAUTH_ACTIVITY_CODE = 100;

    private LoginButton mLoginButton;
    private UiLifecycleHelper mUiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState sessionState, Exception e) {
            onSessionStateChange(session, sessionState, e);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginButton = (LoginButton) rootView.findViewById(R.id.authButton);
        mLoginButton.setFragment(this);
        mLoginButton.setReadPermissions(Arrays.asList("public_profile"));
        return rootView;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (session != null && session.isOpened()) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        if (state.isOpened()) {
            mLoginButton.setVisibility(View.INVISIBLE);
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            mLoginButton.setVisibility(View.VISIBLE);
            Log.i(TAG, "Logged out...");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REAUTH_ACTIVITY_CODE) {
            mUiHelper.onActivityResult(requestCode, resultCode, data);
        }
        mUiHelper.onActivityResult(requestCode, resultCode, data);
    }
}
