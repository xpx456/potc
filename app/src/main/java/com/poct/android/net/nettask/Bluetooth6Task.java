package com.poct.android.net.nettask;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.poct.android.asks.TestReportAsks;
import com.poct.android.entity.NetObject;
import com.poct.android.entity.TempGet;
import com.poct.android.handler.PotcTestHandler;
import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.net.NetUtils;
import com.poct.android.prase.DataPrase;
import com.poct.android.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Bluetooth6Task extends NetTask {

    public String time;
    public int begin;
    public BluetoothSocket mmSocket;
    public OutputStream outputStream;
    public InputStream inputStream;
    public ArrayList<TempGet> item;
    public long systime = 0;
    public Bluetooth6Task(String url, Handler mHandler, int successEvent, int failEvent, Context mContext, int count, ArrayList<TempGet> objects, String itime) {
        super(url, mHandler, successEvent, failEvent, mContext);
        this.begin = count;
        this.time = itime;
        this.item = objects;
    }

    @Override
    public void run() {
        try {
            if (BluetoothSetManager.getInstance().netBluetoothDevice != null) {
                mmSocket = BluetoothSetManager.getInstance().netBluetoothDevice.createRfcommSocketToServiceRecord(BluetoothSetManager.getInstance().MY_UUID_SECURE);
                mmSocket.connect();
                outputStream = mmSocket.getOutputStream();
                inputStream = mmSocket.getInputStream();
                int bytes = 0;
                String data = "";
                int readcount = 0;
                outputStream.flush();
                outputStream.write(StringUtil.HexCommandtoByte(mUrl.getBytes()));
                outputStream.flush();
                Thread.sleep(200);
                systime = System.currentTimeMillis();

                while (readcount == 0 && (System.currentTimeMillis() -systime) < 200)
                {
                    readcount = inputStream.available();
                }

                while (readcount == 0)
                {
                    String url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                    outputStream.flush();
                    outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                    outputStream.flush();
                    Thread.sleep(200);
                    outputStream.flush();
                    outputStream.write(StringUtil.HexCommandtoByte(mUrl.getBytes()));
                    outputStream.flush();
                    Thread.sleep(200);
                    systime = System.currentTimeMillis();
                    while (readcount == 0 && (System.currentTimeMillis() -systime) < 200)
                    {
                        readcount = inputStream.available();
                    }
                }
                while (readcount != 0) {
                    byte[] buffer = new byte[1024];
                    bytes += inputStream.read(buffer);
                    data += new String(buffer, 0, bytes, "GBK");
                    readcount = inputStream.available();
                }
                String get = "";
                if (data.length() > 0 && data.contains("SXW")) {
                    get = NetUtils.praseData(data, "SXW");
                }
                int count = 0;
                if (get.length() > 0) {
                    count = DataPrase.praseGetReportCount(get);
                }
                String url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                outputStream.flush();
                outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                outputStream.flush();
                Thread.sleep(200);
                systime = System.currentTimeMillis();
                while (readcount == 0 && (System.currentTimeMillis() -systime) < 200)
                {
                    readcount = inputStream.available();
                }
                while (readcount != 0) {
                    byte[] buffer = new byte[1024];
                    bytes += inputStream.read(buffer);
                    data += new String(buffer, 0, bytes, "GBK");
                    readcount = inputStream.available();
                }
                int addrealcount = 0;
                int startid = 1;
                for (int i = 0; i < item.size(); i++) {

                    TempGet mTempGet = item.get(i);
                    if(startid > count)
                    {
                        break;
                    }
                    for(int j = startid ; j < count+1 ; j++)
                    {
                        bytes = 0;
                        data = "";
                        String get2 = "";
                        //从输入流中读取数据
                        int begin = j;
                        outputStream.flush();
                        outputStream.write(StringUtil.HexCommandtoByte(TestReportAsks.getReportUrl(time, begin, begin).getBytes()));
                        outputStream.flush();
                        Thread.sleep(200);
                        systime = System.currentTimeMillis();
                        while (readcount == 0 && (System.currentTimeMillis() -systime) < 200)
                        {
                            readcount = inputStream.available();
                        }
                        while (readcount != 0) {
                            byte[] buffer = new byte[1024];
                            bytes += inputStream.read(buffer);
                            data += new String(buffer, 0, bytes, "GBK");
                            readcount = inputStream.available();
                        }
                        get2 = "";

                        if (data.length() > 0 && data.contains("SZD") && mTempGet != null) {
                            get2 = NetUtils.praseData(data, "SZD", String.valueOf(begin));
                        }

                        //发送数据到界面
                        if (get2.startsWith("SZD") ) {
                            String gettempid = DataPrase.getPotcTempid(get2);
                            int getid = Integer.parseInt(gettempid);
                            int itemid = Integer.valueOf(mTempGet.mTempid);
                            if(getid < itemid) {
                                url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                                outputStream.flush();
                                outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                                outputStream.flush();
                                Thread.sleep(200);
                                systime = System.currentTimeMillis();
                                while (readcount == 0 && (System.currentTimeMillis() -systime) < 200)
                                {
                                    readcount = inputStream.available();
                                }
                                while (readcount != 0) {
                                    byte[] buffer = new byte[1024];
                                    bytes += inputStream.read(buffer);
                                    data += new String(buffer, 0, bytes, "GBK");
                                    readcount = inputStream.available();
                                }
                            }
                            else if(getid == itemid) {
                                addrealcount++;
                                NetObject object = new NetObject();
                                object.result = get2;
                                object.item = mTempGet;
                                Message msg = new Message();
                                msg.what = PotcTestHandler.EVENT_GET_POTC_DATA_ITEM_SUCCESS;
                                msg.arg1 = 0;
                                msg.arg2 = count;
                                msg.obj = object;
                                mHandler.sendMessage(msg);
                                url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                                outputStream.flush();
                                outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                                outputStream.flush();
                                Thread.sleep(200);
                                systime = System.currentTimeMillis();
                                while (readcount == 0 && (System.currentTimeMillis() -systime) < 200)
                                {
                                    readcount = inputStream.available();
                                }
                                while (readcount != 0) {
                                    byte[] buffer = new byte[1024];
                                    bytes += inputStream.read(buffer);
                                    data += new String(buffer, 0, bytes, "GBK");
                                    readcount = inputStream.available();
                                }
                                startid = j+1;
                                if(j == count) {
                                    if(mTempGet.realcount == -1)
                                    {
                                        mTempGet.realcount = addrealcount;
                                    }
                                    Message msg2 = new Message();
                                    msg2.obj = mTempGet;
                                    msg2.what = PotcTestHandler.EVENT_GET_POTC_GETTEMP_UPDATA;
                                    mHandler.sendMessage(msg2);
                                }
                            }
                            else
                            {
                                if(mTempGet.realcount == -1)
                                {
                                    mTempGet.realcount = addrealcount;
                                }
                                Message msg = new Message();
                                msg.obj = mTempGet;
                                msg.what = PotcTestHandler.EVENT_GET_POTC_GETTEMP_UPDATA;
                                mHandler.sendMessage(msg);
                                addrealcount = 0;
                                url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                                outputStream.flush();
                                outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                                outputStream.flush();
                                Thread.sleep(200);
                                systime = System.currentTimeMillis();
                                while (readcount == 0 && (System.currentTimeMillis() -systime) < 200)
                                {
                                    readcount = inputStream.available();
                                }
                                while (readcount != 0) {
                                    byte[] buffer = new byte[1024];
                                    bytes += inputStream.read(buffer);
                                    data += new String(buffer, 0, bytes, "GBK");
                                    readcount = inputStream.available();
                                }
                                break;
                            }

                        } else {
                            if(!data.contains("SZD|0|"))
                            {
                                j--;
                            }
                            url = "0B " + StringUtil.str2HexStr("AA|") + " " + "1C 0D";
                            outputStream.flush();
                            outputStream.write(StringUtil.HexCommandtoByte(url.getBytes()));
                            outputStream.flush();
                            Thread.sleep(200);
                            systime = System.currentTimeMillis();
                            while (readcount == 0 && (System.currentTimeMillis() -systime) < 200)
                            {
                                readcount = inputStream.available();
                            }
                            while (readcount != 0) {
                                byte[] buffer = new byte[1024];
                                bytes += inputStream.read(buffer);
                                data += new String(buffer, 0, bytes, "GBK");
                                readcount = inputStream.available();
                            }


                        }
                    }


                }
                Message msg = new Message();
                msg.what = PotcTestHandler.EVENT_GET_POTC_DATA_FINISH;
                mHandler.sendMessage(msg);
                outputStream.close();
                inputStream.close();
                mmSocket.close();
            } else {
                Message msg = new Message();
                msg.what = failEvent;
                mHandler.sendMessage(msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
