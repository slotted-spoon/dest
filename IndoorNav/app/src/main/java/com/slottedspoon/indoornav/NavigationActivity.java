package com.slottedspoon.indoornav;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import static android.view.KeyEvent.KEYCODE_BUTTON_L1;
import static android.view.KeyEvent.KEYCODE_BUTTON_L2;
import static android.view.KeyEvent.KEYCODE_BUTTON_R2;

public class NavigationActivity extends AppCompatActivity {
    private Integer stairsRoute[] = {R.drawable.exit_room, R.drawable.turn_left_from_studio, R.drawable.up_stairs,
    R.drawable.turn_around_after_stairs, R.drawable.continue_after_stairs, R.drawable.turn_around_at_room, R.drawable.arrive_after_stairs};
    private Integer stairsDirections[] = {R.string.exitRoom, R.string.leftFromStudio, R.string.stairs,
    R.string.turnAroundStairs, R.string.continueAfterStairs, R.string.turnAroundRoom, R.string.arrive};
    private Integer elevatorRoute[] = {R.drawable.exit_room, R.drawable.elevator, R.drawable.in_elevator, R.drawable.left_from_elevator,
    R.drawable.continue_down_4th_floor, R.drawable.arrive_from_elevator};
    private Integer elevatorDirections[] = {R.string.exitRoom, R.string.elevator, R.string.inElevator, R.string.leftFromElevator,
    R.string.continueDown4thFloor, R.string.arrive};
    private Integer images[];
    private Integer directions[];
    private int currStep = 0;
    private Vibrator vibrator;
    private boolean shouldVibrate;
    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Intent intent = getIntent();
        Boolean takeElevator = intent.getExtras().getBoolean(RouteOptionsActivity.ELEVATOR);
        shouldVibrate = intent.getExtras().getBoolean(RouteOptionsActivity.VIBRATE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(takeElevator) {
            images = elevatorRoute;
            directions = elevatorDirections;
        } else {
            images = stairsRoute;
            directions = stairsDirections;
        }
        initializeImageSwitcher();
        initializeTextSwitcher();
        setInitialDirections();
        gestureDetectorCompat = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {return false;}

            @Override
            public void onShowPress(MotionEvent e) {}

            @Override
            public boolean onSingleTapUp(MotionEvent e) {return false;}

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return false;}

            @Override
            public void onLongPress(MotionEvent e) {}

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e2.getX() > e1.getX()) {
                    rotateStep(-1);
                } else if(e2.getX() < e1.getX()) {
                    rotateStep(1);
                }
                return true;
            }
        });
    }

    private void initializeTextSwitcher() {
        final TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        textSwitcher.setFactory(new TextSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView myText = new TextView(NavigationActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(24);
                return myText;
            }
        });

        textSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
    }

    private void initializeImageSwitcher() {
        final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(NavigationActivity.this);
            }
        });

        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
    }

    public void rotateStep(int step) {
        currStep = (currStep+step)%images.length;
        if (currStep == images.length) {
            currStep = 0;
        } else if (currStep < 0) {
            currStep = images.length-1;
        }
        setCurrentImage();
        setCurrentText();
    }

    public void finish(View arg0) {
        finish();
    }

    private void setInitialDirections() {
        setCurrentImage();
        setCurrentText();
    }

    private void setCurrentImage() {
        final ImageSwitcher imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setImageResource(images[currStep]);
    }

    private void setCurrentText() {
        final TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        textSwitcher.setText(getText(directions[currStep]));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
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
    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent) {
        int keyaction = KEvent.getAction();

        if (keyaction == KeyEvent.ACTION_DOWN) {
            int keycode = KEvent.getKeyCode();
            switch (keycode) {
                case KEYCODE_BUTTON_L1:
                    vibrator.cancel();
                    vibrator.vibrate(createHeartbeat());
                    break;
                case KEYCODE_BUTTON_L2:
                    vibrator.cancel();
                    vibrator.vibrate(angryBuzz());
                    break;
                case KEYCODE_BUTTON_R2:
                    if(shouldVibrate) {
                        vibrator.cancel();
                        vibrator.vibrate(notificationBuzz());
                    }
                    rotateStep(1);
                    break;
            }
        }
        return super.dispatchKeyEvent(KEvent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private VibrationEffect softBuzz() {
        return VibrationEffect.createOneShot(500,50);
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
}