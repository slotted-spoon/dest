package com.slottedspoon.indoornav;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.viewpagerindicator.CirclePageIndicator;

import static android.view.KeyEvent.KEYCODE_BUTTON_L1;
import static android.view.KeyEvent.KEYCODE_BUTTON_L2;
import static android.view.KeyEvent.KEYCODE_BUTTON_R2;

public class NavigationActivity extends AppCompatActivity {
    private Integer stairsRoute[] = {R.drawable.exit_room, R.drawable.turn_left_from_studio, R.drawable.up_stairs, R.drawable.on_stairs,
            R.drawable.turn_around_after_stairs, R.drawable.continue_after_stairs, R.drawable.turn_around_at_room, R.drawable.arrive_after_stairs};
    private Integer elevatorRoute[] = {R.drawable.exit_room, R.drawable.elevator, R.drawable.in_elevator, R.drawable.left_from_elevator,
            R.drawable.continue_down_4th_floor, R.drawable.arrive_from_elevator};
    LayoutInflater inflater;    //Used to create individual pages
    ViewPager vp;               //Reference to class to swipe views
    CirclePageIndicator mIndicator;
    private String destName;
    String[] directions;
    Integer[] images;
    private SlidingUpPanelLayout mLayout;
    private Vibrator vibrator;
    private boolean shouldVibrate;
    private int currStep = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Intent intent = getIntent();
        destName = intent.getExtras().getString(EnterDestActivity.DESTINATION);
        boolean takeElevator = intent.getExtras().getBoolean(OldRouteOptionsActivity.ELEVATOR);
        shouldVibrate = intent.getExtras().getBoolean(OldRouteOptionsActivity.VIBRATE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(takeElevator) {
            images = elevatorRoute;
            directions = getResources().getStringArray(R.array.elevRoute);
        } else {
            images = stairsRoute;
            directions = getResources().getStringArray(R.array.stairsRoute);
        }

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Reference ViewPager defined in activity
        vp=(ViewPager)findViewById(R.id.viewPager);
        //set the adapter that will create the individual pages
        vp.setAdapter(new MyPagesAdapter());
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(vp);

        ListView lv = (ListView) findViewById(R.id.list);

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                directions );

        lv.setAdapter(arrayAdapter);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {}
            @Override
            public void onPanelCollapsed(View panel) {}
            @Override
            public void onPanelExpanded(View panel) {}
            @Override
            public void onPanelAnchored(View panel) {}
            @Override
            public void onPanelHidden(View panel) {}
        });
        mLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vp.setCurrentItem(position, true);
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });

        TextView t = (TextView) findViewById(R.id.dest);
        t.setText(destName);
        Button f = (Button) findViewById(R.id.end);
        f.setText("End Navigation");
        f.setMovementMethod(LinkMovementMethod.getInstance());
        f.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent) {
        int keyaction = KEvent.getAction();
        System.out.println(KEvent.getKeyCode());

        if (keyaction == KeyEvent.ACTION_UP) {
            int keycode = KEvent.getKeyCode();
            switch (keycode) {
                case KEYCODE_BUTTON_L1:
                    System.out.println("Heartbeat");
                    if(shouldVibrate) {
                        vibrator.cancel();
                        vibrator.vibrate(createHeartbeat());
                    }
                    break;
                case KEYCODE_BUTTON_L2:
                    System.out.println("Anger");
                    vibrator.cancel();
                    vibrator.vibrate(angryBuzz());
                    break;
                case KEYCODE_BUTTON_R2:
                    System.out.println("Change");
                    vibrator.cancel();
                    vibrator.vibrate(notificationBuzz());
                    rotateStep(1);
                    break;
            }
        }
        return super.dispatchKeyEvent(KEvent);
    }
    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent MEvent) {
        float motionAction = MEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
        if(Math.abs(motionAction) > .3) {
            if (motionAction > 0) {
                rotateStep(1);
            } else {
                rotateStep(-1);
            }
        }
        return super.dispatchGenericMotionEvent(MEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private VibrationEffect notificationBuzz() {
        return VibrationEffect.createOneShot(200,200);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private VibrationEffect angryBuzz() {
        return VibrationEffect.createOneShot(800,255);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private VibrationEffect createHeartbeat() {
        long[] vibratePattern = {0, 250, 100, 250, 1000, 250, 100, 250, 1000};
        if(vibrator.hasAmplitudeControl()) {
            int[] ampPattern = {0, 50, 0, 50, 0, 50, 0, 50, 0};
            return VibrationEffect.createWaveform(vibratePattern, ampPattern, 0);
        } else {
            return VibrationEffect.createWaveform(vibratePattern, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottomnav, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mLayout != null) {
            if (mLayout.getPanelState() == PanelState.HIDDEN) {
                item.setTitle("Show");
            } else {
                item.setTitle("Hide");
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.HIDDEN) {
                        mLayout.setPanelState(PanelState.HIDDEN);
                        item.setTitle("Show");
                    } else {
                        mLayout.setPanelState(PanelState.COLLAPSED);
                        item.setTitle("Hide");
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.7f);
                        mLayout.setPanelState(PanelState.ANCHORED);
                        item.setTitle("Disable Anchor");
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(PanelState.COLLAPSED);
                        item.setTitle("Enable Anchor");
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private void rotateStep(int direction) {
        currStep = currStep+direction;
        if (currStep == images.length) {
            goToArrived();
        } else if (currStep < 0) {
            currStep = 0;
        }
        vp.setCurrentItem(currStep, true);
    }

    private void goToArrived() {
        Intent arrived = new Intent(NavigationActivity.this, ArrivedActivity.class);
        startActivity(arrived);
    }

    //Implement PagerAdapter Class to handle individual page creation
    class MyPagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return directions.length;
        }
        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = inflater.inflate(R.layout.nav_step, null);
            ((TextView)page.findViewById(R.id.textMessage)).setText(directions[position]);
            ((ImageView)page.findViewById(R.id.image)).setImageResource(images[position]);
            //Add the page to the front of the queue
            ((ViewPager) container).addView(page, 0);
            return page;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0==(View)arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object=null;
        }
    }
}