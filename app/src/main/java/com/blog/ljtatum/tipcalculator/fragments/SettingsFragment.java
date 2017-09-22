package com.blog.ljtatum.tipcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.app.framework.sharedpref.SharedPref;
import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.activity.MainActivity;
import com.blog.ljtatum.tipcalculator.constants.Constants;
import com.blog.ljtatum.tipcalculator.logger.Logger;

/**
 * Created by LJTat on 2/27/2017.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private TextView tvFragmentHeader;
    private EditText edtTipPercent, edtSharedBy;
    private Switch switchAutoHistory, switchShakeReset, switchRoundOff;
    private SharedPref mSharedPref;


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
        mSharedPref = new SharedPref(mContext, com.app.framework.constants.Constants.PREF_FILE_NAME);

        // instantiate views
        tvFragmentHeader = (TextView) mRootView.findViewById(R.id.tv_fragment_header);
        edtTipPercent = (EditText) mRootView.findViewById(R.id.edt_tip_percent);
        edtSharedBy = (EditText) mRootView.findViewById(R.id.edt_shared_by);
        switchAutoHistory = (Switch) mRootView.findViewById(R.id.switch_auto_history);
        switchShakeReset = (Switch) mRootView.findViewById(R.id.switch_shake_reset);
        switchRoundOff = (Switch) mRootView.findViewById(R.id.switch_round_off);

        // set fragment header
        tvFragmentHeader.setText(getResources().getString(R.string.setting));

        // update settings
        updateSettings();
    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {
        tvFragmentHeader.setOnClickListener(this);
    }


    private void initializeListeners() {
        // TextChangedListener tip percent
        edtTipPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.e("TEST", "<edtTipPercent> s= " + s.toString());
                mSharedPref.setPref(Constants.KEY_DEFAULT_TIP, Integer.parseInt(s.toString()));
            }
        });

        // TextChangedListener shared by
        edtSharedBy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.e("TEST", "<edtSharedBy> s= " + s.toString());
                mSharedPref.setPref(Constants.KEY_DEFAULT_SHARED_BY, Integer.parseInt(s.toString()));
            }
        });
    }

    /**
     * Method is used to update settings
     */
    private void updateSettings() {
        // set default pref
        edtTipPercent.setText(String.valueOf(mSharedPref.getIntPref(Constants.KEY_DEFAULT_TIP, 15)));
        // set default shared by
        edtSharedBy.setText(String.valueOf(mSharedPref.getIntPref(Constants.KEY_DEFAULT_SHARED_BY, 1)));

        // default auto history
        if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_AUTO_HISTORY, true)) {
            // enable
            switchAutoHistory.setChecked(true);
        } else {
            // disable
            switchAutoHistory.setChecked(false);
        }

        // default shake reset
        if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SHAKE_RESET, true)) {
            // enable
            switchShakeReset.setChecked(true);
        } else {
            // disable
            switchShakeReset.setChecked(false);
        }

        // default round off
        if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_ROUND_OFF, true)) {
            // enable
            switchRoundOff.setChecked(true);
        } else {
            // disable
            switchRoundOff.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (!FrameworkUtils.isViewClickable()) {
            return;
        }

        switch (v.getId()) {
            case R.id.tv_fragment_header:
                remove();
                popBackStack();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // disable drawer
        ((MainActivity) mContext).toggleDrawerState(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // enable drawer
        ((MainActivity) mContext).toggleDrawerState(true);
    }

}
