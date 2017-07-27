package com.imliujun.calendar;

import java.util.Locale;

/**
 * 项目名称：Calendar
 * 类描述：
 * 创建人：liujun
 * 创建时间：2017/7/26 18:01
 * 修改人：liujun
 * 修改时间：2017/7/26 18:01
 * 修改备注：
 */
public class CalendarBean implements IncomeCalendarView.DotBean {

    public int day;
    public int color;
    public double money;


    @Override
    public boolean isShowDot() {
        return color != 0;
    }


    @Override
    public int getColor() {
        return color;
    }


    @Override
    public int getDay() {
        return day;
    }


    @Override
    public String getMoney() {
        return String.format(Locale.getDefault(), "%.5f元", money);
    }
}
