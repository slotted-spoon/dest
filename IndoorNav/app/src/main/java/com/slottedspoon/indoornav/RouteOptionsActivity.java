package com.slottedspoon.indoornav;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class RouteOptionsActivity extends AppCompatActivity {

    private TextView mTextMessage;
    String destName;
    public static final String ELEVATOR = "com.slottedspoon.indoornav.ELEVATOR";
    public static final String VIBRATE = "com.slottedspoon.indoornav.VIBRATE";
    boolean takeElevator = false;
    private GestureDetectorCompat gestureDetectorCompat;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_stairs:
                    changeCenterIcon(R.drawable.stairs_icon);
                    changeEstimates(R.string.stairs_dist_est, R.string.stairs_time_est);
                    takeElevator = false;
                    break;
                case R.id.navigation_elev:
                    changeCenterIcon(R.drawable.elevator_icon);
                    changeEstimates(R.string.elev_distance_est, R.string.elev_time_est);
                    takeElevator = true;
                    break;
                case R.id.navigation_go:
                    Intent go = new Intent(RouteOptionsActivity.this, NavigationActivity.class);
                    Switch vibrateSwitch = findViewById(R.id.should_vibrate);
                    boolean shouldVibrate = vibrateSwitch.isChecked();
                    go.putExtra(ELEVATOR, takeElevator);
                    go.putExtra(VIBRATE, shouldVibrate);
                    startActivity(go);
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_options);

        Intent i = getIntent();
        destName = i.getExtras().getString(EnterDestActivity.DESTINATION);
        setDestination();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        gestureDetectorCompat = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {}

            @Override
            public boolean onSingleTapUp(MotionEvent e) {return false;}

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e2.getX() > e1.getX()) {
                    finish();
                }
                return true;
            }
        });
    }

    private void setDestination() {
        final TextView dest = findViewById(R.id.dest);
        dest.setText(destName);
    }

    private void changeCenterIcon(Integer icon) {
        final ImageView iconView = findViewById(R.id.ascend);
        iconView.setImageResource(icon);
    }

    private void changeEstimates(Integer distEst, Integer timeEst) {
        final TextView distEstView = findViewById(R.id.dist_est);
        final TextView timeEstView = findViewById(R.id.time_est);
        distEstView.setText(distEst);
        timeEstView.setText(timeEst);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}