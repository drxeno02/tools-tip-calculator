package com.blog.ljtatum.tipcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.utils.Utils;

/**
 * Created by LJTat on 2/27/2017.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private TextView tvFragmentHeader;
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

        // instantiate adapter


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
        if (!Utils.isViewClickable()) {
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
