package me.jathusan.wheelzo.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import me.jathusan.wheelzo.R;
import me.jathusan.wheelzo.http.WheelzoHttpClient;

public class CreateRideActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new CreateRideJob().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private JSONObject createJSONRide(String origin, String destination, String departureDate,
                                      String departureTime, int capacity, double price, String[] dropOffs){
        JSONObject object = new JSONObject();
        try{
            object.put("origin", origin);
            object.put("destination", destination);
            object.put("departureDate", departureDate);
            object.put("departureTime", departureTime);
            object.put("capacity", capacity);
            object.put("price", price);
            JSONArray dropOffArray = new JSONArray();
            for (String dropOff : dropOffs){
                dropOffArray.put(dropOff);
            }
            object.put("dropOffs", dropOffArray);
        } catch (Exception e){
            Log.e("createJSONRide", "Error Creating Ride", e);
        }
        return object;
    }

    private class CreateRideJob extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject myRide = createJSONRide("Mobile One", "Mobile Two", "2014-11-20", "00:00:00", 2, 10, new String[]{"Waterloo","Toronto"});
            WheelzoHttpClient.createRide(myRide);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Jathusan", "DONE!");
        }
    }


}
