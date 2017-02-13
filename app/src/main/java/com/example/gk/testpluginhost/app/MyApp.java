package com.example.gk.testpluginhost.app;

import android.app.Application;
import android.content.Context;

import com.morgoo.droidplugin.PluginHelper;

/**
 * 项目名称：TestPluginHost
 * 类描述：
 * 创建人：gk
 * 创建时间：2017/2/13 11:13
 * 修改人：gk
 * 修改时间：2017/2/13 11:13
 * 修改备注：
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //这里必须在super.onCreate方法之后，顺序不能变
        PluginHelper.getInstance().applicationOnCreate(getBaseContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);
        super.attachBaseContext(base);
    }
}
