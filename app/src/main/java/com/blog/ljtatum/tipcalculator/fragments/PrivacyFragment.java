package com.blog.ljtatum.tipcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blog.ljtatum.tipcalculator.BuildConfig;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.enums.Enum;
import com.blog.ljtatum.tipcalculator.utils.ShareUtils;
import com.blog.ljtatum.tipcalculator.utils.Utils;

import java.util.Calendar;

/**
 * Created by LJTat on 2/27/2017.
 */
public class PrivacyFragment extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private TextView tvFragmentHeader;
    private ImageView ivBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_privacy, container, false);

        // instantiate views
        initializeViews();
        initializeHandlers();

        return mRootView;
    }

    /**
     * Method is used to instantiate views
     */
    private void initializeViews() {
        tvFragmentHeader = (TextView) mRootView.findViewById(R.id.tv_fragment_header);
        ivBack = (ImageView) mRootView.findViewById(R.id.iv_back);

        // set fragment header
        tvFragmentHeader.setText(getResources().getString(R.string.privacy));
    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {
        ivBack.setOnClickListener(this);
        tvFragmentHeader.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!Utils.isViewClickable()) {
            return;
        }

        switch (view.getId()) {
            case R.id.tv_fragment_header:
            case R.id.iv_back:
                remove();
                popBackStack();
                break;
            default:
                break;
        }
    }
}