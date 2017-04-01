package com.blog.ljtatum.tipcalculator.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
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

public class AboutFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private TextView tvAppVersion, tvCopyright, tvFragmentHeader, tvFeedbackEmail;
    private ImageView ivFb, ivTwitter, ivLinkedin, ivBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_about, container, false);

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
        tvFragmentHeader = (TextView) mRootView.findViewById(R.id.tv_fragment_header);
        tvFeedbackEmail = (TextView) mRootView.findViewById(R.id.tv_feedback_email);
        ivFb = (ImageView) mRootView.findViewById(R.id.iv_fb);
        ivTwitter = (ImageView) mRootView.findViewById(R.id.iv_twitter);
        ivLinkedin = (ImageView) mRootView.findViewById(R.id.iv_linkedin);
        ivBack = (ImageView) mRootView.findViewById(R.id.iv_back);

        // set fragment header
        tvFragmentHeader.setText(getResources().getString(R.string.about));
        // set app version
        tvAppVersion.setText(BuildConfig.VERSION_NAME);
        // set copyright year
        tvCopyright.setText(getActivity().getResources().getString(R.string.copyright_year,
                String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));
        // set email link
        final SpannableString email = new SpannableString(
                getActivity().getResources().getString(R.string.feedback_email));
        Linkify.addLinks(email, Linkify.EMAIL_ADDRESSES);
        tvFeedbackEmail.setText(email);
        tvFeedbackEmail.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {
        ivFb.setOnClickListener(this);
        ivTwitter.setOnClickListener(this);
        ivLinkedin.setOnClickListener(this);
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
