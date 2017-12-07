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

/**
 * Created by Administrator on 2017/12/7.
 */

public  class VerifyActivity extends Activity implements View.OnClickListener {
    private TextView mTextTitle;
    private TextView mTextCancel;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;

    String mima;
    private SharedPreferences mSharedPreferences = null;
    MyApplication s;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit1);
        mSharedPreferences = getSharedPreferences("serect_protect", Context.MODE_PRIVATE);
        //获取用户设置的密码
        mima = mSharedPreferences.getString("inputCode","");
        Log.e("TAG","==="+mima);
        setUpViews();
        setUpListeners();
        //初始化MyApplication
        s = (MyApplication) getApplication();
        s.addActivity_(VerifyActivity.this);//添加到集合
    }

    private void setUpListeners() {
        mTextCancel.setOnClickListener(this);
    }

    private void setUpViews() {
        mTextTitle = (TextView) findViewById(R.id.text_title1);
        mTextTitle.setText("验证指纹");
        mTextCancel = (TextView) findViewById(R.id.text_cancel1);

        mTextTip = (TextView) findViewById(R.id.text_tip1);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container1);
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
                //判断用户是否输入争取
                if (mima == inputCode||mima.equals(inputCode)) {
                    //正确则清除该页面返回主页面
                    finish();
                } else {
                    //失败则重新输入，可在此处添加错误次数操作
                    Log.e("TAG","+++++"+inputCode);

                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>输入错误请重新输入</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(VerifyActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);

                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
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
    }
    private boolean isInputPassValidate(String inputPassword) {

        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_cancel1:
                Log.e("ss","sssssss");
                s.removeALLActivity_();//用户点击取消则退出应用清除所有Activity
                break;
        }
    }
    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
     s.removeALLActivity_();//用户点击返回则退出应用清除所有Activity
//        System.out.println("按下了back键   onBackPressed()");
    }
}
