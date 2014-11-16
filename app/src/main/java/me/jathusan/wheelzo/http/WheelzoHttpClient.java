package me.jathusan.wheelzo.http;

import android.util.Log;

import com.facebook.Session;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WheelzoHttpClient {

    private static final String WHEELZO_URL = "http://www.staging.wheelzo.com/api/v2/";

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

        String url = WHEELZO_URL + "/rides";

        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("FB_WHEELZO_TOKEN", Session.getActiveSession().getAccessToken());

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("origin", "First Mobile Origin!"));
            nameValuePairs.add(new BasicNameValuePair("destination", "First Mobile Destination!"));
            nameValuePairs.add(new BasicNameValuePair("departureDate", "2014-09-23"));
            nameValuePairs.add(new BasicNameValuePair("departureTime", "12:00:00"));
            nameValuePairs.add(new BasicNameValuePair("capacity", "2"));
            nameValuePairs.add(new BasicNameValuePair("price", "10"));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(request);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (Exception e) {
            Log.e("WheelzoHttpClient", "Exception while executing http request", e);
        }
    }

}
