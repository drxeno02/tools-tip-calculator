package com.blog.ljtatum.tipcalculator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.framework.enums.Enum;
import com.app.framework.utilities.FrameworkUtils;
import com.app.framework.utilities.ShareUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.activity.MainActivity;

/**
 * Created by LJTat on 2/27/2017.
 */
public class ShareFragment extends BaseFragment implements View.OnClickListener {

    private View mRootView;
    private TextView tvFragmentHeader;
    private ImageView ivFb, ivTwitter, ivLinkedin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_share, container, false);

        // instantiate views
        initializeViews();
        initializeHandlers();

        return mRootView;
    }

    /**
     * Method is used to instantiate views
     */
    private void initializeViews() {
        tvFragmentHeader = mRootView.findViewById(R.id.tv_fragment_header);
        ivFb = mRootView.findViewById(R.id.iv_fb);
        ivTwitter = mRootView.findViewById(R.id.iv_twitter);
        ivLinkedin = mRootView.findViewById(R.id.iv_linkedin);

        // set fragment header
        tvFragmentHeader.setText(getResources().getString(R.string.share));
    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {
        ivFb.setOnClickListener(this);
        ivTwitter.setOnClickListener(this);
        ivLinkedin.setOnClickListener(this);
        tvFragmentHeader.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!FrameworkUtils.isViewClickable()) {
            return;
        }

        switch (view.getId()) {
            case R.id.tv_fragment_header:
                remove();
                popBackStack();
                break;
            case R.id.iv_fb:
                ShareUtils.openSocialMediaViaIntent(mContext, Enum.SocialMedia.FB, false);
                break;
            case R.id.iv_twitter:
                ShareUtils.openSocialMediaViaIntent(mContext, Enum.SocialMedia.TWITTER, false);
                break;
            case R.id.iv_linkedin:
                ShareUtils.openSocialMediaViaIntent(mContext, Enum.SocialMedia.LINKEDIN, false);
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
        if (!FrameworkUtils.checkIfNull(mOnFragmentRemovedListener)) {
            // set listener
            mOnFragmentRemovedListener.onFragmentRemoved();
        }
        // enable drawer
        ((MainActivity) mContext).toggleDrawerState(true);
        super.onDetach();
    }
}
