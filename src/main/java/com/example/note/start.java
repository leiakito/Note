package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.util.Date;

import util.SQLite;

public class start extends AppCompatActivity {
    public ImageView welcome = null;
    public SQLite sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //测试插入数据
//        insertText();
        //获取权限
        request();
        //设置动画效果
        APlhaAnimation();
    }

    public void request() {
        welcome = findViewById(R.id.login);
    }

    public void APlhaAnimation() {
        AlphaAnimation anima = new AlphaAnimation(0.2f, 1.0f);
        anima.setDuration(3000);
        welcome.startAnimation(anima);
        anima.setAnimationListener(new AnimationImp1());
    }

    public void insertText() {
        sqlite = new SQLite(getBaseContext());
        SQLiteDatabase db;

        Date date = new Date(System.currentTimeMillis());
        String time = String.valueOf(date);
        Log.i("msg", time);

        db = sqlite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", "这是第二条数据");
        values.put("writeTime", time);
        long code = db.insert("message", null, values);
    }

    public class AnimationImp1 implements Animation.AnimationListener {


        @Override
        public void onAnimationStart(Animation animation) {
            welcome.setBackgroundResource(R.mipmap.start);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent = new Intent(start.this, Listview.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

}
