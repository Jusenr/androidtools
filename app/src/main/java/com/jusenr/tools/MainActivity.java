package com.jusenr.tools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.utils.AppUtils;
import com.jusenr.toolslibrary.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        TextView tvText = (TextView) findViewById(R.id.tv_text);
        TextView tvText1 = (TextView) findViewById(R.id.tv_text1);
        TextView tvText2 = (TextView) findViewById(R.id.tv_text2);
        TextView tvText3 = (TextView) findViewById(R.id.tv_text3);
        TextView tvText4 = (TextView) findViewById(R.id.tv_text4);
        TextView tvText5 = (TextView) findViewById(R.id.tv_text5);
        TextView tvText6 = (TextView) findViewById(R.id.tv_text6);
        TextView tvText7 = (TextView) findViewById(R.id.tv_text7);
        tv.setText(stringFJNI());
        tvText.setText(stringFromJNI());
        tvText1.setText(AppUtils.getVersionName(this));
        tvText2.setText(String.valueOf(AppUtils.getVersionCode(this)));

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showAlertToast(getApplicationContext(), "Hello See!");
                Logger.i("Hello See!");
            }
        });
        tvText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(getApplicationContext(), "Think you!");
                Logger.v("Think you! %s", "Mary");
            }
        });
        tvText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(getApplicationContext(), "How dou you dou?", 0);
                Logger.w("How dou you dou?");
            }
        });
        tvText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(getApplicationContext(), "1+1=%d", (Object) 2);
                Logger.e("1+1=%d", 2);
            }
        });
        tvText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.showViewer();
            }
        });

        Logger.d("d");

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String stringFJNI();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getLocalClassName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getLocalClassName());
        MobclickAgent.onPause(this);
    }
}
