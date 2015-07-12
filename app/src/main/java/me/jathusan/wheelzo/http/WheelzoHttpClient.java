package me.jathusan.wheelzo.http;

import android.util.Log;

import com.facebook.Session;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WheelzoHttpClient {

    private static final String API_BASE_URL = "http://www.staging.wheelzo.com/api/v2/";

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String HEADER_FB_TOKEN = "Fb-Wheelzo-Token";

    public static String getBufferResponse(String url, boolean requiresHeader) {

        url = API_BASE_URL + url;

        StringBuilder builder = new StringBuilder();

        try {
            HttpClient client = new DefaultHttpClient();

            HttpGet request = new HttpGet(url);
            request.setHeader("Content-Type", "application/json");

            if (requiresHeader) {
                Session session = Session.getActiveSession();
                if (session == null) {
                    Log.e("WheelzoHttpClient", "Requested Me Data, But Session Was Null!");
                    return null;
                }
                request.setHeader("FB_WHEELZO_TOKEN", session.getAccessToken());
            }

            HttpResponse response = client.execute(request);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

            for (String line = null; (line = br.readLine()) != null; ) {
                builder.append(line).append("\n");
            }

            return builder.toString();

        } catch (Exception e) {
            Log.e("WheelzoHttpClient", "Exception while executing http request", e);
        }

        return null;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Response createRideSwag(JSONObject ride) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, ride.toString());

        Request request = new Request.Builder()
                .url(API_BASE_URL + "rides")
                .addHeader("Content-type", "application/json")
                .addHeader(HEADER_FB_TOKEN, Session.getActiveSession().getAccessToken())
                .post(requestBody)
                .build();

        return client.newCall(request).execute();
    }
}
