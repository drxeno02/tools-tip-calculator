package com.blog.ljtatum.tipcalculator.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.blog.ljtatum.tipcalculator.enums.Enum;

import java.util.List;

/**
 * Created by LJTat on 3/4/2017.
 */

public class ShareUtils {

    /**
     * Method is usd to open social media via intents
     *
     * Facebook profile id:
     * Twitter profile id: drxeno02
     * Linkedin profile id: leonard-tatum-768850105
     *
     * @param socialMedia
     */
    public static void openSocialMediaViaIntent(Context context, Enum.SocialMedia socialMedia) {
        Intent intent = null;
        final PackageManager packageManager = context.getPackageManager();
        try {
            if (socialMedia.equals(Enum.SocialMedia.FB)) {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                String uri;
                if (versionCode >= 3002850) { //newer versions of fb app
                    uri = "fb://facewebmodal/f?href=".concat("https://www.facebook.com/drxeno02");
                } else { //older versions of fb app
                    uri = "fb://page/".concat("drxeno02");
                }
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            } else if (socialMedia.equals(Enum.SocialMedia.TWITTER)) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?drxeno02"));
                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    // otherwise open browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/drxeno02"));
                }
            } else if (socialMedia.equals(Enum.SocialMedia.LINKEDIN)) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://leonard-tatum-768850105"));
                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    // otherwise open browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=leonard-tatum-768850105"));
                }            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
