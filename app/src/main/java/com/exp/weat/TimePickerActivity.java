package com.exp.weat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.graphics.drawable.DrawableCompat;

// 该页面类实现了接口OnTimeSetListener，意味着要重写时间监听器的onTimeSet方法
public class TimePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private TimePicker tp_time;
    private Button btn_confirm;
    private TextView tv_time;
    private Button bm1;
    private Button bm10;
    private Button bm30;
    private Button bm45;
    private Button bh1;
    private Button bh3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        tp_time = findViewById(R.id.tp_time);
        btn_confirm = findViewById(R.id.btn_confirm);
        bm1 = findViewById(R.id.m1);
        bm10 = findViewById(R.id.m10);
        bm30 = findViewById(R.id.m30);
        bm45 = findViewById(R.id.m45);
        bh1 = findViewById(R.id.h1);
        bh3 = findViewById(R.id.h3);
        tv_time = findViewById(R.id.tv_time);
        tp_time.setIs24HourView(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = toolbar.getNavigationIcon();
        if (upArrow != null) {
            DrawableCompat.setTint(upArrow, Color.WHITE); // 设置颜色为白色
        }

        // 为TimePicker设置一个OnTimeChangedListener来监听时间的变化
        tp_time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // 这里只是打印时间变化，您可以在这里添加其他逻辑
                Log.d("TimePicker", "Time changed: " + hourOfDay + ":" + minute);
            }
        });

        btn_confirm.setOnClickListener(this);
        bm1.setOnClickListener(this);
        bm10.setOnClickListener(this);
        bm30.setOnClickListener(this);
        bm45.setOnClickListener(this);
        bh1.setOnClickListener(this);
        bh3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            // 获取用户选择的时间并显示在TextView上
            int hour = tp_time.getHour();
            int minute = tp_time.getMinute();
            // 将时间封装到Intent中
            Intent intent = new Intent();
            intent.putExtra("hour", hour);
            intent.putExtra("minute", minute);
            setResult(RESULT_OK, intent);
            finish();
        }
        if (v.getId() == R.id.m1) {
           tp_time.setHour(0);
           tp_time.setMinute(1);
        }
        if (v.getId() == R.id.m10) {
            tp_time.setHour(0);
            tp_time.setMinute(10);
        }
        if (v.getId() == R.id.m30) {
            tp_time.setHour(0);
            tp_time.setMinute(30);
        }
        if (v.getId() == R.id.m45) {
            tp_time.setHour(0);
            tp_time.setMinute(45);
        }
        if (v.getId() == R.id.h1) {
            tp_time.setHour(1);
            tp_time.setMinute(0);
        }
        if (v.getId() == R.id.h3) {
            tp_time.setHour(3);
            tp_time.setMinute(0);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}