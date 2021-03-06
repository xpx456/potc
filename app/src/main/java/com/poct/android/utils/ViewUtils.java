package com.poct.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.poct.R;
import com.poct.android.entity.MenuItem;

import java.util.ArrayList;

public class ViewUtils {


    public static final void setTitle(Toolbar bar, String title) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setText(title);
    }

    public static final void setTitle(Toolbar bar,View.OnClickListener titleclicklistener, String title) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setOnClickListener(titleclicklistener);
        tv.setText(title);
    }


    public static final void setTitle(Toolbar bar, String title,int color) {
        View v = bar.getRootView();
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setTextColor(color);
        tv.setText(title);
    }

    public static void showMessage(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showMessagetop(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public static void creatDialogTowButton(Context mContext,String message,String title,String btnname1,String btnname2,String btnname3,
                                            DialogInterface.OnClickListener btnlistener1,DialogInterface.OnClickListener btnlistener2,DialogInterface.OnClickListener btnlistener3)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,5);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setNeutralButton(btnname2,btnlistener2);
        builder.setPositiveButton(btnname3, btnlistener3);
        builder.show();
    }

    public static void creatDialogTowButton(Context mContext,String message,String title,String btnname1,String btnname2,
                                            DialogInterface.OnClickListener btnlistener1,DialogInterface.OnClickListener btnlistener2)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,5);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setPositiveButton(btnname2, btnlistener2);
        builder.show();
    }

    public static void creatDialogTowButton(Context mContext,String message,String title,String btnname1,
                                            DialogInterface.OnClickListener btnlistener1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,5);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.show();
    }

    public static AlertDialog creatDialogTowButton(Context mContext, String message, String title, String btnname1,
                                            DialogInterface.OnClickListener btnlistener1, DialogInterface.OnDismissListener dismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,5);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setOnDismissListener(dismissListener);
        return builder.show();
    }

    public static void creatDialogTowButtonEdit(Context mContext,String message,String title,String btnname1,String btnname2,
                                                EditDialogListener btnlistener1,EditDialogListener btnlistener2,String hit)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,5);
        builder.setMessage(message);
        builder.setTitle(title);
        final EditText et = new EditText(mContext);
        et.getText().append(hit);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setPositiveButton(btnname2, btnlistener2);
        if(btnlistener1 != null)
        {
            btnlistener1.setEditText(et);
        }
        if(btnlistener2 != null)
        {
            btnlistener2.setEditText(et);
        }
        builder.show();

    }

    public static void creatDialogTowButton(Context mContext,String message,String title,String btnname1,String btnname2,
                                            DialogInterface.OnClickListener btnlistener1,DialogInterface.OnClickListener btnlistener2,View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,5);
        if(message.length() > 0)
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setView(view);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setPositiveButton(btnname2, btnlistener2);
        builder.show();
    }

    public static void creatDialogTowButton(Context mContext,String message,String title,String btnname1,String btnname2,String btnname3,
                                            DialogInterface.OnClickListener btnlistener1,DialogInterface.OnClickListener btnlistener2,DialogInterface.OnClickListener btnlistener3,View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,5);
        if(message.length() > 0)
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setView(view);
        builder.setNegativeButton(btnname1, btnlistener1 );
        builder.setNeutralButton(btnname2,btnlistener2);
        builder.setPositiveButton(btnname3, btnlistener3);
        builder.show();
    }

    public static final void hidBack(Toolbar bar) {
        bar.setNavigationIcon(null);
    }



    public static final void setSutColor(Activity mContext, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = mContext.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);

            ViewGroup mContentView = (ViewGroup) mContext.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }


        } else {
            Window window = mContext.getWindow();
            ViewGroup mContentView = (ViewGroup) mContext.findViewById(Window.ID_ANDROID_CONTENT);

//First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(mContext);

            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                //如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
                if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                    //不预留系统空间
                    ViewCompat.setFitsSystemWindows(mChildView, false);
                    lp.topMargin += statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }

            View statusBarView = mContentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
                //避免重复调用时多次添加 View
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(mContext);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setBackgroundColor(color);
//向 ContentView 中添加假 View
            mContentView.addView(statusBarView, 0, lp);
        }

    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void measureHit(TextView mview , int hit)
    {
        if (hit < 100) {
            mview.setText(String.valueOf(hit));
            mview.setVisibility(View.VISIBLE);
            if (hit == 0) {
                mview.setVisibility(View.INVISIBLE);
            }
        } else {
            mview.setVisibility(View.VISIBLE);
            mview.setText("99+");
        }
    }

    public static void creatDataPicker(Context mContext,String time,String title,DoubleDatePickerDialog.OnDateSetListener mCollback)
    {
        int theme = 5;

        if(SystemUtil.getSystemModel().toLowerCase().contains("oneplus")||SystemUtil.getSystemVersion().contains("8.0"))
        {
            theme = 5;
        }

        new DoubleDatePickerDialog(mContext, theme, mCollback,title,time,true).show();

    }

    public static void creatMonthPicker(Context mContext,String time,String title,DoubleDatePickerDialog.OnDateSetListener mCollback)
    {

        new DoubleDatePickerDialog(mContext, 5, mCollback,title,time,false).show();
    }

    public static void creatDataAndTimePicker(Context mContext,String time,String title,DoubleDatePickerDialog.OnDateSetListener mCollback)
    {
        int theme = 5;

        if(SystemUtil.getSystemModel().toLowerCase().contains("oneplus")||SystemUtil.getSystemVersion().contains("8.0"))
        {
            theme = 55;
        }

        new DoubleDatePickerDialog(mContext, theme, mCollback,title,time).show();
    }

    public static void creatTimePicker(Context mContext,String time,String title,DoubleDatePickerDialog.OnDateSetListener mCollback)
    {

        new DoubleDatePickerDialog(mContext,  mCollback,title,time).show();
    }

    public static void creatButtomMenu(Context mContext, PopupWindow popupWindow1, final RelativeLayout mRelativeLayout, ArrayList<MenuItem> mMenuItems, View location) {

        if(popupWindow1 != null)
        {
            popupWindow1.dismiss();
        }
        View popupWindowView = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu, null);
        RelativeLayout lsyer = (RelativeLayout) popupWindowView.findViewById(R.id.layer);
        lsyer.setFocusable(true);
        lsyer.setFocusableInTouchMode(true);
        popupWindow1 = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowView.setFocusable(true);
        popupWindowView.setFocusableInTouchMode(true);
        popupWindow1.setAnimationStyle(R.style.PopupAnimation);
        lsyer.setTag(popupWindow1);
        lsyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindow1 = (PopupWindow) v.getTag();
                popupWindow1.dismiss();

            }
        });
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        popupWindow1.setBackgroundDrawable(dw);
        LinearLayout linelayer = (LinearLayout) popupWindowView.findViewById(R.id.pop_layout);
        View view;
        Button btn;
        if(mMenuItems.size() == 1)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn2, null);
            btn = (Button) view.findViewById(R.id.btn);
            btn.setText(mMenuItems.get(0).btnName);
            btn.setTag(mMenuItems.get(0).item);
            btn.setOnClickListener(mMenuItems.get(0).mListener);
            linelayer.addView(view);
        }
        else
        {

            for(int i = 0 ; i < mMenuItems.size() ; i++)
            {

                if(i == 0 )
                {
                    view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn1, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                }
                else if(i == mMenuItems.size()-1)
                {
                    view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn3, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                }
                else
                {
                    view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn4, null);btn = (Button) view.findViewById(R.id.btn);
                    btn.setText(mMenuItems.get(i).btnName);
                    btn.setTag(mMenuItems.get(i).item);
                    btn.setOnClickListener(mMenuItems.get(i).mListener);
                    linelayer.addView(view);
                }
                if(i != mMenuItems.size()-1)
                {
                    view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_line, null);
                    linelayer.addView(view);
                }
            }
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.emptylayer, null);
        linelayer.addView(view);
        view = LayoutInflater.from(mContext).inflate(R.layout.buttom_menu_btn2, null);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setText(mContext.getString(R.string.button_word_cancle));
        btn.setTag(popupWindow1);
//        final PopupWindow finalPopupWindow1 = popupWindow1;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow mPopupWindow = (PopupWindow) v.getTag();
                mPopupWindow.dismiss();
            }
        });
        linelayer.addView(view);

        mRelativeLayout.setVisibility(View.VISIBLE);
        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
        popupWindow1.showAtLocation(location,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
