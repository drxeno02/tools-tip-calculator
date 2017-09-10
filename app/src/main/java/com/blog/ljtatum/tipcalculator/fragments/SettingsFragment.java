package com.blog.ljtatum.tipcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;

/**
 * Created by LJTat on 2/27/2017.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private TextView tvFragmentHeader;
    private EditText edtTipPercent, edtSharedBy;
    private Switch switchAutoHistory, switchShakeReset, switchRoundOff;
    private ImageView ivBack;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // instantiate views
        initializeViews();
        initializeHandlers();
        initializeListeners();
        return mRootView;
    }

    /**
     * Method is used to instantiate views
     */
    private void initializeViews() {
        mContext = getActivity();

        // instantiate views
        tvFragmentHeader = (TextView) mRootView.findViewById(R.id.tv_fragment_header);
        ivBack = (ImageView) mRootView.findViewById(R.id.iv_back);
        edtTipPercent = (EditText) mRootView.findViewById(R.id.edt_tip_percent);
        edtSharedBy = (EditText) mRootView.findViewById(R.id.edt_shared_by);
        switchAutoHistory = (Switch) mRootView.findViewById(R.id.switch_auto_history);
        switchShakeReset = (Switch) mRootView.findViewById(R.id.switch_shake_reset);
        switchRoundOff = (Switch) mRootView.findViewById(R.id.switch_round_off);

        // set fragment header
        tvFragmentHeader.setText(getResources().getString(R.string.setting));
    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {
        ivBack.setOnClickListener(this);
    }


    private void initializeListeners() {

    }

    @Override
    public void onClick(View v) {
        if (!FrameworkUtils.isViewClickable()) {
            return;
        }

        switch (v.getId()) {
            case R.id.iv_back:
                remove();
                popBackStack();
                break;
            default:
                break;
        }
    }

}
