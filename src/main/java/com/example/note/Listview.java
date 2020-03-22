package com.example.note;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.MessageInfo;
import util.SQLite;

public class Listview extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    //实现implement AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener 单击item对象 和长按item对象
    public SimpleAdapter simpleAdapter;
    public ListView listView;
    public List<Map<String, Object>> datalist;
    public TextView tv_content;
    public TextView tv_date;
    public SQLiteDatabase db;
    public SQLite sqLite;
    public Button addnote;
    public int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        //获取XML中的组件对象  new 数据集合 开启sqlitedatabase  SQLite对象 设置按键监听
        requestScoffect();
    }

    private void requestScoffect() {
        tv_content = findViewById(R.id.tv_content);
        tv_date = findViewById(R.id.tv_date);
        datalist = new ArrayList<Map<String, Object>>();
        addnote = findViewById(R.id.add_Bu);
        sqLite = new SQLite(this);
        db = sqLite.getWritableDatabase();
        listView = findViewById(R.id.list_item);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        //编辑新的便签
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到MainActivity中
                Intent intent = new Intent(Listview.this, MainActivity.class);
                Bundle bundle = new Bundle();
                //将数据存入Bundle 中
                bundle.putString("info", "");
                bundle.putInt("enter_state", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //刷新组件对象
        RefreshNoteLIst();
    }

    private void RefreshNoteLIst() {
        //获取集合的大小
        int size = datalist.size();
        //当集合大于0则清空集合 并且告诉适配器 要发生改变
        if (size > 0) {
            datalist.removeAll(datalist);
            simpleAdapter.notifyDataSetChanged();
        }
        //进行查询 查
        Cursor cursor = db.query("message", null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
//              int id =cursor.getInt(cursor.getColumnIndex("id"));
            //cursor.getString  cursor.getColumnINdex(表中的字段名称);
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String writeTime = cursor.getString(cursor.getColumnIndex("writeTime"));
            id = cursor.getInt(cursor.getColumnIndex("id"));
            //新建集合 将从表中得到的字段存入map集合中
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("tv_content", title);
            map.put("tv_date", writeTime);
            //将集合存入数组中
            datalist.add(map);
            Log.i("msg", datalist.toString() + "ListView.java");
        }
        //实例  适配器对象 参数为 context(本类this[] ,数组对象,实例的XML---也就是规范存入数据的xml,new String[]{"数组中的字段名
        // ,字段名2"},new int[]{xml中的分别存入数组中数据的组件对象 })
        simpleAdapter = new SimpleAdapter(this, datalist, R.layout.item,
                new String[]{"tv_content", "tv_date"}
                , new int[]{R.id.tv_content, R.id.tv_date});
        //将listview.setAdapter适配 simpleAdapter对象
        listView.setAdapter(simpleAdapter);
    }

    //重写onItemClick方法 点击后编辑
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //获取listviw中的item位置内容组件
        String content = listView.getItemAtPosition(i) + "";

        Log.i("msg", content + "我是content");
        //将其分割
        String content1 = content.substring(content.indexOf("=") + 1, content.indexOf("tv_date"));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("info", content1);
        bundle.putInt("enter_state", 1);
        bundle.putInt("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //长按删除
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long l) {
        //Builder提示框 AlerDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        listView = (ListView) adapterView;

        //将得到的listview位置的组件内容 存入集合中
        HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(i);
        //获取map中key名称为 tv_content
        final String content = map.get("tv_content");
        //设置builder提示框的title和message
        builder.setTitle("删除该日志");
        builder.setMessage("确认删除日志");
        //设置确定按钮setPositiveButton 实例 单击监听事件  DialogInterface.OnClickListener()
        builder.setPositiveButton("done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//            String content=listView.getItemAtPosition(i)+"";
//            String content1=content.substring(content.indexOf("=")+1,content.indexOf(","));
                Log.i("msg", "我即将删除" + content);
                //执行db删除操作 表名 message 参数条件为 title = ?  new String[]{content}
                db.delete("message", "title =?", new String[]{content});
                //刷新组件
                RefreshNoteLIst();
            }
        });
        //设置取消按键 setNegativeButton
        builder.setNegativeButton("cancel", null);
        //创建 create
        builder.create();
        //并显示
        builder.show();
        //此处返回true的意思为控制全屏幕 可理解为 弹出消息框,就不能去进行其他的操作
        return true;
    }
}
