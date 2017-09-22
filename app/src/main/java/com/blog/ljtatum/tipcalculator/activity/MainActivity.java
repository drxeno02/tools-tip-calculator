package com.blog.ljtatum.tipcalculator.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.app.framework.sharedpref.SharedPref;
import com.app.framework.utilities.AppRaterUtil;
import com.app.framework.utilities.DeviceUtils;
import com.app.framework.utilities.FirebaseUtils;
import com.app.framework.utilities.FrameworkUtils;
import com.app.framework.utilities.NetworkUtils;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.constants.Constants;
import com.blog.ljtatum.tipcalculator.constants.Durations;
import com.blog.ljtatum.tipcalculator.fragments.AboutFragment;
import com.blog.ljtatum.tipcalculator.fragments.GuideFragment;
import com.blog.ljtatum.tipcalculator.fragments.HistoryFragment;
import com.blog.ljtatum.tipcalculator.fragments.PrivacyFragment;
import com.blog.ljtatum.tipcalculator.fragments.SettingsFragment;
import com.blog.ljtatum.tipcalculator.fragments.ShareFragment;
import com.blog.ljtatum.tipcalculator.listeners.ShakeEventListener;
import com.blog.ljtatum.tipcalculator.logger.Logger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by LJTat on 2/23/2017.
 */
public class MainActivity extends BaseActivity implements OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{
    public static String TAG = MainActivity.class.getSimpleName();

    private static final String AD_ID_TEST = "950036DB8197D296BE390357BD9A964E";

    private Context mContext;
    private int sharedNum = 1; // default value
    private int intSelected;
    private int integerPlaces; // track number of integers in editText
    private int decimalPlaces; // track number of decimals in editText
    private static final int[] ARRY_DRAWER_ICONS = {R.drawable.food_01, R.drawable.food_02,
            R.drawable.food_03, R.drawable.food_04, R.drawable.food_05, R.drawable.food_06,
            R.drawable.food_07, R.drawable.food_08, R.drawable.food_09, R.drawable.food_10,
            R.drawable.food_11, R.drawable.food_12, R.drawable.food_13, R.drawable.food_14,
            R.drawable.food_15, R.drawable.food_16, R.drawable.food_17, R.drawable.food_18,
            R.drawable.food_19, R.drawable.food_20, R.drawable.food_21, R.drawable.food_22,
            R.drawable.food_23};

    private double doubleBill, temp1, temp2, temp3, temp4, temp5;
    private boolean clear, specialCase;

    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;

    private Button btnInc; // increase # of shared values
    private Button btnDec; // decrease # of shared values
    private Button btnClear; // clears all fields

    private AdView adView; // container for banner ads

    private TextView tvValue; // container for sharedNum
    private TextView tvService; // container for rating
    private TextView tvPercent; // container for tip percent
    private TextView tvTip; // container for tip amount
    private TextView tvPerson; // container for person pay amount
    private TextView tvTotal; // container for total bill amount

    private EditText edtBill;

    private String strValue;
    private String strFormatted;
    private String strTip; // used for format tip textView
    private String strPerson; // used for format person textView
    private String strTotal; // used for format total bill textView

    private Spinner spinner;
    private Switch switchRoundOff;
    private DrawerLayout mDrawerLayout;

