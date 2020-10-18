package com.bsoft.mhealthp.libs.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.bsoft.mhealthp.libs.AppBase;
import com.bsoft.mhealthp.libs.log.lib.AndroidLogAdapter;
import com.bsoft.mhealthp.libs.log.lib.FormatStrategy;
import com.bsoft.mhealthp.libs.log.lib.PrettyFormatStrategy;


public class LogAdapter extends AndroidLogAdapter {
    private final FormatStrategy formatStrategy;

    public LogAdapter() {
        this.formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("log")
                .build();
    }

    public LogAdapter(@NonNull FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public boolean isLoggable(int priority, @Nullable String tag) {
        return AppBase.isEngineering && !TextUtils.isEmpty(tag) && tag.contains(CoreLogTag.TAG);
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        formatStrategy.log(priority, tag, message);
    }
}
