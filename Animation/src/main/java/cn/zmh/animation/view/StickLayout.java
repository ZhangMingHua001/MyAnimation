package cn.zmh.animation.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import cn.zmh.animation.R;
import cn.zmh.animation.util.ZLog;

public class StickLayout extends LinearLayout {
    private final static String TAG = " StickLayout";
    private final static int SPEED = 500;

    private View mHead;
    private View mBody;

    private float mMoveY;
    private float mTranslateY;
    private float mHeadHeight;
    private float mTranslateMinY;
    private float mTranslateMaxY;
    private int mPointerId;

    private VelocityTracker mVelocityTracker;
    private ValueAnimator animator;

    public StickLayout(Context context) {
        super(context);
    }

    public StickLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        acquireVelocityTracker(event);
        final VelocityTracker verTracker = mVelocityTracker;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mMoveY = event.getY() - mTranslateY;
                mPointerId = event.getPointerId(0);
                return true;
            case MotionEvent.ACTION_MOVE:
                float translateY = event.getY() - mMoveY;
                translateY = Math.max(mTranslateMinY, translateY);
                translateY = Math.min(translateY, mTranslateMaxY);
                translateView(translateY);

                return true;
            case MotionEvent.ACTION_UP:
                mMoveY = 0;
                verTracker.computeCurrentVelocity(1000);
                final float velocityY = verTracker.getYVelocity(mPointerId);
                if (Math.abs(velocityY) > SPEED) {
                    playAnimation(mTranslateY, velocityY > 0 ? mTranslateMaxY : mTranslateMinY);
                } else {
                    boolean down = (mTranslateY - mTranslateMinY > mTranslateMaxY - mTranslateY);
                    playAnimation(mTranslateY, down ? mTranslateMaxY : mTranslateMinY);
                }
                releaseVelocityTracker();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void playAnimation(float from, float to) {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        animator = ValueAnimator.ofFloat(from, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                translateView((float) animation.getAnimatedValue());
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private void translateView(float translateY) {
        mTranslateY = translateY;
        mBody.setTranslationY(mTranslateY);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }


    private void initView() {
        mHead = findViewById(R.id.text_head);
        mBody = findViewById(R.id.text_body);

        if (mHead == null || mBody == null) {
            ZLog.d(TAG, "get Id Exception");
            throw new RuntimeException(TAG + "get Id Exception");
        }
    }

    public void setTranslateMax(float max) {
        if (Math.abs(max) > mHeadHeight) {
            max = mHeadHeight;
        }
        mTranslateMaxY = 0f;
        mTranslateMinY = -Math.abs(max);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RuntimeException run = new RuntimeException();
        run.printStackTrace();
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeadHeight = mHead.getMeasuredHeight();
        setTranslateMax(mHeadHeight);
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
