package com.bsoft.mhealthp.privacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsoft.mhealthp.app.R;
import com.bsoft.mhealthp.libs.utils.EffectUtil;
import com.bsoft.mhealthp.libs.utils.dialog.CoreDialogFragment;

public class PrivacyConfirmDialog extends CoreDialogFragment {
    /*Default*/
    /*Util*/
    private DialogListener listener;
    /*Flag*/
    /*View*/
    private TextView tvService;
    private TextView tvPrivate;
    private TextView tvDisAgree;
    private TextView tvAgree;

    public interface DialogListener {
        void onComplete(boolean ok, String tag);
    }

    public static PrivacyConfirmDialog getInstance() {
        return new PrivacyConfirmDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_privacy_confirm, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvService = view.findViewById(R.id.tvService);
        tvPrivate = view.findViewById(R.id.tvPrivate);
        tvDisAgree = view.findViewById(R.id.tvDisAgree);
        tvAgree = view.findViewById(R.id.tvAgree);

        EffectUtil.addClickEffect(tvService);
        tvService.setOnClickListener(onClickListener);
        EffectUtil.addClickEffect(tvPrivate);
        tvPrivate.setOnClickListener(onClickListener);
        EffectUtil.addClickEffect(tvDisAgree);
        tvDisAgree.setOnClickListener(onClickListener);
        EffectUtil.addClickEffect(tvAgree);
        tvAgree.setOnClickListener(onClickListener);

        setCancelable(false);
    }

    public PrivacyConfirmDialog setDialogListener(DialogListener listener) {
        this.listener = listener;
        return this;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tvService) {
                PrivacyWebActivity.appStart(getActivity(), PrivacyWebActivity.AGREE_TYPE_SERVICE);
            } else if (id == R.id.tvPrivate) {
                PrivacyWebActivity.appStart(getActivity(), PrivacyWebActivity.AGREE_TYPE_PRIVATE);
            } else if (id == R.id.tvDisAgree) {
                dismissAllowingStateLoss();
                if (listener != null) {
                    listener.onComplete(false, getTag());
                }
            } else if (id == R.id.tvAgree) {
                dismissAllowingStateLoss();
                if (listener != null) {
                    listener.onComplete(true, getTag());
                }
            }
        }
    };
}
