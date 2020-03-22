package com.example.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.TooltipCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.tv.TvContentRating;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import util.SQLite;

public class MainActivity extends AppCompatActivity {
    public int enter_state = 0;
    public TextView Edit_info;
    public TextView TV_time;
    public SQLiteDatabase db;
    public SQLite sqLite;
    public String write_time;
    public String lastinfo;
    public int id;
    public String info;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将组件对象 数据对象 intent对象 new实例化
        RequestScoffed();
        //初始化ToolBar对象
        toolBar();
    }

    public void RequestScoffed() {
        sqLite = new SQLite(getBaseContext());
        Edit_info = findViewById(R.id.Edit_info);
        TV_time = findViewById(R.id.TV_time);
        Bundle bundle = this.getIntent().getExtras();
        enter_state = bundle.getInt("enter_state");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        write_time = String.valueOf(date);
        //当传过来的参数大于0的时候说明是编辑便签薄 小于则新建便签
        if (enter_state > 0) {
            lastinfo = bundle.getString("info");//传过来尚未修改的info文本内容
            Edit_info.setText(lastinfo);
            TV_time.setText(write_time);
            Log.i("msg", lastinfo + "此数据为MainActivity.java中 RequestScoffed方法中 enter_state>0 ");
        } else {
            TV_time.setText(write_time);
        }
        info = Edit_info.getText().toString().trim();
    }

    //初始化ToolBar对象
    public void toolBar() {
        final Toolbar toolbar = findViewById(R.id.toobal_normal);
        toolbar.inflateMenu(R.menu.menu);
        final Intent intent = new Intent(MainActivity.this, Listview.class);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_done:
                        if (enter_state > 0) {
                            Bundle bundle = MainActivity.this.getIntent().getExtras();
                            id = bundle.getInt("id");
                            db = sqLite.getWritableDatabase();
                            String info = Edit_info.getText().toString().trim();//获得已经修改过的info文本内容
                            ContentValues values = new ContentValues();
                            values.put("title", info);
                            values.put("writeTime", write_time);
                            int code = db.update("message", values, "id =?", new String[]{id + ""});
                            Log.i("msg", "修改" + code);
                            db.close();
                            lastinfo = null;
                            startActivity(intent);
                            finish();
                        }
                        if (enter_state == 0) {
                            db = sqLite.getWritableDatabase();
                            String info = Edit_info.getText().toString().trim();
                            ContentValues values = new ContentValues();
                            values.put("title", info);
                            values.put("writeTime", write_time);
                            long code = db.insert("message", null, values);
                            db.close();
                            Log.i("msg", "创建" + code);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case R.id.menu_clear:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("简简单单普普通通的对话框");
                        builder.setMessage("确认放弃此次更改的内容");
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setNegativeButton("cancel", null);
                        builder.setPositiveButton("done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        builder.create().show();
                        break;
                }
                return true;
            }
        });
    }
}
