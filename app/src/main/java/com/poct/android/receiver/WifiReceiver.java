package com.poct.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.poct.android.handler.SettingHandler;
import com.poct.android.view.activity.LoginActivity;
import com.poct.android.view.activity.SettingActivity;

public class WifiReceiver extends BroadcastReceiver {
    public Handler handler;
    public WifiReceiver(Handler handler)
    {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())){
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (state){
                /**
                 * WIFI_STATE_DISABLED    WLAN已经关闭
                 * WIFI_STATE_DISABLING   WLAN正在关闭
                 * WIFI_STATE_ENABLED     WLAN已经打开
                 * WIFI_STATE_ENABLING    WLAN正在打开
                 * WIFI_STATE_UNKNOWN     未知
                 */
                case WifiManager.WIFI_STATE_DISABLED:{
                    handler.sendEmptyMessage(SettingHandler.WIFI_CLOSE);
                    break;
                }
                case WifiManager.WIFI_STATE_DISABLING:{
                    break;
                }
                case WifiManager.WIFI_STATE_ENABLED:{
                    handler.sendEmptyMessage(SettingHandler.WIFI_OPEN);
                    break;
                }
                case WifiManager.WIFI_STATE_ENABLING:{
                    break;
                }
                case WifiManager.WIFI_STATE_UNKNOWN:{
                    handler.sendEmptyMessage(SettingHandler.WIFI_CLOSE);
                    break;
                }
            }
        }else if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(NetworkInfo.State.DISCONNECTED == info.getState()){//wifi没连接上
                handler.sendEmptyMessage(SettingHandler.WIFI_CONNECT_FAIL);
            }else if(NetworkInfo.State.CONNECTED == info.getState()){//wifi连接上了
                handler.sendEmptyMessage(SettingHandler.WIFI_CONNECT_SUCCESS);
            }else if(NetworkInfo.State.CONNECTING == info.getState()){//正在连接
                handler.sendEmptyMessage(SettingHandler.WIFI_CONNECT_ING);
            }
        }else if(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())){
            handler.sendEmptyMessage(SettingHandler.WIFI_UPDATA);
        }
        else if(SettingActivity.UPDTAT_PROGRESS.equals(intent.getAction())){
            Message msg = new Message();
            msg.obj = intent;
            msg.what = SettingHandler.PROGRESS_UPDATA;
            handler.sendMessage(msg);
        }
        else if(LoginActivity.ACTION_SYSTEM_UPDATA.equals(intent.getAction())){
            handler.sendEmptyMessage(SettingHandler.PROGRESS_UPDATA_FINISH);
        }
    }

}