    private ArrayList<String> alSpinnerItems;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private Vibrator v;
    private SharedPref mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        // initialize views and listeners
        initializeViews();
        initializeHandlers();
        initializeListeners();
    }

    /**
     * Method is used to initialize views
     */
    private void initializeViews() {
        mContext = MainActivity.this;
        alSpinnerItems = new ArrayList<>();
        mSharedPref = new SharedPref(mContext, com.app.framework.constants.Constants.PREF_FILE_NAME);

        // rate this app
        new AppRaterUtil(mContext);
        switchRoundOff = (Switch) findViewById(R.id.switch_round_off);
        ivStar1 = (ImageView) findViewById(R.id.iv_star_1);
        ivStar2 = (ImageView) findViewById(R.id.iv_star_2);
        ivStar3 = (ImageView) findViewById(R.id.iv_star_3);
        ivStar4 = (ImageView) findViewById(R.id.iv_star_4);
        ivStar5 = (ImageView) findViewById(R.id.iv_star_5);
        tvTip = (TextView) findViewById(R.id.tv_meta_tip);
        tvPerson = (TextView) findViewById(R.id.tv_meta_person);
        tvTotal = (TextView) findViewById(R.id.tv_meta_total);
        edtBill = (EditText) findViewById(R.id.edt_bill);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnInc = (Button) findViewById(R.id.btn_inc);
        btnDec = (Button) findViewById(R.id.btn_dec);
        tvValue = (TextView) findViewById(R.id.tv_meta_num);

        // spinner
        tvService = (TextView) findViewById(R.id.tv_meta_rating);
        tvPercent = (TextView) findViewById(R.id.tv_meta_percent);
        spinner = (Spinner) findViewById(R.id.spinner);
        populateSpinner();

        // drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                edtBill.requestFocus();
                // show keyboard
                DeviceUtils.showKeyboard(mContext);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // hide keyboard
                DeviceUtils.hideKeyboard(mContext, getWindow().getDecorView().getWindowToken());
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        // instantiate vibrator
        v = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        // ad banner
        adView = (AdView) this.findViewById(R.id.ad_view);
        try {
            if (NetworkUtils.isNetworkAvailable(mContext)
                    && NetworkUtils.isConnected(mContext)) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // request banner ads
                        AdRequest adRequestBanner;
                        if (Constants.DEBUG) {
                            // load test ad
                            adRequestBanner = new AdRequest.Builder().addTestDevice(AD_ID_TEST).build();
                        } else {
                            // load production ad
                            adRequestBanner = new AdRequest.Builder().build();
                        }
                        // load banner ads
                        adView.loadAd(adRequestBanner);
                    }
                }, Durations.DELAY_MS_500);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            adView.setBackgroundResource(R.drawable.banner);
        }

    }

    /**
     * Method is used to set click listeners
     */
    private void initializeHandlers() {
        btnInc.setOnClickListener(this);
        btnDec.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        ivStar1.setOnClickListener(this);
        ivStar2.setOnClickListener(this);
        ivStar3.setOnClickListener(this);
        ivStar4.setOnClickListener(this);
        ivStar5.setOnClickListener(this);

        // navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        setupDrawerIcons(navigationView);
    }

    /**
     * Method is used to initialize listeners and callbacks
     */
    private void initializeListeners() {
        // set on shake listener
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake() {
                v.vibrate(Durations.DELAY_MS_500); //vibrate for 500 milliseconds
                clear = true;
                calculate();
            }
        });

        // OnEditorActionListener
        edtBill.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // hide keyboard
                    DeviceUtils.hideKeyboard(mContext, getWindow().getDecorView().getWindowToken());
                }
                return false;
            }
        });

        // TextChangedListener
        edtBill.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                calculate();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtBill.getText().toString().contentEquals(".")) {
                    // handle special case
                    edtBill.setText("0.00");
                } else if (edtBill.getText().toString() != null
                        && edtBill.getText().toString().length() > 0) {
                    editTextUpdate();
                }
            }
        });

        // OnCheckedChangeListener
        switchRoundOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // hide keyboard
                DeviceUtils.hideKeyboard(mContext, getWindow().getDecorView().getWindowToken());
                calculate();
            }
        });
    }

    /**
     * Method is used to enable/disable drawer
     *
     * @param isEnabled
     */
    public void toggleDrawerState(boolean isEnabled) {
        if (!FrameworkUtils.checkIfNull(mDrawerLayout)) {
            if (isEnabled) {
                // only unlock (enable) drawer interaction if it is disabled
                if (mDrawerLayout.getDrawerLockMode(GravityCompat.START) != DrawerLayout.LOCK_MODE_UNLOCKED) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            } else {
                // only allow disabling of drawer interaction if the drawer is closed
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    }

    /**
     * Method is used to setup drawer icons
     */
    private void setupDrawerIcons(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        if (mSharedPref.getIntPref(Constants.KEY_DRAWER_ICON_A, 0) >= 0 &&
                mSharedPref.getIntPref(Constants.KEY_DRAWER_ICON_B, 0) > 0 &&
                mSharedPref.getIntPref(Constants.KEY_DRAWER_ICON_C, 0) > 0 &&
                mSharedPref.getIntPref(Constants.KEY_DRAWER_ICON_D, 0) > 0) {
            // setup menu icons
            for (int i = 0; i < menu.size(); i++) {
                if (i == 0) {
                    menu.findItem(R.id.nav_guide).setIcon(ARRY_DRAWER_ICONS[
                            mSharedPref.getIntPref(Constants.KEY_DRAWER_ICON_A, 0)]);
                } else if (i == 1) {
                    menu.findItem(R.id.nav_settings).setIcon(ARRY_DRAWER_ICONS[
                            mSharedPref.getIntPref(Constants.KEY_DRAWER_ICON_B, 0)]);
                } else if (i == 2) {
                    menu.findItem(R.id.nav_history).setIcon(ARRY_DRAWER_ICONS[
                            mSharedPref.getIntPref(Constants.KEY_DRAWER_ICON_C, 0)]);
                } else if (i == 3) {
                    menu.findItem(R.id.nav_share).setIcon(ARRY_DRAWER_ICONS[
                            mSharedPref.getIntPref(Constants.KEY_DRAWER_ICON_D, 0)]);
                }
            }
        } else {
            // setup menu icons
            Random rand = new Random();
            int pos;
            for (int i = 0; i < menu.size(); i++) {
                if (i == 0) {
                    pos = rand.nextInt(5);
                    menu.findItem(R.id.nav_guide).setIcon(ARRY_DRAWER_ICONS[pos]);
                    // save icons to shared prefs
                    mSharedPref.setPref(Constants.KEY_DRAWER_ICON_A, pos);
                } else if (i == 1) {
                    pos = (rand.nextInt(5) + 6);
                    menu.findItem(R.id.nav_settings).setIcon(ARRY_DRAWER_ICONS[pos]);
                    // save icons to shared prefs
                    mSharedPref.setPref(Constants.KEY_DRAWER_ICON_B, pos);
                } else if (i == 2) {
                    pos = (rand.nextInt(5) + 12);
                    menu.findItem(R.id.nav_history).setIcon(ARRY_DRAWER_ICONS[pos]);
                    // save icons to shared prefs
                    mSharedPref.setPref(Constants.KEY_DRAWER_ICON_C, pos);
                } else if (i == 3) {
                    pos = (rand.nextInt(5) + (ARRY_DRAWER_ICONS.length - 6));
                    menu.findItem(R.id.nav_share).setIcon(ARRY_DRAWER_ICONS[pos]);
                    // save icons to shared prefs
                    mSharedPref.setPref(Constants.KEY_DRAWER_ICON_D, pos);
                }
            }
        }
    }

    /**
     * Method is used to update bill amount
     */
    private void editTextUpdate() {
        DecimalFormat format = new DecimalFormat("0.00");

        // parse, convert and update appropriate values
        doubleBill = Double.parseDouble(edtBill.getText().toString());

        String strParse = String.valueOf(doubleBill);
        integerPlaces = strParse.indexOf('.');
        if (edtBill.getText().toString().contains(".") == true
                && decimalPlaces >= 1) {

            // handle special case
            if (specialCase == true) {
                specialCase = false;
                decimalPlaces = 2; // prevents loop
            } else {
                decimalPlaces = strParse.length() - integerPlaces - 1;
            }
        } else {
            decimalPlaces = strParse.length() - integerPlaces - 2;
        }

        strFormatted = format.format(doubleBill);

        // handle special case
        if (decimalPlaces >= 3) {
            Crouton.showText(MainActivity.this,
                    "Please maintain proper dollar format ex- 'xxx.xx')",
                    Style.ALERT);
            edtBill.setText(strFormatted);
        }

        // handle special case
        if (integerPlaces == 7
                && edtBill.getText().toString().contains(".") == false) {
            edtBill.setText(strFormatted);
        }

        // handle special case
        if (integerPlaces == 7 && decimalPlaces <= 1
                && edtBill.getText().toString().contains(".") == true) {
            specialCase = true;
            decimalPlaces = 2; // prevents loop
            edtBill.setText(strFormatted);
        }
    }

    /**
     * Method is used to set rating from stars
     * @param num
     */
    private void setRating(int num, boolean isSetFromSpinner) {
        if (num == 1) {
            ivStar1.setImageResource(R.drawable.star_filled);
            ivStar2.setImageResource(R.drawable.star_empty);
            ivStar3.setImageResource(R.drawable.star_empty);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
            if (!isSetFromSpinner) {
                spinner.setSelection(5);
            }
        } else if (num == 2) {
            ivStar1.setImageResource(R.drawable.star_filled);
            ivStar2.setImageResource(R.drawable.star_filled);
            ivStar3.setImageResource(R.drawable.star_empty);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
            if (!isSetFromSpinner) {
                spinner.setSelection(10);
            }
        } else if (num == 3) {
            ivStar1.setImageResource(R.drawable.star_filled);
            ivStar2.setImageResource(R.drawable.star_filled);
            ivStar3.setImageResource(R.drawable.star_filled);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
            if (!isSetFromSpinner) {
                spinner.setSelection(15);
            }
        } else if (num == 4) {
            ivStar1.setImageResource(R.drawable.star_filled);
            ivStar2.setImageResource(R.drawable.star_filled);
            ivStar3.setImageResource(R.drawable.star_filled);
            ivStar4.setImageResource(R.drawable.star_filled);
            ivStar5.setImageResource(R.drawable.star_empty);
            if (!isSetFromSpinner) {
                spinner.setSelection(20);
            }
        } else if (num == 5) {
            ivStar1.setImageResource(R.drawable.star_filled);
            ivStar2.setImageResource(R.drawable.star_filled);
            ivStar3.setImageResource(R.drawable.star_filled);
            ivStar4.setImageResource(R.drawable.star_filled);
            ivStar5.setImageResource(R.drawable.star_filled);
            if (!isSetFromSpinner) {
                spinner.setSelection(25);
            }
        }
    }

    /**
     * Method is used to populate spinner
     */
    private void populateSpinner() {
        // setup spinner configurations
        spinner.setAdapter(null); // make sure spinner is empty
        alSpinnerItems.clear(); // make sure arrayList is empty

        alSpinnerItems.add("0%");
        alSpinnerItems.add("1%");
        alSpinnerItems.add("2%");
        alSpinnerItems.add("3%");
        alSpinnerItems.add("4%");
        alSpinnerItems.add("5% Poor");
        alSpinnerItems.add("6%");
        alSpinnerItems.add("7%");
        alSpinnerItems.add("8%");
        alSpinnerItems.add("9%");
        alSpinnerItems.add("10% Fair");
        alSpinnerItems.add("11%");
        alSpinnerItems.add("12%");
        alSpinnerItems.add("13%");
        alSpinnerItems.add("14%");
        alSpinnerItems.add("15% Good!");
        alSpinnerItems.add("16%");
        alSpinnerItems.add("17%");
        alSpinnerItems.add("18%");
        alSpinnerItems.add("19%");
        alSpinnerItems.add("20% Great!");
        alSpinnerItems.add("21%");
        alSpinnerItems.add("22%");
        alSpinnerItems.add("23%");
        alSpinnerItems.add("24%");
        alSpinnerItems.add("25% Royal!");
        alSpinnerItems.add("26%");
        alSpinnerItems.add("27%");
        alSpinnerItems.add("28%");
        alSpinnerItems.add("29%");
        alSpinnerItems.add("30%");

        // create ArrayAdapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_spinner_item,
                alSpinnerItems);
        // set array adapter
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // setup adapter
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(15);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                intSelected = spinner.getSelectedItemPosition();
                if (intSelected >= 0 && intSelected <= 2) {
                    setRating(1, true);
                    tvService.setText("Poor");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.red_shade));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                    calculate();
                } else if (intSelected >= 3 && intSelected <= 4) {
                    setRating(1, true);
                    tvService.setText("Poor");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.red_shade));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                    calculate();
                } else if (intSelected >= 5 && intSelected <= 7) {
                    setRating(1, true);
                    tvService.setText("Poor");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.red_shade));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                    calculate();
                } else if (intSelected >= 8 && intSelected <= 9) {
                    setRating(1, true);
                    tvService.setText("Poor");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.red_shade));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                    calculate();
                } else if (intSelected >= 10 && intSelected <= 12) {
                    setRating(2, true);
                    tvService.setText("Fair");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Fair tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 13 && intSelected <= 14) {
                    setRating(2, true);
                    tvService.setText("Fair");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Fair tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 15 && intSelected <= 17) {
                    setRating(3, true);
                    tvService.setText("Good!");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Good tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 18 && intSelected <= 19) {
                    setRating(3, true);
                    tvService.setText("Good!");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Good service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 20 && intSelected <= 22) {
                    setRating(4, true);
                    tvService.setText("Great!");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Great tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 23 && intSelected <= 24) {
                    setRating(4, true);
                    tvService.setText("Great!");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Great tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 25) {
                    setRating(5, true);
                    tvService.setText("Royal!");
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.purple_shade));
                    Crouton.showText(MainActivity.this, "Royal tip service percent", Style.INFO);
                    calculate();
                }
                tvPercent.setText(intSelected + "%");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    /**
     * Method is used to calculate tip amount
     */
    private void calculate() {
        if (clear == true) {
            Crouton.showText(MainActivity.this, "All fields reset", Style.CONFIRM);
            clear = false;

            // spinner reset (resets rating as well)
            tvService.setText(String.valueOf("Good"));
            tvPercent.setText(String.valueOf("15%"));
            spinner.setSelection(15);

            // meta and temp variable reset (resets textViews as well)
            temp1 = 0.00;
            temp2 = 0.00;
            temp3 = 0.00;
            temp4 = 0.00;
            temp5 = 0.00;

            tvTip.setText(String.valueOf("$0.00"));
            tvPerson.setText(String.valueOf("$0.00"));
            tvTotal.setText(String.valueOf("$0.00"));

            sharedNum = 1;
            tvValue.setText(String.valueOf("1"));
            edtBill.setText("");
        } else {
            /*
			 * Legend:
			 * temp1-amount of the bill
			 * temp2-tip w/o round
			 * temp3-tip w/round
			 * temp4-total amount of bill
			 * temp5-total amount each person pays
			 */

            if (edtBill.getText().toString().length() == 0) {
                tvTip.setText(String.valueOf("$0.00")); // static

            } else {
                // calculate tip amount
                temp1 = Double.parseDouble(strFormatted);
                temp2 = intSelected * temp1 / 100;
                DecimalFormat formatTip = new DecimalFormat("0.00");
                strTip = formatTip.format(temp2);

                if (switchRoundOff.isChecked()) {
                    // round ON; update calculations
                    temp3 = (int) Math.round(temp2);
                    tvTip.setText(String.valueOf("$" + temp3 + "0"));

                    // calculate total bill amount
                    temp4 = temp1 + temp3;
                    strTotal = formatTip.format(temp4);
                    tvTotal.setText(String.valueOf("$" + strTotal));
                } else {
                    // round OFF; update calculations
                    tvTip.setText(String.valueOf("$" + strTip));

                    // calculate total bill amount
                    temp4 = temp1 + temp2;
                    strTotal = formatTip.format(temp4);
                    tvTotal.setText(String.valueOf("$" + strTotal));
                }

                // calculate each person pays amount
                temp5 = temp4 / sharedNum;
                strPerson = formatTip.format(temp5);
                tvPerson.setText(String.valueOf("$" + strPerson));
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (!FrameworkUtils.isViewClickable()) {
            return;
        }

        switch (view.getId()) {
            case R.id.btn_inc:
                // hide keyboard
                DeviceUtils.hideKeyboard(mContext, getWindow().getDecorView().getWindowToken());
                if (sharedNum < 99) {
                    sharedNum++; // increment # of shared values
                    strValue = Integer.toString(sharedNum);
                    tvValue.setText(strValue);
                    calculate();
                }
                break;
            case R.id.btn_dec:
                // hide keyboard
                DeviceUtils.hideKeyboard(mContext, getWindow().getDecorView().getWindowToken());
                if (sharedNum > 1) {
                    sharedNum--; // decrement # of shared values
                    strValue = Integer.toString(sharedNum);
                    tvValue.setText(strValue);
                    calculate();
                }
                break;
            case R.id.btn_clear:
                clear = true;
                calculate();
                break;
            case R.id.iv_star_1:
                setRating(1,false);
                break;
            case R.id.iv_star_2:
                setRating(2, false);
                break;
            case R.id.iv_star_3:
                setRating(3, false);
                break;
            case R.id.iv_star_4:
                setRating(4, false);
                break;
            case R.id.iv_star_5:
                setRating(5, false);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_guide:
                fragment = new GuideFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_history:
                fragment = new HistoryFragment();
                break;
            case R.id.nav_share:
                fragment = new ShareFragment();
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                break;
            case R.id.nav_privacy:
                fragment = new PrivacyFragment();
                break;
            default:
                break;
        }
        // add fragment
        if (!FrameworkUtils.checkIfNull(fragment)) {
            addFragment(fragment);
        }

        // close drawer after selection
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onPause() {
        adView.pause();
        if (!FrameworkUtils.checkIfNull(mSensorManager) && !FrameworkUtils.checkIfNull(mSensorListener)) {
            mSensorManager.unregisterListener(mSensorListener);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
        if (!FrameworkUtils.checkIfNull(mSensorManager) && !FrameworkUtils.checkIfNull(mSensorListener)) {
            mSensorManager.registerListener(mSensorListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onDestroy() {
        if (!FrameworkUtils.checkIfNull(adView)) {
            // destroy the adview
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            // close drawer
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }

    }
}
