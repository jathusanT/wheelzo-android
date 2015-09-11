package me.jathusan.android.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import me.jathusan.android.R;
import me.jathusan.android.adapter.CommentAdapter;
import me.jathusan.android.http.WheelzoHttpApi;
import me.jathusan.android.model.Comment;

public class CommentActivity extends BaseActivity {

    private static final String TAG = "CommentActivity";

    public static final String RIDE_ID_KEY = "ride_id_key";

    private ArrayList<Comment> mComments = new ArrayList<Comment>();

    private RecyclerView mCommentRecycler;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;

    private TextView mNoCommentsText;
    private ProgressBar mSpinner;

    private EditText mCommentEditText;
    private Button mPostCommentButton;

    private int mRideId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCommentRecycler = (RecyclerView) findViewById(R.id.comment_list);
        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mCommentRecycler.setLayoutManager(mRecyclerViewLayoutManager);
        mRecyclerViewAdapter = new CommentAdapter(this, mComments);
        mCommentRecycler.setAdapter(mRecyclerViewAdapter);

        mNoCommentsText = (TextView) findViewById(R.id.no_comments_text);
        mSpinner = (ProgressBar) findViewById(R.id.spinner);
        mPostCommentButton = (Button) findViewById(R.id.post_button);
        mCommentEditText = (EditText) findViewById(R.id.comment_edittext);
        mCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    mPostCommentButton.setEnabled(true);
                } else {
                    mPostCommentButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mPostCommentButton.setEnabled(false);
        mPostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPostCommentButton.isEnabled()) {
                    String comment = mCommentEditText.getText().toString();
                    try {
                        JSONObject jsonComment = new JSONObject();
                        jsonComment.put("comment", comment);
                        jsonComment.put("rideID", Integer.toString(mRideId));

                        Log.d(TAG, jsonComment.toString());
                        Log.d(TAG, "" + mRideId);

                        new CreateCommentJob(jsonComment).execute();
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to create JSON Comment.");
                        Toast.makeText(CommentActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mRideId = getIntent().getExtras().getInt(RIDE_ID_KEY, -1);
        if (mRideId == -1) {
            finishActivityWithError();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchCommentsForRide(mRideId).execute();
    }

    private JSONObject createJSONComment(int rideId, String comment) {
        JSONObject object = new JSONObject();
        try {
            object.put("rideID", rideId);
            object.put("comment", comment);
            return object;
        } catch (Exception e) {
            Log.e(TAG, "Error Creating Comment JSONObject", e);
            return null;
        }
    }

    private class FetchCommentsForRide extends AsyncTask<Void, Void, Void> {

        private int mRideId;

        public FetchCommentsForRide(int rideId) {
            mRideId = rideId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mComments.clear();
            mSpinner.setVisibility(View.VISIBLE);
            mNoCommentsText.setVisibility(View.GONE);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Response response = WheelzoHttpApi.getCommentsForRide(mRideId);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "Failed to fetch comments for ride " + mRideId);
                    return null;
                }

                JSONArray jsonArray = new JSONArray(response.body().string());

                Gson gson = new Gson();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Comment comment = gson.fromJson(jsonArray.get(i).toString(), Comment.class);
                    Log.d(TAG, comment.getComment());
                    mComments.add(comment);
                }

            } catch (IOException e) {
                Log.e(TAG, "IOException occured while getting rides", e);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException occured while parsing get rides request", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mSpinner.setVisibility(View.GONE);
            if (mComments == null || mComments.isEmpty()) {
                mNoCommentsText.setVisibility(View.VISIBLE);
            } else {
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private class CreateCommentJob extends AsyncTask<Void, Void, Boolean> {

        JSONObject mComment;

        public CreateCommentJob(JSONObject comment) {
            mComment = comment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCommentEditText.setText(null);
            mCommentEditText.setEnabled(false);
            mPostCommentButton.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Response response = WheelzoHttpApi.addComment(mComment);
                if (!response.isSuccessful()) {
                    Log.e(TAG, response.toString());
                }
                return response.isSuccessful();
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean successful) {
            super.onPostExecute(successful);
            if (successful) {
                new FetchCommentsForRide(mRideId).execute();
            } else {
                Toast.makeText(CommentActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
            mCommentEditText.setEnabled(true);
        }
    }

}
