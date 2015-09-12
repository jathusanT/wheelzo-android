package me.jathusan.android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import me.jathusan.android.R;
import me.jathusan.android.activities.LoginActivity;
import me.jathusan.android.model.RoundedImageView;
import me.jathusan.android.util.ImageUtil;

public class MyAccountFragment extends Fragment{

    private static final String TAG = "MyAccountFragment";

    private TextView mUserAccount;

    private String mFacebookId = null;
    private RoundedImageView mFacebookPicture;

    private Button mLogoutButton;
    private Button mContactButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
        setHasOptionsMenu(false);
        mUserAccount = (TextView) rootView.findViewById(R.id.user_account);
        mFacebookPicture = (RoundedImageView) rootView.findViewById(R.id.rounded_profile);
        mLogoutButton = (Button) rootView.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogout();
            }
        });
        mContactButton = (Button) rootView.findViewById(R.id.contact_button);
        mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getString(R.string.wheelzo_email), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_title));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email_with)));
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
            makeMeRequest(Session.getActiveSession());
            if (mFacebookId != null) {
                ImageUtil.loadFacebookImageIntoView(getActivity(), mFacebookId, mFacebookPicture);
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void handleLogout() {
        Activity activity = getActivity();
        Session.getActiveSession().closeAndClearTokenInformation();
        Intent intent = new Intent(activity, LoginActivity.class);
        startActivity(intent);
        activity.finish();
    }

    private void makeMeRequest(final Session session) {
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                mUserAccount.setText(user.getName());
                                mFacebookId = user.getId();
                                ImageUtil.loadFacebookImageIntoView(getActivity(), mFacebookId, mFacebookPicture);
                            }
                        }
                        if (response.getError() != null) {
                            // TODO: handle errors better
                        }
                    }
                });
        request.executeAsync();
    }
}
