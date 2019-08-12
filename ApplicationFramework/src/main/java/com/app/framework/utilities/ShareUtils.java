package com.app.framework.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.framework.R;
import com.app.framework.enums.Enum;

import java.util.List;

public class ShareUtils {

    // social media packages
    private static final String SHARE_URL = "https://play.google.com/store/apps/details?id=";
    private static final String FACEBOOK_SHARER_PHP = "https://www.facebook.com/sharer/sharer.php?u=";
    private static final String FACEBOOK_PACKAGE = "com.facebook.katana";
    private static final String TWITTER_PACKAGE = "com.twitter";
    private static final String TWITTER_PACKAGE_ANDROID = "com.twitter.android";
    private static final String LINKEDIN_PACKAGE = "com.linkedin";
    private static final String LINKEDIN_PACKAGE_ANDROID = "com.linkedin.android";
    private static final String TWITTER_MSG_FORMAT = "https://twitter.com/intent/tweet?text=%s&amp;url=%s";

    // personal social media
    private static final String FACEBOOK_PERSONAL_PAGE = "https://www.facebook.com/drxeno02";
    private static final String TWITTER_PERSONAL_PAGE = "https://twitter.com/drxeno02";
    private static final String LINKEDIN_PERSONAL_PAGE = "http://www.linkedin.com/profile/view?id=leonard-tatum-768850105";

    // intent
    private static final String INTENT_TYPE_TEXT = "text/*";
    private static final String INTENT_TYPE_TEXT_PLAIN = "text/plain";

    /**
     * Method is used to open social media via intents
     *
     * Facebook/Twitter profile id: drxeno02
     * Linkedin profile id: leonard-tatum-768850105
     *
     * @param socialMedia Social media type
     */
    public static void openSocialMediaViaIntent(@NonNull Context context,
                                                @NonNull Enum.SocialMedia socialMedia,
                                                boolean isPersonalSocialMedia) {
        // create intent object
        Intent intent = null;
        // create packageManager object
        final PackageManager packageManager = context.getPackageManager();
        try {
            if (isPersonalSocialMedia) {
                if (socialMedia.equals(Enum.SocialMedia.FB)) {
                    String urlToShare = FACEBOOK_PERSONAL_PAGE;
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType(INTENT_TYPE_TEXT_PLAIN);
                    intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

                    // see if official Facebook is found
                    boolean resolved = false;
                    List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
                    for (ResolveInfo resolveInfo : matches) {
                        if (isResolveInfoValid(resolveInfo) &&
                                resolveInfo.activityInfo.packageName.toLowerCase().startsWith(FACEBOOK_PACKAGE)) {
                            intent.setPackage(resolveInfo.activityInfo.packageName);
                            resolved = true;
                            break;
                        }
                    }

                    // as fallback, launch sharer.php in a browser
                    if (!resolved) {
                        String sharerUrl = FACEBOOK_SHARER_PHP.concat(urlToShare);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                    }
                } else if (socialMedia.equals(Enum.SocialMedia.TWITTER)) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(
                            R.string.twitter_share_msg_personal, TWITTER_PERSONAL_PAGE));
                    intent.setType(INTENT_TYPE_TEXT_PLAIN);

                    // see if official Twitter is found
                    boolean resolved = false;
                    List<ResolveInfo> matches = packageManager.queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : matches) {
                        if (isResolveInfoValid(resolveInfo) &&
                                (resolveInfo.activityInfo.packageName.toLowerCase().startsWith(TWITTER_PACKAGE) ||
                                resolveInfo.activityInfo.packageName.toLowerCase().startsWith(TWITTER_PACKAGE_ANDROID))) {
                            intent.setClassName(resolveInfo.activityInfo.packageName,
                                    resolveInfo.activityInfo.name);
                            resolved = true;
                            break;
                        }
                    }

                    // fallback twitter
                    if (!resolved) {
                        String tweetMsg = String.format(TWITTER_MSG_FORMAT,
                                FrameworkUtils.urlEncode(context.getResources().getString(
                                        R.string.twitter_share_msg_personal, TWITTER_PERSONAL_PAGE)),
                                FrameworkUtils.urlEncode(SHARE_URL.concat(context.getPackageName())));
                        intent.setData(Uri.parse(tweetMsg));
                    }

                } else if (socialMedia.equals(Enum.SocialMedia.LINKEDIN)) {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType(INTENT_TYPE_TEXT);
                    intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(
                            R.string.linkedin_share_msg_personal, LINKEDIN_PERSONAL_PAGE));

