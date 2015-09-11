package me.jathusan.android.http;

import android.util.Log;

import com.facebook.Session;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class WheelzoHttpApi {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "WheelzoHttpApi";
    private static final String API_BASE_URL = "http://staging.wheelzo.com/api/v2";
    private static final String HEADER_FB_TOKEN = "Fb-Wheelzo-Token";
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    public static Response getRides() throws IOException {
        Request request = new Request.Builder()
                .url(API_BASE_URL + "/rides")
                .addHeader("Content-type", "application/json")
                .get()
                .build();

        return mOkHttpClient.newCall(request).execute();
    }

    public static Response getMyRides() throws IOException {
        Request request = new Request.Builder()
                .url(API_BASE_URL + "/rides/me")
                .addHeader("Content-Type", "application/json")
                .addHeader(HEADER_FB_TOKEN, Session.getActiveSession().getAccessToken())
                .get()
                .build();

        return mOkHttpClient.newCall(request).execute();
    }

    public static Response getCommentsForRide(int rideId) throws IOException{
        Request request = new Request.Builder()
                .url(API_BASE_URL + "/comments?ride_id=" + rideId)
                .addHeader("Content-type", "application/json")
                .get()
                .build();

        return mOkHttpClient.newCall(request).execute();
    }

    public static Response addComment(JSONObject comment) throws IOException {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, comment.toString());
        Request request = new Request.Builder()
                .url(API_BASE_URL + "/comments")
                .addHeader("Content-Type", "application/json")
                .addHeader(HEADER_FB_TOKEN, Session.getActiveSession().getAccessToken())
                .post(requestBody)
                .build();

        return mOkHttpClient.newCall(request).execute();
    }

    public static Response deleteRide(int rideID) throws IOException {
        Request request = new Request.Builder()
                .url(API_BASE_URL + "/rides/index/" + rideID)
                .addHeader("Content-Type", "application/json")
                .addHeader(HEADER_FB_TOKEN, Session.getActiveSession().getAccessToken())
                .delete()
                .build();

        return mOkHttpClient.newCall(request).execute();
    }

    public static Response createRide(JSONObject ride) throws IOException {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, ride.toString());
        Request request = new Request.Builder()
                .url(API_BASE_URL + "/rides")
                .addHeader("Content-Type", "application/json")
                .addHeader(HEADER_FB_TOKEN, Session.getActiveSession().getAccessToken())
                .post(requestBody)
                .build();

        return mOkHttpClient.newCall(request).execute();
    }
}
