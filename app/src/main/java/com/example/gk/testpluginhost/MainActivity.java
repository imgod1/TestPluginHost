package com.example.gk.testpluginhost;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.morgoo.droidplugin.pm.PluginManager;
import com.morgoo.helper.compat.PackageManagerCompat;

import java.io.File;
import java.util.List;

/**
 *测试插件化
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txt_title;
    private Button btn_check;
    private Button btn_install;
    private Button btn_uninstall;
    private Button btn_open;
    public static final String PLUGIN_PACKAGE_NAME = "com.kk.imgod.testcustomview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        btn_check = (Button) findViewById(R.id.btn_check);
        btn_install = (Button) findViewById(R.id.btn_install);
        btn_uninstall = (Button) findViewById(R.id.btn_uninstall);
        btn_open = (Button) findViewById(R.id.btn_open);
        btn_check.setOnClickListener(this);
        btn_install.setOnClickListener(this);
        btn_uninstall.setOnClickListener(this);
        btn_open.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check:
                check();
                break;
            case R.id.btn_install:
                install();
                break;
            case R.id.btn_uninstall:
                unInstall();
                break;
            case R.id.btn_open:
                openPlugin();
                break;
            default:
                break;
        }
    }

    /**
     * 检查是否已经安装了插件
     */
    private void check() {
        Intent intent = getLaunchehIntent();
        //通过这种方法来判断插件是不是安装不准确,毕竟如果插件是安装在手机里面的话也不为空
        //精准的方法是通过下面PluginManager里面的方法,然后再判断
        if (null != intent) {
            Toast.makeText(getApplicationContext(), "当前插件已经安装", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "当前插件还未安装", Toast.LENGTH_SHORT).show();
        }

        try {
            List<ApplicationInfo> appList = PluginManager.getInstance().getInstalledApplications(0);
            if (null != appList) {
                Log.e("test", "installed app:" + appList.size());
                for (ApplicationInfo applicationInfo : appList) {
                    Log.e("test_app", applicationInfo.packageName);
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            List<PackageInfo> packageList = PluginManager.getInstance().getInstalledPackages(0);
            if (null != packageList) {
                Log.e("test", "installed package:" + packageList.size());
                for (PackageInfo packageInfo : packageList) {
                    Log.e("test_package", packageInfo.packageName);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装插件
     */
    private void install() {
        //获取插件
        File file = new File(Environment.getExternalStorageDirectory(), "/apppp/app/test_plugin.apk");
        //没有插件
        if (file.exists()) {
            Log.e("test", "插件文件存在");
            int installResult = 0;
            try {
                installResult = PluginManager.getInstance().installPackage(file.getAbsolutePath(), PackageManagerCompat.INSTALL_REPLACE_EXISTING);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e("test", "插件文件安装失败");
            }
            Log.e("test", "插件文件安装成功:installResult:" + installResult);
            if (installResult == PackageManagerCompat.INSTALL_SUCCEEDED) {
                Toast.makeText(getApplicationContext(), "插件安装成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "插件安装失败:" + installResult, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("test", "插件文件不存在");
        }
    }

    /**
     * 卸载插件
     */
    private void unInstall() {
        //获取插件
        //没有插件
        try {
            PluginManager.getInstance().deletePackage(PLUGIN_PACKAGE_NAME, 0);
            Toast.makeText(getApplicationContext(), "插件卸载成功", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "插件卸载失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开插件
     */
    private void openPlugin() {
        Intent intent = getLaunchehIntent();
        if (null != intent) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//经测试 不用这个flag也行
            startActivity(intent);
            Log.e("test", "插件已经启动");
        } else {
            Toast.makeText(getApplicationContext(), "插件还未安装", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 拿到Intent
     *
     * @return
     */
    private Intent getLaunchehIntent() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(PLUGIN_PACKAGE_NAME);
        return intent;
    }
}
