package com.blog.ljtatum.tipcalculator.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.listeners.OnFragmentRemoved;
import com.blog.ljtatum.tipcalculator.listeners.ShakeEventListener;
import com.blog.ljtatum.tipcalculator.logger.Logger;

/**
 * Created by LJTat on 3/3/2017.
 */

public class BaseFragment extends Fragment {

    protected static OnFragmentRemoved mOnFragmentRemovedListener;

    /**
     * Method is used to set callback for when fragment(s) are removed
     * @param listener Callback for when fragment(s) are removed
     */
    public static void onFragmentRemoved(OnFragmentRemoved listener) {
        mOnFragmentRemovedListener = listener;
    }

    /**
     * Method is used to pop the top state off the back stack. Returns true if there
     * was one to pop, else false. This function is asynchronous -- it enqueues the
     * request to pop, but the action will not be performed until the application
     * returns to its event loop.
     */
    public void popBackStack() {
        if (!FrameworkUtils.checkIfNull(getActivity())) {
            try {
                getActivity().getSupportFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for removing the Fragment view
     */
    void remove() {
        if (!FrameworkUtils.checkIfNull(getActivity())) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.ui_slide_in_from_bottom_frag, R.anim.ui_slide_out_to_bottom_frag);
            ft.remove(this).commitAllowingStateLoss();
        }
    }
}
