package com.timepp.democollection.customview.drawDemo;

import android.view.animation.AnimationUtils;

/**
 * 现在只允许从左往右跑，因为我懒得去做太多支持，反正我搞懂原理能实现就好了
 */
public class SimpleMarquee {

    public static final int STOP = 0;
    public static final int COLD = 1;
    public static final int RUN = 2;
    public static final long WAIT_TIME = 1000L;

    private int mStatus = STOP; // 跑马灯状态

    private long mStartTime = 0L; // 开始时间
    private long mDuration = 0L; // 单圈跑动时间
    private long mCycDuration = 0L; // 单圈跑动时间

    private float mCycOffset = 0f; // 循环滚动时的其实点位
    private float mCycTolDlt = 0f;
    private float mTolDlt = 0f;

    private boolean isCycling = false;

    /**
     * 1
     * onDraw()中去用，第一次绘制结束再调用
     * 调用时先构造对象，再判断有没有初始化，没有初始化就老老实实初始化
     * @return 是否有初始化过
     */
    public boolean isInit() {
        return mTolDlt > 0 && mCycTolDlt > 0;
    }

    /**
     * 2
     * 判断了上面没有初始化后，先初始化一下，这一步很重要，不要传一些蛇皮参数，会被拒绝初始化的
     * @param showWd 展示容器的宽度
     * @param gap 内容之间的间隔
     * @param conWd 内容宽度
     * @param vel 速度时间单位为：pixel/s，不允许小于1
     */
    public void init(float showWd, float gap, float conWd, float vel) {
        if (showWd < 0 || conWd <= showWd || vel < 1) {
            return;
        }
        mCycTolDlt = gap + conWd;
        mTolDlt = conWd * 2 + gap - showWd;
        mCycOffset = conWd - showWd;
        mDuration = (long) (mTolDlt / vel * 1000); // 直接把速度转换成时间间隔方便处理
        if (mDuration < 500) { // 滚太快鬼尼玛能看到效果
            mDuration = 500;
        }
        mCycDuration = (long)(mCycTolDlt / mTolDlt * mDuration);
    }

    /**
     * 3
     * 初始化完成之后，紧着着就掉这个方法
     * 我为啥不写在init里？ 我个人喜欢
     */
    public boolean start() {
        if (isInit() && mDuration >= 500 && mStatus == STOP) {
            mStatus = RUN;
            isCycling = false;
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * 4
     * 这个方法你得放在绘制之前，判断有没有马在跑，有你再取偏移值
     * @return 是否正在运行跑马灯
     */
    public boolean onMarquee() {
        return mStatus != STOP;
    }

    /**
     * 5
     * 上面那个鬼方法为true时，再调这个方法 canvas.translate(getDelta(), dy)
     * @return 偏移值
     */
    public float getDelta() {
        switch (mStatus) {
            case STOP:
                // 状态不对，break出去之后，最后会直接返回0
                return 0;
            case COLD:
                long now = AnimationUtils.currentAnimationTimeMillis();
                // 冷却时间过了，可以切换状态了
                if (now - mStartTime >= mDuration + WAIT_TIME) {
                    mStatus = RUN; // 改变一下状态
                    if (!isCycling) {
                        isCycling= true;
                    }
                    mStartTime = now; // 刷一下动画开始的时间
                }
                return -mCycOffset;
            case RUN:
                // 进来就先算完成度，这里需要注意，除数千万不可为0
                float duration = isCycling ? mCycDuration : mDuration;
                float fraction = (AnimationUtils.currentAnimationTimeMillis() - mStartTime) / duration;
                float curDlt = isCycling ? (mCycOffset + fraction * mCycTolDlt) : (fraction * mTolDlt);
                // 矫正一下值，顺便把状态转换一下
                if (curDlt < 0) {
                    curDlt = 0;
                } else if (curDlt > mTolDlt) {
                    curDlt = mTolDlt;
                    mStatus = COLD;
                }
                return -curDlt;
            default:
                return 0;
        }
    }

    /**
     * 6
     * 想刷新数据了，就调这个方法，把之前的一切都清掉重来
     */
    public void cancel() {
        mStatus = STOP;
        mDuration = 0L;
        mCycTolDlt = 0f;
        mTolDlt = 0f;
        mCycOffset = 0f;
    }

    public boolean onCold() {
        return mStatus == COLD;
    }

    public int getStatus() {
        return mStatus;
    }

}
