package com.blog.ljtatum.tipcalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends Activity {
    public static String TAG = MainActivity.class.getSimpleName();

    private int sharedNum = 1; // default value (1)
    private int intSelected;
    private int integerPlaces; // track number of integers in editText
    private int decimalPlaces; // track number of decimals in editText

    private float floatRating;
    private double doubleBill;
    private double temp1;
    private double temp2;
    private double temp3;
    private double temp4;
    private double temp5;
    private boolean bToggle = false;
    private boolean clear = false;
    private boolean specialCase = false;
    private boolean spinnerChange = false;

    private Button btnInc; // increase # of shared values
    private Button btnDec; // decrease # of shared values
    private Button btnGuide; // displays alert dialog with Tip Guide
    private Button btnClear; // clears all fields

    private AdView adView; // container for banner ads

    private TextView tvValue; // container for sharedNum
    private TextView tvService; // container for rating
    private TextView tvPercent; // container for tip percent
    private TextView tvTip; // container for tip amount
    private TextView tvPerson; // container for person pay amount
    private TextView tvTotal; // container for total bill amount

    private EditText etBill;

    private String strValue;
    private String strFormatted;
    private String strTip; // used for format tip textView
    private String strPerson; // used for format person textView
    private String strTotal; // used for format total bill textView

    private Spinner spinner;
    private ToggleButton tglbtn;

    private RatingBar rb;
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
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Main layout loaded successfully");

        // rate this app
        AppRater.app_launched(this);

        // tip textView
        tvTip = (TextView) findViewById(R.id.metatip);

        // shared by textView
        tvPerson = (TextView) findViewById(R.id.metaperson);

        // total bill amount textView
        tvTotal = (TextView) findViewById(R.id.metatotal);

        // toggle button
        tglbtn = (ToggleButton) findViewById(R.id.btntoggle);

        // instantiate vibrator
        v = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener
                .setOnShakeListener(new ShakeEventListener.OnShakeListener() {
                    @Override
                    public void onShake() {
                        // TODO Auto-generated method stub
                        Log.d(TAG, "onShake()");

                        v.vibrate(500); //vibrate for 500 milliseconds
                        clear = true;
                        calculate();
                    }
                });

        // bill editText
        etBill = (EditText) findViewById(R.id.edittxtbill);

        // OnEditorActionListener
        etBill.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etBill.getWindowToken(), 0);
                }
                return false;
            }
        });

        // TextChangedListener
        etBill.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                calculate();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                if (etBill.getText().toString().contentEquals(".")) {
                    // handle special case
                    etBill.setText("0.00");
                } else if (etBill.getText().toString() != null
                        && etBill.getText().toString().length() > 0) {
                    editTextUpdate();
                }
            }
        });

        // rating bar
        rb = (RatingBar) findViewById(R.id.servicebar);
        rb.setRating(3.0f); // default value: 15% (Good!)
        ratingBar();

        // spinner
        tvService = (TextView) findViewById(R.id.metarating);
        tvPercent = (TextView) findViewById(R.id.metapercent);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner();

        // increment, decrement buttons
        btnInc = (Button) findViewById(R.id.btninc);
        btnDec = (Button) findViewById(R.id.btndec);
        tvValue = (TextView) findViewById(R.id.metanum);

        btnInc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideKeyboard();
                if (sharedNum < 99) {
                    sharedNum++; // increment # of shared values
                    strValue = Integer.toString(sharedNum);
                    tvValue.setText(strValue);
                    calculate();
                }
            }
        });

        btnDec.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideKeyboard();
                if (sharedNum > 1) {
                    sharedNum--; // decrement # of shared values
                    strValue = Integer.toString(sharedNum);
                    tvValue.setText(strValue);
                    calculate();
                }
            }
        });

        // tip guide button
        btnGuide = (Button) findViewById(R.id.btnguide);
        btnGuide.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d(TAG, "tip guide button");

                // set dialog title and icon
                AlertDialog.Builder dialogGuide = new AlertDialog.Builder(
                        MainActivity.this);
                dialogGuide.setTitle("Tip Guide: 5 Things You Should Know");
                dialogGuide.setIcon(R.drawable.ic_launcher);

                // set dialog message
                dialogGuide.setMessage(R.string.txt_guide);
                dialogGuide.setPositiveButton("Close",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                // do nothing
                            }
                        });

                AlertDialog alertDialog = dialogGuide.create();
                alertDialog.show();
            }
        });

        // clear button
        btnClear = (Button) findViewById(R.id.btnclear);
        btnClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d(TAG, "clear button");

                clear = true;
                calculate();
            }

        });

        ConnectivityManager conMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // ad banner
        adView = (AdView) this.findViewById(R.id.adView);

        try {
            if (conMgr.getActiveNetworkInfo().isConnected()
                    && conMgr.getActiveNetworkInfo().isAvailable()) {

                // request test banner ads
                AdRequest adRequestBanner = new AdRequest.Builder().build();

                // load banner ads
                adView.loadAd(adRequestBanner);
            }
        } catch (RuntimeException rte) {
            Log.e(TAG, "RuntimeException");
            adView.setBackgroundResource(R.drawable.banner);
        }

    } // end onCreate

    private void hideKeyboard() {
        // hide virtual keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etBill.getWindowToken(), 0);
    }

    private void editTextUpdate() {
        DecimalFormat format = new DecimalFormat("0.00");

        // parse, convert and update appropriate values
        doubleBill = Double.parseDouble(etBill.getText().toString());

        String strParse = String.valueOf(doubleBill);
        integerPlaces = strParse.indexOf('.');
        if (etBill.getText().toString().contains(".") == true
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
            etBill.setText(strFormatted);
        }

        // handle special case
        if (integerPlaces == 7
                && etBill.getText().toString().contains(".") == false) {
            etBill.setText(strFormatted);
        }

        // handle special case
        if (integerPlaces == 7 && decimalPlaces <= 1
                && etBill.getText().toString().contains(".") == true) {
            specialCase = true;
            decimalPlaces = 2; // prevents loop
            etBill.setText(strFormatted);
        }
    } // end editTextUpdate()

    private void ratingBar() {
        // TODO Auto-generated method stub

        rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // TODO Auto-generated method stub
                hideKeyboard();

                floatRating = rb.getRating();
                if (spinnerChange == false) {
                    if (floatRating == 0.0) {
                        tvService.setText(String.valueOf("Poor"));
                        tvPercent.setText(String.valueOf("0%"));
                        spinner.setSelection(0);
                    } else if (floatRating == 0.5) {
                        tvService.setText(String.valueOf("Poor"));
                        tvPercent.setText(String.valueOf("3%"));
                        spinner.setSelection(3);
                    } else if (floatRating == 1.0) {
                        tvService.setText(String.valueOf("Poor"));
                        tvPercent.setText(String.valueOf("5%"));
                        spinner.setSelection(5);
                    } else if (floatRating == 1.5) {
                        tvService.setText(String.valueOf("Poor"));
                        tvPercent.setText(String.valueOf("8%"));
                        spinner.setSelection(8);
                    } else if (floatRating == 2.0) {
                        tvService.setText(String.valueOf("Fair"));
                        tvPercent.setText(String.valueOf("10%"));
                        spinner.setSelection(10);
                    } else if (floatRating == 2.5) {
                        tvService.setText(String.valueOf("Fair"));
                        tvPercent.setText(String.valueOf("13%"));
                        spinner.setSelection(13);
                    } else if (floatRating == 3.0) {
                        tvService.setText(String.valueOf("Good"));
                        tvPercent.setText(String.valueOf("15%"));
                        spinner.setSelection(15);
                    } else if (floatRating == 3.5) {
                        tvService.setText(String.valueOf("Good"));
                        tvPercent.setText(String.valueOf("18%"));
                        spinner.setSelection(18);
                    } else if (floatRating == 4.0) {
                        tvService.setText(String.valueOf("Great"));
                        tvPercent.setText(String.valueOf("20%"));
                        spinner.setSelection(20);
                    } else if (floatRating == 4.5) {
                        tvService.setText(String.valueOf("Great"));
                        tvPercent.setText(String.valueOf("23%"));
                        spinner.setSelection(23);
                    } else if (floatRating >= 5.0) {
                        tvService.setText(String.valueOf("Royal"));
                        tvPercent.setText(String.valueOf("25%"));
                        spinner.setSelection(25);
                    }
                }
            }
        });
    } // end ratingBar()

    private void spinner() {
        // TODO Auto-generated method stub
        hideKeyboard();

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
                // TODO Auto-generated method stub

                spinnerChange = true;
                intSelected = spinner.getSelectedItemPosition();
                if (intSelected >= 0 && intSelected <= 2) {
                    rb.setRating(0.0f);
                    tvService.setText("Poor");
                    tvService.setTextColor(getResources().getColor(R.color.red_shade));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                    calculate();
                } else if (intSelected >= 3 && intSelected <= 4) {
                    rb.setRating(0.5f);
                    tvService.setText("Poor");
                    tvService.setTextColor(getResources().getColor(R.color.red_shade));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                    calculate();
                } else if (intSelected >= 5 && intSelected <= 7) {
                    rb.setRating(1.0f);
                    tvService.setText("Poor");
                    tvService.setTextColor(getResources().getColor(R.color.red_shade));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                    calculate();
                } else if (intSelected >= 8 && intSelected <= 9) {
                    rb.setRating(1.5f);
                    tvService.setText("Poor");
                    tvService.setTextColor(getResources().getColor(R.color.red_shade));
                    Crouton.showText(MainActivity.this, "Poor tip service percent", Style.ALERT);
                    calculate();
                } else if (intSelected >= 10 && intSelected <= 12) {
                    rb.setRating(2.0f);
                    tvService.setText("Fair");
                    tvService.setTextColor(getResources().getColor(R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Fair tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 13 && intSelected <= 14) {
                    rb.setRating(2.5f);
                    tvService.setText("Fair");
                    tvService.setTextColor(getResources().getColor(R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Fair tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 15 && intSelected <= 17) {
                    rb.setRating(3.0f);
                    tvService.setText("Good!");
                    tvService.setTextColor(getResources().getColor(R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Good tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 18 && intSelected <= 19) {
                    rb.setRating(3.5f);
                    tvService.setText("Good!");
                    tvService.setTextColor(getResources().getColor(R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Good service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 20 && intSelected <= 22) {
                    rb.setRating(4.0f);
                    tvService.setText("Great!");
                    tvService.setTextColor(getResources().getColor(R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Great tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 23 && intSelected <= 24) {
                    rb.setRating(4.5f);
                    tvService.setText("Great!");
                    tvService.setTextColor(getResources().getColor(R.color.green_dark));
                    Crouton.showText(MainActivity.this, "Great tip service percent", Style.CONFIRM);
                    calculate();
                } else if (intSelected >= 25) {
                    rb.setRating(5.0f);
                    tvService.setText("Royal!");
                    tvService.setTextColor(getResources().getColor(R.color.purple_shade));
                    Crouton.showText(MainActivity.this, "Royal tip service percent", Style.INFO);
                    calculate();
                }
                tvPercent.setText(intSelected + "%");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                // do nothing
            }
        });
    } // end spinner()

    // toggle onClick method handler
    public void toggleOnClickHandler(View view) {
        hideKeyboard();
        bToggle = ((ToggleButton) view).isChecked();
        if (bToggle) {
            // round ON; update calculations
            calculate();
        } else {
            // round OFF; update calculations
            calculate();
        }
    }

    private void calculate() {
        // TODO Auto-generated method stub

        if (spinnerChange == true) {
            spinnerChange = false;
        }

        if (clear == true) {
            Crouton.showText(MainActivity.this, "All fields reset", Style.CONFIRM);
            clear = false;

            // toggle button reset
            if (tglbtn.isChecked() == true && bToggle == true) {
                tglbtn.setChecked(false);
                bToggle = false;
            }

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
            etBill.setText("");
        } else {
            /*
			 * Legend: temp1-amount of the bill temp2-tip w/o round temp3-tip
			 * w/round temp4-total amount of bill temp5-total amount each person
			 * pays
			 */

            if (etBill.getText().toString().length() == 0) {
                tvTip.setText(String.valueOf("$0.00")); // static

            } else {
                // calculate tip amount
                temp1 = Double.parseDouble(strFormatted);
                temp2 = intSelected * temp1 / 100;

                DecimalFormat formatTip = new DecimalFormat("0.00");
                strTip = formatTip.format(temp2);

                if (bToggle == true) {
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
    } // end calculate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
    } // end shareIntent()

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
    } // end getShareIntent()

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
        Log.d(TAG, "onPause()");
        adView.pause();
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
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
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

}// end MainActivity class
