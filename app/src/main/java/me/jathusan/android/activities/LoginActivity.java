package me.jathusan.android.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import me.jathusan.android.R;

public class LoginActivity extends FragmentActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
