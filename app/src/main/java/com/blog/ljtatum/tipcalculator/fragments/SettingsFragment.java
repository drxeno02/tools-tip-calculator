package com.blog.ljtatum.tipcalculator.fragments;

import android.Manifest;
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
import android.widget.Button;
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
import com.blog.ljtatum.tipcalculator.utils.DialogUtils;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by LJTat on 2/27/2017.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {
    // permission code
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 100;

    // views
    private View mRootView;
    private TextView tvFragmentHeader, tvSaveHistory, tvSaveLocation, tvShakeReset, tvRoundOff;
    private ImageView ivSaveHistory, ivSaveLocation;
    private EditText edtTipPercent, edtSharedBy;
    private Switch switchSaveHistory, switchSaveLocation, switchShakeReset, switchRoundOff;
    private Button btnSave;

    private SharedPref mSharedPref;
    private boolean isLocationServicePending;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        mSharedPref = new SharedPref(mContext, com.app.framework.constants.Constants.PREF_FILE_NAME);

        // instantiate views
        tvFragmentHeader = mRootView.findViewById(R.id.tv_fragment_header);
        tvSaveHistory = mRootView.findViewById(R.id.tv_save_history);
        tvSaveLocation = mRootView.findViewById(R.id.tv_save_location);
        tvShakeReset = mRootView.findViewById(R.id.tv_shake_reset);
        tvRoundOff = mRootView.findViewById(R.id.tv_round_off);
        ivSaveHistory = mRootView.findViewById(R.id.iv_save_history);
        ivSaveLocation = mRootView.findViewById(R.id.iv_save_location);
        edtTipPercent = mRootView.findViewById(R.id.edt_tip_percent);
        edtTipPercent.requestFocus();
        edtSharedBy = mRootView.findViewById(R.id.edt_shared_by);
        switchSaveHistory = mRootView.findViewById(R.id.switch_save_history);
        switchSaveLocation = mRootView.findViewById(R.id.switch_save_location);
        switchShakeReset = mRootView.findViewById(R.id.switch_shake_reset);
        switchRoundOff = mRootView.findViewById(R.id.switch_round_off);
        btnSave = mRootView.findViewById(R.id.btn_save);

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
        btnSave.setOnClickListener(this);
    }

    /**
     * Method is used to initialize listeners and callbacks
     */
    private void initializeListeners() {
        // OnEditorActionListener tip percent
        edtTipPercent.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // update tip percent
                    updateTipPercent();
                    return true;
                }
                return false;
            }
        });

        // OnEditorActionListener shared by
        edtSharedBy.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // update shared by
                    updateSharedBy();
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
                tvSaveHistory.setText(b ? getResources().getString(R.string.settings_save_history, "Enabled") :
                        getResources().getString(R.string.settings_save_history, "Disabled"));
                // show banner
                Crouton.showText(mActivity, "Saved!", Style.CONFIRM);
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
                        tvSaveLocation.setText(getResources().getString(R.string.settings_save_location, "Enabled"));
                        // show banner
                        Crouton.showText(mActivity, "Saved!", Style.CONFIRM);

                    }
                } else {
                    if (isLocationServicePending) {
                        isLocationServicePending = false; // reset
                    } else {
                        // update default save location settings
                        mSharedPref.setPref(Constants.KEY_DEFAULT_SAVE_LOCATION, b);
                        tvSaveLocation.setText(getResources().getString(R.string.settings_save_location, "Disabled"));
                        // show banner
                        Crouton.showText(mActivity, "Saved!", Style.CONFIRM);
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
                tvShakeReset.setText(b ? getResources().getString(R.string.settings_shake, "Enabled") :
                        getResources().getString(R.string.settings_shake, "Disabled"));
                // show banner
                Crouton.showText(mActivity, "Saved!", Style.CONFIRM);
            }
        });

        // OnCheckedChangeListener
        switchRoundOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // update default auto round-off settings
                mSharedPref.setPref(Constants.KEY_DEFAULT_ROUND_OFF, b);
                tvRoundOff.setText(b ? getResources().getString(R.string.settings_round_off, "Enabled") :
                        getResources().getString(R.string.settings_round_off, "Disabled"));
                // show banner
                Crouton.showText(mActivity, "Saved!", Style.CONFIRM);
            }
        });
    }

    /**
     * Method is used to update tip percent
     */
    private void updateTipPercent() {
        if (!FrameworkUtils.isStringEmpty(edtTipPercent.getText().toString()) &&
                Integer.parseInt(edtTipPercent.getText().toString()) !=
                        mSharedPref.getIntPref(Constants.KEY_DEFAULT_TIP, 26)) {

            if (Integer.parseInt(edtTipPercent.getText().toString()) > 25) {
                // if value is greater than 25
                mSharedPref.setPref(Constants.KEY_DEFAULT_TIP, 25);
                // show banner
                Crouton.showText(mActivity, "Maximum tip value is 25%. Information saved!", Style.CONFIRM);
            } else if (Integer.parseInt(edtTipPercent.getText().toString()) < 0) {
                // if value is less than 0
                mSharedPref.setPref(Constants.KEY_DEFAULT_TIP, 0);
                // show banner
                Crouton.showText(mActivity, "Minimum tip value is 0%. Information saved!", Style.CONFIRM);
            } else {
                // if value is between [0-25)
                mSharedPref.setPref(Constants.KEY_DEFAULT_TIP, Integer.parseInt(edtTipPercent.getText().toString()));
                // show banner
                Crouton.showText(mActivity, "Saved!", Style.CONFIRM);
            }
        }
    }

    /**
     * Method is used to update shared by
     */
    private void updateSharedBy() {
        if (!FrameworkUtils.isStringEmpty(edtSharedBy.getText().toString()) &&
                Integer.parseInt(edtSharedBy.getText().toString()) !=
                        mSharedPref.getIntPref(Constants.KEY_DEFAULT_SHARED_BY, 100)) {

            if (Integer.parseInt(edtSharedBy.getText().toString()) > 99) {
                // if value is greater than 99
                mSharedPref.setPref(Constants.KEY_DEFAULT_SHARED_BY, 99);
                // show banner
                Crouton.showText(mActivity, "Maximum value is 99. Information saved!", Style.CONFIRM);
            } else if (Integer.parseInt(edtSharedBy.getText().toString()) <= 0) {
                // if value is less than equal to 0
                mSharedPref.setPref(Constants.KEY_DEFAULT_SHARED_BY, 1);
                // show banner
                Crouton.showText(mActivity, "Minimum value is 1. Information saved!", Style.CONFIRM);
            } else {
                // if value is between (0-100)
                mSharedPref.setPref(Constants.KEY_DEFAULT_SHARED_BY, Integer.parseInt(edtSharedBy.getText().toString()));
                // show banner
                Crouton.showText(mActivity, "Saved!", Style.CONFIRM);
            }
        }
    }

    /**
     * Method is used to show dialog that location services is not enabled
     */
    private void showLocationServiceDisabledDialog() {
        DialogUtils.showYesNoAlert(mContext, getResources().getString(R.string.settings),
                getString(R.string.enable_location_services_message, getResources().getString(R.string.app_name)),
                getResources().getString(R.string.ok), getResources().getString(R.string.cancel),
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
                getResources().getString(R.string.settings_save_history, "Enabled") :
                getResources().getString(R.string.settings_save_history, "Disabled"));
        tvSaveLocation.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SAVE_LOCATION, false) ?
                getResources().getString(R.string.settings_save_location, "Enabled") :
                getResources().getString(R.string.settings_save_location, "Disabled"));
        tvShakeReset.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SHAKE_RESET, true) ?
                getResources().getString(R.string.settings_shake, "Enabled") :
                getResources().getString(R.string.settings_shake, "Disabled"));
        tvRoundOff.setText(mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_ROUND_OFF, true) ?
                getResources().getString(R.string.settings_round_off, "Enabled") :
                getResources().getString(R.string.settings_round_off, "Disabled"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_LOCATION) {
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
                        tvSaveLocation.setText(getResources().getString(R.string.settings_save_location, "Enabled"));
                        // show banner
                        Crouton.showText(mActivity, "Saved!", Style.CONFIRM);
                        // only track location if save location setting is true
                        ((MainActivity) mContext).connectGoogleClient();

                    }
                }

            });
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
                Crouton.showText(mActivity, getResources().getString(R.string.settings_save_history_info), Style.INFO);
                break;
            case R.id.iv_save_location:
                Crouton.showText(mActivity, getResources().getString(R.string.settings_save_location_info), Style.INFO);
                break;
            case R.id.btn_save:
                // save updated values
                updateTipPercent();
                updateSharedBy();
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
                tvSaveLocation.setText(getResources().getString(R.string.settings_save_location, "Disabled"));
                // show banner
                Crouton.showText(mActivity, "Location services not enabled", Style.ALERT);

            } else {
                // update default save location settings
                mSharedPref.setPref(Constants.KEY_DEFAULT_SAVE_LOCATION, true);
                tvSaveLocation.setText(getResources().getString(R.string.settings_save_location, "Enabled"));
                // show banner
                Crouton.showText(mActivity, "Saved!", Style.CONFIRM);
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
        DeviceUtils.hideKeyboard(mContext, mActivity.getWindow().getDecorView().getWindowToken());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!FrameworkUtils.checkIfNull(mOnFragmentRemovedListener)) {
            // set listener
            mOnFragmentRemovedListener.onFragmentRemoved();
        }
        // hide keyboard
        DeviceUtils.hideKeyboard(mContext, mActivity.getWindow().getDecorView().getWindowToken());
        // enable drawer
        ((MainActivity) mContext).toggleDrawerState(true);
    }

}
