package com.blog.ljtatum.tipcalculator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blog.ljtatum.tipcalculator.R;

/**
 * Created by LJTat on 2/27/2017.
 */

public class AboutFragment extends BaseFragment {

    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_about, container, false);

        // initialize views and listeners
        initializeViews();

        return mRootView;
    }

    /**
     * Method is used to instantiate views
     */
    private void initializeViews() {

    }
}
