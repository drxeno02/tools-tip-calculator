package com.blog.ljtatum.tipcalculator.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;

/**
 * Created by leonard on 9/28/2015.
 * BaseActivity is extended by all Activities. It provides useful functions such as
 * adding and removing fragments, as well as showing and hiding network and deprecation dialogs.
 */
@SuppressLint("RestrictedApi")
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
     * Method is used to retrieve the current fragment the user is on
     *
     * @return Returns the TopFragment if there is one, otherwise returns null
     */
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
