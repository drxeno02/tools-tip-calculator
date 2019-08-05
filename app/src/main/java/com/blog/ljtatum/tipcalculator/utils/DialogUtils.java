package com.blog.ljtatum.tipcalculator.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.framework.utilities.FrameworkUtils;
import com.app.framework.utilities.NetworkUtils;
import com.blog.ljtatum.tipcalculator.R;


/**
 * Created by Leonard on 8/2/2016.
 */
public class DialogUtils {
    @Nullable
    private static Dialog mDialog;
    // click listener for default dialog
    private static final DialogInterface.OnClickListener mDefaultListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismissDialog();
        }
    };
    @Nullable
    private static AlertDialog mNetworkDialog;
    private static ProgressDialog mProgressDialog; // display during processing requests/responses

    /**
     * Method is used to dismiss dialog
     */
    public static void dismissDialog() {
        try {
            if (!FrameworkUtils.checkIfNull(mDialog) && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (@NonNull IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to dismiss progress dialog
     */
    public static void dismissProgressDialog() {
        try {
            if (!FrameworkUtils.checkIfNull(mProgressDialog) && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (@NonNull IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to dismiss no network dialog
     */
    public static void dismissNoNetworkDialog() {
        try {
            if (!FrameworkUtils.checkIfNull(mNetworkDialog) && mNetworkDialog.isShowing()) {
                mNetworkDialog.dismiss();
                mNetworkDialog = null;
            }
        } catch (@NonNull IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context Interface to global information about an application environment
     * @param title   The displayed title
     * @param msg     The displayed message
     */
    public static void showDefaultNoNetworkAlert(@NonNull final Context context, String title, String msg) {
        if (((Activity) context).isFinishing() || (!FrameworkUtils.checkIfNull(mNetworkDialog) && mNetworkDialog.isShowing())) {
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.retry), null);
        // create builder and set equal to dialog
        mNetworkDialog = builder.create();

        if (!FrameworkUtils.checkIfNull(mNetworkDialog)) {
            if (!((Activity) context).isFinishing()) {
                // show dialog
                mNetworkDialog.show();
            }

            mNetworkDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetworkUtils.isNetworkAvailable(context) && NetworkUtils.isConnected(context)) {
                        dismissNoNetworkDialog();
                    }
                }
            });
        }
    }


    /**
     * Dialog constructor
     *
     * @param context Interface to global information about an application environment
     * @param title   The displayed title
     * @param msg     The displayed message
     */
    public static void showDefaultOKAlert(@NonNull Context context, String title, String msg) {
        showDefaultOKAlert(context, title, msg, null);
    }

    /**
     * Dialog constructor
     *
     * @param context  Interface to global information about an application environment
     * @param title    The displayed title
     * @param msg      The displayed message
     * @param listener Interface used to allow the creator of a dialog to run some code
     *                 when an item on the dialog is clicked
     */
    public static void showDefaultOKAlert(@NonNull Context context, String title, String msg, DialogInterface.OnClickListener listener) {
        if (((Activity) context).isFinishing() || (!FrameworkUtils.checkIfNull(mDialog) && mDialog.isShowing())) {
            return;
        }
        if (FrameworkUtils.checkIfNull(listener)) {
            listener = mDefaultListener;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), listener);
        // create builder and set equal to dialog
        mDialog = builder.create();
        if (!((Activity) context).isFinishing() && !FrameworkUtils.checkIfNull(mDialog)) {
            // show dialog
            mDialog.show();
        }
    }

    /**
     * Method is used to construct dialog with a message, and both negative and positive buttons
     *
     * @param context     Interface to global information about an application environment
     * @param msg         The displayed message
     * @param yesListener Interface used to allow the creator of a dialog to run some code
     *                    when an item on the dialog is clicked
     * @param noListener  Interface used to allow the creator of a dialog to run some code
     *                    when an item on the dialog is clicked
     */
    public static void showYesNoAlert(@NonNull Context context, String title, String msg, String yesText, String noText,
                                      DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        if (((Activity) context).isFinishing() || (!FrameworkUtils.checkIfNull(mDialog) && mDialog.isShowing())) {
            return;
        }

        String yes = !FrameworkUtils.isStringEmpty(yesText) ? yesText :
                context.getResources().getString(R.string.yes);
        String no = !FrameworkUtils.isStringEmpty(noText) ? noText :
                context.getResources().getString(R.string.no);

        if (FrameworkUtils.checkIfNull(yesListener)) {
            yesListener = mDefaultListener;
        }
        if (FrameworkUtils.checkIfNull(noListener)) {
            noListener = mDefaultListener;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(yes, yesListener)
                .setNegativeButton(no, noListener);
        // create builder and set equal to dialog
        mDialog = builder.create();
        if (!((Activity) context).isFinishing() && !FrameworkUtils.checkIfNull(mDialog)) {
            // show dialog
            mDialog.show();
        }
    }

    /**
     * Method is used to display progress dialog. Call when processing requests/responses
     *
     * @param context Interface to global information about an application environment
     */
    public static void showProgressDialog(@NonNull Context context) {
        if (((Activity) context).isFinishing() || (!FrameworkUtils.checkIfNull(mProgressDialog) && mProgressDialog.isShowing())) {
            return;
        }

        try {
            mProgressDialog = ProgressDialog.show(context, null, null, true, false);
            if (!FrameworkUtils.checkIfNull(mProgressDialog.getWindow())) {
                mProgressDialog.getWindow().setBackgroundDrawable(FrameworkUtils.getDrawable(context, R.color.transparent));
                mProgressDialog.setContentView(R.layout.dialog_progress);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
