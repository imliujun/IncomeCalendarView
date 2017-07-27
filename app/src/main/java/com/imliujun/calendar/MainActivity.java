package com.imliujun.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    IncomeCalendarView mIncomeCalendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIncomeCalendarView = (IncomeCalendarView) findViewById(R.id.calendar);
        final List<CalendarBean> list = new ArrayList<>(31);
        Random random = new Random();
        for (int i = 1; i < 32; i++) {
            CalendarBean bean = new CalendarBean();
            bean.day = i;
            if (i % 7 == 0) {
                bean.color = Color.parseColor("#3ddabf");
            } else if (i % 3 == 0) {
                bean.color = Color.parseColor("#ff7979");
            }
            bean.money = random.nextDouble();
            list.add(bean);
        }
        mIncomeCalendarView.setData(list);

        mIncomeCalendarView.setOnClickListener(new IncomeCalendarView.OnClickListener() {
            @Override
            public void onLeftRowClick() {
                Toast.makeText(MainActivity.this, "点击减箭头", Toast.LENGTH_SHORT).show();
                mIncomeCalendarView.monthChange(-1);
                mIncomeCalendarView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIncomeCalendarView.setData(list);
                    }
                }, 1000);
            }


            @Override
            public void onRightRowClick() {
                Toast.makeText(MainActivity.this, "点击加箭头", Toast.LENGTH_SHORT).show();
                mIncomeCalendarView.monthChange(1);
                mIncomeCalendarView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIncomeCalendarView.setData(list);
                    }
                }, 1000);
            }


            @Override
            public void onTitleClick(String monthStr, Date month) {
                Toast.makeText(MainActivity.this, "点击了标题：" + monthStr, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onWeekClick(int weekIndex, String weekStr) {
                Toast.makeText(MainActivity.this, "点击了星期：" + weekStr, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onDayClick(int day, String dayStr, IncomeCalendarView.DotBean finish) {
                Toast.makeText(MainActivity.this, "点击了日期：" + dayStr, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
