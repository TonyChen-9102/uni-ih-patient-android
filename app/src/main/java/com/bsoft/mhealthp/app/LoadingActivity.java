package com.bsoft.mhealthp.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.bsoft.mhealthp.libs.net.init.ConstantsHttp;
import com.bsoft.mhealthp.libs.shapref.CoreSharpref;
import com.bsoft.mhealthp.libs.utils.SignCheck;
import com.bsoft.mhealthp.libs.utils.ToastUtil;
import com.bsoft.mhealthp.app.privacy.PrivacyConfirmDialog;

import org.json.JSONObject;

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
        SignCheck signCheck = new SignCheck(this, "73:E9:99:CA:C3:99:08:B1:FF:27:0C:53:E5:5F:19:5D:2E:F7:01:D3");
        if (!signCheck.check()) {
            ToastUtil.toast("签名错误");
            finish();
            return;
        }
        showPrivacyDialog();
    }

    private void showPrivacyDialog() {
        if (!CoreSharpref.getInstance().isAgreePrivacy()) {
            PrivacyConfirmDialog.getInstance()
                    .setDialogListener(new PrivacyConfirmDialog.DialogListener() {
                        @Override
                        public void onComplete(boolean ok, String tag) {
                            CoreSharpref.getInstance().setAgreePrivacy(ok);
                            if (ok) {
                                loadingStart();
                            } else {
                                finish();
                            }
                        }
                    })
                    .show(LoadingActivity.this);
        } else {
            loadingStart();
        }
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
            JSONObject arguments = new JSONObject();
            arguments.put("appEnv", ConstantsHttp.environment);
            arguments.put("tenantId",getString(R.string.tenantId));
            DCUniMPSDK.getInstance().startApp(LoadingActivity.this, "__UNI__6348280");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

}
