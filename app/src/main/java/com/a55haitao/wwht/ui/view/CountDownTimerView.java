package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by a55 on 2017/3/20.
 * 计时器
 */

public class CountDownTimerView extends LinearLayout {

    // 小时，十位
    private TextView tv_hour_decade;
    // 小时，个位
    private TextView tv_hour_unit;
    // 分钟，十位
    private TextView tv_min_decade;
    // 分钟，个位
    private TextView tv_min_unit;
    // 秒，十位
    private TextView tv_sec_decade;
    // 秒，个位
    private TextView tv_sec_unit;

    private int          hour_decade;
    private int          hour_unit;
    private int          min_decade;
    private int          min_unit;
    private int          sec_decade;
    private int          sec_unit;
    // 计时器
    private Timer        timer;
//    private TextView     tvSwitch;
    private LinearLayout llytRoot;
    private LinearLayout llytTime;

    private boolean bStart;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        };
    };

    public CountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_countdowntimer, this);

        llytRoot = (LinearLayout) view.findViewById(R.id.llyt_root);
        tv_hour_decade = (HaiTextView) view.findViewById(R.id.tv_hour_decade);
        tv_hour_unit = (HaiTextView) view.findViewById(R.id.tv_hour_unit);
        tv_min_decade = (HaiTextView) view.findViewById(R.id.tv_min_decade);
        tv_min_unit = (HaiTextView) view.findViewById(R.id.tv_min_unit);
        tv_sec_decade = (HaiTextView) view.findViewById(R.id.tv_sec_decade);
        tv_sec_unit = (HaiTextView) view.findViewById(R.id.tv_sec_unit);
//        tvSwitch = (TextView) view.findViewById(R.id.txt_switch);
        llytTime = (LinearLayout) view.findViewById(R.id.time_layout);

    }

    /**
     *
     * @Description: 开始计时
     * @param
     * @return void
     * @throws
     */
    public void start() {

        if (timer == null) {
            timer = new Timer();
            bStart = true;
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    /**
     *
     * @Description: 停止计时
     * @param
     * @return void
     * @throws
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            bStart = false;
            timer = null;
        }
    }

    public boolean hasStart() {
        return bStart;
    }

    /**
     * @throws Exception
     *
     * @Description: 设置倒计时的时长
     * @param
     * @return void
     * @throws
     */
    public void setTime(int hour, int min, int sec) {

        if (min >= 60 || sec >= 60 || hour < 0 || min < 0
                || sec < 0) {
            throw new RuntimeException("Time format is error,please check out your code");
        }

        hour_decade = hour / 10;
        hour_unit = hour - hour_decade * 10;

        min_decade = min / 10;
        min_unit = min - min_decade * 10;

        sec_decade = sec / 10;
        sec_unit = sec - sec_decade * 10;

        tv_hour_decade.setText(hour_decade + "");
        tv_hour_unit.setText(hour_unit + "");
        tv_min_decade.setText(min_decade + "");
        tv_min_unit.setText(min_unit + "");
        tv_sec_decade.setText(sec_decade + "");
        tv_sec_unit.setText(sec_unit + "");

    }

    /**
     *
     * @Description: 倒计时
     * @param
     * @return boolean
     * @throws
     */
    private void countDown() {

        if (isCarry4Unit(tv_sec_unit)) {
            if (isCarry4Decade(tv_sec_decade)) {

                if (isCarry4Unit(tv_min_unit)) {
                    if (isCarry4Decade(tv_min_decade)) {

                        if (isCarry4Unit(tv_hour_unit)) {
                            if (isCarry4Decade(tv_hour_decade)) {
//                                tvSwitch.setText("已关闭");
//                                llytTime.setVisibility(GONE);
                                setTime(0, 0, 0);
                                stop();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @Description: 变化十位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarry4Decade(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 5;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    /**
     *
     * @Description: 变化个位，并判断是否需要进位
     * @param
     * @return boolean
     * @throws
     */
    private boolean isCarry4Unit(TextView tv) {

        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 9;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    public void setBackgroundRed(){
        llytRoot.setBackgroundColor(Color.parseColor("#00ffffff"));
    }

    public void setBackgroundBlack(){
        llytRoot.setBackgroundColor(Color.parseColor("#26241F"));
    }
}
