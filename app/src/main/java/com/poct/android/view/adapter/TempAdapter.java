package com.poct.android.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poct.R;
import com.poct.android.entity.Report;
import com.poct.android.entity.TempGet;
import com.poct.android.handler.TestCenterHandler;
import com.poct.android.prase.DataPrase;

import java.util.ArrayList;

public class TempAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private Handler mHandler;
    public String type;
    public String oldTempid;
    public ArrayList<TempGet> mTemps;
    public ArrayList<Report> reports;
    public TempAdapter(Context context, ArrayList<TempGet> mTemps, Handler mHandler,String type,String oldTempid,ArrayList<Report> reports)
    {
        this.mContext = context;
        this.mHandler = mHandler;
        this.mTemps =  mTemps;
        this.type = type;
        this.oldTempid = oldTempid;
        this.reports = reports;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mTemps.size();
    }

    @Override
    public TempGet getItem(int position) {
        return mTemps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TempGet temp = mTemps.get(position);
        ViewHolder mViewHolder;
        if( null == convertView )
        {
            convertView =  mInflater.inflate( R.layout.temp_item, null );
            mViewHolder = new ViewHolder();
            mViewHolder.state = (TextView) convertView.findViewById(R.id.state);
            mViewHolder.tempid = (TextView) convertView.findViewById( R.id.tempid );
            mViewHolder.delete = convertView.findViewById( R.id.oper );
            convertView.setTag(mViewHolder);
        }
        mViewHolder = (ViewHolder) convertView.getTag();
        mViewHolder.tempid.setText(temp.mTempid);
        if(temp.isGet || DataPrase.hasTempitem(temp.mTempid,reports)) {
            mViewHolder.state.setText("数据已获取");
            mViewHolder.delete.setVisibility(View.INVISIBLE);
        }
        else
        {
            mViewHolder.state.setText("数据未获取");
            mViewHolder.delete.setVisibility(View.VISIBLE);
            mViewHolder.delete.setTag(temp);
            mViewHolder.delete.setOnClickListener(deleteListener);
        }
        return convertView;
    }

    public View.OnClickListener deleteListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            TempGet tempGet = (TempGet) v.getTag();
            mTemps.remove(tempGet);
            oldTempid = DataPrase.deleteTempDbData(oldTempid,tempGet.mTempid);
            mHandler.sendEmptyMessage(TestCenterHandler.EVENT_DELETE_TEMPID);
        }
    };

    private static class ViewHolder {
        private TextView delete;
        private TextView tempid;
        private TextView state;
    }
}
