/*
 * Copyright (c) 2014-present, ZTRIP. All rights reserved.
 */

package com.blog.ljtatum.tipcalculator.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;


/**
 * Created by leonard on 9/28/2015.
 * BaseActivity is extended by all Activities. It provides useful functions such as
 * adding and removing fragments, as well as showing and hiding network and deprecation dialogs.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param fragment The new Fragment that is going to replace the container
     */
    public void addFragment(@NonNull Fragment fragment) {
        // check if the fragment has been added already
        Fragment temp = mFragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());
        if (!FrameworkUtils.checkIfNull(temp) && temp.isAdded()) {
            return;
        }

        // add fragment and transition with animation
        mFragmentManager.beginTransaction().setCustomAnimations(R.anim.ui_slide_in_from_bottom_frag,
                R.anim.ui_slide_out_to_bottom_frag, R.anim.ui_slide_in_from_bottom_frag,
                R.anim.ui_slide_out_to_bottom_frag).add(R.id.frag_container, fragment,
                fragment.getClass().getSimpleName()).addToBackStack(null).commit();
    }

    /**
     * Method is used to pop the top state off the back stack.
     * Returns true if there was one to pop, else false.
     */
    private void popBackStack() {
        mFragmentManager.popBackStack();
    }

    /**
     * Method is used to pop the top state off the back stack.
     * Returns true if there was one to pop, else false.
     */
    public void popBackStack(String name) {
        mFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Method is used to remove a fragment
     *
     * @param fragment The fragment to be removed
     */
    void removeFragment(Fragment fragment) {
        try {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragment).commitAllowingStateLoss();
            popBackStack();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to remove all fragments
     */
    @SuppressLint("RestrictedApi")
    public void removeAllFragments() {
        try {
            for (Fragment fragment : mFragmentManager.getFragments()) {
                if (!FrameworkUtils.checkIfNull(fragment)) {
                    FragmentTransaction ft = mFragmentManager.beginTransaction();
                    ft.remove(fragment).commit();
                    popBackStack(fragment.getTag());
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to retrieve the current fragment the user is on
     *
     * @return Returns the TopFragment if there is one, otherwise returns null
     */
    @SuppressLint("RestrictedApi")
    @Nullable
    public Fragment getTopFragment() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            int i = mFragmentManager.getBackStackEntryCount();
            while (i >= 0) {
                i--;
                Fragment topFragment = mFragmentManager.getFragments().get(i);
                if (!FrameworkUtils.checkIfNull(topFragment)) {
                    return topFragment;
                }
            }
        }
        return null;
    }
}
