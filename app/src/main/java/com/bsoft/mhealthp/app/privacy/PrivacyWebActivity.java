package com.bsoft.mhealthp.app.privacy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsoft.mhealthp.app.R;
import com.bsoft.mhealthp.libs.AppBase;
import com.bsoft.mhealthp.libs.log.LogUtil;
import com.bsoft.mhealthp.libs.net.base.BaseObserver;
import com.bsoft.mhealthp.libs.net.init.ConstantsHttp;
import com.bsoft.mhealthp.libs.net.post.PostManager;
import com.bsoft.mhealthp.libs.utils.EffectUtil;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public class PrivacyWebActivity extends AppCompatActivity {
    /*Default*/
    //隐私协议
    public static String AGREE_TYPE_PRIVATE = "1";
    //用户注册协议
    public static String AGREE_TYPE_SERVICE = "2";

    /*Util*/
    /*Flag*/
    private String agreeType;
    /*View*/
    protected WebView yjhealthCoreWebview;
    private ImageView ivBack;
    private ImageView ivCha;
    private TextView tvTitle;

    public static void appStart(Context context, String agreeType) {
        Intent intent = new Intent(context, PrivacyWebActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("agreeType", agreeType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wise_set_activity_privacy_web);
        parseIntent();
        initLayout();
        getData();
    }

    protected void initLayout() {
        yjhealthCoreWebview = findViewById(R.id.yjhealthCoreWebview);

        yjhealthCoreWebview.getSettings().setJavaScriptEnabled(true);
        yjhealthCoreWebview.getSettings().setDomStorageEnabled(true);
        yjhealthCoreWebview.getSettings().setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            yjhealthCoreWebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        yjhealthCoreWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        yjhealthCoreWebview.requestFocus();
        yjhealthCoreWebview.getSettings().setDefaultTextEncodingName("UTF-8");
        yjhealthCoreWebview.getSettings().setAllowContentAccess(true);
        yjhealthCoreWebview.getSettings().setAllowFileAccess(true);
        yjhealthCoreWebview.getSettings().setAllowFileAccessFromFileURLs(false);
        yjhealthCoreWebview.getSettings().setAllowUniversalAccessFromFileURLs(false);
        yjhealthCoreWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 表重写此方法明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        // 设置可以支持缩放
        yjhealthCoreWebview.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        yjhealthCoreWebview.getSettings().setBuiltInZoomControls(true);


        ivBack = findViewById(R.id.ivBack);
        ivCha = findViewById(R.id.ivCha);
        tvTitle = findViewById(R.id.tvTitle);

        if (TextUtils.equals(agreeType, AGREE_TYPE_PRIVATE)) {
            tvTitle.setText("隐私协议");
        } else if (TextUtils.equals(agreeType, AGREE_TYPE_SERVICE)) {
            tvTitle.setText("用户注册协议");
        }



        EffectUtil.addClickEffect(ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        EffectUtil.addClickEffect(ivCha);
        ivCha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        agreeType = intent.getStringExtra("agreeType");
    }

    public void load(@NonNull String url) {
        if (yjhealthCoreWebview == null) {
            return;
        }
        LogUtil.d("CoreWebActivity;load;url=" + url);
        if (!TextUtils.isEmpty(url)) {
            yjhealthCoreWebview.loadUrl(url);
        }
    }

    private void getData() {
        ArrayMap<String, String> head = new ArrayMap<>();
        head.put(ConstantsHttp.Head_Id, ConstantsHttp.CAS_BASE_AGREEMENT_VERSION_SERVICE);
        head.put(ConstantsHttp.Head_Method, "findBaseAgreementVersionsByProduct");

        ArrayList body = new ArrayList();
        ArrayMap<String, String> map = new ArrayMap();
        map.put("agreeType", agreeType);
        map.put("productCode", AppBase.getApplication().getString(R.string.tenantId)+".patient");
        body.add(map);

        PostManager.getInstance().postList("*.jsonRequest", head, body, PrivacyVo.class,
                new BaseObserver<ArrayList<PrivacyVo>>() {
                    @Override
                    public void onHandlePrePare(Disposable disposable) {
                    }

                    @Override
                    protected void onHandleSuccess(ArrayList<PrivacyVo> value) {
                        if (value != null && !value.isEmpty()) {
                            load(value.get(0).getUrl());
                        } else {
                        }
                    }

                    @Override
                    protected void onHandleError(String errorType, String msg) {
                    }

                    @Override
                    protected void onHandleComplete() {

                    }
                });
    }
}
