package com.poct.android.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.poct.android.entity.EquipmentItem;
import com.poct.android.entity.PageSeraisReport;
import com.poct.android.entity.SeriesReport;
import com.poct.android.manager.EquipMentManager;
import com.poct.android.prase.DataPrase;
import com.poct.android.view.PoctApplication;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "poct.db";
    public static final int DB_VERSION = 25;

    private static final String TABLE_BLUEDEVICE = "TABLE_BLUEDEVICE";
    public static final String BLUEDEVICE_NAME = "BLUEDEVICE_NAME";
    public static final String BLUEDEVICE_ID = "BLUEDEVICE_ID";
    public static final String BLUEDEVICE_ADDRESS = "BLUEDEVICE_ADDRESS";
    public static final String BLUEDEVICE_TYPE = "BLUEDEVICE_TYPE";

    private static final String TABLE_TEST_REPORT = "TABLE_TEST_REPORT";
    private static final String TEST_REPORT_ID = "TEST_REPORT_ID";
    private static final String TEST_REPORT_PID = "TEST_REPORT_PID";
    private static final String TEST_REPORT_AID = "TEST_REPORT_AID";
    private static final String TEST_REPORT_NAME = "TEST_REPORT_NAME";
    private static final String TEST_REPORT_SEX = "TEST_REPORT_SEX";
    private static final String TEST_REPORT_AGE = "TEST_REPORT_AGE";
    private static final String TEST_REPORT_SENDER = "TEST_REPORT_SENDER";
    private static final String TEST_REPORT_CNUM = "TEST_REPORT_CNUM";
    private static final String TEST_REPORT_HNUM = "TEST_REPORT_HNUM";
    private static final String TEST_REPORT_DEPART = "TEST_REPORT_DEPART";
    private static final String TEST_REPORT_BAD = "TEST_REPORT_BAD";
    private static final String TEST_REPORT_DES = "TEST_REPORT_DES";
    private static final String TEST_REPORT_MEMO = "TEST_REPORT_MEMO";
    private static final String TEST_REPORT_TIME = "TEST_REPORT_TIME";
    private static final String TEST_REPORT_STIME = "TEST_REPORT_STIME";
    private static final String TEST_REPORT_ISUPDATE = "TEST_REPORT_ISUPDATE";
    private static final String TEST_REPORT_ISAPPROVE = "TEST_REPORT_ISAPPROVE";
    private static final String TEST_REPORT_APPROVE = "TEST_REPORT_APPROVE";
    private static final String TEST_REPORT_APPROVE_TIME = "TEST_REPORT_APPROVE_TIME";
    private static final String TEST_REPORT_TEMPIDS = "TEST_REPORT_TEMPIDS";
    private static final String TEST_REPORT_JSON = "TEST_REPORT_JSON";

    private static final String TABLE_TEMPID = "TABLE_TEMPID";
    private static final String TEMPID_RECORDID = "TEMPID_RECORDID";
    private static final String TEMPID_ID = "TEMPID_ID";

    private static DBHelper mDBHelper;
    private SQLiteDatabase db = null;

    public static DBHelper getInstance(Context context) {
        if (null == mDBHelper) {

            mDBHelper = new DBHelper(context);
        }
        return mDBHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        openDB();
    }


    private void openDB() {
        if (null == db || !db.isOpen()) {
            db = this.getWritableDatabase();
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLUEDEVICE);
//            String sql = "CREATE TABLE " + TABLE_BLUEDEVICE + " (" +  BLUEDEVICE_TYPE + " TEXT PRIMARY KEY," + BLUEDEVICE_ADDRESS + " TEXT,"
//                    +BLUEDEVICE_ID + " TEXT," + BLUEDEVICE_NAME + " TEXT)";
//            db.execSQL(sql);
        }
    }

