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
import android.widget.Toast;

import com.jusenr.toolslibrary.R;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.log.logger.PTLogBean;
import com.jusenr.toolslibrary.utils.StringUtils;
import com.jusenr.toolslibrary.widgets.CustomDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Log view page.
 * Created by Wangxiaopeng on 17/08/13.
 */
public class PTLogActivity extends AppCompatActivity {
    private static final String TAG = PTLogActivity.class.getSimpleName();
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    private static final int ONE_DAY = 24 * 60 * 60 * 1000;
    private Button btn_close;
    private Button btn_trace;
    private Button btn_begin;
    private Button btn_end;
    private Button btn_count;
    private Button btn_delete;
    private TextView tv_content;
    private ScrollView sv_scroll;
    private CustomDatePicker mDatePicker_begin;
    private CustomDatePicker mDatePicker_end;
    private Date mEndDate;
    private Date mBeginDate;
    private int mSelectedLogLevel = 0; // default is ALL
    private int mLimit = 100;// default is 100

    private int selectedLogLevel = 0;// default is VERBOSE
    private int selectedLogCount = 2;// default is 100
    private int defaultDeleteTime = -1;

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

    private void initUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btn_close = (Button) this.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_trace = (Button) this.findViewById(R.id.btn_trace);
        btn_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logLevelSelect();
            }
        });

        btn_begin = (Button) this.findViewById(R.id.btn_begin);
        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.CHINA);
                String dataStr = sdf.format(mBeginDate);
                mDatePicker_begin.show(dataStr);
            }
        });

        btn_end = (Button) this.findViewById(R.id.btn_end);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.CHINA);
                String dataStr = sdf.format(mEndDate);
                mDatePicker_end.show(dataStr);
            }
        });

        btn_count = (Button) this.findViewById(R.id.btn_count);
        btn_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logCountSelect();
            }
        });

        btn_delete = (Button) this.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logDelete();
            }
        });

        tv_content = (TextView) this.findViewById(R.id.tv_content);
        sv_scroll = (ScrollView) this.findViewById(R.id.sv_scroll);

        mEndDate = new Date(System.currentTimeMillis());
        mBeginDate = new Date(System.currentTimeMillis() - ONE_DAY); // previous day

        btn_begin.setText(date2String(mBeginDate));
        btn_end.setText(date2String(mEndDate));

        btn_trace.setText(level2String(mSelectedLogLevel));
        btn_count.setText(String.valueOf(mLimit));

        initDatePicker();
    }

    private void initDatePicker() {
        mDatePicker_begin = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // The callback interface gets the selected time.
                mBeginDate = string2Date(time);
                Log.i(TAG, "initDatePicker start:" + mBeginDate.toString());
                btn_begin.setText(date2String(mBeginDate));
                queryLog();

            }
        }, "2010-01-01 00:00", date2String(mBeginDate)); // Initial date format: yyyy-MM-dd HH:mm, otherwise it will not function properly.
        mDatePicker_begin.showSpecificTime(true); // Display hours and minutes.
        mDatePicker_begin.setIsLoop(true); // Allow cyclic scrolling.


        mDatePicker_end = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                mEndDate = string2Date(time);
                Log.i(TAG, "initDatePicker end:" + mEndDate.toString());

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
        final String[] types = getResources().getStringArray(R.array.loglevels);
        final int[] typesColor = getResources().getIntArray(R.array.color_level);
        CharSequence[] charSequences = StringUtils.getCharSequences(types, typesColor);
        AlertDialog.Builder b = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.log_level_dialog_title))
                .setSingleChoiceItems(charSequences, selectedLogLevel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        selectedLogLevel = which;
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

        b.create().show();
    }

    private void logCountSelect() {
        final String[] types = getResources().getStringArray(R.array.logcounts);
        AlertDialog.Builder b = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.log_count_dialog_title))
                .setSingleChoiceItems(types, selectedLogCount, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        selectedLogCount = which;
                        mLimit = Integer.valueOf(types[which]);

                        btn_count.setText(String.valueOf(mLimit));

                        // query the logs with the log level
                        queryLog();
                    }
                });

        b.create().show();
    }

    private void logDelete() {
        final String[] types = getResources().getStringArray(R.array.delete_times);
        final int[] times = {ONE_DAY, 2 * ONE_DAY, 3 * ONE_DAY, 5 * ONE_DAY, 7 * ONE_DAY, 10 * ONE_DAY, 0};
        if (defaultDeleteTime == -1) {
            defaultDeleteTime = times.length - 2;
        }
        AlertDialog.Builder b = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.log_delete_dialog_title))
                .setSingleChoiceItems(types, defaultDeleteTime, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        defaultDeleteTime = which;

                        Date date = new Date(System.currentTimeMillis() - times[which]);
                        int deleteLogCount = -1;
                        if (which == (times.length - 1)) {
                            Log.d(TAG, "DELETE-->Level:" + level2String(Logger.VERBOSE) + "\ndate:" + types[which]);
                            deleteLogCount = Logger.deleteLog(Logger.VERBOSE, date);
                        } else {
                            Log.d(TAG, "DELETE-->Level:" + level2String(mSelectedLogLevel) + "\ndate:" + types[which]);
                            deleteLogCount = Logger.deleteLog(mSelectedLogLevel, date);
                        }

                        Toast.makeText(getApplicationContext(), String.format(getString(R.string.delete_toast_text), deleteLogCount), Toast.LENGTH_SHORT).show();

                        // query the logs with the log level
                        queryLog();
                    }
                });

        b.show();
    }

    private void queryLog() {
        Log.d(TAG, "Level:" + level2String(mSelectedLogLevel) +
                "\nbegin: " + date2String(mBeginDate) +
                "\nstart: " + date2String(mEndDate) +
                "\nlimit: " + mLimit);

        List<PTLogBean> logList = Logger.queryLog(mSelectedLogLevel, mBeginDate, mEndDate, mLimit);

        setLogContent(logList);
    }

    private String date2String(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        return formatter.format(date);
    }

    private Date string2Date(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
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
