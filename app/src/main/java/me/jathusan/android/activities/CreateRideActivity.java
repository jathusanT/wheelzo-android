package me.jathusan.android.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import me.jathusan.android.R;
import me.jathusan.android.model.Ride;
import me.jathusan.android.http.WheelzoHttpApi;

public class CreateRideActivity extends BaseActivity {

    private static final String TAG = "CreateRideActivity";

    private final Calendar mCalendar = Calendar.getInstance();
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;

    // Views
    private EditText mOriginEditText;
    private EditText mDestinationEditText;
    private EditText mDateEditText;
    private EditText mTimeEditText;
    private EditText mCapacityEditText;
    private EditText mPriceEditText;
    private Button mCreateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOriginEditText = (EditText) findViewById(R.id.origin_edit_text);
        mDestinationEditText = (EditText) findViewById(R.id.dest_edit_text);
        mDateEditText = (EditText) findViewById(R.id.date_edit_text);
        mTimeEditText = (EditText) findViewById(R.id.time_edit_text);
        mCapacityEditText = (EditText) findViewById(R.id.capacity_edit_text);
        mPriceEditText = (EditText) findViewById(R.id.price_edit_text);

        mCreateButton = (Button) findViewById(R.id.create_button);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ride ride = new Ride();
                boolean successful = true;
                try {
                    ride.setOrigin(mOriginEditText.getText().toString());
                    ride.setDestination(mDestinationEditText.getText().toString());
                    ride.setDepartureDate(mDateEditText.getText().toString());
                    ride.setDepartureTime(mTimeEditText.getText().toString());
                    ride.setCapacity(Integer.parseInt(mCapacityEditText.getText().toString()));
                    ride.setPrice(Integer.parseInt(mPriceEditText.getText().toString()));
                } catch (Exception e) {
                    successful = false;
                    Toast.makeText(CreateRideActivity.this, R.string.create_ride_missing_information, Toast.LENGTH_SHORT).show();
                }

                if (successful) {
                    new CreateRideJob(ride).execute();
                    finish();
                }
            }
        });

        /* Date Picker */
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                updateDateLabel();
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
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                updateTimeLabel(hour, minute);
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

    }

    private void updateDateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDateEditText.setText(sdf.format(mCalendar.getTime()));
    }

    private void updateTimeLabel(int hours, int minutes) {
        mTimeEditText.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private JSONObject createJSONRide(Ride ride) {
        JSONObject object = new JSONObject();
        try {
            object.put("origin", ride.getOrigin());
            object.put("destination", ride.getDestination());
            object.put("departureDate", ride.getDepartureDate());
            object.put("departureTime", ride.getDepartureTime());
            object.put("capacity", Integer.toString(ride.getCapacity()));
            object.put("price", Integer.toString(ride.getPrice()));
            JSONArray dropOffArray = new JSONArray();
            for (String dropOff : ride.getDropOffs()) {
                dropOffArray.put(dropOff);
            }
            object.put("dropOffs", dropOffArray);
        } catch (Exception e) {
            Log.e("createJSONRide", "Error Creating Ride", e);
        }
        return object;
    }

    private class CreateRideJob extends AsyncTask<Void, Void, Boolean> {

        Ride mRide;

        public CreateRideJob(Ride ride) {
            mRide = ride;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject myRide = createJSONRide(mRide);
            try {
                Response response = WheelzoHttpApi.createRide(myRide);
                if (!response.isSuccessful()) {
                    Log.e(TAG, response.toString());
                }
                return response.isSuccessful();
            } catch (IOException e) {
                return false;
            }
        }
    }


}
