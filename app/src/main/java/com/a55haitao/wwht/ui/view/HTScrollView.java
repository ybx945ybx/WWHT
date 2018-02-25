package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Haotao_Fujie on 16/10/17.
 */

public class HTScrollView extends ScrollView {

    private ScrollListener scrollListener;

    private boolean isReachBottom;
    private int startY;
    private int overOffsetHeight;
    private static int MIN_OVER_OFFSET_HEIGHT = 80;

    public int htScrollOffsetWhenTouchBottom = 0;
    public int htScrollPageHeight = 0;
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    public HTScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HTScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public HTScrollView(Context context) {
        super(context);
    }

    public void setOverOffsetHeight(int overOffsetHeight) {
        if (overOffsetHeight < 1) {
            this.overOffsetHeight = MIN_OVER_OFFSET_HEIGHT;
        }else {
            this.overOffsetHeight = overOffsetHeight;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt){
        super.onScrollChanged(l,t,oldl,oldt);
        htScrollPageHeight = computeVerticalScrollRange();
        if(t + getHeight() >=  htScrollPageHeight){
            // ScrollView滑动到底部了
            isReachBottom = true;
            if (htScrollOffsetWhenTouchBottom == 0) {
                htScrollOffsetWhenTouchBottom = t;
            }
        }else {
            isReachBottom = false;
        }
    }

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = HTScrollView.this.getScrollY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if(scrollListener != null){
                scrollListener.onScroll(scrollY);
            }

        };

    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);

        return true;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener{
        public void scrollBottom();
        public void onScroll(int scrollY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (scrollListener != null) {
            switch (ev.getAction()) {

                case MotionEvent.ACTION_UP: {
                    System.out.print("action up");
                    float a1 = this.computeVerticalScrollOffset();
                    System.out.print(a1);
                    handler.sendMessageDelayed(handler.obtainMessage(), 20);
                    break;
                }

                case MotionEvent.ACTION_DOWN: {
                    System.out.print("action down");
                    float a2 = this.computeVerticalScrollOffset();
                    System.out.print(a2);
                    if (isReachBottom) {
                        startY = (int) ev.getY();
                    }else {
                        startY = 0;
                    }

                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    System.out.print("action move");
                    float a3 = this.computeVerticalScrollOffset();
                    System.out.print(a3);
                    int movingY = (int) ev.getY();
                    if (isReachBottom) {
                        if (startY - movingY >= overOffsetHeight) {
                            scrollListener.scrollBottom();
                            startY = 0;
                        }
                    }
                    break;
                }
            }
        }

        return super.onTouchEvent(ev);
    }
}
