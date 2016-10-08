package com.morening.project.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.morening.project.project_clockview.R;

/**
 * Created by morening on 2016/10/5.
 */

public class CycleClockWithTime extends View {

    private final static String TAG = "CycleClockWithTime";

    private final int DEFAULT_HOUR_REACH_COLOR = 0xFF388E3C;
    private final int DEFAULT_MINUTE_REACH_COLOR = 0xFF8BC34A;
    private final int DEFAULT_SECOND_REACH_COLOR = 0xFFCDDC39;

    private final int DEFAULT_HOUR_UNREACH_COLOR = 0x77388E3C;
    private final int DEFAULT_MINUTE_UNREACH_COLOR = 0x778BC34A;
    private final int DEFAULT_SECOND_UNREACH_COLOR = 0x77CDDC39;

    private final int DEFAULT_HOUR_PAINT_WIDTH = 4; //dp
    private final int DEFAULT_MINUTE_PAINT_WIDTH = 3; //dp
    private final int DEFAULT_SECOND_PAINT_WIDTH = 2; //dp

    private final int DEFAULT_TIME_TEXT_SIZE = 12; //sp
    private final int DEFAULT_TIME_TEXT_COLOR = 0xFF000000;

    private final int DEFAULT_CYCLE_OFFSET = 6; //dp
    private final int DEFAULT_CLOCK_RADIUS = 100; //dp

    private int mHourReachColor = DEFAULT_HOUR_REACH_COLOR;
    private int mMinReachColor = DEFAULT_MINUTE_REACH_COLOR;
    private int mSecReachColor = DEFAULT_SECOND_REACH_COLOR;

    private int mHourUnreachColor = DEFAULT_HOUR_UNREACH_COLOR;
    private int mMinUnreachColor = DEFAULT_MINUTE_UNREACH_COLOR;
    private int mSecUnreachColor = DEFAULT_SECOND_UNREACH_COLOR;

    private int mHourPaintWidth = dp2px(DEFAULT_HOUR_PAINT_WIDTH);
    private int mMinPaintWidth = dp2px(DEFAULT_MINUTE_PAINT_WIDTH);
    private int mSecPaintWidth = dp2px(DEFAULT_SECOND_PAINT_WIDTH);

    private int mTimeTextSize = sp2px(DEFAULT_TIME_TEXT_SIZE);
    private int mTimeTextColor = DEFAULT_TIME_TEXT_COLOR;

    private int mCycleOffset = dp2px(DEFAULT_CYCLE_OFFSET);
    private int mClockRadius = dp2px(DEFAULT_CLOCK_RADIUS);

    private Paint mPaint = new Paint();

    public CycleClockWithTime(Context context) {
        this(context, null);
    }

    public CycleClockWithTime(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CycleClockWithTime(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(attrs);

        Handler handler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                invalidate();

                sendEmptyMessageDelayed(0x01, 1000);
            }
        };
        handler.sendEmptyMessageDelayed(0x01, 0);
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CycleClockWithTime);

        mHourReachColor = ta.getColor(R.styleable.CycleClockWithTime_hour_reach_color, mHourReachColor);
        mMinReachColor = ta.getColor(R.styleable.CycleClockWithTime_minute_reach_color, mMinReachColor);
        mSecReachColor = ta.getColor(R.styleable.CycleClockWithTime_second_reach_color, mSecReachColor);

        mHourUnreachColor = ta.getColor(R.styleable.CycleClockWithTime_hour_unreach_color, mHourUnreachColor);
        mMinUnreachColor = ta.getColor(R.styleable.CycleClockWithTime_minute_unreach_color, mMinUnreachColor);
        mSecUnreachColor = ta.getColor(R.styleable.CycleClockWithTime_second_unreach_color, mSecUnreachColor);

        mHourPaintWidth = (int) ta.getDimension(R.styleable.CycleClockWithTime_hour_paint_width, mHourPaintWidth);
        mMinPaintWidth = (int) ta.getDimension(R.styleable.CycleClockWithTime_minute_paint_width, mMinPaintWidth);
        mSecPaintWidth = (int) ta.getDimension(R.styleable.CycleClockWithTime_second_paint_width, mSecPaintWidth);

        mTimeTextSize = (int) ta.getDimension(R.styleable.CycleClockWithTime_time_text_size, mTimeTextSize);
        mTimeTextColor = ta.getColor(R.styleable.CycleClockWithTime_time_text_color, mTimeTextColor);

        mCycleOffset = (int) ta.getDimension(R.styleable.CycleClockWithTime_cycle_offset, mCycleOffset);
        mClockRadius = (int) ta.getDimension(R.styleable.CycleClockWithTime_clock_radius, mClockRadius);
