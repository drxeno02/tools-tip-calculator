package com.blog.ljtatum.tipcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.app.framework.sharedpref.SharedPref;
import com.app.framework.utilities.DeviceUtils;
import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.activity.MainActivity;
import com.blog.ljtatum.tipcalculator.constants.Constants;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by LJTat on 2/27/2017.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private TextView tvFragmentHeader, tvAutoHistory, tvShakeReset, tvRoundOff;
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
        tvAutoHistory = (TextView) mRootView.findViewById(R.id.tv_auto_history);
        tvShakeReset = (TextView) mRootView.findViewById(R.id.tv_shake_reset);
        tvRoundOff = (TextView) mRootView.findViewById(R.id.tv_round_off);
        edtTipPercent = (EditText) mRootView.findViewById(R.id.edt_tip_percent);
        edtTipPercent.requestFocus();
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
        // OnEditorActionListener tip percent
        edtTipPercent.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!FrameworkUtils.isStringEmpty(v.getText().toString()) &&
                            Integer.parseInt(v.getText().toString()) != mSharedPref.getIntPref(Constants.KEY_DEFAULT_TIP, 26)) {

                        if (Integer.parseInt(v.getText().toString()) > 25) {
                            // if value is greater than 25
                            mSharedPref.setPref(Constants.KEY_DEFAULT_TIP, 25);
                            // show banner
                            Crouton.showText(getActivity(), "Maximum tip value is 25%. Information saved!", Style.CONFIRM);
                        } else if (Integer.parseInt(v.getText().toString()) < 0) {
                            // if value is less than 0
                            mSharedPref.setPref(Constants.KEY_DEFAULT_TIP, 0);
                            // show banner
                            Crouton.showText(getActivity(), "Minimum tip value is 0%. Information saved!", Style.CONFIRM);
                        } else {
                            // if value is between [0-25)
                            mSharedPref.setPref(Constants.KEY_DEFAULT_TIP, Integer.parseInt(v.getText().toString()));
                            // show banner
                            Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        // OnEditorActionListener shared by
        edtSharedBy.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!FrameworkUtils.isStringEmpty(v.getText().toString()) &&
                            Integer.parseInt(v.getText().toString()) != mSharedPref.getIntPref(Constants.KEY_DEFAULT_SHARED_BY, 100)) {

                        if (Integer.parseInt(v.getText().toString()) > 99) {
                            // if value is greater than 99
                            mSharedPref.setPref(Constants.KEY_DEFAULT_SHARED_BY, 99);
                            // show banner
                            Crouton.showText(getActivity(), "Maximum value is 99. Information saved!", Style.CONFIRM);
                        } else if (Integer.parseInt(v.getText().toString()) <= 0) {
                            // if value is less than equal to 0
                            mSharedPref.setPref(Constants.KEY_DEFAULT_SHARED_BY, 1);
                            // show banner
                            Crouton.showText(getActivity(), "Minimum value is 1. Information saved!", Style.CONFIRM);
                        } else {
                            // if value is between (0-100)
                            mSharedPref.setPref(Constants.KEY_DEFAULT_SHARED_BY, Integer.parseInt(v.getText().toString()));
                            // show banner
                            Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        // OnCheckedChangeListener
        switchAutoHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // update default auto history settings
                mSharedPref.setPref(Constants.KEY_DEFAULT_AUTO_HISTORY, b);
                tvAutoHistory.setText(b ? getActivity().getResources().getString(R.string.settings_auto_history, "Enabled") :
                        getActivity().getResources().getString(R.string.settings_auto_history, "Disabled"));
                // show banner
                Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
            }
        });

        // OnCheckedChangeListener
        switchShakeReset.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // update default shake to reset settings
                mSharedPref.setPref(Constants.KEY_DEFAULT_SHAKE_RESET, b);
                tvShakeReset.setText(b ? getActivity().getResources().getString(R.string.settings_shake, "Enabled") :
                        getActivity().getResources().getString(R.string.settings_shake, "Disabled"));
                // show banner
                Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
            }
        });

        // OnCheckedChangeListener
        switchRoundOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // update default auto round-off settings
                mSharedPref.setPref(Constants.KEY_DEFAULT_ROUND_OFF, b);
                tvRoundOff.setText(b ? getActivity().getResources().getString(R.string.settings_round_off, "Enabled") :
                        getActivity().getResources().getString(R.string.settings_round_off, "Disabled"));
                // show banner
                Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
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

        // update default label values
        tvAutoHistory.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_AUTO_HISTORY, true) ?
                getActivity().getResources().getString(R.string.settings_auto_history, "Enabled") :
                getActivity().getResources().getString(R.string.settings_auto_history, "Disabled"));
        tvShakeReset.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SHAKE_RESET, true) ?
                getActivity().getResources().getString(R.string.settings_shake, "Enabled") :
                getActivity().getResources().getString(R.string.settings_shake, "Disabled"));
        tvRoundOff.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_ROUND_OFF, true) ?
                getActivity().getResources().getString(R.string.settings_round_off, "Enabled") :
                getActivity().getResources().getString(R.string.settings_round_off, "Disabled"));
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
        // show keyboard
        DeviceUtils.showKeyboard(mContext);
        edtTipPercent.requestFocus();
        // disable drawer
        ((MainActivity) mContext).toggleDrawerState(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        // hide keyboard
        DeviceUtils.hideKeyboard(mContext, getActivity().getWindow().getDecorView().getWindowToken());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // hide keyboard
        DeviceUtils.hideKeyboard(mContext, getActivity().getWindow().getDecorView().getWindowToken());
        // enable drawer
        ((MainActivity) mContext).toggleDrawerState(true);
    }

}
