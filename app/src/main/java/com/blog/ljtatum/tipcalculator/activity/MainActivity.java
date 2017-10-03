package com.blog.ljtatum.tipcalculator.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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

import com.app.framework.entities.Address;
import com.app.framework.listeners.AddressListener;
import com.app.framework.sharedpref.SharedPref;
import com.app.framework.utilities.AppRaterUtil;
import com.app.framework.utilities.DeviceUtils;
import com.app.framework.utilities.FrameworkUtils;
import com.app.framework.utilities.NetworkReceiver;
import com.app.framework.utilities.NetworkUtils;
import com.app.framework.utilities.map.GoogleServiceUtility;
import com.app.framework.utilities.map.model.PlaceModel;
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
import com.blog.ljtatum.tipcalculator.utils.DialogUtils;
import com.blog.ljtatum.tipcalculator.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by LJTat on 2/23/2017.
 */
public class MainActivity extends BaseActivity implements OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener,
        com.google.android.gms.location.LocationListener, NetworkReceiver.NetworkStatusObserver {
    public static String TAG = MainActivity.class.getSimpleName();

    private static final String AD_ID_TEST = "950036DB8197D296BE390357BD9A964E";
    private static final int LOCATION_REQUEST_INTERVAL_DEFAULT = 30000;
    private static final int LOCATION_REQUEST_INTERVAL_FASTEST = 15000;
    private static final int[] ARRY_DRAWER_ICONS = {R.drawable.food_01, R.drawable.food_02,
            R.drawable.food_03, R.drawable.food_04, R.drawable.food_05, R.drawable.food_06,
            R.drawable.food_07, R.drawable.food_08, R.drawable.food_09, R.drawable.food_10,
            R.drawable.food_11, R.drawable.food_12, R.drawable.food_13, R.drawable.food_14,
            R.drawable.food_15, R.drawable.food_16, R.drawable.food_17, R.drawable.food_18,
            R.drawable.food_19, R.drawable.food_20, R.drawable.food_21, R.drawable.food_22,
            R.drawable.food_23};

    private Context mContext;
    private int sharedNum, intSelected, decimalPlaces;
    private boolean clear, specialCase;

    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;

    private Button btnInc; // increase # of shared values
    private Button btnDec; // decrease # of shared values
    private Button btnClear; // clears all fields

    private AdView adView; // container for banner ads

    private TextView tvSharedBy; // container for sharedNum
    private TextView tvService; // container for rating
    private TextView tvPercent; // container for tip percent
    private TextView tvTip; // container for tip amount
    private TextView tvPerson; // container for person pay amount
    private TextView tvTotal; // container for total bill amount

    private EditText edtBill;

    private String strFormatted, strAddress;

    private Spinner spinner;
    private Switch switchRoundOff;
    private DrawerLayout mDrawerLayout;

    private ArrayList<String> alSpinnerItems;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private Vibrator v;
    private SharedPref mSharedPref;

    // google services
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LatLng mCurrentLocation;
    private PlaceModel mPlace;

    // network
    private NetworkReceiver mNetworkReceiver;

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
        mNetworkReceiver = new NetworkReceiver();
        alSpinnerItems = new ArrayList<>();
        mSharedPref = new SharedPref(mContext, com.app.framework.constants.Constants.PREF_FILE_NAME);

        // GoogleApiClient; location services API and places API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();

        // rate this app
        new AppRaterUtil(mContext, mContext.getPackageName());
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
        tvSharedBy = (TextView) findViewById(R.id.tv_meta_shared_by);

        // set default round-off value
        if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_ROUND_OFF, true)) {
            switchRoundOff.setChecked(true);
        } else {
            switchRoundOff.setChecked(false);
        }

        // set initial shared by value
        sharedNum = mSharedPref.getIntPref(Constants.KEY_DEFAULT_SHARED_BY, 1);

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
                if (FrameworkUtils.checkIfNull(getTopFragment())) {
                    edtBill.requestFocus();
                    // show keyboard
                    DeviceUtils.showKeyboard(mContext);
                }
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
                if (mSharedPref.getBooleanPref(Constants.KEY_DEFAULT_SHAKE_RESET, true)) {
                    v.vibrate(Durations.DELAY_MS_500); // vibrate for 500 milliseconds
                    clear = true;
                    calculate();
                } else {
                    Crouton.showText(MainActivity.this, "Shake to reset feature currently disabled. To enable, go to Settings", Style.ALERT);
                }
            }
        });

        // OnEditorActionListener
        edtBill.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // hide keyboard
                    DeviceUtils.hideKeyboard(mContext, getWindow().getDecorView().getWindowToken());

                    // store tip value to history
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
                } else if (!FrameworkUtils.isStringEmpty(edtBill.getText().toString())
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
     * Method is used to begin location request updates using FusedLocationApi
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        Logger.v("TEST", "startLocationUpdates called");
        if (FrameworkUtils.checkAppPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION) && !FrameworkUtils.checkIfNull(mGoogleApiClient) &&
                mGoogleApiClient.isConnected()) {

            if (!FrameworkUtils.checkIfNull(mLocationRequest)) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest, this);
            }
        }
    }

    /**
     * Method is used to refresh current location
     */
    @SuppressLint("MissingPermission")
    private void initLocationRequest() {
        Logger.v("TEST", "initLocationRequest called");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setFastestInterval(LOCATION_REQUEST_INTERVAL_FASTEST);
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL_DEFAULT);

        int locationMode = -99;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (locationMode != Settings.Secure.LOCATION_MODE_OFF &&
                locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
            // check if device has permissions for high accuracy
            if (FrameworkUtils.checkAppPermissions(mContext, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // set attributes of location request object
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            } else {
                // set attributes of location request object
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            }
        } else {
            // set attributes of location request object
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Logger.v("TEST", "onLocationChanged called - 1, lat= " + location.getLatitude() + " //lng= " + location.getLongitude());
                    // update the user's current location every time his/her location changes
                    mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    // process location change
                    processOnLocationChanged();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    // do nothing
                }

                @Override
                public void onProviderEnabled(String s) {
                    // do nothing
                }

                @Override
                public void onProviderDisabled(String s) {
                    // do nothing
                }
            };

            // create location provider
            String locationProvider = LocationManager.PASSIVE_PROVIDER;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (!FrameworkUtils.checkIfNull(locationManager)) {
                // make request for location updates
                locationManager.requestLocationUpdates(locationProvider, LOCATION_REQUEST_INTERVAL_FASTEST,
                        0, locationListener);
                // retrieve current location
                Location lastLocation = locationManager.getLastKnownLocation(locationProvider);
                if (!FrameworkUtils.checkIfNull(lastLocation)) {
                    mCurrentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                }
            }
        }
    }

    /**
     * Method is used to process onLoCationChanged
     */
    private void processOnLocationChanged() {
        // retrieve address using user's current latlng
        GoogleServiceUtility.getAddress(mContext, mCurrentLocation, new AddressListener() {

            @Override
            public void onAddressResponse(@NonNull Address address) {
                // create Place object
                if (!FrameworkUtils.checkIfNull(mPlace)) {
                    mPlace = new PlaceModel();
                }
                mPlace.streetNo = address.streetNumber;
                mPlace.streetName = address.addressLine1;
                mPlace.city = address.city;
                mPlace.state = address.stateCode;
                mPlace.zipCode = address.postalCode;
                mPlace.country = address.countryCode;

                Logger.e("TEST", "1) address= " + mPlace.getShortFormattedAddress(false));
                Logger.e("TEST", "2) address= " + mPlace.getShortFormattedAddress(true));
                Logger.e("TEST", "3) address= " + mPlace.getFormattedAddress());
            }

            @Override
            public void onAddressError() {
                // do nothing
            }

            @Override
            public void onZeroResults() {
                // do nothing
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
        double doubleBill = Double.parseDouble(edtBill.getText().toString());

        String strParse = String.valueOf(doubleBill);
        int integerPlaces = strParse.indexOf('.');
        if (edtBill.getText().toString().contains(".") && decimalPlaces >= 1) {

            // handle special case
            if (specialCase) {
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
        if (integerPlaces == 7 && !edtBill.getText().toString().contains(".")) {
            edtBill.setText(strFormatted);
        }

        // handle special case
        if (integerPlaces == 7 && decimalPlaces <= 1 && edtBill.getText().toString().contains(".")) {
            specialCase = true;
            decimalPlaces = 2; // prevents loop
            edtBill.setText(strFormatted);
        }
    }

    /**
     * Method is used to set rating from stars
     *
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
        spinner.setSelection(mSharedPref.getIntPref(Constants.KEY_DEFAULT_TIP, 15));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Logger.e("TEST", "<onItemSelected>");
                intSelected = spinner.getSelectedItemPosition();
                if (intSelected >= 0 && intSelected <= 2) {
                    setRating(1, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.material_red_400_color_code));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                } else if (intSelected >= 3 && intSelected <= 4) {
                    setRating(1, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.material_red_400_color_code));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                } else if (intSelected >= 5 && intSelected <= 7) {
                    setRating(1, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.material_red_400_color_code));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                } else if (intSelected >= 8 && intSelected <= 9) {
                    setRating(1, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.material_red_400_color_code));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                } else if (intSelected >= 10 && intSelected <= 12) {
                    setRating(2, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    Crouton.showText(MainActivity.this, "Fair tip service percent", Style.CONFIRM);
                } else if (intSelected >= 13 && intSelected <= 14) {
                    setRating(2, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    Crouton.showText(MainActivity.this, "Fair tip service percent", Style.CONFIRM);
                } else if (intSelected >= 15 && intSelected <= 17) {
                    setRating(3, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    Crouton.showText(MainActivity.this, "Good tip service percent", Style.CONFIRM);
                } else if (intSelected >= 18 && intSelected <= 19) {
                    setRating(3, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    Crouton.showText(MainActivity.this, "Good service percent", Style.CONFIRM);
                } else if (intSelected >= 20 && intSelected <= 22) {
                    setRating(4, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    Crouton.showText(MainActivity.this, "Great tip service percent", Style.CONFIRM);
                } else if (intSelected >= 23 && intSelected <= 24) {
                    setRating(4, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                    Crouton.showText(MainActivity.this, "Great tip service percent", Style.CONFIRM);
                } else if (intSelected >= 25) {
                    setRating(5, true);
                    tvService.setTextColor(ContextCompat.getColor(mContext, R.color.material_purple_500_color_code));
                    Crouton.showText(MainActivity.this, "Royal tip service percent", Style.INFO);

                }
                tvService.setText(Utils.getTipQuality(mContext, intSelected));
                tvPercent.setText(intSelected + "%");
                calculate();
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
        // temp values
        double temp1, temp2, temp3, temp4, temp5;

        if (clear) {
            Crouton.showText(MainActivity.this, "All fields reset", Style.CONFIRM);
            clear = false;

            // spinner reset (resets rating as well)
            tvService.setText(mContext.getResources().getString(R.string.txt_good));
            tvPercent.setText(mContext.getResources().getString(R.string.txt_percent_default));
            spinner.setSelection(15);

            // meta and temp variable reset (resets textViews as well)
            temp1 = 0.00;
            temp2 = 0.00;
            temp3 = 0.00;
            temp4 = 0.00;
            temp5 = 0.00;

            // reset tip and payment values
            tvTip.setText(mContext.getResources().getString(R.string.txt_zero_dollar));
            tvPerson.setText(mContext.getResources().getString(R.string.txt_zero_dollar));
            tvTotal.setText(mContext.getResources().getString(R.string.txt_zero_dollar));

            // update shared by value
            tvSharedBy.setText(String.valueOf(mSharedPref.getIntPref(Constants.KEY_DEFAULT_SHARED_BY, 1)));

            // reset bill amount
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
                tvTip.setText(mContext.getResources().getString(R.string.txt_zero_dollar)); // static
            } else {
                // calculate tip amount
                temp1 = Double.parseDouble(strFormatted);
                temp2 = intSelected * temp1 / 100;
                DecimalFormat formatTip = new DecimalFormat("0.00");
                String strTip = formatTip.format(temp2);

                String strTotal;
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
                String strPerson = formatTip.format(temp5);
                tvPerson.setText(String.valueOf("$" + strPerson));
            }

            // update shared by value
            tvSharedBy.setText(String.valueOf(sharedNum));
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
                // maximum shared by is 99
                if (sharedNum < 99) {
                    sharedNum++; // increment # of shared values
                    tvSharedBy.setText(String.valueOf(sharedNum));
                    calculate();
                }
                break;
            case R.id.btn_dec:
                // hide keyboard
                DeviceUtils.hideKeyboard(mContext, getWindow().getDecorView().getWindowToken());
                // least shared by is 1
                if (sharedNum > 1) {
                    sharedNum--; // decrement # of shared values
                    tvSharedBy.setText(String.valueOf(sharedNum));
                    calculate();
                }
                break;
            case R.id.btn_clear:
                clear = true;
                calculate();
                break;
            case R.id.iv_star_1:
                setRating(1, false);
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
    protected void onStart() {
        super.onStart();
        if (!FrameworkUtils.checkIfNull(mGoogleApiClient)) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // disconnect GoogleApiClient
        if (!FrameworkUtils.checkIfNull(mGoogleApiClient) && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        // pause adview
        adView.pause();
        // unregister sensor manager
        if (!FrameworkUtils.checkIfNull(mSensorManager) && !FrameworkUtils.checkIfNull(mSensorListener)) {
            mSensorManager.unregisterListener(mSensorListener);
        }
        // unregister network receiver
        if (mNetworkReceiver.getObserverSize() > 0 && mNetworkReceiver.contains(this)) {
            try {
                // unregister network receiver
                unregisterReceiver(mNetworkReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            mNetworkReceiver.removeObserver(this);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // resume adview
        adView.resume();
        // register sensor manager
        if (!FrameworkUtils.checkIfNull(mSensorManager) && !FrameworkUtils.checkIfNull(mSensorListener)) {
            mSensorManager.registerListener(mSensorListener,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_UI);
        }
        // only register receiver if it has not already been registered
        if (!mNetworkReceiver.contains(this)) {
            // register network receiver
            mNetworkReceiver.addObserver(this);
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            // print observer list
            mNetworkReceiver.printObserverList();
        }
    }

    @Override
    public void onDestroy() {
        // unregister network receiver
        if (mNetworkReceiver.getObserverSize() > 0 && mNetworkReceiver.contains(this)) {
            try {
                // unregister network receiver
                unregisterReceiver(mNetworkReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            mNetworkReceiver.removeObserver(this);
        }
        if (!FrameworkUtils.checkIfNull(adView)) {
            // destroy the adview
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            // close drawer
            mDrawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Logger.e("TEST", "onConnected");
        // initialize LocationRequest object
        if (FrameworkUtils.checkIfNull(mLocationRequest)) {
            initLocationRequest();
        }
        // retrieve location from fusedLocationApi
        @SuppressLint("MissingPermission") android.location.Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (!FrameworkUtils.checkIfNull(lastLocation)) {
            mCurrentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
        // start location updates
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // connection was lost, re-attempt connecting to google services
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        GoogleServiceUtility.checkGooglePlaySevices(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Logger.v("TEST", "onLocationChanged called - 2, lat= " + location.getLatitude() + " //lng= " + location.getLongitude());
        // update the user's current location every time his/her location changes
        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // process location change
        processOnLocationChanged();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // do nothing
    }

    @Override
    public void onProviderEnabled(String provider) {
        // do nothing
    }

    @Override
    public void onProviderDisabled(String provider) {
        // do nothing
    }

    @Override
    public void notifyConnectionChange(boolean isConnected) {
        if (isConnected) {
            // app is connected to network
            DialogUtils.dismissNoNetworkDialog();
        } else {
            // app is not connected to network
            DialogUtils.showDefaultNoNetworkAlert(mContext, null,
                    mContext.getString(R.string.check_network));
        }
    }
}
