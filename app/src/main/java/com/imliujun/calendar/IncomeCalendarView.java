package com.imliujun.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 项目名称：Calendar
 * 类描述：
 * 创建人：liujun
 * 创建时间：2017/7/25 18:38
 * 修改人：liujun
 * 修改时间：2017/7/25 18:38
 * 修改备注：
 */
public class IncomeCalendarView extends View {
    private static final String TAG = "CalendarView";

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private final String[] WEEK_STR = new String[] { "日", "一", "二", "三", "四", "五", "六" };

    @ColorInt private int mBgMonth = Color.TRANSPARENT;
    @ColorInt private int mBgWeek = Color.TRANSPARENT;
    @ColorInt private int mBgDay = Color.TRANSPARENT;
    @ColorInt private int mBgDot = Color.TRANSPARENT;

    @ColorInt private int mTextColorMonth = Color.BLACK;        //标题字体颜色
    @ColorInt private int mTextColorWeek = 0xa3a3a3;            //星期字体颜色
    @ColorInt private int mTextColorDay = Color.BLACK;          //日期字体颜色
    @ColorInt private int mSelectTextColor = Color.TRANSPARENT; //选中日期字体颜色
    @ColorInt private int mSelectBg = Color.TRANSPARENT;        //选中日期背景
    @ColorInt private int mCurrentTextColor = Color.RED;       //当天日期背景
    @ColorInt private int mPopupColor = Color.WHITE;       //当天日期背景

    private float mTextSizeMonth;             //标题字体大小
    private float mTextSizeWeek;              //星期字体大小
    private float mTextSizeDay;               //日期字体大小
    private float mTextSizePopup;               //弹窗字体大小

    private Bitmap mPopupBg;
    private Bitmap mMonthRowL;            //月份箭头
    private Bitmap mMonthRowR;            //月份箭头
    private float mMonthRowSpace;           //箭头间距
    private float mMonthTopSpace;           //箭头上间距

    private float mSelectRadius;            //选中日期背景半径
    private float mMonthUpSpace;              //标题月份下间隔
    private float mMonthDownSpace;              //标题月份下间隔
    private float mLineSpace;               //日期行间距
    private float mDotSpace;         //日期和小圆点上下间距
    private float mPopupSpace;         //弹窗下间距
    private float mDotRadius;         //小圆点半径

    private String mMonthFormat;        //title显示的月份格式

    private Paint mPaint;
    private Paint mBgPaint;
    private Paint mDotPaint;
    private Paint mBitmapPaint;
    private Paint mPopupPaint;

    private SimpleDateFormat mDateFormat;

    private Calendar mCalendar;     //当前月份的日历
    private boolean mIsCurrentMonth; //展示的月份是否是当前月
    private int mCurrentDay;        //当前日期
    private int mSelectDay;          //选中的日期
    private int mLastSelectDay;      //上一次选中的日期（避免造成重复回调请求）
    private int mDayOfMonth;         //月份天数
    private int mFirstIndex;         //当月第一天位置索引
    private int mLineNum;            //日期行数
    private int mFirstLineNum;       //第一行、最后一行能展示多少日期
    private int mLastLineNum;

    private float mTitleHeight;
    private float mWeekHeight;
    private float mDayHeight;
    private float mDotHeight;
    private float mOneHeight;

    private int mColumnWidth;       //每列宽度

    private boolean mDrawRoundRect;
    private boolean mIsShowRoundRect;

    private RectF mPopupRectF;      //弹窗的位置

    private OnClickListener mOnClickListener;

    /** 绘制月份 */
    private int mRowLStart, mRowRStart, mRowWidth;

    //焦点坐标
    private PointF mFocusPoint = new PointF();

    //控制事件是否响应
    private boolean mResponseWhenEnd = false;

    private SparseArray<DotBean> mData = new SparseArray<>(31);


    public IncomeCalendarView(Context context) {
        this(context, null);
    }


