package com.blog.ljtatum.tipcalculator.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;

import com.blog.ljtatum.tipcalculator.constants.Constants;
import com.blog.ljtatum.tipcalculator.utils.AppRaterUtil;
import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.listeners.ShakeEventListener;
import com.blog.ljtatum.tipcalculator.utils.NetworkUtils;
import com.blog.ljtatum.tipcalculator.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by LJTat on 2/23/2017.
 */
public class MainActivity extends AppCompatActivity implements OnClickListener,
        NavigationView.OnNavigationItemSelectedListener{
    public static String TAG = MainActivity.class.getSimpleName();

    private static final String AD_ID_TEST = "950036DB8197D296BE390357BD9A964E";
    private static final long AD_LOAD_DELAY = 500;

    private Context mContext;
    private int sharedNum = 1; // default value
    private int intSelected;
    private int integerPlaces; // track number of integers in editText
    private int decimalPlaces; // track number of decimals in editText

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
    private Switch mSwitch;

    private ArrayList<String> stringArray = new ArrayList<String>();
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    private Vibrator v;

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
    }

    /**
     * Method is used to initialize views
     */
    private void initializeViews() {
        mContext = MainActivity.this;

        // rate this app
        new AppRaterUtil(mContext);
        mSwitch = (Switch) findViewById(R.id.btn_switch);
        ivStar1 = (ImageView) findViewById(R.id.iv_star_1);
        ivStar2 = (ImageView) findViewById(R.id.iv_star_2);
        ivStar3 = (ImageView) findViewById(R.id.iv_star_3);
        ivStar4 = (ImageView) findViewById(R.id.iv_star_4);
        ivStar5 = (ImageView) findViewById(R.id.iv_star_5);
        // tip textView
        tvTip = (TextView) findViewById(R.id.tv_meta_tip);
        // shared by textView
        tvPerson = (TextView) findViewById(R.id.tv_meta_person);
        // total bill amount textView
        tvTotal = (TextView) findViewById(R.id.tv_meta_total);
        // bill editText
        edtBill = (EditText) findViewById(R.id.edt_bill);
        // clear button
        btnClear = (Button) findViewById(R.id.btn_clear);
        // increment, decrement buttons
        btnInc = (Button) findViewById(R.id.btn_inc);
        btnDec = (Button) findViewById(R.id.btn_dec);
        tvValue = (TextView) findViewById(R.id.tv_meta_num);
        // spinner
        tvService = (TextView) findViewById(R.id.tv_meta_rating);
        tvPercent = (TextView) findViewById(R.id.tv_meta_percent);
        spinner = (Spinner) findViewById(R.id.spinner);
        populateSpinner();
        // drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
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
                }, AD_LOAD_DELAY);
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

        // set on shake listener
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake() {
                v.vibrate(500); //vibrate for 500 milliseconds
                clear = true;
                calculate();
            }
        });

        // OnEditorActionListener
        edtBill.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtBill.getWindowToken(), 0);
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
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Utils.hideKeyboard(mContext, MainActivity.this.getWindow().getDecorView().getWindowToken());
                calculate();
            }
        });
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
        Utils.hideKeyboard(mContext, MainActivity.this.getWindow().getDecorView().getWindowToken());

        // setup spinner configurations
        spinner.setAdapter(null); // make sure spinner is empty
        stringArray.clear(); // make sure arrayList is empty

        stringArray.add("0%");
        stringArray.add("1%");
        stringArray.add("2%");
        stringArray.add("3%");
        stringArray.add("4%");
        stringArray.add("5% Poor");
        stringArray.add("6%");
        stringArray.add("7%");
        stringArray.add("8%");
        stringArray.add("9%");
        stringArray.add("10% Fair");
        stringArray.add("11%");
        stringArray.add("12%");
        stringArray.add("13%");
        stringArray.add("14%");
        stringArray.add("15% Good!");
        stringArray.add("16%");
        stringArray.add("17%");
        stringArray.add("18%");
        stringArray.add("19%");
        stringArray.add("20% Great!");
        stringArray.add("21%");
        stringArray.add("22%");
        stringArray.add("23%");
        stringArray.add("24%");
        stringArray.add("25% Royal!");
        stringArray.add("26%");
        stringArray.add("27%");
        stringArray.add("28%");
        stringArray.add("29%");
        stringArray.add("30%");

        // create ArrayAdapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_spinner_item,
                stringArray);

        arrayAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

                if (mSwitch.isChecked()) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Method is used to share via email, facebook and twitter
     * @return
     */
    private Intent shareIntent() {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("text/plain");

        String shareMsg = "Check out this great tip calculator app "
                + "developed by Leonard Tatum and Kazuya Shibuta! "
                + "http://play.google.com/store/apps/details?id=com.blog.ljtatum.tipcalculator";

        String emailMsg = "Hello Leonard Tatum and Kazuya Shibuta, "
                + "I have something to tell you about your application....\n\n";

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            String packageName = app.activityInfo.packageName;
            Intent targetedShareIntent = new Intent(
                    android.content.Intent.ACTION_SEND_MULTIPLE);
            targetedShareIntent.setType("text/plain");
            targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "Tip Calculator");

            if (TextUtils.equals(packageName, "com.google.android.gm")) {
                targetedShareIntent.putExtra(
                        android.content.Intent.EXTRA_EMAIL, new String[]{
                                "ljtatum@hotmail.com",
                                "kazuyashibuta@gmail.com"});
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        emailMsg);
            } else if (TextUtils.equals(packageName, "com.android.email")) {
                targetedShareIntent.putExtra(
                        android.content.Intent.EXTRA_EMAIL, new String[]{
                                "ljtatum@hotmail.com",
                                "kazuyashibuta@gmail.com"});
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        emailMsg);
            } else if (TextUtils.equals(packageName, "com.android.mms")) {
                targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        shareMsg);
            }

            targetedShareIntent.setPackage(packageName);
            targetedShareIntents.add(targetedShareIntent);
        }

        Intent facebookIntent = getShareIntent("facebook", "Tip Calculator",
                shareMsg);
        if (facebookIntent != null)
            targetedShareIntents.add(facebookIntent);

        Intent twitterIntent = getShareIntent("twitter", "Tip Calculator",
                shareMsg);
        if (twitterIntent != null)
            targetedShareIntents.add(twitterIntent);

        Intent chooserIntent = Intent.createChooser(
                targetedShareIntents.remove(0), "Share via");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
        startActivity(chooserIntent);
        return shareIntent;
    }

    /**
     * Method is used to retrieve share intent
     * @param type
     * @param subject
     * @param text
     * @return
     */
    private Intent getShareIntent(String type, String subject, String text) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = this.getPackageManager()
                .queryIntentActivities(share, 0);
        System.out.println("resinfo: " + resInfo);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase(Locale.US).contains(type)
                        || info.activityInfo.name.toLowerCase(Locale.US).contains(type)) {
                    share.putExtra(Intent.EXTRA_SUBJECT, subject);
                    share.putExtra(Intent.EXTRA_TEXT, text);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return null;
            return share;
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share:
                shareIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        adView.pause();
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            // destroy the adview
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (!Utils.isViewClickable()) {
            return;
        }

        switch (view.getId()) {
            case R.id.btn_inc:
                Utils.hideKeyboard(mContext, MainActivity.this.getWindow().getDecorView().getWindowToken());
                if (sharedNum < 99) {
                    sharedNum++; // increment # of shared values
                    strValue = Integer.toString(sharedNum);
                    tvValue.setText(strValue);
                    calculate();
                }
                break;
            case R.id.btn_dec:
                Utils.hideKeyboard(mContext, MainActivity.this.getWindow().getDecorView().getWindowToken());
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
        return false;
    }
}
