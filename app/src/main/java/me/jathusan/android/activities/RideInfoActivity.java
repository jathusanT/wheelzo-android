package me.jathusan.android.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.jathusan.android.R;
import me.jathusan.android.http.WheelzoHttpApi;
import me.jathusan.android.model.Ride;
import me.jathusan.android.model.RoundedImageView;
import me.jathusan.android.util.ImageUtil;

public class RideInfoActivity extends BaseActivity {

    private static final String TAG = "RideInfoActivity";

    /* Keys */
    public static final String RIDE_PARCELABLE_KEY = "me.jathusan.wheelzo.Ride";

    private TextView mOrigin;
    private TextView mDestination;
    private TextView mDate;
    private TextView mCapacity;
    private TextView mPrice;
    private TextView mDriverName;
    private Ride mRide;
    private RoundedImageView mDriverPicture;
    private Button mCommentsButton;
    private Button mProfileButton;
    private Button mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_info);

        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, "Failed to create RideInfoActivity -- intent was null");
            finishActivityWithError();
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.e(TAG, "Failed to create RideInfoActivity -- bundle was null");
            finishActivityWithError();
        }

        mRide = bundle.getParcelable(RIDE_PARCELABLE_KEY);
        if (mRide == null) {
            Log.e(TAG, "Failed to create RideInfoActivity -- parcelable was null");
            finishActivityWithError();
        }

        mCommentsButton = (Button) findViewById(R.id.view_comments_button);
        mCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentsActivity = new Intent(RideInfoActivity.this, CommentActivity.class);
                commentsActivity.putExtra(CommentActivity.RIDE_ID_KEY, mRide.getId());
                startActivity(commentsActivity);
            }
        });

        mProfileButton = (Button) findViewById(R.id.view_profile_button);
        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    mDriverPicture.callOnClick();
                } else {
                    mDriverPicture.performClick();
                }
            }
        });

        mDeleteButton = (Button) findViewById(R.id.delete_ride_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(RideInfoActivity.this)
                        .setTitle(R.string.delete_confirmation_title)
                        .setMessage(R.string.delete_confirmation_message)
                        .setPositiveButton(getString(R.string.delete_confirmation_yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        new DeleteRideJob(mRide.getId()).execute();
                                        finish();
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.delete_confirmation_no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        // noop
                                    }
                                })
                        .setCancelable(false)
                        .show();
            }
        });

        mDriverPicture = (RoundedImageView) findViewById(R.id.driver_picture);
        mDriverName = (TextView) findViewById(R.id.driver_name);
        mOrigin = (TextView) findViewById(R.id.origin_text);
        mDestination = (TextView) findViewById(R.id.dest_text);
        mDate = (TextView) findViewById(R.id.date_text);
        mCapacity = (TextView) findViewById(R.id.cap_text);
        mPrice = (TextView) findViewById(R.id.price_text);

        if (mRide.isPersonal()) {
            mProfileButton.setVisibility(View.GONE);
            mDeleteButton.setVisibility(View.VISIBLE);
        }

        // Get the back button to show up in the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageUtil.loadFacebookImageIntoView(this, mRide.getDriverFacebookid(), mDriverPicture);
        setupInterface();
    }

    private void setupInterface() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0);
        }
        mDriverName.setText(mRide.getDriverName());
        mOrigin.setText("Origin: " + mRide.getOrigin());
        mDestination.setText("Destination: " + mRide.getDestination());
        mDate.setText(convertDate(mRide.getStart()));
        mCapacity.setText(Integer.toString(mRide.getCapacity()) + " Seat Capacity");
        mPrice.setText(mRide.getPrice() + ".00 per seat");
    }

    private String convertDate(String start) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start);
            return new SimpleDateFormat("EEE, MMM d yyyy, hh:mm aaa").format(date);
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse date");
            return start;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private class DeleteRideJob extends AsyncTask<Void, Void, Boolean> {

        int mRideId;

        public DeleteRideJob(int rideId) {
            mRideId = rideId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Response response = WheelzoHttpApi.deleteRide(mRideId);
                if (!response.isSuccessful()) {
                    Log.e(TAG, response.toString());
                }
                return response.isSuccessful();
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean successful) {
            super.onPostExecute(successful);
            if (!successful) {
                Toast.makeText(RideInfoActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
