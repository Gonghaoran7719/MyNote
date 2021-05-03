package com.example.apple.jishiben;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText subject;
    private EditText body;
    private EditText date;
    private Button chooseDate;
    private Button add;
    private Button query;
    private ListView result;
    private LinearLayout title;
    private MyDataBaseHelper mMyDataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //控件初始化
        subject=(EditText)findViewById(R.id.subject);
        body=(EditText)findViewById(R.id.body);
        date=(EditText)findViewById(R.id.date);
        chooseDate=(Button)findViewById(R.id.chooseDate);
        add=(Button)findViewById(R.id.add);
        query=(Button)findViewById(R.id.query);
        result=(ListView)findViewById(R.id.result);
        title=(LinearLayout)findViewById(R.id.title);

        //选择日期，添加，查询的监听事件
        chooseDate.setOnClickListener(this);
        add.setOnClickListener(this);
        query.setOnClickListener(this);
        //查询情况默认隐藏，只有点击查询时才有效果
        title.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        //创建数据库
        mMyDataBaseHelper=new MyDataBaseHelper(MainActivity.this,"memento.db",null,1);
        SQLiteDatabase sqLiteDatabase=mMyDataBaseHelper.getReadableDatabase();
        //获取输入框的内容
        String strSubject=subject.getText().toString().trim();
        String strBody=body.getText().toString().trim();
        String strDate=date.getText().toString().trim();

        switch (v.getId()){
            case R.id.chooseDate:
                Calendar calendar=Calendar.getInstance();
                //选择时间，并将时间设置在date中
                new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(year+"-"+month+"-"+dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.add:
                //添加内容到数据库中，添加方法是add
                title.setVisibility(View.INVISIBLE);
                add(sqLiteDatabase,strSubject,strBody,strDate);
                Toast.makeText(this, "success!", Toast.LENGTH_SHORT).show();
                result.setAdapter(null);
                break;
            case R.id.query:
                //查询时显示结果设置为visible
                title.setVisibility(View.VISIBLE);
                //查询的结果保存在cursor中
                Cursor cursor=query(sqLiteDatabase,strSubject,strBody,strDate);
                SimpleCursorAdapter simpleCursorAdapter=new SimpleCursorAdapter(MainActivity.this,R.layout.result,cursor,new String[]{"_id", "subject", "body", "date"},
                        new int[]{R.id.memento_num, R.id.memento_subject, R.id.memento_body, R.id.memento_date});
                //为listview添加适配器
                result.setAdapter(simpleCursorAdapter);
                break;
        }
    }

    //添加数据到数据库中的方法
    public void add(SQLiteDatabase sqLiteDatabase,String subject,String body,String date){
        sqLiteDatabase.execSQL("Insert into memento_tb values(null,?,?,?)",new String[]{subject,body,date});
        //初始设置为空
        this.subject.setText("");
        this.body.setText("");
        this.date.setText("");
    }

    //查询的方法，返回值为cursor
    public Cursor query(SQLiteDatabase sqLiteDatabase, String subject, String body, String date){
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select * from memento_tb where subject like ? and body like ? and date like ? ",
                new String[]{"%" + subject + "%", "%" + body + "%",
                        "%" + date + "%"});//对表的查询（insert的操作）
        return cursor;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mMyDataBaseHelper != null) {
            mMyDataBaseHelper.close();
        }
    }
}
