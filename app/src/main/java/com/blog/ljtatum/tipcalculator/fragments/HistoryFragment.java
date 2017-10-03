package com.blog.ljtatum.tipcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.activity.MainActivity;
import com.blog.ljtatum.tipcalculator.adapter.HistoryAdapter;
import com.blog.ljtatum.tipcalculator.model.HistoryModel;

import java.util.ArrayList;

/**
 * Created by LJTat on 2/27/2017.
 */

public class HistoryFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private TextView tvFragmentHeader, tvTipWeek, tvAvgPercWeek, tvAvgPercOverall;
    private RecyclerView rvHistory;
    private HistoryAdapter mHistoryAdapter;
    private ArrayList<HistoryModel> alTipHistory;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_history, container, false);

        // instantiate views
        initializeViews();
        initializeHandlers();
        initializeListeners();
        // populate adapter
        populateHistoryList();

        return mRootView;
    }

    /**
     * Method is used to instantiate views
     */
    private void initializeViews() {
        mContext = getActivity();
        alTipHistory = new ArrayList<>();

        // instantiate views
        rvHistory = (RecyclerView) mRootView.findViewById(R.id.rv_history);
        tvFragmentHeader = (TextView) mRootView.findViewById(R.id.tv_fragment_header);
        tvTipWeek = (TextView) mRootView.findViewById(R.id.tv_total_tip_week);
        tvAvgPercWeek = (TextView) mRootView.findViewById(R.id.tv_avg_tip_percentage_week);
        tvAvgPercOverall = (TextView) mRootView.findViewById(R.id.tv_avg_tip_percentage_overall);

        // instantiate adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvHistory.setLayoutManager(layoutManager);
        mHistoryAdapter = new HistoryAdapter(mContext, new ArrayList<HistoryModel>());
        rvHistory.setAdapter(mHistoryAdapter);

        // set fragment header
        tvFragmentHeader.setText(getResources().getString(R.string.history));
    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {
        tvFragmentHeader.setOnClickListener(this);
    }


    private void initializeListeners() {

    }

    private void populateHistoryList() {
        mHistoryAdapter = new HistoryAdapter(mContext, alTipHistory);
        rvHistory.setAdapter(mHistoryAdapter);
        mHistoryAdapter.notifyDataSetChanged();
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
