package com.example.stopwatch_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_UPDATE_TIME = "updateTime";
    private static final String KEY_TIME_SWAP_BUFF = "timeSwapBuff";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_IS_RUNNING = "isRunning";
    private static final String KEY_LAP_COUNT = "lapCount";
    private static final String KEY_LAPS = "laps";

    private TextView tvTimer;
    private Button btnStartResume, btnPause, btnLap, btnReset;
    private LinearLayout containerLaps;

    private Handler handler;
    private long startTime, timeInMilliseconds, timeSwapBuff, updateTime = 0L;
    private int lapCount = 0;
    private boolean isRunning = false;
    private ArrayList<String> lapList = new ArrayList<>();

    private final Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            updateTimerText(updateTime);
            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTimer = findViewById(R.id.tvTimer);
        btnStartResume = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnLap = findViewById(R.id.btnLap);
        btnReset = findViewById(R.id.btnReset);
        containerLaps = findViewById(R.id.containerLaps);

        handler = new Handler(Looper.getMainLooper());

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

        btnStartResume.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimerThread, 0);
            isRunning = true;
            updateUI();
        });

        btnPause.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimerThread);
            isRunning = false;
            updateUI();
        });

        btnReset.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            handler.removeCallbacks(updateTimerThread);
            startTime = 0L;
            timeInMilliseconds = 0L;
            timeSwapBuff = 0L;
            updateTime = 0L;
            lapCount = 0;
            isRunning = false;
            lapList.clear();
            tvTimer.setText(R.string.default_time);
            containerLaps.removeAllViews();
            updateUI();
        });

        btnLap.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            lapCount++;
            String lapText = getString(R.string.lap_item_format, lapCount, tvTimer.getText().toString());
            lapList.add(0, lapText);
            addLapToView(lapText);
        });

        updateUI();
    }

    private void restoreState(Bundle savedInstanceState) {
        updateTime = savedInstanceState.getLong(KEY_UPDATE_TIME);
        timeSwapBuff = savedInstanceState.getLong(KEY_TIME_SWAP_BUFF);
        startTime = savedInstanceState.getLong(KEY_START_TIME);
        isRunning = savedInstanceState.getBoolean(KEY_IS_RUNNING);
        lapCount = savedInstanceState.getInt(KEY_LAP_COUNT);
        lapList = savedInstanceState.getStringArrayList(KEY_LAPS);

        if (isRunning) {
            startTime = SystemClock.uptimeMillis() - (updateTime - timeSwapBuff);
            handler.postDelayed(updateTimerThread, 0);
        }
        updateTimerText(updateTime);
        if (lapList != null) {
            for (String lapText : lapList) addLapToView(lapText);
        }
    }

    private void addLapToView(String text) {
        TextView lapView = new TextView(this);
        lapView.setText(text);
        lapView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        lapView.setTextColor(ContextCompat.getColor(this, R.color.custom_green));
        lapView.setPadding(0, 16, 0, 16);
        containerLaps.addView(lapView, 0);
    }

    private void updateTimerText(long timeMs) {
        int secs = (int) (timeMs / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (timeMs % 1000);
        tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d:%03d", mins, secs, milliseconds));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_UPDATE_TIME, updateTime);
        outState.putLong(KEY_TIME_SWAP_BUFF, timeSwapBuff);
        outState.putLong(KEY_START_TIME, startTime);
        outState.putBoolean(KEY_IS_RUNNING, isRunning);
        outState.putInt(KEY_LAP_COUNT, lapCount);
        outState.putStringArrayList(KEY_LAPS, lapList);
    }

    private void updateUI() {
        btnStartResume.setEnabled(!isRunning);
        btnStartResume.setText(updateTime > 0 ? R.string.resume : R.string.start);
        btnPause.setEnabled(isRunning);
        btnLap.setEnabled(isRunning);
        btnReset.setEnabled(!isRunning);
    }
}
