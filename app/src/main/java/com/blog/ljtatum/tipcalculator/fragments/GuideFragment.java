package com.blog.ljtatum.tipcalculator.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.activity.MainActivity;
import com.blog.ljtatum.tipcalculator.adapter.GuideAdapter;
import com.blog.ljtatum.tipcalculator.model.GuideModel;

import java.util.ArrayList;

/**
 * Created by LJTat on 2/27/2017.
 */

public class GuideFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = GuideFragment.class.getSimpleName();

    // array of flag icons
    private static final int[] ARRY_COUNTRY_FLAG_ICONS = {R.drawable.flag_australia, R.drawable.flag_bahamas,
            R.drawable.flag_canada, R.drawable.flag_china, R.drawable.flag_cuba,
            R.drawable.flag_dominican_republic, R.drawable.flag_france, R.drawable.flag_germany,
            R.drawable.flag_ghana, R.drawable.flag_jamaica, R.drawable.flag_japan,
            R.drawable.flag_kenya, R.drawable.flag_mexico, R.drawable.flag_niger,
            R.drawable.flag_spain, R.drawable.flag_united_kingdom, R.drawable.flag_united_states};

    // array of country names
    private static final int[] ARRY_COUNTRY_NAMES = {R.string.australia, R.string.bahamas,
            R.string.canada, R.string.china, R.string.cuba,
            R.string.dominican_republic, R.string.france, R.string.germany,
            R.string.ghana, R.string.jamaica, R.string.japan,
            R.string.kenya, R.string.mexico, R.string.niger,
            R.string.spain, R.string.united_kingdom, R.string.united_states};

    // array of country descriptions
    private static final int[] ARRY_COUNTRY_DESCRIPTIONS = {R.string.guide_australia, R.string.guide_bahamas,
            R.string.guide_canada, R.string.guide_china, R.string.guide_cuba,
            R.string.guide_dominican_republic, R.string.guide_france, R.string.guide_germany,
            R.string.guide_ghana, R.string.guide_jamaica, R.string.guide_japan,
            R.string.guide_kenya, R.string.guide_mexico, R.string.guide_niger,
            R.string.guide_spain, R.string.guide_united_kingdom, R.string.guide_united_states};

    // array of country tip requirements
    private static final int[] ARRY_COUNTRY_TIP_REQUIREMENTS = {R.string.not_required, R.string.required,
            R.string.required, R.string.not_required, R.string.required,
            R.string.required, R.string.required, R.string.required,
            R.string.not_required, R.string.not_required, R.string.not_required,
            R.string.required, R.string.required, R.string.not_required,
            R.string.required, R.string.required, R.string.required};

    private View mRootView;
    private RecyclerView rvGuide;
    private GuideAdapter mGuideAdapter;
    private ArrayList<GuideModel> alGuideModel;
    private TextView tvFragmentHeader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_guide, container, false);

        // instantiate views
        initializeViews();
        initializeHandlers();
        // populate adapter
        populateGuideModelList();

        return mRootView;
    }

    /**
     * Method is used to instantiate views
     */
    private void initializeViews() {
        alGuideModel = new ArrayList<>();

        // instantiate views
        rvGuide = mRootView.findViewById(R.id.rv_guide);
        tvFragmentHeader = mRootView.findViewById(R.id.tv_fragment_header);

        // instantiate adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGuide.setLayoutManager(layoutManager);
        mGuideAdapter = new GuideAdapter(mContext, new ArrayList<GuideModel>());
        rvGuide.setAdapter(mGuideAdapter);

        // set fragment header
        tvFragmentHeader.setText(getResources().getString(R.string.guide));
    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {
        tvFragmentHeader.setOnClickListener(this);
    }

    /**
     * Method is used to populate guide model list
     */
    private void populateGuideModelList() {
        for (int i = 0; i < ARRY_COUNTRY_FLAG_ICONS.length; i++) {
            GuideModel guideModel = new GuideModel();
            guideModel.countryFlagIcon = ARRY_COUNTRY_FLAG_ICONS[i];
            guideModel.countryName = getResources().getString(ARRY_COUNTRY_NAMES[i]);
            guideModel.countryReq = getResources().getString(ARRY_COUNTRY_TIP_REQUIREMENTS[i]);
            guideModel.ccountryDesc = getResources().getString(ARRY_COUNTRY_DESCRIPTIONS[i]);
            alGuideModel.add(guideModel);
        }

        mGuideAdapter = new GuideAdapter(mContext, alGuideModel);
        rvGuide.setAdapter(mGuideAdapter);
        mGuideAdapter.notifyDataSetChanged();
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
        if (!FrameworkUtils.checkIfNull(mOnFragmentRemovedListener)) {
            // set listener
            mOnFragmentRemovedListener.onFragmentRemoved();
        }
        // enable drawer
        ((MainActivity) mContext).toggleDrawerState(true);
    }
}
