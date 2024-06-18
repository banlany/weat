package com.exp.weat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WarningActivity extends AppCompatActivity {
    TextView messageTextView;
    TextView fromTextView;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning); // 设置布局
        fromTextView = findViewById(R.id.tv_from);
        //视图元素
        messageTextView = findViewById(R.id.tv_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = toolbar.getNavigationIcon();
        if (upArrow != null) {
            DrawableCompat.setTint(upArrow, Color.WHITE); // 设置颜色为白色
        }

        // 发起网络请求
        fetchPoetry();
    }

    private void fetchPoetry() {
        String url = "https://v1.jinrishici.com/all.json";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 显示错误消息到TextView或其他UI元素
                updateTextView("网络请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String content = jsonObject.getString("content");
                        String from = "——"+ jsonObject.getString("author") +" 《" +jsonObject.getString("origin")+ "》";
                        // 显示诗句内容
                        updateTextView(content);
                        updateFromView(from);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // 显示JSON解析错误到TextView或其他UI元素
                        updateTextView("JSON解析错误");
                    }
                }
            }
        });
    }

    // 用于在主线程中更新TextView的方法
    private void updateTextView(final String text) {
        messageTextView.post(new Runnable() {
            @Override
            public void run() {
                messageTextView.setText(text);
            }
        });
    }
    private void updateFromView(final String text) {
        fromTextView.post(new Runnable() {
            @Override
            public void run() {
                fromTextView.setText(text);
            }
        });
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