package com.bsoft.mhealthp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.bsoft.mhealthp.app.R;

import java.util.Timer;
import java.util.TimerTask;

import io.dcloud.feature.sdk.DCUniMPSDK;


/**
 * @类说明
 */
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //tank.key 73:E9:99:CA:C3:99:08:B1:FF:27:0C:53:E5:5F:19:5D:2E:F7:01:D3
//        SignCheck signCheck = new SignCheck(this, "73:E9:99:CA:C3:99:08:B1:FF:27:0C:53:E5:5F:19:5D:2E:F7:01:D3");
//        if (!signCheck.check()) {
//            ToastUtil.toast("签名错误");
//            finish();
//            return;
//        }
        loadingStart();
    }


    private void loadingStart() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                redirectTo();
            }
        }, 1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        try {
            DCUniMPSDK.getInstance().startApp(LoadingActivity.this, "__UNI__6348280");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

}
