package com.blog.ljtatum.tipcalculator.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.framework.listeners.OnFirebaseValueListener;
import com.app.framework.model.HistoryModel;
import com.app.framework.utilities.FirebaseUtils;
import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.activity.MainActivity;
import com.blog.ljtatum.tipcalculator.adapter.HistoryAdapter;
import com.blog.ljtatum.tipcalculator.utils.DialogUtils;
import com.blog.ljtatum.tipcalculator.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by LJTat on 2/27/2017.
 */

public class HistoryFragment extends BaseFragment implements View.OnClickListener {

    private static final int NUM_HISTORY_RESULTS = 50;
    private static final int NUM_DAYS_TIP_HISTORY = 30; // last 30 days
    private Context mContext;
    private View mRootView;
    private TextView tvFragmentHeader, tvTipWeek, tvAvgPercWeek, tvAvgPercOverall, tvNoHistory;
    private LinearLayout llWrapper;
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
        llWrapper = (LinearLayout) mRootView.findViewById(R.id.ll_wrapper);
        rvHistory = (RecyclerView) mRootView.findViewById(R.id.rv_history);
        tvFragmentHeader = (TextView) mRootView.findViewById(R.id.tv_fragment_header);
        tvTipWeek = (TextView) mRootView.findViewById(R.id.tv_total_tip_week);
        tvAvgPercWeek = (TextView) mRootView.findViewById(R.id.tv_avg_tip_percentage_week);
        tvAvgPercOverall = (TextView) mRootView.findViewById(R.id.tv_avg_tip_percentage_overall);
        tvNoHistory = (TextView) mRootView.findViewById(R.id.tv_no_history);

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

    /**
     * Method is used to initialize listeners and callbacks
     */
    private void initializeListeners() {
        FirebaseUtils.onFirebaseValueListener(new OnFirebaseValueListener() {
            @Override
            public void onUpdateDataChange(DataSnapshot dataSnapshot) {
                // do nothing
            }

            @Override
            public void onUpdateDatabaseError(DatabaseError databaseError) {
                // do nothing
            }

            @SuppressLint("StringFormatInvalid")
            @Override
            public void onRetrieveDataChangeWithFilter(HashMap<String, HistoryModel> map) {
                // dismiss progress dialog
                DialogUtils.dismissProgressDialog();
                if (map.size() > 0 && !map.isEmpty()) {
                    FrameworkUtils.setViewVisible(llWrapper);
                    FrameworkUtils.setViewGone(tvNoHistory);
                } else {
                    FrameworkUtils.setViewVisible(tvNoHistory);
                    FrameworkUtils.setViewGone(llWrapper);
                }

                // update adapter
                alTipHistory = new ArrayList<>(map.values());
                mHistoryAdapter.updateData(alTipHistory);

                int counter = 0;
                int tipPercWeek = 0;
                double tipAmountWeek = 0;
                for (int i = 0; i < alTipHistory.size(); i++) {
                    if (FrameworkUtils.isDateAfterCurrentDate(
                            new Date(System.currentTimeMillis() - NUM_DAYS_TIP_HISTORY * TimeUnit.DAYS.toMillis(1)),
                            alTipHistory.get(i).date, "MM/dd/yyyy hh:mm:ss a")) {

                        counter++;
                        tipAmountWeek = tipAmountWeek + Double.parseDouble(alTipHistory.get(i).tipAmount);
                        tipPercWeek = tipPercWeek + Integer.parseInt(alTipHistory.get(i).tipPercent);
                    }
                }
                // tip amount for the week
                tvTipWeek.setText(tipAmountWeek == 0 ? getResources().getString(R.string.tip_amount_week,
                        getResources().getString(R.string.not_applicable)) :
                        getResources().getString(R.string.tip_amount_week,
                                String.valueOf(FrameworkUtils.convertToDollarFormat(tipAmountWeek))));
                // average tip percent for week
                tvAvgPercWeek.setText(tipPercWeek == 0 ? getResources().getString(R.string.tip_amount_week,
                        getResources().getString(R.string.not_applicable)) :
                        getResources().getString(R.string.avg_tip_percentage_week, String.valueOf(tipPercWeek / counter)));
                // average tip percent overall
                int avgPercOverall = 0;
                for (int i = 0; i < alTipHistory.size(); i++) {
                    avgPercOverall = avgPercOverall + Integer.parseInt(alTipHistory.get(i).tipPercent);
                }
                tvAvgPercOverall.setText(avgPercOverall == 0 ? getResources().getString(R.string.tip_amount_week,
                        getResources().getString(R.string.not_applicable)) :
                        getResources().getString(R.string.avg_tip_percentage_overall,
                                String.valueOf(avgPercOverall / alTipHistory.size()),
                                Utils.getTipQuality(mContext, avgPercOverall / alTipHistory.size())));
            }

            @Override
            public void onRetrieveDataChange(DataSnapshot dataSnapshot) {
                // do nothing
            }

            @Override
            public void onRetrieveDataError(DatabaseError databaseError) {
                // do nothing
            }
        });
    }

    /**
     * Method is used to populate list of tipping history
     */
    private void populateHistoryList() {
        // show progress dialog
        DialogUtils.showProgressDialog(mContext);
        // retrieve trip history for past x-number of days
        FirebaseUtils.retrieveValuesWithFilter(NUM_HISTORY_RESULTS,
                new Date(System.currentTimeMillis() - NUM_DAYS_TIP_HISTORY * TimeUnit.DAYS.toMillis(1)));
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
