package me.jathusan.wheelzo.http;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WheelzoHttpClient {

    public static String getBufferResponse(String url) {

        StringBuilder builder = new StringBuilder();

        try {
            Log.d("getBufferResponse", "URL = " + url);

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);

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


}