    public IncomeCalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.calendar_custom);
    }


    public IncomeCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context
                .obtainStyledAttributes(attrs, R.styleable.IncomeCalendarView, defStyleAttr, R.style.CalendarDefault);
        if (DEBUG) {
            mBgMonth = ta.getColor(R.styleable.IncomeCalendarView_mBgMonth, Color.RED);
            mBgWeek = ta.getColor(R.styleable.IncomeCalendarView_mBgWeek, Color.GRAY);
            mBgDay = ta.getColor(R.styleable.IncomeCalendarView_mBgDay, Color.BLUE);
            mBgDot = ta.getColor(R.styleable.IncomeCalendarView_mBgDot, Color.GREEN);
        }
        mTextColorMonth = ta.getColor(R.styleable.IncomeCalendarView_mTextColorMonth, Color.BLACK);
        mTextColorWeek = ta.getColor(R.styleable.IncomeCalendarView_mTextColorWeek, 0xa3a3a3);
        mTextColorDay = ta.getColor(R.styleable.IncomeCalendarView_mTextColorDay, Color.BLACK);
        mSelectTextColor = ta
                .getColor(R.styleable.IncomeCalendarView_mSelectTextColor, mTextColorDay);
        mSelectBg = ta.getColor(R.styleable.IncomeCalendarView_mSelectBg, Color.TRANSPARENT);
        mCurrentTextColor = ta
                .getColor(R.styleable.IncomeCalendarView_mCurrentTextColor, mTextColorDay);
        mPopupColor = ta.getColor(R.styleable.IncomeCalendarView_mPopupTextColor, mTextColorDay);

        mTextSizeMonth = ta.getDimension(R.styleable.IncomeCalendarView_mTextSizeMonth, 78);
        mTextSizeWeek = ta.getDimension(R.styleable.IncomeCalendarView_mTextSizeWeek, 36);
        mTextSizeDay = ta.getDimension(R.styleable.IncomeCalendarView_mTextSizeDay, 39);
        mTextSizePopup = ta.getDimension(R.styleable.IncomeCalendarView_mTextSizePopup, 39);

        mMonthRowL = BitmapFactory.decodeResource(getResources(), ta
                .getResourceId(R.styleable.IncomeCalendarView_mMonthRowL, R.drawable.arrow_left));
        mMonthRowR = BitmapFactory.decodeResource(getResources(), ta
                .getResourceId(R.styleable.IncomeCalendarView_mMonthRowR, R.drawable.arrow_right));
        mPopupBg = BitmapFactory.decodeResource(getResources(), ta
                .getResourceId(R.styleable.IncomeCalendarView_mPopupBg, R.drawable.popup_bg));

        mMonthRowSpace = ta.getDimension(R.styleable.IncomeCalendarView_mMonthRowSpace, 138);
        mMonthTopSpace = ta.getDimension(R.styleable.IncomeCalendarView_mMonthTopSpace, 90);

        mSelectRadius = ta.getDimension(R.styleable.IncomeCalendarView_mSelectRadius, 0);
        mMonthDownSpace = ta.getDimension(R.styleable.IncomeCalendarView_mMonthDownSpace, 10);
        mMonthUpSpace = ta.getDimension(R.styleable.IncomeCalendarView_mMonthUpSpace, 10);
        mLineSpace = ta.getDimension(R.styleable.IncomeCalendarView_mLineSpace, 10);
        mPopupSpace = ta.getDimension(R.styleable.IncomeCalendarView_mPopupSpace, 6);
        mDotSpace = ta.getDimension(R.styleable.IncomeCalendarView_mDotSpace, 12);
        mDotRadius = ta.getDimension(R.styleable.IncomeCalendarView_mDotRadius, 10);

        mMonthFormat = ta.getString(R.styleable.IncomeCalendarView_mMonthFormat);

        ta.recycle();

        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPopupPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPopupRectF = new RectF();
        mPopupPaint.setColor(mPopupColor);
        mPopupPaint.setTextSize(mTextSizePopup);

        mDotHeight = mDotRadius * 2;//小红点高度

        mPaint.setFakeBoldText(true);
        //标题高度
        mPaint.setTextSize(mTextSizeMonth);
        mTitleHeight = getFontHeight(mPaint) + mMonthUpSpace + mMonthDownSpace;
        //星期高度
        mPaint.setFakeBoldText(false);
        mPaint.setTextSize(mTextSizeWeek);
        mWeekHeight = getFontHeight(mPaint) + mDotHeight + mDotSpace;
        //日期高度
        mPaint.setTextSize(mTextSizeDay);
        mDayHeight = getFontHeight(mPaint);

        //每行高度 = 行间距 + 日期字体高度 + 小圆点间距 + 小圆点高度
        mOneHeight = mLineSpace + mDayHeight + mDotHeight + mDotSpace;

        setMonth(new Date());
    }


    /**
     * 设置月份
     */
    private void setMonth(Date month) {
        mCalendar = Calendar.getInstance();

        //获取当前年月日
        mCalendar.setTime(new Date());
        int currentYear = mCalendar.get(Calendar.YEAR);
        int currentMonth = mCalendar.get(Calendar.MONTH);
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        //设置的日历
        mCalendar.setTime(month);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        //判断是否为当月
        if (currentYear == mCalendar.get(Calendar.YEAR) &&
                currentMonth == mCalendar.get(Calendar.MONTH)) {
            mIsCurrentMonth = true;
            //mSelectDay = mCurrentDay;//当月默认选中当前日
        } else {
            mIsCurrentMonth = false;
            mSelectDay = 0;
            mIsShowRoundRect = false;
        }
        //Log.i(TAG, "设置月份：" + getMonthStr(month) + "   今天" + mCurrentDay + "号, 是否为当前月：" +
        //        mIsCurrentMonth);

        mDayOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //第一行1号显示在什么位置（星期几）
        mFirstIndex = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        mLineNum = 1;
        //第一行能展示的天数
        mFirstLineNum = 7 - mFirstIndex;
        mLastLineNum = 0;
        int surplus = mDayOfMonth - mFirstLineNum;
        while (surplus > 7) {
            mLineNum++;
            surplus -= 7;
        }
        if (surplus > 0) {
            mLineNum++;
            mLastLineNum = surplus;
        }
        //Log.i(TAG, getMonthStr(month) + "一共有" + mDayOfMonth + "天,第一天的索引是：" + mFirstIndex + "   有" +
        //        mLineNum + "行，第一行" + mFirstLineNum + "个，最后一行" + mLastLineNum + "个");
    }


    private String getMonthStr(Date month) {
        if (mDateFormat == null) {
            if (mMonthFormat == null) {
                mMonthFormat = "yyyy年MM月";
            }
            mDateFormat = new SimpleDateFormat(mMonthFormat, Locale.getDefault());
        }
        return mDateFormat.format(month);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度 = 填充父窗体
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        mColumnWidth = widthSize / 7;
        //高度 = 标题高度 + 星期高度 + 日期行数*每行高度
        float height = mTitleHeight + mWeekHeight + (mLineNum * mOneHeight);
        //Log.i(TAG, "标题高度：" + mTitleHeight + " 星期高度：" + mWeekHeight + " 每行高度：" + mOneHeight + " 行数：" +
        //        mLineNum + "  \n控件高度：" + height);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), (int) height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMonth(canvas);
        drawWeek(canvas);
        drawDayAndDot(canvas);
    }


    private void drawMonth(Canvas canvas) {
        //背景
        if (DEBUG) {
            mBgPaint.setColor(mBgMonth);
            RectF rect = new RectF(0, 0, getWidth(), mTitleHeight);
            canvas.drawRect(rect, mBgPaint);
        }
        //绘制月份
        mPaint.setFakeBoldText(true);
        mPaint.setTextSize(mTextSizeMonth);
        mPaint.setColor(mTextColorMonth);
        String monthStr = getMonthStr(mCalendar.getTime());
        float textLen = getFontLength(mPaint, monthStr);
        float textStart = (getWidth() - textLen) / 2;
        canvas.drawText(monthStr, textStart, mMonthUpSpace + getFontLeading(mPaint), mPaint);
        //绘制左右箭头
        mRowWidth = mMonthRowL.getWidth();
        //float left, float top
        mRowLStart = (int) (textStart - mMonthRowSpace - mRowWidth);
        canvas.drawBitmap(mMonthRowL, mRowLStart, mMonthTopSpace, mBitmapPaint);
        mRowRStart = (int) (textStart + textLen + mMonthRowSpace);
        canvas.drawBitmap(mMonthRowR, mRowRStart, mMonthTopSpace, mBitmapPaint);
    }


    private void drawWeek(Canvas canvas) {
        //背景
        if (DEBUG) {
            mBgPaint.setColor(mBgWeek);
            RectF rect = new RectF(0, mTitleHeight, getWidth(), mTitleHeight + mWeekHeight);
            canvas.drawRect(rect, mBgPaint);
        }
        //绘制星期：七天
        mPaint.setFakeBoldText(false);
        mPaint.setTextSize(mTextSizeWeek);
        mPaint.setColor(mTextColorWeek);
        for (int i = 0; i < WEEK_STR.length; i++) {
            int len = (int) getFontLength(mPaint, WEEK_STR[i]);
            int x = i * mColumnWidth + (mColumnWidth - len) / 2;
            canvas.drawText(WEEK_STR[i], x, mTitleHeight + getFontLeading(mPaint), mPaint);
        }
    }


    private void drawDayAndDot(Canvas canvas) {
        //某行开始绘制的Y坐标，第一行开始的坐标为标题高度+星期部分高度
        float top = mTitleHeight + mWeekHeight;
        //行
        for (int line = 0; line < mLineNum; line++) {
            if (line == 0) {
                //第一行
                drawDayAndDot(canvas, top, mFirstLineNum, 0, mFirstIndex);
            } else if (line == mLineNum - 1) {
                //最后一行
                top += mOneHeight;
                drawDayAndDot(canvas, top, mLastLineNum, mFirstLineNum + (line - 1) * 7, 0);
            } else {
                //满行
                top += mOneHeight;
                drawDayAndDot(canvas, top, 7, mFirstLineNum + (line - 1) * 7, 0);
            }
        }
    }


    /**
     * 绘制某一行的日期
     *
     * @param top 顶部坐标
     * @param count 此行需要绘制的日期数量（不一定都是7天）
     * @param overDay 已经绘制过的日期，从overDay+1开始绘制
     * @param startIndex 此行第一个日期的星期索引
     */
    private void drawDayAndDot(Canvas canvas, float top, int count, int overDay, int startIndex) {
        //Log.i(TAG, "总共" + mDayOfMonth + "天  有" + mLineNum + "行" + "  已经画了" + overDay + "天,下面绘制：" +
        //        count + "天");
        //背景
        float topDot = top + mLineSpace + mDayHeight;
        if (DEBUG) {
            mBgPaint.setColor(mBgDay);
            RectF rect = new RectF(0, top, getWidth(), topDot);
            canvas.drawRect(rect, mBgPaint);

            mBgPaint.setColor(mBgDot);
            rect = new RectF(0, topDot, getWidth(), topDot + mDotSpace + mDotHeight);
            canvas.drawRect(rect, mBgPaint);
        }
        mPaint.setTextSize(mTextSizeDay);
        float dayTextLeading = getFontLeading(mPaint);
        //        Log.v(TAG, "当前日期："+currentDay+"   选择日期："+mSelectDay+"  是否为当前月："+mIsCurrentMonth);
        for (int i = 0; i < count; i++) {
            int left = (startIndex + i) * mColumnWidth;
            int day = (overDay + i + 1);

            mPaint.setTextSize(mTextSizeDay);

            int len = (int) getFontLength(mPaint, day + "");
            int x = left + (mColumnWidth - len) / 2;
            float y = top + mLineSpace + dayTextLeading;
            DotBean dotBean = mData.get(day);
            //选中的日期，如果是本月，选中日期正好是当天日期，下面的背景会覆盖上面绘制的虚线背景
            if (mSelectDay == day) {
                //选中的日期字体白色，橙色背景
                mPaint.setColor(mSelectTextColor);
                mBgPaint.setColor(mSelectBg);
                //绘制橙色圆背景，参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
                canvas.drawCircle(
                        left + mColumnWidth / 2,
                        top + mLineSpace + mDayHeight / 2, mSelectRadius, mBgPaint);

                if (mDrawRoundRect && dotBean != null) {
                    mIsShowRoundRect = true;
                    //绘制选中弹窗
                    int popupX = x - (mPopupBg.getWidth() - len) / 2;
                    float popupY = y - mPopupSpace - dayTextLeading - mPopupBg.getHeight();
                    int maxX = canvas.getWidth() - mPopupBg.getWidth();
                    if (popupX > maxX) {
                        popupX = maxX;
                    } else if (popupX < 0) {
                        popupX = 0;
                    }
                    mPopupRectF.set(popupX, popupY,
                            popupX + mPopupBg.getWidth(), popupY + mPopupBg.getHeight());
                    canvas.drawBitmap(mPopupBg, null, mPopupRectF, mBitmapPaint);
                    String money = dotBean.getMoney();
                    int moneyLen = (int) getFontLength(mPopupPaint, money);
                    int m = popupX + (mPopupBg.getWidth() - moneyLen) / 2;
                    canvas.drawText(money, m,
                            popupY + getFontLeading(mPopupPaint) + 30, mPopupPaint);
                } else {
                    mIsShowRoundRect = false;
                }
            } else {
                if (mIsCurrentMonth && mCurrentDay == day) {
                    mPaint.setColor(mCurrentTextColor);
                } else {
                    mPaint.setColor(mTextColorDay);
                }
            }

            canvas.drawText(day + "", x, y, mPaint);

            //绘制小圆点
            if (dotBean != null && dotBean.isShowDot()) {
                mDotPaint.setColor(dotBean.getColor());
                x += len / 2;
                canvas.drawCircle(x, topDot + mDotSpace + mDotRadius, mDotRadius, mDotPaint);
            }
        }
    }


    /****************************事件处理↓↓↓↓↓↓↓****************************/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mFocusPoint.set(event.getX(), event.getY());
                touchFocusMove(mFocusPoint, false);
                break;
            case MotionEvent.ACTION_MOVE:
                //mFocusPoint.set(event.getX(), event.getY());
                //touchFocusMove(mFocusPoint, false);
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mFocusPoint.set(event.getX(), event.getY());
                touchFocusMove(mFocusPoint, true);
                break;
        }
        return true;
    }


    /**
     * 焦点滑动
     */
    public void touchFocusMove(final PointF point, boolean eventEnd) {
        //Log.e(TAG, "点击坐标：(" + point.x + " ，" + point.y + "),事件是否结束：" + eventEnd);
        //标题和星期只有在事件结束后才响应
        if (point.y <= mTitleHeight) {
            mDrawRoundRect = false;
            //事件在标题上
            if (eventEnd && mOnClickListener != null) {
                if (point.x >= (mRowLStart - mRowWidth * 2) &&
                        point.x < (mRowLStart + mRowWidth * 3)) {
                    //Log.w(TAG, "点击左箭头");
                    mOnClickListener.onLeftRowClick();
                } else if (point.x > (mRowRStart - mRowWidth * 2) &&
                        point.x < (mRowRStart + mRowWidth * 3)) {
                    //Log.w(TAG, "点击右箭头");
                    mOnClickListener.onRightRowClick();
                } else if (point.x > mRowLStart && point.x < mRowRStart) {
                    Date time = mCalendar.getTime();
                    mOnClickListener.onTitleClick(getMonthStr(time), time);
                    hidePopup();
                } else {
                    hidePopup();
                }
            }
        } else if (point.y <= (mTitleHeight + mWeekHeight)) {
            if (mIsShowRoundRect && mPopupRectF.contains(point.x, point.y)) {
                //Log.e(TAG, "点击在弹窗上面");
                if (eventEnd) {
                    hidePopup();
                }
                return;
            }
            hidePopup();
            //事件在星期部分
            if (eventEnd && mOnClickListener != null) {
                //根据X坐标找到具体的焦点日期
                int xIndex = (int) point.x / mColumnWidth;
                //Log.e(TAG, "列宽：" + mColumnWidth + "  x坐标余数：" + (point.x / mColumnWidth));
                if ((point.x / mColumnWidth - xIndex) > 0) {
                    xIndex += 1;
                }
                mOnClickListener.onWeekClick(xIndex - 1, WEEK_STR[xIndex - 1]);
            }
        } else {
            //日期部分按下和滑动时重绘，只有在事件结束后才响应
            touchDay(point, eventEnd);
        }
    }


    /** 事件点在 日期区域 范围内 */
    private void touchDay(final PointF point, boolean eventEnd) {
        if (mIsShowRoundRect && mPopupRectF.contains(point.x, point.y)) {
            //Log.e(TAG, "点击在弹窗上面");
            if (eventEnd) {
                clickEmpty();
            }
            return;
        }
        //根据Y坐标找到焦点行
        boolean availability = false;  //事件是否有效
        //日期部分
        float top = mTitleHeight + mWeekHeight + mOneHeight;
        int focusLine = 1;
        while (focusLine <= mLineNum) {
            if (top >= point.y) {
                availability = true;
                break;
            }
            top += mOneHeight;
            focusLine++;
        }
        if (availability) {
            //根据X坐标找到具体的焦点日期
            int xIndex = (int) point.x / mColumnWidth;
            if ((point.x / mColumnWidth - xIndex) > 0) {
                xIndex += 1;
            }
            //            Log.e(TAG, "列宽："+mColumnWidth+"  x坐标余数："+(point.x / mColumnWidth));
            if (xIndex <= 0) {
                xIndex = 1;   //避免调到上一行最后一个日期
            }
            if (xIndex > 7) {
                xIndex = 7;   //避免调到下一行第一个日期
            }
            //            Log.e(TAG, "事件在日期部分，第"+focusLine+"/"+mLineNum+"行, "+xIndex+"列");
            if (focusLine == 1) {
                //第一行
                if (xIndex <= mFirstIndex) {
                    //Log.e(TAG, "点到开始空位了");
                    clickEmpty();
                    //setSelectedDay(mSelectDay, true);
                } else {
                    setSelectedDay(xIndex - mFirstIndex, eventEnd);
                }
            } else if (focusLine == mLineNum) {
                //最后一行
                if (xIndex > mLastLineNum) {
                    //Log.e(TAG, "点到结尾空位了");
                    clickEmpty();
                    //setSelectedDay(mSelectDay, true);
                } else {
                    setSelectedDay(mFirstLineNum + (focusLine - 2) * 7 + xIndex, eventEnd);
                }
            } else {
                setSelectedDay(mFirstLineNum + (focusLine - 2) * 7 + xIndex, eventEnd);
            }
        } else {
            //超出日期区域后，视为事件结束，不响应最后一个选择日期的回调
            //Log.e(TAG, "点击超出日期区域了");
            clickEmpty();
            //setSelectedDay(mSelectDay, true);
        }
    }


    private void clickEmpty() {
        mDrawRoundRect = false;
        mResponseWhenEnd = false;
        if (mIsShowRoundRect) {
            invalidate();
        }
    }


    /**
     * 设置选中的日期
     */
    private void setSelectedDay(int day, boolean eventEnd) {
        //Log.w(TAG, "选中：" + day + "  事件是否结束" + eventEnd);
        mSelectDay = day;
        invalidate();
        if (mOnClickListener != null && eventEnd && mResponseWhenEnd &&
                mLastSelectDay != mSelectDay) {
            mDrawRoundRect = true;
            mLastSelectDay = mSelectDay;
            mOnClickListener.onDayClick(mSelectDay,
                    getMonthStr(mCalendar.getTime()) + mSelectDay + "日", mData.get(mSelectDay));
        } else if (eventEnd && mLastSelectDay == mSelectDay) {
            mDrawRoundRect = !mIsShowRoundRect;
        }
        mResponseWhenEnd = !eventEnd;
    }


    /****************************事件处理↑↑↑↑↑↑↑****************************/

    public void setData(Date month, List<? extends DotBean> list) {
        setMonth(month);
        if (list != null && list.size() > 0) {
            mData.clear();
            for (DotBean finish : list) {
                mData.put(finish.getDay(), finish);
            }
        }
        invalidate();
    }


    public void setData(List<? extends DotBean> list) {
        if (list != null && list.size() > 0) {
            mData.clear();
            for (DotBean finish : list) {
                mData.put(finish.getDay(), finish);
            }
        }
        invalidate();
    }


    /**
     * 月份增减
     */
    public void monthChange(int change) {
        mCalendar.add(Calendar.MONTH, change);
        setMonth(mCalendar.getTime());
        mData.clear();
        invalidate();
    }


    /**
     * 隐藏弹窗
     */
    public void hidePopup() {
        mDrawRoundRect = false;
        if (mIsShowRoundRect) {
            invalidate();
        }
    }


    /**
     * @return 返回指定笔和指定字符串的长度
     */
    public static float getFontLength(Paint paint, String str) {
        return paint.measureText(str);
    }


    /**
     * @return 返回指定笔的文字高度
     */
    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }


    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }


    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }


    interface OnClickListener {

        void onLeftRowClick();

        void onRightRowClick();

        void onTitleClick(String monthStr, Date month);

        void onWeekClick(int weekIndex, String weekStr);

        void onDayClick(int day, String dayStr, DotBean finish);
    }

    interface DotBean {
        boolean isShowDot();

        @ColorInt
        int getColor();

        int getDay();

        String getMoney();
    }
}