                    // see if official Linkedin is found
                    List<ResolveInfo> matches = packageManager.queryIntentActivities(intent, 0);
                    for (ResolveInfo resolveInfo : matches) {
                        if (isResolveInfoValid(resolveInfo) &&
                                (resolveInfo.activityInfo.packageName.toLowerCase().startsWith(LINKEDIN_PACKAGE) ||
                                resolveInfo.activityInfo.packageName.toLowerCase().startsWith(LINKEDIN_PACKAGE_ANDROID))) {
                            intent.setPackage(resolveInfo.activityInfo.packageName);
                            break;
                        }
                    }
                }
            } else {
                if (socialMedia.equals(Enum.SocialMedia.FB)) {
                    String urlToShare = SHARE_URL.concat(context.getPackageName());
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType(INTENT_TYPE_TEXT_PLAIN);
                    intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

                    // see if official Facebook is found
                    boolean resolved = false;
                    List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
                    for (ResolveInfo resolveInfo : matches) {
                        if (isResolveInfoValid(resolveInfo) &&
                                resolveInfo.activityInfo.packageName.toLowerCase().startsWith(FACEBOOK_PACKAGE)) {
                            intent.setPackage(resolveInfo.activityInfo.packageName);
                            resolved = true;
                            break;
                        }
                    }

                    // as fallback, launch sharer.php in a browser
                    if (!resolved) {
                        String sharerUrl = FACEBOOK_SHARER_PHP.concat(urlToShare);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                    }
                } else if (socialMedia.equals(Enum.SocialMedia.TWITTER)) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.twitter_share_msg));
                    intent.setType(INTENT_TYPE_TEXT_PLAIN);

                    // see if official Twitter is found
                    boolean resolved = false;
                    List<ResolveInfo> matches = packageManager.queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : matches) {
                        if (isResolveInfoValid(resolveInfo) &&
                                (resolveInfo.activityInfo.packageName.toLowerCase().startsWith(TWITTER_PACKAGE) ||
                                resolveInfo.activityInfo.packageName.toLowerCase().startsWith(TWITTER_PACKAGE_ANDROID))) {
                            intent.setClassName(resolveInfo.activityInfo.packageName,
                                    resolveInfo.activityInfo.name);
                            resolved = true;
                            break;
                        }
                    }

                    // fallback twitter
                    if (!resolved) {
                        String tweetMsg = String.format(TWITTER_MSG_FORMAT,
                                FrameworkUtils.urlEncode(context.getResources().getString(R.string.twitter_share_msg)),
                                FrameworkUtils.urlEncode(SHARE_URL.concat(context.getPackageName())));
                        intent.setData(Uri.parse(tweetMsg));
                    }

                } else if (socialMedia.equals(Enum.SocialMedia.LINKEDIN)) {
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType(INTENT_TYPE_TEXT);
                    intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(
                            R.string.linkedin_share_msg).concat("/n").concat(SHARE_URL.concat(context.getPackageName())));

                    // see if official Linkedin is found
                    List<ResolveInfo> matches = packageManager.queryIntentActivities(intent, 0);
                    for (ResolveInfo resolveInfo : matches) {
                        if (isResolveInfoValid(resolveInfo) &&
                                (resolveInfo.activityInfo.packageName.toLowerCase().startsWith(LINKEDIN_PACKAGE) ||
                                resolveInfo.activityInfo.packageName.toLowerCase().startsWith(LINKEDIN_PACKAGE_ANDROID))) {
                            intent.setPackage(resolveInfo.activityInfo.packageName);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!FrameworkUtils.checkIfNull(intent) &&
                    !FrameworkUtils.checkIfNull(intent.resolveActivity(context.getPackageManager()))) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent);
            }
        }
    }

    /**
     * Method is used to check if information that is returned from resolving an intent
     * against an IntentFilter is retrievable e.g. not null or empty
     * @return True if Resolveinfo content is not null or empty, otherwise false
     */
    private static boolean isResolveInfoValid(@Nullable ResolveInfo resolveInfo) {
        return !FrameworkUtils.checkIfNull(resolveInfo) &&
                !FrameworkUtils.checkIfNull(resolveInfo.activityInfo) &&
                !FrameworkUtils.isStringEmpty(resolveInfo.activityInfo.packageName);
    }
}