//    private void closeDB() {
//        if (null != db ) {
//            if(db.isOpen())
//            db.close();
//        }
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLUEDEVICE);
        String sql = "CREATE TABLE " + TABLE_BLUEDEVICE + " (" +  BLUEDEVICE_TYPE + " TEXT PRIMARY KEY," + BLUEDEVICE_ADDRESS + " TEXT,"
                +BLUEDEVICE_ID + " TEXT," + BLUEDEVICE_NAME + " TEXT)";
        db.execSQL(sql);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_REPORT);
        sql = "CREATE TABLE " + TABLE_TEST_REPORT + " (" +  TEST_REPORT_ID + " TEXT PRIMARY KEY," + TEST_REPORT_PID + " TEXT,"
                + TEST_REPORT_AID + " TEXT,"+ TEST_REPORT_NAME + " TEXT,"+ TEST_REPORT_SEX + " TEXT,"+ TEST_REPORT_AGE + " TEXT,"
                + TEST_REPORT_SENDER + " TEXT,"+ TEST_REPORT_CNUM + " TEXT,"+ TEST_REPORT_HNUM + " TEXT,"+ TEST_REPORT_DEPART + " TEXT,"
                + TEST_REPORT_BAD + " TEXT,"+ TEST_REPORT_DES + " TEXT,"+ TEST_REPORT_MEMO + " TEXT,"+ TEST_REPORT_TIME + " TEXT,"
                + TEST_REPORT_STIME + " TEXT,"+ TEST_REPORT_ISUPDATE + " TEXT,"+ TEST_REPORT_ISAPPROVE + " TEXT,"+ TEST_REPORT_APPROVE + " TEXT,"
                + TEST_REPORT_APPROVE_TIME + " TEXT,"+ TEST_REPORT_TEMPIDS + " TEXT,"+ TEST_REPORT_JSON + " TEXT)";
        db.execSQL(sql);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPID);
        sql = "CREATE TABLE " + TABLE_TEMPID + " (" +  TEMPID_RECORDID + " TEXT PRIMARY KEY,"+ TEMPID_ID + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(PoctApplication.mApp.mUpDataModel.mVersionCode < 18)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPID);
            String sql = "CREATE TABLE " + TABLE_TEMPID + " (" +  TEMPID_RECORDID + " TEXT PRIMARY KEY,"+ TEMPID_ID + " TEXT)";
            db.execSQL(sql);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_REPORT);
            sql = "CREATE TABLE " + TABLE_TEST_REPORT + " (" +  TEST_REPORT_ID + " TEXT PRIMARY KEY," + TEST_REPORT_PID + " TEXT,"
                    + TEST_REPORT_AID + " TEXT,"+ TEST_REPORT_NAME + " TEXT,"+ TEST_REPORT_SEX + " TEXT,"+ TEST_REPORT_AGE + " TEXT,"
                    + TEST_REPORT_SENDER + " TEXT,"+ TEST_REPORT_CNUM + " TEXT,"+ TEST_REPORT_HNUM + " TEXT,"+ TEST_REPORT_DEPART + " TEXT,"
                    + TEST_REPORT_BAD + " TEXT,"+ TEST_REPORT_DES + " TEXT,"+ TEST_REPORT_MEMO + " TEXT,"+ TEST_REPORT_TIME + " TEXT,"
                    + TEST_REPORT_STIME + " TEXT,"+ TEST_REPORT_ISUPDATE + " TEXT,"+ TEST_REPORT_ISAPPROVE + " TEXT,"+ TEST_REPORT_APPROVE + " TEXT,"
                    + TEST_REPORT_APPROVE_TIME + " TEXT,"+ TEST_REPORT_TEMPIDS + " TEXT,"+ TEST_REPORT_JSON + " TEXT)";
            db.execSQL(sql);
        }
    }


    public ArrayList<EquipmentItem> scanBlueToothItems() {
        ArrayList<EquipmentItem> equipmentItems = new ArrayList<EquipmentItem>();
        String sql = "SELECT * FROM " + TABLE_BLUEDEVICE;
        Cursor c = db.rawQuery(sql, new String[]
                {});
        while (c.moveToNext()) {
            EquipmentItem info = new EquipmentItem();
            info.mName = c.getString(c.getColumnIndex(BLUEDEVICE_NAME));
            info.mAddress = c.getString(c.getColumnIndex(BLUEDEVICE_ADDRESS));
            info.id = c.getString(c.getColumnIndex(BLUEDEVICE_ID));
            info.type = c.getString(c.getColumnIndex(BLUEDEVICE_TYPE));
            equipmentItems.add(info);
        }

        c.close();
        return equipmentItems;

    }

    public void upDateDevice(EquipmentItem sInfo) {
        openDB();
        ContentValues cv = new ContentValues();
        cv.put(BLUEDEVICE_NAME, sInfo.mName);
        cv.put(BLUEDEVICE_ADDRESS, sInfo.mAddress);
        cv.put(BLUEDEVICE_ID, sInfo.id);
        cv.put(BLUEDEVICE_TYPE, sInfo.type);
        if(sInfo.type.equals(EquipMentManager.DEVICE_FADA))
        {
            if(EquipMentManager.getInstance().hasFada)
            {
                db.update(TABLE_BLUEDEVICE, cv, BLUEDEVICE_TYPE + "=?", new String[]
                        {sInfo.type});
            }
            else
            {
                db.insert(TABLE_BLUEDEVICE, null, cv);
            }
        }
        else if(sInfo.type.equals(EquipMentManager.DEVICE_POTC))
        {
            if(EquipMentManager.getInstance().hasPotc)
            {
                db.update(TABLE_BLUEDEVICE, cv, BLUEDEVICE_TYPE + "=?", new String[]
                        {sInfo.type});
            }
            else
            {
                db.insert(TABLE_BLUEDEVICE, null, cv);
            }
        }
        else if(sInfo.type.equals(EquipMentManager.DEVICE_PRINT))
        {
            if(EquipMentManager.getInstance().hasPrint)
            {
                db.update(TABLE_BLUEDEVICE, cv, BLUEDEVICE_TYPE + "=?", new String[]
                        {sInfo.type});
            }
            else
            {
                db.insert(TABLE_BLUEDEVICE, null, cv);
            }
        }
//        int iRet = (int) db.insert(TABLE_BLUEDEVICE, null, cv);
//        if (-1 == iRet) {
//            db.update(TABLE_BLUEDEVICE, cv, BLUEDEVICE_TYPE + "=?", new String[]
//                    {sInfo.type});
//        }

    }

    public EquipmentItem getDevice(String type) {
        String sql = "SELECT * FROM " + TABLE_BLUEDEVICE + " WHERE " + BLUEDEVICE_TYPE + "=?";
        Cursor c = db.rawQuery(sql, new String[]
                {type});
        EquipmentItem device = null;
        while (c.moveToNext()) {
            EquipmentItem info = new EquipmentItem();
            info.mName = c.getString(c.getColumnIndex(BLUEDEVICE_NAME));
            info.mAddress = c.getString(c.getColumnIndex(BLUEDEVICE_ADDRESS));
            info.id = c.getString(c.getColumnIndex(BLUEDEVICE_ID));
            info.type = c.getString(c.getColumnIndex(BLUEDEVICE_TYPE));
            device = info;
        }
        return device;
    }

    public void saveReport(SeriesReport mSeriesReport) {
        ContentValues cv = new ContentValues();
        cv.put(TEST_REPORT_ID, mSeriesReport.mReportId);
        cv.put(TEST_REPORT_PID, mSeriesReport.mPId);
        cv.put(TEST_REPORT_AID, mSeriesReport.mAccountId);
        cv.put(TEST_REPORT_NAME, mSeriesReport.mName);
        cv.put(TEST_REPORT_SEX, mSeriesReport.mSex);
        cv.put(TEST_REPORT_AGE, mSeriesReport.mAge);
        cv.put(TEST_REPORT_SENDER, mSeriesReport.mSender);
        cv.put(TEST_REPORT_TIME, mSeriesReport.mSendTime);
        cv.put(TEST_REPORT_STIME, mSeriesReport.mSendTime);
        cv.put(TEST_REPORT_APPROVE, mSeriesReport.mApprover);
        cv.put(TEST_REPORT_CNUM, mSeriesReport.mCNum);
        cv.put(TEST_REPORT_HNUM, mSeriesReport.mHNum);
        cv.put(TEST_REPORT_DEPART, mSeriesReport.mDepart);
        cv.put(TEST_REPORT_BAD, mSeriesReport.mBad);
        cv.put(TEST_REPORT_DES, mSeriesReport.mDes);
        cv.put(TEST_REPORT_MEMO, mSeriesReport.mMemo);
        cv.put(TEST_REPORT_ISAPPROVE, mSeriesReport.isApprove);
        cv.put(TEST_REPORT_ISUPDATE, String.valueOf(mSeriesReport.isUpdate));
        cv.put(TEST_REPORT_APPROVE_TIME, mSeriesReport.mApproveTime);
        cv.put(TEST_REPORT_TEMPIDS, mSeriesReport.tempId);
        cv.put(TEST_REPORT_JSON, DataPrase.mageLocalReport(mSeriesReport.mReports));
        int iRet = (int) db.insert(TABLE_TEST_REPORT, null, cv);
//        if (-1 == iRet) {
//            db.update(TABLE_TEST_REPORT, cv, TEST_REPORT_ID + "=?", new String[]
//                    {mSeriesReport.mReportId});
//        }
    }

    public void updataReport(SeriesReport mSeriesReport) {
        ContentValues cv = new ContentValues();
        cv.put(TEST_REPORT_ID, mSeriesReport.mReportId);
        cv.put(TEST_REPORT_PID, mSeriesReport.mPId);
        cv.put(TEST_REPORT_AID, mSeriesReport.mAccountId);
        cv.put(TEST_REPORT_NAME, mSeriesReport.mName);
        cv.put(TEST_REPORT_SEX, mSeriesReport.mSex);
        cv.put(TEST_REPORT_AGE, mSeriesReport.mAge);
        cv.put(TEST_REPORT_SENDER, mSeriesReport.mSender);
        cv.put(TEST_REPORT_TIME, mSeriesReport.mSendTime);
        cv.put(TEST_REPORT_STIME, mSeriesReport.mSendTime);
        cv.put(TEST_REPORT_APPROVE, mSeriesReport.mApprover);
        cv.put(TEST_REPORT_CNUM, mSeriesReport.mCNum);
        cv.put(TEST_REPORT_HNUM, mSeriesReport.mHNum);
        cv.put(TEST_REPORT_DEPART, mSeriesReport.mDepart);
        cv.put(TEST_REPORT_BAD, mSeriesReport.mBad);
        cv.put(TEST_REPORT_DES, mSeriesReport.mDes);
        cv.put(TEST_REPORT_MEMO, mSeriesReport.mMemo);
        cv.put(TEST_REPORT_ISAPPROVE, mSeriesReport.isApprove);
        cv.put(TEST_REPORT_ISUPDATE, String.valueOf(mSeriesReport.isUpdate));
        cv.put(TEST_REPORT_APPROVE_TIME, mSeriesReport.mApproveTime);
        cv.put(TEST_REPORT_TEMPIDS, mSeriesReport.tempId);
        cv.put(TEST_REPORT_JSON, DataPrase.mageLocalReport(mSeriesReport.mReports));
        db.update(TABLE_TEST_REPORT, cv, TEST_REPORT_ID + "=?", new String[]
                {mSeriesReport.mReportId});
    }

    public void removeReport(SeriesReport mSeriesReport) {
        db.delete(TABLE_TEST_REPORT, TEST_REPORT_ID + "=?", new String[]
                {mSeriesReport.mReportId});
    }

    public void removeReport(String id) {
        db.delete(TABLE_TEST_REPORT, TEST_REPORT_ID + "=?", new String[]
                {id});
    }

    public PageSeraisReport getReports(String aid,String name1,String name2,String data1 ,String data2) {
        String sql = "";
        Cursor c = null;
//        if(name1.length() == 0 && name2.length() > 0)
//        {
//            sql = "SELECT * FROM " + TABLE_TEST_REPORT + " WHERE " + TEST_REPORT_AID + " = ? and "+ TEST_REPORT_SENDER + "=?";
//            c = db.rawQuery(sql, new String[]
//                    {aid,name2});
//        }
//        else if(name2.length() == 0 && name1.length() > 0)
//        {
//            sql = "SELECT * FROM " + TABLE_TEST_REPORT + " WHERE " + TEST_REPORT_AID + " = ? and "+ TEST_REPORT_NAME + "=?";
//            c = db.rawQuery(sql, new String[]
//                    {aid,name1});
//        }
//        else if(name2.length() == 0 && name1.length() == 0)
//        {
//            sql = "SELECT * FROM " + TABLE_TEST_REPORT + " WHERE " + TEST_REPORT_AID + "=?";
//            c = db.rawQuery(sql, new String[]
//                    {aid});
//        }
//        else
//        {
//            sql = "SELECT * FROM " + TABLE_TEST_REPORT + " WHERE " + TEST_REPORT_AID + " = ?";
//            c = db.rawQuery(sql, new String[]
//                    {aid});
//        }
        sql = "SELECT * FROM " + TABLE_TEST_REPORT + " WHERE " + TEST_REPORT_AID + " = ?";
        c = db.rawQuery(sql, new String[]
                {aid});
        PageSeraisReport pageSeraisReports = new PageSeraisReport(20);
        while (c.moveToNext()) {


            SeriesReport info = new SeriesReport();
            info.mReportId = c.getString(c.getColumnIndex(TEST_REPORT_ID));
            info.mPId = c.getString(c.getColumnIndex(TEST_REPORT_PID));
            info.mAccountId = c.getString(c.getColumnIndex(TEST_REPORT_AID));
            info.mName = c.getString(c.getColumnIndex(TEST_REPORT_NAME));
            info.mSex = c.getString(c.getColumnIndex(TEST_REPORT_SEX));
            info.mAge = c.getString(c.getColumnIndex(TEST_REPORT_AGE));
            info.mSender = c.getString(c.getColumnIndex(TEST_REPORT_SENDER));
//            info.mTime = c.getString(c.getColumnIndex(TEST_REPORT_TIME));
            info.mSendTime = c.getString(c.getColumnIndex(TEST_REPORT_STIME));
            info.mApprover = c.getString(c.getColumnIndex(TEST_REPORT_APPROVE));
            info.mCNum = c.getString(c.getColumnIndex(TEST_REPORT_CNUM));
            info.mHNum = c.getString(c.getColumnIndex(TEST_REPORT_HNUM));
            info.mDepart = c.getString(c.getColumnIndex(TEST_REPORT_DEPART));
            info.mBad = c.getString(c.getColumnIndex(TEST_REPORT_BAD));
            info.mDes = c.getString(c.getColumnIndex(TEST_REPORT_DES));
            info.mMemo = c.getString(c.getColumnIndex(TEST_REPORT_MEMO));
            info.isApprove = c.getString(c.getColumnIndex(TEST_REPORT_ISAPPROVE));
            info.mApproveTime = c.getString(c.getColumnIndex(TEST_REPORT_APPROVE_TIME));
            info.tempId = c.getString(c.getColumnIndex(TEST_REPORT_TEMPIDS));
            info.isUpdate = Boolean.valueOf(c.getString(c.getColumnIndex(TEST_REPORT_ISUPDATE)));
            boolean canadd = true;
            if(data1.length() > 0)
            {
                if(!AppUtils.measureBefore(data1+" 00:00:00",info.mSendTime.substring(0,10)+" 00:00:00"))
                {
                    canadd = false;
                }
            }
            if(data2.length() > 0)
            {
                if(!AppUtils.measureBefore(info.mSendTime.substring(0,10)+" 00:00:00",data2+" 00:00:00"))
                {
                    canadd = false;
                }
            }
            if(name1.length()> 0)
            {
                if(!info.mName.contains(name1))
                {
                    canadd = false;
                }
            }
            if(name2.length()> 0)
            {
                if(!info.mSender.contains(name2))
                {
                    canadd = false;
                }
            }

            if(canadd)
            {
                ArrayList<SeriesReport> seriesReports = pageSeraisReports.map.get(String.valueOf(pageSeraisReports.pageCount));
                if(seriesReports != null)
                {
                    if(seriesReports.size() == pageSeraisReports.pageMax)
                    {
                        pageSeraisReports.pageCount++;
                        seriesReports = pageSeraisReports.map.get(String.valueOf(pageSeraisReports.pageCount));
                    }
                }
                if(seriesReports == null) {
                    seriesReports = new ArrayList<SeriesReport>();
                    pageSeraisReports.map.put(String.valueOf(pageSeraisReports.pageCount),seriesReports);
                }
                info.mReports.addAll(DataPrase.praseLocalReport(c.getString(c.getColumnIndex(TEST_REPORT_JSON))));
                seriesReports.add(info);
                pageSeraisReports.totalcount++;

            }
        }
        c.close();
        pageSeraisReports.check();
        return pageSeraisReports;
    }

    public SeriesReport getSeriesReport(String id)
    {
        String sql = "";
        Cursor c = null;
        sql = "SELECT * FROM " + TABLE_TEST_REPORT + " WHERE " + TEST_REPORT_AID + " = ? and "+ TEST_REPORT_ID + "=?";
        c = db.rawQuery(sql, new String[]
                {PoctApplication.mApp.account.mAccountId,id});
        SeriesReport info = null;
        while (c.moveToNext()) {
            info = new SeriesReport();
            info.mReportId = c.getString(c.getColumnIndex(TEST_REPORT_ID));
            info.mPId = c.getString(c.getColumnIndex(TEST_REPORT_PID));
            info.mAccountId = c.getString(c.getColumnIndex(TEST_REPORT_AID));
            info.mName = c.getString(c.getColumnIndex(TEST_REPORT_NAME));
            info.mSex = c.getString(c.getColumnIndex(TEST_REPORT_SEX));
            info.mAge = c.getString(c.getColumnIndex(TEST_REPORT_AGE));
            info.mSender = c.getString(c.getColumnIndex(TEST_REPORT_SENDER));
//            info.mTime = c.getString(c.getColumnIndex(TEST_REPORT_TIME));
            info.mSendTime = c.getString(c.getColumnIndex(TEST_REPORT_STIME));
            info.mApprover = c.getString(c.getColumnIndex(TEST_REPORT_APPROVE));
            info.mCNum = c.getString(c.getColumnIndex(TEST_REPORT_CNUM));
            info.mHNum = c.getString(c.getColumnIndex(TEST_REPORT_HNUM));
            info.mDepart = c.getString(c.getColumnIndex(TEST_REPORT_DEPART));
            info.mBad = c.getString(c.getColumnIndex(TEST_REPORT_BAD));
            info.mDes = c.getString(c.getColumnIndex(TEST_REPORT_DES));
            info.mMemo = c.getString(c.getColumnIndex(TEST_REPORT_MEMO));
            info.isApprove = c.getString(c.getColumnIndex(TEST_REPORT_ISAPPROVE));
            info.mApproveTime = c.getString(c.getColumnIndex(TEST_REPORT_APPROVE_TIME));
            info.isUpdate = Boolean.valueOf(c.getString(c.getColumnIndex(TEST_REPORT_ISUPDATE)));
            info.tempId = c.getString(c.getColumnIndex(TEST_REPORT_TEMPIDS));
            info.mReports.addAll(DataPrase.praseLocalReport(c.getString(c.getColumnIndex(TEST_REPORT_JSON))));

        }
        c.close();
        return info;
    }

    public String getTempId(String recordid) {
        String sql = "SELECT * FROM " + TABLE_TEMPID + " WHERE " + TEMPID_RECORDID + "=?";
        Cursor c = db.rawQuery(sql, new String[]
                {recordid});
        String id = "";
        while (c.moveToNext()) {
            id = c.getString(c.getColumnIndex(TEMPID_ID));
        }
        return id;
    }

    public void saveTempId(String recordid,String id) {
        String ids = getTempId(recordid);
        ContentValues cv = new ContentValues();
        cv.put(TEMPID_RECORDID, recordid);
        cv.put(TEMPID_ID,id);
        if(ids.length() == 0)
        {
            int iRet = (int) db.insert(TABLE_TEMPID, null, cv);
        }
        else
        {
            db.update(TABLE_TEMPID, cv, TEMPID_RECORDID + "=?", new String[]
                    {recordid});
        }
//
//        if (-1 == iRet) {
//
//        }
    }
}
