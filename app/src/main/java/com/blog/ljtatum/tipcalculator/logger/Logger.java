/*
 * Copyright (c) 2014-present, ZTRIP. All rights reserved.
 */

package com.blog.ljtatum.tipcalculator.logger;

import android.util.Log;

import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.constants.Constants;


/**
 * Created by leonard on 11/13/2015.
 * Helper class to print logs to console
 * <p>The order in terms of verbosity, from least to most is ERROR, WARN, INFO, DEBUG, VERBOSE.
 * Verbose should never be compiled into an application except during development. Debug logs
 * are compiled in but stripped at runtime. Error, warning and info logs are always kept</p>
 */
public class Logger {
    /**
     * Helper method for logging e-verbose
     *
     * @param tag The tag of the fragment you want to log
     * @param msg The message to log
     */
    public static void e(String tag, String msg) {
        if (!FrameworkUtils.checkIfNull(msg)) {
            if (Constants.DEBUG) {
                Log.e(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging d-verbose
     *
     * @param tag The tag of the fragment you want to log
     * @param msg The message to log
     */
    public static void d(String tag, String msg) {
        if (!FrameworkUtils.checkIfNull(msg)) {
            if (Constants.DEBUG) {
                Log.d(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging i-verbose
     *
     * @param tag The tag of the fragment you want to log
     * @param msg The message to log
     */
    public static void i(String tag, String msg) {
        if (!FrameworkUtils.checkIfNull(msg)) {
            if (Constants.DEBUG) {
                Log.i(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging v-verbose
     *
     * @param tag The tag of the fragment you want to log
     * @param msg The message to log
     */
    public static void v(String tag, String msg) {
        if (!FrameworkUtils.checkIfNull(msg)) {
            if (Constants.DEBUG) {
                Log.v(tag, msg);
            }
        }
    }

    /**
     * Helper method for logging wtf-verbose
     *
     * @param tag The tag of the fragment you want to log
     * @param msg The message to log
     */
    public static void wtf(String tag, String msg) {
        if (!FrameworkUtils.checkIfNull(msg)) {
            if (Constants.DEBUG) {
                Log.wtf(tag, msg);
            }
        }
    }
}
