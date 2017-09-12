package kr.co.dorandoran;

import android.app.Application;
import android.content.Context;

/**
 * Created by LeeHaNeul on 2016-09-07.
 */
public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext =this;
    }

    public static Context getMyContext() {
        return mContext;
    }
}