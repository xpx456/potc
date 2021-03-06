package com.poct.android.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poct.android.entity.SeriesReport;
import com.poct.android.presenter.ReportPrintPresenter;

/**
 * Created by xpx on 2017/8/18.
 */

public class ReportPrintActivity extends BaseActivity {

    public ReportPrintPresenter mReportPrintPresenter = new ReportPrintPresenter(this);
    public RelativeLayout btnPrint;
    public RelativeLayout btnBack;
    public SeriesReport printReport;
    public TextView txtName;
    public TextView txtAge;
    public TextView txtSex;
    public TextView txtCNUM;
    public TextView txtHNUM;
    public TextView txtDes;
    public TextView txtDep;
    public TextView txtBad;
    public TextView txtSender;
    public TextView txtSendTime;
    public ListView mListView;
    public RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReportPrintPresenter.Create();
    }

    @Override
    protected void onDestroy() {
        mReportPrintPresenter.Destroy();
        super.onDestroy();
    }

    public View.OnClickListener printListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v) {
            mReportPrintPresenter.doPrint();
        }
    };

    public View.OnClickListener doBacklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mReportPrintPresenter.mReportPrintActivity.finish();
        }
    };
}
