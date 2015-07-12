package me.jathusan.wheelzo.http;

import com.facebook.Session;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class WheelzoHttpApi {

    private static final String TAG = "WheelzoHttpApi";
    private static final String API_BASE_URL = "http://www.staging.wheelzo.com/api/v2";
    private static final String HEADER_FB_TOKEN = "Fb-Wheelzo-Token";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

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

    public static Response createRideSwag(JSONObject ride) throws IOException {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, ride.toString());

        Request request = new Request.Builder()
                .url(API_BASE_URL + "/rides")
                .addHeader("Content-type", "application/json")
                .addHeader(HEADER_FB_TOKEN, Session.getActiveSession().getAccessToken())
                .post(requestBody)
                .build();

        return mOkHttpClient.newCall(request).execute();
    }
}
