package com.poct.android.view;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.example.yf_a64_api.YF_A64_API_Manager;
import com.pantum.mobileprint.LoadJNI;
import com.poct.android.entity.Account;
import com.poct.android.entity.ScreenDefine;
import com.poct.android.entity.UpDataModel;
import com.poct.android.manager.BluetoothTaskManagerThread;
import com.poct.android.net.NetTaskManagerThread;
import com.poct.android.thread.UpdataDownloadTask;
import com.poct.android.utils.AppUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Author: pan Email:gdpancheng@gmail.com
 * Created Date:2013-7-23
 * Copyright @ 2013 BU
 * Description: 全局application
 *
 * History:
 */


public class PoctApplication extends Application {

	public static PoctApplication mApp;
	public static ScreenDefine mScreenDefine;
	public UpDataModel mUpDataModel;
	public boolean isActivity = false;
	public Account account = new Account();
	public UpdataDownloadTask mDownloadTask;
	public String json_Str	= "{\"AndroidSDKVersion\":\"" + "21" + "\", \"PlatformType\":\"" + "MPlatformPrint" + "\", \"PrintModelType\":\"" + "NET" + "\"}";
	public LoadJNI mPrintUtil = new LoadJNI();
	public YF_A64_API_Manager mYF_A64_API_Manager;
	public int day;
	public NetTaskManagerThread mNetTaskManagerThread = new NetTaskManagerThread();
	public BluetoothTaskManagerThread mBluetoothTaskManagerThread = new BluetoothTaskManagerThread();
	public void onCreate() {
		mApp = this;
		mYF_A64_API_Manager = new YF_A64_API_Manager(mApp);
		mYF_A64_API_Manager.yfsetNavigationBarVisibility(false);
		PackageManager packageManager = mApp.getPackageManager();
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					mApp.getPackageName(), 0);
			mUpDataModel = new UpDataModel(packInfo.versionCode,packInfo.versionName,"");
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		day = AppUtils.getday();
		boolean a = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
		mScreenDefine = new ScreenDefine(this.getApplicationContext());
		mNetTaskManagerThread.setStop(false);
		new Thread(mNetTaskManagerThread).start();
		mBluetoothTaskManagerThread.setStop(false);
		new Thread(mBluetoothTaskManagerThread).start();
		//最大分配内存
		super.onCreate();
	}

	public void installSlient() {
		String newAppPath = getNewAppPath();
		try {
			Runtime.getRuntime().exec("pm install "+ newAppPath+ File.separator + "doctest.apk");
		} catch (IOException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public String getNewAppPath() {
		try {
			String f = Environment.getExternalStorageDirectory().getCanonicalPath();
			return f;
		} catch (IOException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int installSlient (Context context , String filePath ) {
		File file = new File ( filePath ) ;
		if ( filePath == null || filePath . length ( ) == 0 || ( file = new File ( filePath ) ) ==null || file . length ( ) <= 0
				|| ! file . exists ( ) || ! file . isFile ( ) ) {
			return 1 ;
		}

		if(!file.exists())
		{
			return 1;
		}
		long size = file.length();
		String [ ] args = { "pm" , "install" , "-r" , filePath } ;
		ProcessBuilder processBuilder = new ProcessBuilder ( args ) ;

		Process process = null ;
		BufferedReader successResult = null ;
		BufferedReader errorResult = null ;
		StringBuilder successMsg = new StringBuilder ( ) ;
		StringBuilder errorMsg = new StringBuilder ( ) ;
		int result ;
		try {
			process = processBuilder . start ( ) ;
			successResult = new BufferedReader ( new InputStreamReader ( process . getInputStream ( )) ) ;
			errorResult = new BufferedReader ( new InputStreamReader( process . getErrorStream ( ) )) ;
			String s ;

			while ( ( s = successResult . readLine ( ) ) != null ) {
				successMsg . append ( s ) ;
			}

			while ( ( s = errorResult . readLine ( ) ) != null ) {
				errorMsg . append ( s ) ;
			}
		} catch ( IOException e ) {
			e . printStackTrace ( ) ;
			result = 2 ;
		} catch ( Exception e ) {
			e . printStackTrace ( ) ;
			result = 2 ;
		} finally {
			try {
				if ( successResult != null ) {
					successResult . close ( ) ;
				}
				if ( errorResult != null ) {
					errorResult . close ( ) ;
				}
			} catch ( IOException e ) {
				e . printStackTrace ( ) ;
			}
			if ( process != null ) {
				process . destroy ( ) ;
			}
		}

		// TODO should add memory is not enough here
		if ( successMsg . toString ( ) . contains ( "Success" ) || successMsg . toString ( ) .contains ( "success" ) ) {
			result = 0 ;
		} else {
			result = 2 ;
		}
//		Log . d ( "installSlient" , "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg ) ;
		return result ;
	}
	public static void installSlient ( String filePath ) {
		mApp.mYF_A64_API_Manager.yfslientinstallapk(filePath);
	}

	public static boolean startApp(String packageName, String startActivityName) {
		boolean isSuccess = false;
		String cmd = "am start -n " + packageName + "/" + startActivityName + " \n";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			int value = process.waitFor();
			return returnResult(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return isSuccess;
	}

	private static boolean returnResult(int value) {
		// 代表成功
		if (value == 0) {
			return true;
		} else if (value == 1) { // 失败
			return false;
		} else { // 未知情况
			return false;
		}
	}
}