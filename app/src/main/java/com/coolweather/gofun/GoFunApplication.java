package com.coolweather.gofun;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.coolweather.gofun.LocalDb.SqliteUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class GoFunApplication  extends Application {
    public static Context applicationContext;
    private List<Activity> activityList = new LinkedList();//退出应用集合
    private List<Activity> temporaryActivityList = new LinkedList();//临时销毁Activity集合
    private static GoFunApplication instance;
    public static String token = "0";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化全局上下文
        applicationContext=this;
        SqliteUtil sqliteUtil = new SqliteUtil(getContext());
        if(sqliteUtil.getToken("token")!=null){
            token = sqliteUtil.getToken("token");
            Log.d("TOKENq",token);
        }else{

        }
    }

    public  static Context getGlobalApplication() {
        return applicationContext;
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GoFunApplication getApplication() {
        return instance;
    }

    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return applicationContext;
    }

    public static String getToken(){
        return token;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void clearActivity() {
        activityList.clear();
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    //临时添加Activity到容器中
    public void temporaryAddActivity(Activity activity) {
        temporaryActivityList.add(activity);
    }

    //临时遍历所有Activity并finish
    public void temporaryExit() {
        for (Activity activity : temporaryActivityList) {
            activity.finish();
        }
    }
}
