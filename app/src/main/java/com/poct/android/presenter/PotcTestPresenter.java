package com.poct.android.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import com.poct.R;
import com.poct.android.asks.ReportAsks;
import com.poct.android.asks.TestReportAsks;
import com.poct.android.entity.Report;
import com.poct.android.entity.TempGet;
import com.poct.android.handler.PotcTestHandler;
import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.prase.DataPrase;
import com.poct.android.receiver.GattUpdateReceiver;
import com.poct.android.utils.AppUtils;
import com.poct.android.utils.DBHelper;
import com.poct.android.utils.ViewUtils;
import com.poct.android.view.activity.PotcTestActivity;
import com.poct.android.view.activity.ReportPrintActivity;
import com.poct.android.view.activity.TestCenterActivity;
import com.poct.android.view.adapter.DeviceReportAdapter;
import com.poct.android.view.adapter.PatientReportAdapter;

import java.util.ArrayList;

/**
 * Created by xpx on 2017/8/18.
 */

public class PotcTestPresenter implements Presenter {

    public PotcTestHandler mPotcTestHandler;
    public PotcTestActivity mPotcTestActivity;
    public GattUpdateReceiver mGattUpdateReceiver;

    public PotcTestPresenter(PotcTestActivity mPotcTestActivity)
    {
        this.mPotcTestActivity = mPotcTestActivity;
        this.mPotcTestHandler = new PotcTestHandler(mPotcTestActivity);
        this.mGattUpdateReceiver= new GattUpdateReceiver(mPotcTestHandler);
    }

