package com.bc.capital.azhiwen;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
public class MyApplication extends Application {

    /**
     * 管理activity
     */
    private List<Activity> oList;//用于存放所有启动的Activity的集合
    public  static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        oList = new ArrayList<Activity>();
    }

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
// 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(Activity activity) {
//判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }
}

////    <创建BaseActivity 继承 Activity> 用于管理所有的Activity，所有的Activity都继承这个类
//public class BaseActivity extends Activity {
//    private MyApplication application;
//    private BaseActivity oContext;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (application == null) {
//            // 得到Application对象
//            application = (MyApplication) getApplication();
//        }
//        oContext = this;// 把当前的上下文对象赋值给BaseActivity
//        addActivity();// 调用添加方法
//    }
//}
