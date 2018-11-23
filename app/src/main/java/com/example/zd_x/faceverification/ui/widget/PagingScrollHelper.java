package com.example.zd_x.faceverification.ui.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PagingScrollHelper {
    private static final String TAG = "PagingScrollHelper";
    private RecyclerView mRecyclerView;
    private MyOnScrollListener mOnScrollListener = new MyOnScrollListener();
    private MyOnTouchListener mOnTouchListener = new MyOnTouchListener();

    private boolean isSlidingToLast = true;

    public void setUpRecycleView(RecyclerView recycleView) {
        if (recycleView == null) {
            throw new IllegalArgumentException("recycleView must be not null");
        }
        mRecyclerView = recycleView;
        //设置滚动监听，记录滚动的状态，和总的偏移量
        recycleView.setOnScrollListener(mOnScrollListener);
        //记录滚动开始的位置
        recycleView.setOnTouchListener(mOnTouchListener);
    }

//    public int getStartPageIndex() {
//        int p = 0;
//        if (mRecyclerView.getHeight() == 0 || mRecyclerView.getWidth() == 0) {
//            //没有宽高无法处理
//            return p;
//        }
//        if (mOrientation == ORIENTATION.VERTICAL) {
//            p = startY / mRecyclerView.getHeight();
//        }
//        return p;
//    }

//    public int getPageIndex() {
//        int p = 0;
//        if (mRecyclerView.getHeight() == 0 || mRecyclerView.getWidth() == 0) {
//            return p;
//        }
//        if (mOrientation == ORIENTATION.VERTICAL) {
//            p = offsetY / mRecyclerView.getHeight();
//        }
//        return p;
//    }

//    enum ORIENTATION {
//        HORIZONTAL, VERTICAL, NULL
//    }

//    public class MyOnFlingListener extends RecyclerView.OnFlingListener {
//
//        @Override
//        public boolean onFling(int velocityX, int velocityY) {
//            if (mOrientation == ORIENTATION.NULL) {
//                return false;
//            }
//            //获取开始滚动时所在页面的index
//            int p = getStartPageIndex();
//            //记录滚动开始和结束的位置
//            int endPoint = 0;
//            int startPoint = 0;
//
//            //如果是垂直方向
//            if (mOrientation == ORIENTATION.VERTICAL) {
//                startPoint = offsetY;
//
//                if (velocityY < 0) {
//                    p--;
//                } else if (velocityY > 0) {
//                    p++;
//                }
//                //根据不同的速度判断需要滚动的方向
//                //注意，此处有一个技巧，就是当速度为0的时候就滚动会开始的页面，即实现页面复位
//                endPoint = p * mRecyclerView.getHeight();
//            }
//
//            if (endPoint < 0) {
//                endPoint = 0;
//            }
//            //使用动画处理滚动
//            if (mAnimator == null) {
//                mAnimator = new ValueAnimator().ofInt(startPoint, endPoint);
//                mAnimator.setDuration(300);
//                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        int nowPoint = (int) animation.getAnimatedValue();
//                        if (mOrientation == ORIENTATION.VERTICAL) {
//                            int dy = nowPoint - offsetY;
//                            //这里通过RecyclerView的scrollBy方法实现滚动。
//                            mRecyclerView.scrollBy(0, dy);
//                        }
//                    }
//                });
//                mAnimator.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        //回调监听
//                        if (null != mOnPageChangeListener) {
//                            mOnPageChangeListener.onPageChange(getPageIndex());
//                        }
//                        mRecyclerView.stopScroll();
//                        startY = offsetY;
//                        startX = offsetX;
//                    }
//                });
//            } else {
//                mAnimator.cancel();
//                mAnimator.setIntValues(startPoint, endPoint);
//            }
//            mAnimator.start();
//            return true;
//        }
//    }

//    public class MyOnTouchListener implements View.OnTouchListener {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            //手指按下的时候记录开始滚动的坐标
//            if (firstTouch) {
//                //第一次touch可能是ACTION_MOVE或ACTION_DOWN,所以使用这种方式判断
//                firstTouch = false;
//                startY = offsetY;
//                startX = offsetX;
//            }
//            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
//                firstTouch = true;
//            }
//
//            return false;
//        }
//    }

    public class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //newState==0表示滚动停止，此时需要处理回滚
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //获取最后一个完全显示的ItemPosition
                int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                Log.e(TAG, "onScrollStateChanged: lastVisibleItem:  " + lastVisibleItem);
                int totalItemCount = manager.getItemCount();
                Log.e(TAG, "onScrollStateChanged: totalItemCount:  " + totalItemCount);
                // 判断是否滚动到底部，并且是向右滚动
                if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                    //加载更多功能的代码
                    if (totalItemCount % 10 == 0) {
                        mOnPageChangeListener.onPageChange((totalItemCount) / 10, true);
                    }
                } else {
                    mOnPageChangeListener.onPageChange(0, false);
                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //dx用来判断横向滑动方向，dy用来判断纵向滑动方向

            if (dy > 0) {
                Log.e(TAG, "onScrolled: dy: "+dy );
                //大于0表示正在向下滚动
                isSlidingToLast = true;
            } else {
                Log.e(TAG, "onScrolled: dy---: "+dy );

                //小于等于0表示停止或向上滚动
                isSlidingToLast = false;
            }
        }
    }

    private boolean firstTouch = true;
    private int offsetY = 0;
    private int offsetX = 0;

    int startY = 0;
    int startX = 0;
    public class MyOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //手指按下的时候记录开始滚动的坐标
            if (firstTouch) {
                //第一次touch可能是ACTION_MOVE或ACTION_DOWN,所以使用这种方式判断
                float x = event.getX();
                Log.e(TAG, "onTouch: ------"+x );
                firstTouch = false;
                startY = offsetY;
                startX = offsetX;
            }
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                firstTouch = true;
            }
            return false;
        }
    }

    onPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(onPageChangeListener listener) {
        mOnPageChangeListener = listener;
    }

    public interface onPageChangeListener {
        void onPageChange(int index, boolean IsPullUp);
    }
}
