package me.jathusan.android.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import me.jathusan.android.R;
import me.jathusan.android.http.WheelzoHttpApi;

public class CreateRideActivity extends BaseActivity {

    private final Calendar mCalendar = Calendar.getInstance();
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;

    // Views
    private EditText mDateEditText;
    private EditText mTimeEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDateEditText = (EditText) findViewById(R.id.date_edit_text);
        mTimeEditText = (EditText) findViewById(R.id.time_edit_text);

        /* Date Picker */
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            }
        };
        mDatePickerDialog = new DatePickerDialog(CreateRideActivity.this, dateSetListener,
                mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatePickerDialog.show();
            }
        });

        /* Time Picker */
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

            }
        };
        mTimePickerDialog = new TimePickerDialog(CreateRideActivity.this, timeSetListener,
                mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);
        mTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimePickerDialog.show();
            }
        });

        //new CreateRideJob().execute();
    }

    private void updateDateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDateEditText.setText(sdf.format(mCalendar.getTime()));
    }

    private void updateTimeLabel() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private JSONObject createJSONRide(String origin, String destination, String departureDate,
                                      String departureTime, int capacity, double price, String[] dropOffs) {
        JSONObject object = new JSONObject();
        try {
            object.put("origin", origin);
            object.put("destination", destination);
            object.put("departureDate", departureDate);
            object.put("departureTime", departureTime);
            object.put("capacity", capacity);
            object.put("price", price);
            JSONArray dropOffArray = new JSONArray();
            for (String dropOff : dropOffs) {
                dropOffArray.put(dropOff);
            }
            object.put("dropOffs", dropOffArray);
        } catch (Exception e) {
            Log.e("createJSONRide", "Error Creating Ride", e);
        }
        return object;
    }

    private class CreateRideJob extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject myRide = createJSONRide("Mobile One", "Mobile Two", "2015-11-20", "00:00:00", 2, 10, new String[]{"Waterloo", "Toronto"});

            try {
                Response response = WheelzoHttpApi.createRideSwag(myRide);
                return response.isSuccessful();
            } catch (IOException e) {
                return false;
            }
        }
    }


}
