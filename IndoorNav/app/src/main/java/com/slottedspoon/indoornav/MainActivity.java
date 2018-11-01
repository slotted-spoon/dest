package com.slottedspoon.indoornav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    public static final String NO_STAIRS = "com.slottedspoon.indoornav.NO_STAIRS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
    }

    public void startNavigation(View view) {
        Intent intent = new Intent(this, NavigationActivity.class);
        Switch noStairs = (Switch) findViewById(R.id.no_stairs);
        Boolean avoid_stairs = noStairs.isChecked();
        intent.putExtra(NO_STAIRS, avoid_stairs);
        startActivity(intent);
    }
}
