package com.cheng.test.baskballboard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.cheng.test.baskballboard.utils.UIUtils;
import com.cheng.test.baskballboard.R;

/**
 * 项目名称：BasketballBoard
 * 类描述：
 * 创建人：Cheng
 * 创建时间：2016/8/24 13:16
 * 修改人：Cheng
 * 修改时间：2016/8/24 13:16
 * 修改备注：
 */
public class AutoTextView extends TextSwitcher implements
        ViewSwitcher.ViewFactory {

    private float mHeight;
    private int mColor;
    private Context mContext;
    //mInUp,mOutUp分离构成向下翻页的进出动画
    private Rotate3dAnimation mInUp;
    private Rotate3dAnimation mOutUp;

    //mInDown,mOutDown分离构成向下翻页的进出动画
    private Rotate3dAnimation mInDown;
    private Rotate3dAnimation mOutDown;

    public AutoTextView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }
    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoTextView);
        mHeight = a.getDimension(R.styleable.AutoTextView_textSize, 10);
//        Log.d("AutoTextView", "mHeight1 = "+mHeight);
//        mHeight =  a.getDimensionPixelSize(R.styleable.AutoTextView_textSize, 10);
        Log.d("AutoTextView", "mHeight2 = "+mHeight);
        mColor = a.getColor(R.styleable.AutoTextView_textColor,0x000000);
        a.recycle();
        mContext = context;
        init();
    }
    private void init() {
        // TODO Auto-generated method stub
        setFactory(this);
        mInUp = createAnim(-90, 0 , true, true);
        mOutUp = createAnim(0, 90, false, true);
        mInDown = createAnim(90, 0 , true , false);
        mOutDown = createAnim(0, -90, false, false);
        //TextSwitcher重要用于文件切换，比如 从文字A 切换到 文字 B，
        //setInAnimation()后，A将执行inAnimation，
        //setOutAnimation()后，B将执行OutAnimation
        setInAnimation(mInUp);
        setOutAnimation(mOutUp);

    }

    private Rotate3dAnimation createAnim(float start, float end, boolean turnIn, boolean turnUp){
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
        rotation.setDuration(800);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }
    //这里返回的TextView，就是我们看到的View
    @Override
    public View makeView() {
         TextView text = new TextView(mContext);
        // TODO Auto-generated method stub
        text.setPadding(0, UIUtils.dip2px(mContext, 8), 0, 0);
        text.setGravity(Gravity.CENTER);

        text.setTextSize(mHeight);
        text.setMaxLines(1);
        text.setTextColor(mColor);
        return text;
    }

    //定义动作，向下滚动翻页
    public void previous(){
        if(getInAnimation() != mInDown){
            setInAnimation(mInDown);
        }
        if(getOutAnimation() != mOutDown){
            setOutAnimation(mOutDown);
        }
    }
    //定义动作，向上滚动翻页
    public void next(){
        if(getInAnimation() != mInUp){
            setInAnimation(mInUp);
        }
        if(getOutAnimation() != mOutUp){
            setOutAnimation(mOutUp);
        }
    }

    class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;
        public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }
        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight() / 2;
            mCenterX = getWidth() / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
            final float centerX = mCenterX ;
            final float centerY = mCenterY ;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1: -1;
            final Matrix matrix = t.getMatrix();
            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection *mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection *mCenterY * (interpolatedTime), 0.0f);
            }

            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}