package com.exp.weat;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Handler handler = new Handler();
    private Runnable runnable;
    boolean isRunning;
    private long totalTimeMillis = 0L; // 初始化为0，等待用户输入
    private int allTime = 0;
    private TextView text;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView expView;
    private Button expButton;
    private boolean isPhonePickUp = false;
    Button button;
    private int exp = 0;
    private int lv = 0;
    private int expOverLv = 0;
    private int needToUp = 0;
    private int count = 0;
    private int totalTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRunning = false;
        final TextView textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        Button setTime = findViewById(R.id.setTime);
        //Button test = findViewById(R.id.testbutton);
        //expButton = findViewById(R.id.exp_button);
        expView = findViewById(R.id.exp_View);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        updateExp();
        updateSum(false);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    Toast.makeText(MainActivity.this, "专注运行中，请先暂停后使用。", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, TimePickerActivity.class);
                    startActivityForResult(intent, 11);

                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    stopCountdown();
                    button.setText("继续专注");
                } else {
                    if(totalTimeMillis!= 0) {
                        startCountdown();
                        button.setText("暂停专注");
                    }
                    else{
                        Toast.makeText(MainActivity.this, "请先设置专注时间！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                            Intent intent = new Intent(MainActivity.this, RewardActivity.class);
//                            startActivity(intent);
//                    }
//                },500);
//            }
//        });
//        expButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateExp();
//            }
//        });
        updateTextView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateExp();
        if(accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        if(isRunning){
            stopCountdown();
            button.setText("继续专注");
            Intent intent = new Intent(MainActivity.this, WarningActivity.class);
            startActivity(intent);
        }
        sensorManager.unregisterListener(this);
    }
    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        if(isRunning){
            stopCountdown();
            button.setText("继续专注");
            Intent intent = new Intent(MainActivity.this, WarningActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        float yaccel = event.values[1];
        if(yaccel<6.0&&yaccel>3.0){
            if(isRunning){
                Toast.makeText(MainActivity.this, "手机抬起!", Toast.LENGTH_SHORT).show();
                stopCountdown();
                isPhonePickUp = true;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if ( isPhonePickUp) { // 假设isVisible是一个检查Activity是否可见的方法
                            Intent intent = new Intent(MainActivity.this, WarningActivity.class);
                            startActivity(intent);
                            button.setText("继续专注");
                        }
                        isPhonePickUp = false;
                    }
                },500);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private void startCountdown() {
        isRunning = true;
        Toast.makeText(MainActivity.this, "开始!", Toast.LENGTH_SHORT).show();
        runnable = new Runnable() {
            @Override
            public void run() {
                totalTimeMillis -= 1000; // 每秒更新一次

                updateTextView();

                if (totalTimeMillis <= 0) {
                    stopCountdown();
                    updateTextView();
                    Toast.makeText(MainActivity.this, "时间到!", Toast.LENGTH_SHORT).show();
                    updateSum(true);
                    Intent intent = new Intent(MainActivity.this, RewardActivity.class);
                    startActivity(intent);
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void stopCountdown() {
        isRunning = false;
        updateTextView();
        handler.removeCallbacks(runnable);
    }

    private void updateTextView() {
        if (totalTimeMillis + 1 > 0) {
            int hours = (int) (totalTimeMillis / (1000 * 60 * 60));
            int minutes = (int) ((totalTimeMillis % (1000 * 60 * 60)) / (1000 * 60));
            int seconds = (int) ((totalTimeMillis % (1000 * 60)) / 1000);
            String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            TextView textView = findViewById(R.id.textView);
            textView.setText(timeLeftFormatted);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            if (data != null) {
                int hour = data.getIntExtra("hour", 0);
                int minute = data.getIntExtra("minute", 0);
                // 使用时间做进一步处理
                totalTimeMillis = (long) hour * 60 * 60 * 1000 + (long) minute * 60*1000;
                allTime = (int) (totalTimeMillis/60)/1000;
                updateTextView();

                button.setText("开始专注");
                Toast.makeText(MainActivity.this, "时间设置成功！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected void updateExp(){
        // 获取SharedPreferences对象（使用默认模式）
        SharedPreferences sharedPreferences = getSharedPreferences("exp_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        int exp_s = sharedPreferences.getInt("exp", 0);
        Log.i("exp", String.valueOf(exp_s));
        if(exp_s == 0){
            editor.putInt("exp", exp);
            editor.apply();
        }
        exp = exp_s;
        calculateLv();
        String s = String.format("Lv%d exp %d/%d",lv,expOverLv,needToUp);
        Log.i("exp", "This is an info log message: " + s);
        Log.i("exp", String.valueOf(exp));
        expView.setText(s);
    }
    private void calculateLv(){
        for(int i =0;exp >= i*i ;i++ ){
            lv = i;
            expOverLv = exp - i*i;
            needToUp = (i+1)*(i+1) - i*i;
        }
    }
    private void updateSum(boolean flag){
        SharedPreferences sharedPreferences = getSharedPreferences("exp_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int count_t = sharedPreferences.getInt("count", 0);
        if(count_t == 0){
            editor.putInt("count", 0);
            editor.apply();
        }
        int time_t = sharedPreferences.getInt("totalTime", 0);
        if(time_t == 0){
            editor.putInt("totalTime", 0);
            editor.apply();
        }
        if(flag){//添加此次
            count_t = count_t + 1;
            time_t = time_t + allTime;
            editor.putInt("count", count_t);
            editor.putInt("totalTime", time_t);
            editor.apply();
        }
        TextView sum = findViewById(R.id.sumView);
        String s  = String.format("完成专注 %d 次，共 %d分钟",count_t ,time_t);
        sum.setText(s);
    }
}