//        Log.d(TAG, "[obtainStyledAttrs] mClockRadius: "+mClockRadius);

        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int expectWidth = getPaddingLeft() + getPaddingRight() + mHourPaintWidth + mClockRadius*2;
        int expectHeight = getPaddingTop() + getPaddingBottom() + mHourPaintWidth + mClockRadius*2;
        int width = resolveSize(expectWidth, widthMeasureSpec);
        int height = resolveSize(expectHeight, heightMeasureSpec);

        int realSize = Math.min(width, height);
        mClockRadius = (realSize - getPaddingLeft() - getPaddingRight() - mHourPaintWidth) / 2;

        /*Log.d(TAG, "[onMeasure] expectWidth: "+expectWidth+"  expectHeight: "
                +expectHeight+"  width: "+width+"  height: "
                +height+"  realSize: "+realSize+"  mClockRadius: "+mClockRadius);*/

        setMeasuredDimension(realSize, realSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.translate(getPaddingLeft() + mHourPaintWidth/2, getPaddingLeft() + mHourPaintWidth/2);

        // draw hour unreach
        mPaint.setColor(mHourUnreachColor);
        mPaint.setStrokeWidth(mHourPaintWidth/3);
        int hourRadius = mClockRadius;
        canvas.drawCircle(hourRadius, hourRadius, hourRadius, mPaint);

        // draw hour reach
        mPaint.setColor(mHourReachColor);
        mPaint.setStrokeWidth(mHourPaintWidth);
        float hourSweep = getHour()*1.0f / 24 * 360;
        int hourRectFStart = 0;
        int hourRectFEnd = hourRadius*2;
        canvas.drawArc(new RectF(hourRectFStart, hourRectFStart, hourRectFEnd, hourRectFEnd), -90, hourSweep, false, mPaint);

        // draw min unreach
        mPaint.setColor(mMinUnreachColor);
        mPaint.setStrokeWidth(mMinPaintWidth/3);
        int minOffset = mHourPaintWidth/2 + mCycleOffset + mMinPaintWidth/2;
        int minRadius = hourRadius - minOffset;
        canvas.drawCircle(hourRadius, hourRadius, minRadius, mPaint);

        // draw min reach
        mPaint.setColor(mMinReachColor);
        mPaint.setStrokeWidth(mMinPaintWidth);
        float minSweep = getMin()*1.0f / 60 * 360;
        int minRectFStart = hourRectFStart + minOffset;
        int minRectFEnd = hourRectFEnd - minOffset;
        canvas.drawArc(new RectF(minRectFStart, minRectFStart, minRectFEnd, minRectFEnd), -90, minSweep, false, mPaint);

        // draw sec unreach
        mPaint.setColor(mSecUnreachColor);
        mPaint.setStrokeWidth(mSecPaintWidth/3);
        int secOffset = mMinPaintWidth/2 + mCycleOffset + mSecPaintWidth/2;
        int secRadius = minRadius - secOffset;
        canvas.drawCircle(hourRadius, hourRadius, secRadius, mPaint);

        // draw sec reach
        mPaint.setColor(mSecReachColor);
        mPaint.setStrokeWidth(mSecPaintWidth);
        float secSweep = getSec()*1.0f / 60 * 360;
        int secRectFStart = minRectFStart + secOffset;
        int secRectFEnd = minRectFEnd - secOffset;
        canvas.drawArc(new RectF(secRectFStart, secRectFStart, secRectFEnd, secRectFEnd), -90, secSweep, false, mPaint);

        // draw text
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFakeBoldText(true);
        mPaint.setColor(mTimeTextColor);
        mPaint.setTextSize(mTimeTextSize);
        StringBuffer sb = new StringBuffer();
        sb.append(getFormatHour()).append(":").append(getFormatMin()).append(":").append(getFormatSec());
        String text = sb.toString();
        float textWidth = mPaint.measureText(text);
        float textHeight = mPaint.descent() - mPaint.ascent();
        canvas.drawText(text, hourRadius-textWidth/2, hourRadius+textHeight/2, mPaint);

        canvas.restore();
    }

    /* get hour min & sec int value */
    private int getHour(){

        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    private int getMin(){

        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    private int getSec(){

        return Calendar.getInstance().get(Calendar.SECOND);
    }

    /* get hour min & sec string value */
    private String getFormatHour(){
        int hour = getHour();
        if (hour < 10){
            return "0"+hour;
        }
        return String.valueOf(hour);
    }

    private String getFormatMin(){
        int min = getMin();
        if (min < 10){
            return "0"+min;
        }
        return String.valueOf(min);
    }

    private String getFormatSec(){
        int sec = getSec();
        if (sec < 10){
            return "0"+sec;
        }
        return String.valueOf(sec);
    }

    /* convert sp & dp into px */
    private int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    private int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }
}
