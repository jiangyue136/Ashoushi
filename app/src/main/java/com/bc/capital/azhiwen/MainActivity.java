package com.bc.capital.azhiwen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private ToggleButton toggle_more,toggle_fingerprint;
    Context context;
    private SharedPreferences sp;
    SharedPreferences mSharedPreferences;
    boolean isOpen;
    MyApplication s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        //初始化MyApplication
        s = (MyApplication) getApplication();
        //将Activity添加至集合中。用处，应用进入时如果用户已经开启手势密码则会进入验证页，在验证页中，如果用户不输入返回则会关闭所有Activity
        s.addActivity_(MainActivity.this);
        //打开手势密码按钮
        toggle_more = (ToggleButton) findViewById(R.id.toggle_more);
        //数据库初始化
        sp = this.getSharedPreferences("serect_protect", Context.MODE_PRIVATE);
        mSharedPreferences = getSharedPreferences("serect_protect", Context.MODE_PRIVATE);
        //判断手势密码是否打开,如果打开则设置按钮，并进入验证页
        isOpen = mSharedPreferences.getBoolean("isOpen", false);
        toggle_more.setChecked(isOpen);
        if (isOpen){
            Intent intent = new Intent(MainActivity.this,VerifyActivity.class);
            startActivity(intent);
        }
        SetGesturePasssword();
    }
    //按钮的监听事件
    private void SetGesturePasssword() {
        toggle_more.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(context,"开启手势密码",Toast.LENGTH_LONG).show();
                    sp.edit().putBoolean("isOpen", true).commit();
                    String inputCode = sp.getString("inputCode", "");

                    if (TextUtils.isEmpty(inputCode)) {//表示之前没有设置过
                        new AlertDialog.Builder(context)
                                .setTitle("设置手势密码")
                                .setMessage("是否现在设置手势密码")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context,"现在设置手势密码",Toast.LENGTH_LONG).show();
                                        sp.edit().putBoolean("isOpen", true).commit();
                                        // toggleMore.setChecked(true);
                                        //开启一个新的Activvity，进入设置手势页：
                                        Intent i1 = new Intent(MainActivity.this,GestureEditActivity.class);
                                        startActivity(i1);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context,"取消了现在设置手势密码",Toast.LENGTH_LONG).show();
                                sp.edit().putBoolean("isOpen", false).commit();
                                toggle_more.setChecked(false);
                            }
                        }).show();
                    } else {
                        Toast.makeText(context,"开启手势",Toast.LENGTH_LONG).show();
                        sp.edit().putBoolean("isOpen", true).commit();
                        //     toggleMore.setChecked(true);
                    }
                } else {
                    //关闭手势密码，将密码设置为null，按钮恢复false
                    Toast.makeText(context,"关闭手势密码",Toast.LENGTH_LONG).show();
                    sp.edit().putString("inputCode",null).commit();
                    sp.edit().putBoolean("isOpen", false).commit();

                }
            }
        });
    }
    //从其他页返回此页面时重新判断并社会按钮
    @Override
    protected void onRestart() {
        super.onRestart();
        boolean isOpen = mSharedPreferences.getBoolean("isOpen", false);
        toggle_more.setChecked(isOpen);
    }
}
