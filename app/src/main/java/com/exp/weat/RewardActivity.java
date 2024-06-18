package com.exp.weat;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.graphics.drawable.DrawableCompat;

import java.util.Random;

public class RewardActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView reView;
    private Button reButton;
    private int flag = 0;
    private int MAX_EXP = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        // 使用findViewById并将返回的View对象赋值给相应的变量
        reView = (TextView) findViewById(R.id.rewardView); // 假设TextView的ID是rewardView
        reButton = (Button) findViewById(R.id.reward_button); // 假设Button的ID是reward_button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 为按钮设置点击监听器
        Drawable upArrow = toolbar.getNavigationIcon();
        if (upArrow != null) {
            DrawableCompat.setTint(upArrow, Color.WHITE); // 设置颜色为白色
        }
        reButton.setOnClickListener(this);


    }
    @Override
    public void onClick(View v){
        if (v.getId() == R.id.reward_button&&flag ==0){
            flag = 1;
            // 创建一个AlphaAnimation对象，从0（完全透明）到1（完全不透明）
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            // 设置动画持续时间
            alphaAnimation.setDuration(2000); // 2秒
            // 设置为填充模式，以保持动画结束后的状态
            alphaAnimation.setFillAfter(true);
            Log.d("RewardActivity", "Button clicked, starting animation.");

            // 开始动画


            SharedPreferences sharedPreferences = getSharedPreferences("exp_file", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int exp_s = sharedPreferences.getInt("exp", 0);
            MAX_EXP = exp_s + 5;
            int n = getExp();
            String s = "经验+ " + String.valueOf(n);
            reView.setText(s);
            exp_s = exp_s + n;
            Log.d("commit", String.valueOf(exp_s));
            editor.putInt("exp", exp_s);
            boolean x = editor.commit();
            Log.d("commit", String.valueOf(x));
            reView.startAnimation(alphaAnimation);
        } else if (v.getId() == R.id.reward_button) {
            Toast.makeText(RewardActivity.this, "已经领取过奖励了", Toast.LENGTH_SHORT).show();

        }
    }
    protected int getExp(){
        int n = 1;
        Random rand = new Random(); // 创建一个Random对象
        // 生成一个MIN_EXP（包含）到MAX_EXP（不包含）之间的随机整数
        n = rand.nextInt(MAX_EXP/4 - 1 + 1) + 1;
        if(n > 20){
            n = rand.nextInt(MAX_EXP/4 - 1 + 1) + 21;
        }
        if(n > 50){
            n = rand.nextInt(10) + 50;
        }
        return n;
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