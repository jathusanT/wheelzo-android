package me.jathusan.wheelzo.http;

import android.util.Log;

import com.facebook.Session;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WheelzoHttpClient {

    private static final String WHEELZO_URL = "http://www.staging.wheelzo.com/api/v2/";
    private static final int HTTP_TIMEOUT = 10000;

    public static String getBufferResponse(String url, boolean requiresHeader) {

        url = WHEELZO_URL + url;

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

    public static void createRide(JSONObject ride) {

        String url = WHEELZO_URL + "rides";

        try {

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), HTTP_TIMEOUT);
            HttpPost post = new HttpPost(url);
            StringEntity rideEntity = new StringEntity(ride.toString());
            rideEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(rideEntity);
            post.setHeader("Accept", "application/json");
            post.setHeader(HTTP.CONTENT_TYPE, "application/json");
            post.setHeader("FB_WHEELZO_TOKEN", Session.getActiveSession().getAccessToken());
            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            Log.d("Jathusan", result.toString());

        } catch (ClientProtocolException e) {
            Log.i("Jathusan", "BAD");
        } catch (IOException e) {
            Log.i("Jathusan", "BAD");
        } catch (Exception e) {
            Log.e("Jathusan", "Exception while executing http request", e);
        }
    }
}
