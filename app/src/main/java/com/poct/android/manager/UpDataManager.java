package com.poct.android.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.poct.R;
import com.poct.android.entity.UpDataModel;
import com.poct.android.handler.LoginHandler;
import com.poct.android.handler.SettingHandler;
import com.poct.android.net.NetTaskManager;
import com.poct.android.net.nettask.NetTask;
import com.poct.android.thread.AddApkThread;
import com.poct.android.thread.UpdataDownloadTask;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.PoctApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class UpDataManager {

    public final static String CHECK_VERSION_URL = "http://yimi.intersky.com.cn/static/yimi.txt";
    public final static String UPDATA_APP_URL = "http://yimi.intersky.com.cn/static/yimi.apk";
    public static final String UPDATE_INFO = "update_info";
    public static final String UPDATE_VNAME = "update_vname";
    public static final String UPDATE_VCODE = "update_vcode";
    public static final int UPDATA_DOWNLOAD = 1;
    public static final int UPDATA_FINISH = 2;
    public static final int UPDATA_NONE = 3;
    public static boolean isInstall = false;
    public static String version = "";
    public static boolean installdialog = false;
    public static void checkVersin(Context mContext, Handler mHandel) {

        NetTask mNetTask = new NetTask(UpDataManager.CHECK_VERSION_URL, mHandel, LoginHandler.EVENT_CHECK_UPDATA, LoginHandler.EVENT_CHECK_UPDATA_FAIL,
                mContext);
        NetTaskManager.getInstance().addNetTask(mNetTask);

    }

    public static void checkVersin2(Context mContext, Handler mHandel) {

        NetTask mNetTask = new NetTask(UpDataManager.CHECK_VERSION_URL, mHandel, SettingHandler.STSETM_UPDATE_CHECK, SettingHandler.STSETM_UPDATE_CHECK_FAIL,
                mContext);
        NetTaskManager.getInstance().addNetTask(mNetTask);

    }

    public static int initJson(String json, Context mContext,Handler handler) {
        try {
            JSONObject jObject = new JSONObject(json);
            UpDataModel mUpDataModel = new UpDataModel(jObject.getInt("versioncode")
                    , jObject.getString("version"), jObject.getString("msg"));
            version = mUpDataModel.mVersionName;
            if (mUpDataModel.mVersionCode > PoctApplication.mApp.mUpDataModel.mVersionCode) {

                if(checkDownloadApkImf(mContext,mUpDataModel.mVersionName,mUpDataModel.mVersionCode))
                {
                    doUpDataAppView(mContext,mUpDataModel.mVersionName,handler);
                    return UPDATA_FINISH;
                }
                else
                {
                    if (canDownloadState(mContext))
                        doUpDataApp(mContext, mUpDataModel,handler);
                    return UPDATA_DOWNLOAD;
                }

            }
            return UPDATA_NONE;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return UPDATA_NONE;
        }
    }

    private static boolean canDownloadState(Context ctx) {
        try {
            int state = ctx.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void doUpDataApp(final Context mContext, UpDataModel mUpDataModel, final Handler mHandler) {

        if(PoctApplication.mApp.mDownloadTask == null)
        {
            PoctApplication.mApp.mDownloadTask = new UpdataDownloadTask(mUpDataModel.mVersionName,mUpDataModel.mVersionCode);
            saveDownloadApkImf(mContext,mUpDataModel.mVersionName,0);
            PoctApplication.mApp.mDownloadTask.start();
        }
    }

    public static boolean checkDownloadApkImf(Context mContext,String versionname,int versioncode)
    {
        SharedPreferences sharedPre = mContext.getSharedPreferences(UPDATE_INFO, 0);
        int vc = sharedPre.getInt(UPDATE_VCODE,0);
        String vn = sharedPre.getString(UPDATE_VNAME,"");
        if(versioncode == vc)
        {
            if(versionname.equals(vn))
            {
                File file = new File(PoctApplication.mApp.getNewAppPath()+"/doctest.apk");
                if(file.exists())
                return true;
                else
                    return false;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public static void saveDownloadApkImf(Context mContext,String versionname,int versioncode)
    {
        SharedPreferences sharedPre = mContext.getSharedPreferences(UPDATE_INFO, 0);
        SharedPreferences.Editor e = sharedPre.edit();
        e.putString(UPDATE_VNAME,versionname);
        e.putInt(UPDATE_VCODE,versioncode);
        e.commit();
    }

    public static void doUpDataAppView(final Context mContext, String versionname, final Handler mHandler) {
        if(installdialog == false) {
            installdialog = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext,5);
            builder.setMessage(PoctApplication.mApp.getString(R.string.keyword_version)+versionname+"\r\n");
            builder.setTitle(PoctApplication.mApp.getString(R.string.keyword_newversion));
            builder.setNegativeButton(PoctApplication.mApp.getString(R.string.keyword_updata), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    if(UpDataManager.isInstall == false)
                    {
                        AddApkThread mAddApkThread = new AddApkThread(mContext, PoctApplication.mApp.getNewAppPath()+"/doctest.apk",mHandler);
                        mAddApkThread.start();
                    }
                    else
                    {
                        ViewUtils.showMessage(mContext,mContext.getString(R.string.setting_title_system_install_already));
                    }
                    installdialog = false;
                }
            });
            builder.setPositiveButton(PoctApplication.mApp.getString(R.string.keyword_updata_later), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    installdialog = false;
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    installdialog = false;
                }
            });
            builder.create().show();
        }
    }
}
