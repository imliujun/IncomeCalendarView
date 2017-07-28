# IncomeCalendarView

一个简单的收益日历，展示基本的日历样式，日期下面可以展示不同颜色的小圆点，点击日期弹出当天的收益金额。

在 [ Android自定义View（CustomCalendar－定制日历控件）][1] 上进行了一些修改，满足现在的需求 **就一个类，超轻。**

![][image-1]

## 使用

```xml
<com.imliujun.calendar.IncomeCalendarView
        android:id="@+id/calendar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

### 自定义属性：

```xml
<declare-styleable name="IncomeCalendarView">
        <!--这四个颜色分别是月份、星期、日期、任务的背景色，只是方便调试测量时使用，正式试用时可配置透明色-->
        <attr name="mBgMonth" format="color"/>
        <attr name="mBgWeek" format="color"/>
        <attr name="mBgDay" format="color"/>
        <attr name="mBgDot"  format="color"/>

        <attr name="mTextColorMonth" format="color"/>  <!--标题字体颜色-->
        <attr name="mTextColorWeek"  format="color"/>  <!--星期字体颜色-->
        <attr name="mTextColorDay" format="color"/>   <!--日期字体颜色-->
        <attr name="mSelectTextColor"  format="color"/>  <!--选中日期字体颜色-->
        <attr name="mSelectBg"  format="color"/>     <!--选中日期背景-->
        <attr name="mCurrentTextColor"  format="color"/>    <!--当天日期字体颜色-->
        <attr name="mPopupTextColor" format="color"/>    <!--弹窗字体颜色-->

        <attr name="mTextSizeMonth" format="dimension"/>  <!--标题字体大小-->
        <attr name="mTextSizeWeek"  format="dimension"/>  <!--星期字体大小-->
        <attr name="mTextSizeDay"  format="dimension"/>   <!--日期字体大小-->
        <attr name="mTextSizePopup"  format="dimension"/> <!--弹窗字体大小-->

        <attr name="mMonthRowL" format="reference"/>   <!--月份箭头-->
        <attr name="mMonthRowR"  format="reference"/>   <!--月份箭头-->
        <attr name="mPopupBg"  format="reference"/>   <!--弹窗背景图-->
        <attr name="mMonthRowSpace"   format="dimension"/>
        <attr name="mMonthTopSpace" format="dimension"/>

        <attr name="mSelectRadius"    format="dimension"/> <!--选中日期背景半径-->
        <attr name="mMonthUpSpace"  format="dimension"/>   <!--标题月份下间隔-->
        <attr name="mMonthDownSpace" format="dimension"/>   <!--标题月份下间隔-->
        <attr name="mLineSpace"   format="dimension"/>    <!--日期行间距-->
        <attr name="mDotSpace"  format="dimension"/>  <!--小圆点上下间距-->
        <attr name="mPopupSpace" format="dimension"/>   <!--弹窗下间距-->
        <attr name="mDotRadius"  format="dimension"/>   <!--小圆点半径-->
        <attr name="mMonthFormat" format="string"/>   <!--title显示的月份格式-->
    </declare-styleable>
```

**大部分元素都可以进行配置，如果还不能满足需求，可以自行修改源码。**

### 感谢

[ Android自定义View（CustomCalendar－定制日历控件）][2]


[1]:	http://blog.csdn.net/xmxkf/article/details/54020386
[2]:	http://blog.csdn.net/xmxkf/article/details/54020386

[image-1]:	Screen/Screen.gif

