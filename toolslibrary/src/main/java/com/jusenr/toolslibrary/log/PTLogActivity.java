package com.jusenr.toolslibrary.log;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jusenr.toolslibrary.R;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.log.logger.PTLogBean;
import com.jusenr.toolslibrary.widgets.CustomDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PTLogActivity extends AppCompatActivity implements View.OnClickListener {
    static final String TAG = PTLogActivity.class.getSimpleName();

    private Button btn_close;
    private Button btn_trace;
    private Button btn_begin;
    private Button btn_end;
    private Button btn_count;
    private TextView tv_content;
    private ScrollView sv_scroll;
    private CustomDatePicker mDatePicker_begin;
    private CustomDatePicker mDatePicker_end;
    private Date mEndDate;
    private Date mBeginDate;
    private int mSelectedLogLevel = 0; // default is ALL
    private int mLimit = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptlog);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryLog();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_close) {
            finish();
        } else if (i == R.id.btn_trace) {
            logLevelSelect();
        } else if (i == R.id.btn_begin) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            String dataStr = sdf.format(mBeginDate);
            mDatePicker_begin.show(dataStr);
        } else if (i == R.id.btn_end) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            String dataStr = sdf.format(mEndDate);
            mDatePicker_end.show(dataStr);
        } else if (i == R.id.btn_count) {
            logCountSelect();
        }
    }

    private void initUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btn_close = (Button) this.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        btn_trace = (Button) this.findViewById(R.id.btn_trace);
        btn_trace.setOnClickListener(this);

        btn_begin = (Button) this.findViewById(R.id.btn_begin);
        btn_begin.setOnClickListener(this);

        btn_end = (Button) this.findViewById(R.id.btn_end);
        btn_end.setOnClickListener(this);

        btn_count = (Button) this.findViewById(R.id.btn_count);
        btn_count.setOnClickListener(this);

        tv_content = (TextView) this.findViewById(R.id.tv_content);
        sv_scroll = (ScrollView) this.findViewById(R.id.sv_scroll);

        mEndDate = new Date(System.currentTimeMillis());
        mBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // previous day

        btn_begin.setText(date2String(mBeginDate));
        btn_end.setText(date2String(mEndDate));

        btn_trace.setText(level2String(mSelectedLogLevel));
        btn_count.setText(String.valueOf(mLimit));

        initDatePicker();
    }

    private void initDatePicker() {
        mDatePicker_begin = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                mBeginDate = string2Date(time);
                Log.d(TAG, mBeginDate.toString());
                btn_begin.setText(date2String(mBeginDate));
                queryLog();

            }
        }, "2010-01-01 00:00", date2String(mBeginDate)); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        mDatePicker_begin.showSpecificTime(true); // 显示时和分
        mDatePicker_begin.setIsLoop(true); // 允许循环滚动


        mDatePicker_end = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                mEndDate = string2Date(time);
                Log.d(TAG, mEndDate.toString());

                if (mEndDate.getTime() < mBeginDate.getTime()) {
                    mEndDate = new Date();
                }
                btn_end.setText(date2String(mEndDate));
                queryLog();

            }
        }, "2010-01-01 00:00", date2String(mEndDate));
        mDatePicker_end.showSpecificTime(true);
        mDatePicker_end.setIsLoop(true);
    }

    private void setLogContent(List<PTLogBean> logList) {
        //clear the content
        tv_content.setText("");

        // pretty log
        for (PTLogBean log : logList) {
            StringBuilder stringBuilder = new StringBuilder("");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(log.getDate());
            stringBuilder.append("\n=================================\n");
            stringBuilder.append(dateString);
            stringBuilder.append(" --> ");
            stringBuilder.append(log.getContent());
            stringBuilder.append("\n=================================\n");

            // set the font color
            int color = Color.WHITE;
            switch (log.getLevel()) {
                case Logger.VERBOSE:
                    color = getResources().getColor(R.color.color_v);
                    break;
                case Logger.DEBUG:
                    color = getResources().getColor(R.color.color_d);
                    break;
                case Logger.INFO:
                    color = getResources().getColor(R.color.color_i);
                    break;
                case Logger.WARN:
                    color = getResources().getColor(R.color.color_w);
                    break;
                case Logger.ERROR:
                    color = getResources().getColor(R.color.color_e);
                    break;
                case Logger.ASSERT:
                    color = getResources().getColor(R.color.color_a);
                    break;
                default:
                    break;
            }

            Spannable colorString = new SpannableString(stringBuilder.toString());
            colorString.setSpan(new ForegroundColorSpan(color), 0, colorString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_content.append(colorString);
        }
        // auto scroll to bottom
        sv_scroll.post(new Runnable() {
            public void run() {
                sv_scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void logLevelSelect() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("select the log level");
        String[] types = {"VERBOSE", "DEBUG", "INFO", "WARN", "ERROR", "ASSERT"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    case 0:
                        mSelectedLogLevel = Logger.VERBOSE; // ALL
                        break;
                    case 1:
                        mSelectedLogLevel = Logger.DEBUG;
                        break;
                    case 2:
                        mSelectedLogLevel = Logger.INFO;
                        break;
                    case 3:
                        mSelectedLogLevel = Logger.WARN;
                        break;
                    case 4:
                        mSelectedLogLevel = Logger.ERROR;
                        break;
                    case 5:
                        mSelectedLogLevel = Logger.ASSERT;
                        break;
                    default:
                        break;
                }
                btn_trace.setText(level2String(mSelectedLogLevel));

                // query the logs with the log level
                queryLog();
            }
        });

        b.show();
    }

    private void logCountSelect() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("select the log count");
        String[] types = {"10", "50", "100", "200", "500"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    case 0:
                        mLimit = 10; // ALL
                        break;
                    case 1:
                        mLimit = 50;
                        break;
                    case 2:
                        mLimit = 100;
                        break;
                    case 3:
                        mLimit = 200;
                        break;
                    case 4:
                        mLimit = 500;
                        break;
                    default:
                        break;
                }
                btn_count.setText(String.valueOf(mLimit));
                // query the logs with the log level
                queryLog();
            }
        });

        b.show();
    }

    private void queryLog() {
        Log.d(TAG, "Level:" + mSelectedLogLevel + " begin: "
                + date2String(mBeginDate) + " start: "
                + date2String(mEndDate) + " limit: "
                + mLimit);
        List<PTLogBean> logList = Logger.queryLog(mSelectedLogLevel, mBeginDate, mEndDate, mLimit);
        setLogContent(logList);
    }

    private String date2String(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(date);
    }

    private Date string2Date(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String level2String(int logLevel) {
        switch (logLevel) {
            case Logger.VERBOSE:
                return "VERBOSE";
            case Logger.DEBUG:
                return "DEBUG";
            case Logger.INFO:
                return "INFO";
            case Logger.WARN:
                return "WARN";
            case Logger.ERROR:
                return "ERROR";
            case Logger.ASSERT:
                return "ASSERT";
            default:
                break;
        }
        return "VERBOSE";
    }
}
