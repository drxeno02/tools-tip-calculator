package com.blog.ljtatum.tipcalculator.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.framework.sharedpref.SharedPref;
import com.app.framework.utilities.DeviceUtils;
import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.activity.MainActivity;
import com.blog.ljtatum.tipcalculator.constants.Constants;
import com.blog.ljtatum.tipcalculator.logger.Logger;
import com.blog.ljtatum.tipcalculator.utils.DialogUtils;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by LJTat on 2/27/2017.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 100; // permissions
    private Context mContext;
    private View mRootView;
    private TextView tvFragmentHeader, tvSaveHistory, tvSaveLocation, tvShakeReset, tvRoundOff;
    private ImageView ivSaveHistory, ivSaveLocation;
    private EditText edtTipPercent, edtSharedBy;
    private Switch switchSaveHistory, switchSaveLocation, switchShakeReset, switchRoundOff;
    private SharedPref mSharedPref;
    private boolean isLocationServicePending;

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
        tvSaveHistory = (TextView) mRootView.findViewById(R.id.tv_save_history);
        tvSaveLocation = (TextView) mRootView.findViewById(R.id.tv_save_location);
        tvShakeReset = (TextView) mRootView.findViewById(R.id.tv_shake_reset);
        tvRoundOff = (TextView) mRootView.findViewById(R.id.tv_round_off);
        ivSaveHistory = (ImageView) mRootView.findViewById(R.id.iv_save_history);
        ivSaveLocation = (ImageView) mRootView.findViewById(R.id.iv_save_location);
        edtTipPercent = (EditText) mRootView.findViewById(R.id.edt_tip_percent);
        edtTipPercent.requestFocus();
        edtSharedBy = (EditText) mRootView.findViewById(R.id.edt_shared_by);
        switchSaveHistory = (Switch) mRootView.findViewById(R.id.switch_save_history);
        switchSaveLocation = (Switch) mRootView.findViewById(R.id.switch_save_location);
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
        ivSaveHistory.setOnClickListener(this);
        ivSaveLocation.setOnClickListener(this);
    }

    /**
     * Method is used to initialize listeners and callbacks
     */
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
        switchSaveHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // update default save history settings
                mSharedPref.setPref(Constants.KEY_DEFAULT_SAVE_HISTORY, b);
                tvSaveHistory.setText(b ? getActivity().getResources().getString(R.string.settings_save_history, "Enabled") :
                        getActivity().getResources().getString(R.string.settings_save_history, "Disabled"));
                // show banner
                Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
            }
        });

        // OnCheckedChangeListener
        switchSaveLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // check location services
                if (b) {
                    // request location permission if permission is not enabled
                    if (!FrameworkUtils.checkAppPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION};
                        requestPermissions(permissions, PERMISSION_REQUEST_CODE_LOCATION);
                    } else if (!DeviceUtils.isLocationServiceEnabled(mContext)) {
                        isLocationServicePending = true;
                        showLocationServiceDisabledDialog();
                    } else {
                        // update default save location settings
                        mSharedPref.setPref(Constants.KEY_DEFAULT_SAVE_LOCATION, b);
                        tvSaveLocation.setText(b ? getActivity().getResources().getString(R.string.settings_save_location, "Enabled") :
                                getActivity().getResources().getString(R.string.settings_save_location, "Disabled"));
                        // show banner
                        Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);

                    }
                } else {
                    if (isLocationServicePending) {
                        isLocationServicePending = false; // reset
                    } else {
                        // update default save location settings
                        mSharedPref.setPref(Constants.KEY_DEFAULT_SAVE_LOCATION, b);
                        tvSaveLocation.setText(b ? getActivity().getResources().getString(R.string.settings_save_location, "Enabled") :
                                getActivity().getResources().getString(R.string.settings_save_location, "Disabled"));
                        // show banner
                        Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
                    }
                }
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
     * Method is used to show dialog that location services is not enabled
     */
    private void showLocationServiceDisabledDialog() {
        DialogUtils.showYesNoAlert(mContext, getResources().getString(R.string.settings),
                getString(R.string.enable_location_services_message, mContext.getResources().getString(R.string.app_name)),
                mContext.getResources().getString(R.string.ok), mContext.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogUtils.dismissDialog();
                        String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                        startActivity(new Intent(action));
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogUtils.dismissDialog();
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

        // default save history: TRUE
        if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SAVE_HISTORY, true)) {
            // enable
            switchSaveHistory.setChecked(true);
        } else {
            // disable
            switchSaveHistory.setChecked(false);
        }

        // default save location: FALSE
        if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SAVE_LOCATION, false)) {
            // enable
            switchSaveLocation.setChecked(true);
        } else {
            // disable
            switchSaveLocation.setChecked(false);
        }


        // default shake reset: TRUE
        if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SHAKE_RESET, true)) {
            // enable
            switchShakeReset.setChecked(true);
        } else {
            // disable
            switchShakeReset.setChecked(false);
        }

        // default round off: TRUE
        if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_ROUND_OFF, true)) {
            // enable
            switchRoundOff.setChecked(true);
        } else {
            // disable
            switchRoundOff.setChecked(false);
        }

        // update default label values
        tvSaveHistory.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SAVE_HISTORY, true) ?
                getActivity().getResources().getString(R.string.settings_save_history, "Enabled") :
                getActivity().getResources().getString(R.string.settings_save_history, "Disabled"));
        tvSaveLocation.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SAVE_LOCATION, false) ?
                getActivity().getResources().getString(R.string.settings_save_location, "Enabled") :
                getActivity().getResources().getString(R.string.settings_save_location, "Disabled"));
        tvShakeReset.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SHAKE_RESET, true) ?
                getActivity().getResources().getString(R.string.settings_shake, "Enabled") :
                getActivity().getResources().getString(R.string.settings_shake, "Disabled"));
        tvRoundOff.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_ROUND_OFF, true) ?
                getActivity().getResources().getString(R.string.settings_round_off, "Enabled") :
                getActivity().getResources().getString(R.string.settings_round_off, "Disabled"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_LOCATION:
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!DeviceUtils.isLocationServiceEnabled(mContext)) {
                            isLocationServicePending = true;
                            showLocationServiceDisabledDialog();
                        } else if (FrameworkUtils.checkAppPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            // update default save location settings
                            mSharedPref.setPref(Constants.KEY_DEFAULT_SAVE_LOCATION, true);
                            tvSaveLocation.setText(getActivity().getResources().getString(R.string.settings_save_location, "Enabled"));
                            // show banner
                            Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
                            // only track location if save location setting is true
                            ((MainActivity) mContext).connectGoogleClient();

                        }
                    }

                });
                break;
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
            case R.id.iv_save_history:
                Crouton.showText(getActivity(), getActivity().getResources().getString(R.string.settings_save_history_info), Style.INFO);
                break;
            case R.id.iv_save_location:
                Crouton.showText(getActivity(), getActivity().getResources().getString(R.string.settings_save_location_info), Style.INFO);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isLocationServicePending) {
//            isLocationServicePending = false; // reset
            if (!DeviceUtils.isLocationServiceEnabled(mContext)) {
                // update default save location settings
                mSharedPref.setPref(Constants.KEY_DEFAULT_SAVE_LOCATION, false);
                tvSaveLocation.setText(getActivity().getResources().getString(R.string.settings_save_location, "Disabled"));
                // show banner
                Crouton.showText(getActivity(), "Location services not enabled", Style.ALERT);

            } else {
                // update default save location settings
                mSharedPref.setPref(Constants.KEY_DEFAULT_SAVE_LOCATION, true);
                tvSaveLocation.setText(getActivity().getResources().getString(R.string.settings_save_location, "Enabled"));
                // show banner
                Crouton.showText(getActivity(), "Saved!", Style.CONFIRM);
                // only track location if save location setting is true
                ((MainActivity) mContext).connectGoogleClient();
            }

            updateSettings();
        }

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
        if (!FrameworkUtils.checkIfNull(mOnFragmentRemovedListener)) {
            // set listener
            mOnFragmentRemovedListener.onFragmentRemoved();
        }
        // hide keyboard
        DeviceUtils.hideKeyboard(mContext, getActivity().getWindow().getDecorView().getWindowToken());
        // enable drawer
        ((MainActivity) mContext).toggleDrawerState(true);
    }

}
