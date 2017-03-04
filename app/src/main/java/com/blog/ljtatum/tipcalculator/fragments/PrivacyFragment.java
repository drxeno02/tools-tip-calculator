package com.blog.ljtatum.tipcalculator.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.activity.MainActivity;
import com.blog.ljtatum.tipcalculator.enums.Enum;
import com.blog.ljtatum.tipcalculator.utils.ShareUtils;
import com.blog.ljtatum.tipcalculator.utils.Utils;

/**
 * Created by LJTat on 2/27/2017.
 */
public class PrivacyFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private TextView tvAppVersion, tvCopyright;
    private ImageView ivFb, ivTwitter, ivLinkedin;

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
        mContext = getActivity();
        tvAppVersion = (TextView) mRootView.findViewById(R.id.tv_app_version);
        tvCopyright = (TextView) mRootView.findViewById(R.id.tv_copyright);
        ivFb = (ImageView) mRootView.findViewById(R.id.iv_fb);
        ivTwitter = (ImageView) mRootView.findViewById(R.id.iv_twitter);
        ivLinkedin = (ImageView) mRootView.findViewById(R.id.iv_linkedin);

    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {

    }

    @Override
    public void onClick(View view) {
        if (!Utils.isViewClickable()) {
            return;
        }

        switch (view.getId()) {
            case R.id.iv_fb:
                ShareUtils.openSocialMediaViaIntent(mContext, Enum.SocialMedia.FB);
                break;
            case R.id.iv_twitter:
                ShareUtils.openSocialMediaViaIntent(mContext, Enum.SocialMedia.TWITTER);
                break;
            case R.id.iv_linkedin:
                ShareUtils.openSocialMediaViaIntent(mContext, Enum.SocialMedia.LINKEDIN);
                break;
            default:
                break;
        }
    }
}