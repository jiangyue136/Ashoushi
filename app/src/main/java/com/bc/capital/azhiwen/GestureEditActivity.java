package com.bc.capital.azhiwen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;




public class GestureEditActivity extends Activity implements View.OnClickListener {
    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    /**
     * 首次提示绘制手势密码，可以选择跳过
     */
    public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
    private TextView mTextTitle;
    private TextView mTextCancel;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextReset;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private SharedPreferences mSharedPreferences = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);
        setUpViews();
        setUpListeners();
    }

    private void setUpViews() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
        mTextReset = (TextView) findViewById(R.id.text_reset);
        mTextReset.setClickable(false);
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mSharedPreferences = this.getSharedPreferences("serect_protect", Context.MODE_PRIVATE);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                //判断必须链接4个以上
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>"));
                    //立刻还原，让用户重新输入
                    mGestureContentView.clearDrawlineState(0L);

                    return;
                }
                //第一次输入密码保存，并刷新，再次确认密码
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
                    //点击重新绘制密码
                    mTextReset.setClickable(true);
                    mTextReset.setText(getString(R.string.reset_gesture_code));
                    mTextTip.setText("请确认手势密码");
                } else {
                    //判断两次相同设置成功，否则重新设置
                    if (inputCode.equals(mFirstPassword)) {
                        mFirstPassword = inputCode;

                        updateCodeList(inputCode);
                        Toast.makeText(GestureEditActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        mGestureContentView.clearDrawlineState(0L);
                        //设置成功关闭
                        GestureEditActivity.this.finish();
                    } else {
                        mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>"));

                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);

                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }

                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });

        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);

        updateCodeList("");
    }

    private void setUpListeners() {
        mTextCancel.setOnClickListener(this);
        mTextReset.setOnClickListener(this);
    }

    private void updateCodeList(String inputCode) {

        // 在上方小图标更新选择的图案
        mLockIndicator.setPath(inputCode);
        mSharedPreferences.edit().putString("inputCode",inputCode).commit();
        Log.e("TAG", "inputCode = " + inputCode);
    }
    //监听返回键
    @Override
    public void onBackPressed() {
        //如果未设置完返回键则设置按钮false，并将密码清空
        this.finish();
//                mTextReset.setClickable(false);
        mSharedPreferences.edit().putBoolean("isOpen", false).commit();
        updateCodeList("");
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.text_cancel:
                //如果未设置完返回键则设置按钮false，并将密码清空
                this.finish();
                mSharedPreferences.edit().putBoolean("isOpen", false).commit();
                updateCodeList("");
                break;

            case R.id.text_reset:
                //点击重新设置手势密码
                mIsFirstInput = true;
                updateCodeList("");
                mTextTip.setText(getString(R.string.set_gesture_pattern));
                break;


            default:
                break;
        }
    }
    //判断密码长度
    private boolean isInputPassValidate(String inputPassword) {

        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }

        return true;
    }
}