    @Override
    public void Create() {
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothSetManager.ACTION_DEVICE_POTC_CONNECTED);
        intentFilter.addAction(BluetoothSetManager.ACTION_DEVICE_POTC_GET);
        intentFilter.addAction(BluetoothSetManager.ACTION_DEVICE_CHECK_POTC);
        intentFilter.addAction(BluetoothSetManager.ACTION_DEVICE_POTC_UNCONNECTED);
        mPotcTestActivity.registerReceiver(mGattUpdateReceiver,intentFilter);
    }

    @Override
    public void initView() {
        mPotcTestActivity.setContentView(R.layout.activity_potc_test);
        mPotcTestActivity.btnBack = mPotcTestActivity.findViewById(R.id.layerback);
        mPotcTestActivity.imfText = mPotcTestActivity.findViewById(R.id.title2);
        mPotcTestActivity.headTime = mPotcTestActivity.findViewById(R.id.headtime);
        mPotcTestActivity.btnGet = mPotcTestActivity.findViewById(R.id.btnget);
        mPotcTestActivity.connectState = mPotcTestActivity.findViewById(R.id.device_state);
        mPotcTestActivity.deviceList = mPotcTestActivity.findViewById(R.id.reportlist);
        mPotcTestActivity.userList = mPotcTestActivity.findViewById(R.id.reportlist2);
        mPotcTestActivity.buttomimf = mPotcTestActivity.findViewById(R.id.buttomimf);
        mPotcTestActivity.btnGive = mPotcTestActivity.findViewById(R.id.btnok);
        mPotcTestActivity.buttomimf2 = mPotcTestActivity.findViewById(R.id.buttomimf2);
        mPotcTestActivity.btnPrint = mPotcTestActivity.findViewById(R.id.btnprint);
//        mPotcTestActivity.mSeriesReport = DBHelper.getInstance(mPotcTestActivity).getSeriesReport(mPotcTestActivity.getIntent().getStringExtra(EquipMentManager.REPORT_ID));
        mPotcTestActivity.mSeriesReport = mPotcTestActivity.getIntent().getParcelableExtra("report");
        mPotcTestActivity.tempGets.addAll(DataPrase.getTempid(mPotcTestActivity.mSeriesReport.tempId,EquipMentManager.DEVICE_POTC,mPotcTestActivity.mSeriesReport,mPotcTestActivity.mReports));
        updataButtomImf();
        mPotcTestActivity.deviceReportAdapter = new DeviceReportAdapter(mPotcTestActivity,mPotcTestActivity.mReports,mPotcTestHandler);
        mPotcTestActivity.patientReportAdapter = new PatientReportAdapter(mPotcTestActivity,mPotcTestActivity.mSeriesReport.mReports,mPotcTestHandler,EquipMentManager.DEVICE_POTC);
        mPotcTestActivity.deviceList.setAdapter(mPotcTestActivity.deviceReportAdapter);
        mPotcTestActivity.userList.setAdapter(mPotcTestActivity.patientReportAdapter);
        mPotcTestActivity.imfText.setText("("+ mPotcTestActivity.mSeriesReport.mName+","+ mPotcTestActivity.mSeriesReport.mSex+","+
                mPotcTestActivity.mSeriesReport.mAge+"岁)");
        mPotcTestActivity.headTime.setText(mPotcTestActivity.mSeriesReport.mSendTime.substring(0,10));
        mPotcTestActivity.btnBack.setOnClickListener(mPotcTestActivity.doBacklistener);
        mPotcTestActivity.btnGet.setOnClickListener(mPotcTestActivity.doGetDeviceData);
        mPotcTestActivity.btnGive.setOnClickListener(mPotcTestActivity.doGaveData);
        mPotcTestActivity.connectState.setOnClickListener(mPotcTestActivity.doConnectDevice);
        mPotcTestActivity.deviceList.setOnItemClickListener(mPotcTestActivity.listDeviceListener);
        mPotcTestActivity.btnPrint.setOnClickListener(mPotcTestActivity.printListener);
        if(BluetoothSetManager.getInstance().blueToothAdapter.isEnabled() == true)
        {
            EquipMentManager.getInstance().connectPotc(mPotcTestActivity,mPotcTestActivity.connectState);
        }

    }

    @Override
    public void Start() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Destroy() {
//        EquipMentManager.getInstance().disConnectPotc();
        mPotcTestActivity.unregisterReceiver(mGattUpdateReceiver);
    }

    public void getDeviceData()
    {
        if(mPotcTestActivity.connectState.getText().toString().equals(mPotcTestActivity.getString(R.string.device_state_connected)))
        {
            String getid = "";
            for(int i = 0 ; i < mPotcTestActivity.tempGets.size() ; i++)
            {
                if(mPotcTestActivity.tempGets.get(i).isGet == false)
                {
                    getid = mPotcTestActivity.tempGets.get(i).mTempid;
                    mPotcTestActivity.mTempGet = mPotcTestActivity.tempGets.get(i);
                    break;
                }
            }
            if(getid.length() != 0 && mPotcTestActivity.isGetting == false)
            {
                mPotcTestActivity.waitDialog.show();
                mPotcTestActivity.isGetting = true;
                getPotcData();
//                TestReportAsks.getReportCount(mPotcTestActivity,mPotcTestHandler, AppUtils.getDate());
            }
            else
            {
                ViewUtils.showMessage(mPotcTestActivity,mPotcTestActivity.getString(R.string.qtest_title_device_nodata));
            }

        }
        else
        {
            ViewUtils.showMessage(mPotcTestActivity,mPotcTestActivity.getString(R.string.qtest_title_device_unconnect));
        }

    }

    public void getPotcData()
    {
        boolean has = false;
        ArrayList<TempGet> tempGets = new ArrayList<TempGet>();
        for(int i = 0 ; i < mPotcTestActivity.tempGets.size() ; i++)
        {
            if(mPotcTestActivity.tempGets.get(i).isGet == false)
            {
                tempGets.add(mPotcTestActivity.tempGets.get(i));
                has = true;
            }
        }

        if(has == false)
        {
            mPotcTestActivity.isGetting = false;
            mPotcTestActivity.waitDialog.hide();
            ViewUtils.showMessage(mPotcTestActivity,mPotcTestActivity.getString(R.string.qtest_title_device_nodata));
        }
        else
        {
            mPotcTestActivity.waitDialog.show();
            TestReportAsks.getReportData(mPotcTestActivity,mPotcTestHandler, AppUtils.getDate(),tempGets,1);
        }
    }

    public DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener()
    {

        @Override
        public void onDismiss(DialogInterface dialog) {
            mPotcTestActivity.isGetting = false;
        }
    };

    public void updataButtomImf()
    {
        String ids = "";
        String ids2 = "";
        for(int i = 0 ;i <mPotcTestActivity.tempGets.size() ; i++)
        {
            if(mPotcTestActivity.tempGets.get(i).isGet == false)
            {
                boolean has = false;

                for(int j = 0 ; j < mPotcTestActivity.mReports.size() ; j++)
                {
                    if(mPotcTestActivity.mReports.get(j).tempId.equals(mPotcTestActivity.tempGets.get(i).mTempid))
                    {
                        has = true;
                        break;
                    }
                }
                if(has == false)
                {
                    if(ids.length() == 0)
                    {
                        ids +=  mPotcTestActivity.tempGets.get(i).mTempid;
                    }
                    else
                    {
                        ids += ","+mPotcTestActivity.tempGets.get(i).mTempid;
                    }
                    mPotcTestActivity.lastcount = Integer.valueOf(mPotcTestActivity.tempGets.get(i).mTempid);
                }
            }
            boolean has2 = false;
            int count = mPotcTestActivity.tempGets.get(i).realcount;
            for(int k = 0 ; k < mPotcTestActivity.mSeriesReport.mReports.size() ; k++)
            {
                if(mPotcTestActivity.mSeriesReport.mReports.get(k).tempId.equals(mPotcTestActivity.tempGets.get(i).mTempid) && mPotcTestActivity.mSeriesReport.mReports.get(k).type.equals(EquipMentManager.DEVICE_POTC))
                {
                    if(count >  0)
                    {
                        count--;
                    }
                    if(count == 0)
                    {
                        has2 = true;
                        break;
                    }
                }
            }
            if(has2 == false)
            {
                if(ids2.length() == 0)
                {
                    ids2 +=  mPotcTestActivity.tempGets.get(i).mTempid;
                }
                else
                {
                    ids2 += ","+mPotcTestActivity.tempGets.get(i).mTempid;
                }
            }
        }
        mPotcTestActivity.buttomimf.setText(mPotcTestActivity.getString(R.string.qtest_buttom_imf_fada_get)+":"+ ids);
        mPotcTestActivity.buttomimf2.setText(mPotcTestActivity.getString(R.string.qtest_buttom_imf_fada_give)+":"+ids2);
    }

    public void doGaveData()
    {
        for(int i = 0 ; i < mPotcTestActivity.mReports.size() ; i++)
        {
            Report report = mPotcTestActivity.mReports.get(i);
            if(report.isSelect)
            {
                mPotcTestActivity.mReports.remove(report);
                report.isSelect = false;
                i--;
                mPotcTestActivity.mSeriesReport.mReports.add(report);
            }
        }

        measureTempid();
        updataButtomImf();
        mPotcTestActivity.deviceReportAdapter.notifyDataSetChanged();
        mPotcTestActivity.patientReportAdapter.notifyDataSetChanged();
        DBHelper.getInstance(mPotcTestActivity).updataReport(mPotcTestActivity.mSeriesReport);
        Intent intent = new Intent(TestCenterActivity.ACTION_UPDETE_REPORT_LIST);
        mPotcTestActivity.mSeriesReport.updatacount = 0;
        if(AppUtils.gotoMain(mPotcTestActivity) == false)
        {
            ReportAsks.reportAdd(mPotcTestActivity.mSeriesReport,mPotcTestHandler,mPotcTestActivity);
        }
        intent.putExtra(EquipMentManager.REPORT_ID,mPotcTestActivity.mSeriesReport.mReportId);
        mPotcTestActivity.sendBroadcast(intent);

    }

    public void measureTempid()
    {
        mPotcTestActivity.tempGets.clear();
        mPotcTestActivity.tempGets.addAll(DataPrase.getTempid(mPotcTestActivity.mSeriesReport.tempId,EquipMentManager.DEVICE_POTC,mPotcTestActivity.mSeriesReport,mPotcTestActivity.mReports));
    }

    public void doSelect(Report report) {
        if(report.isSelect == false)
        {
            report.isSelect = true;
        }
        else
        {
            report.isSelect = false;
        }
        mPotcTestActivity.deviceReportAdapter.notifyDataSetChanged();
    };

    public void doDelete(Report report) {
        mPotcTestActivity.mSeriesReport.mReports.remove(report);
        mPotcTestActivity.mReports.add(report);
        measureTempid();
        updataButtomImf();
        mPotcTestActivity.deviceReportAdapter.notifyDataSetChanged();
        mPotcTestActivity.patientReportAdapter.notifyDataSetChanged();
        DBHelper.getInstance(mPotcTestActivity).updataReport(mPotcTestActivity.mSeriesReport);
        Intent intent = new Intent(TestCenterActivity.ACTION_UPDETE_REPORT_LIST);
        mPotcTestActivity.mSeriesReport.updatacount = 0;
        if(AppUtils.gotoMain(mPotcTestActivity) == false)
        {
            ReportAsks.reportAdd(mPotcTestActivity.mSeriesReport,mPotcTestHandler,mPotcTestActivity);
        }
        intent.putExtra(EquipMentManager.REPORT_ID,mPotcTestActivity.mSeriesReport.mReportId);
        mPotcTestActivity.sendBroadcast(intent);

    }

    public void doConnectDevice()
    {
        if(mPotcTestActivity.connectState.getText().toString().equals(mPotcTestActivity.getString(R.string.device_state_disconnected)))
        {
            if(BluetoothSetManager.getInstance().blueToothAdapter.isEnabled() == false)
            {
                ViewUtils.showMessage(mPotcTestActivity,mPotcTestActivity.getString(R.string.device_bluetooth_error));
            }
            else
            {
                EquipMentManager.getInstance().connectPotc(mPotcTestActivity,mPotcTestActivity.connectState);
            }

        }
    }

    public void startPrint()
    {
        if(mPotcTestActivity.mSeriesReport.mReports.size()>0)
        ReportAsks.reportAdd(mPotcTestActivity.mSeriesReport,mPotcTestHandler,mPotcTestActivity);
        Intent intent = new Intent(mPotcTestActivity, ReportPrintActivity.class);
        intent.putExtra("report",mPotcTestActivity.mSeriesReport);
//        intent.putExtra(EquipMentManager.REPORT_ID,DataPrase.praseReportJson(mPotcTestActivity.mSeriesReport));
        mPotcTestActivity.startActivity(intent);
    }

}
