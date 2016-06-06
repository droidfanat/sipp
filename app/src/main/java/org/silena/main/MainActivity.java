package org.silena.main;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.Intent;
import static android.content.Intent.ACTION_TIME_TICK;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.silena.R;
import org.silena.main.fragments.CallLogFragment;
import org.silena.main.fragments.ContactsFragment;
import org.silena.main.fragments.DialerFragment;
import org.silena.main.fragments.MainBalanceFragment;
import org.silena.main.model.Call;
import org.silena.main.model.Contact;
import org.silena.ser.httpService;
import org.sipdroid.sipua.ui.InCallScreen;
import org.sipdroid.sipua.ui.Receiver;
import org.sipdroid.sipua.ui.RegisterService;
import org.sipdroid.sipua.ui.Settings;
import org.opentelecoms.util.dns.SRVRecordHelper;
import org.sipdroid.sipua.UserAgent;
import org.sipdroid.sipua.ui.CallbackScreen;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_READ_CALL_LOG = 200;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private LinkedList<Contact> contacts;
    private LinkedList<Call> calls;

    public String getBalnceString() {
        return balnceString;
    }

    private String balnceString;

    public LinkedList<Contact> getContacts() {
        return contacts;
    }
    public LinkedList<Call> getCalls() { return calls; }

    public static String LOG_TAG = "Silena_Log";
    SharedPreferences sPref;
    final String SAVED_USERID = "user";
    final String SAVED_USERPASS = "password";
    Button call_button;
    Button callback_button;
    public static final boolean release = true;
    public static final boolean market = false;
    public Intent intent = null;
    TextView Balance;
    String billing = null;
    String[] billing_list = null;
    Intent Balanceintent = null;
    SRVRecordHelper srv;

    /* Following the menu item constants which will be used for menu creation */
    public static final int FIRST_MENU_ID = Menu.FIRST;
    public static final int CONFIGURE_MENU_ITEM = FIRST_MENU_ID + 1;
    public static final int ABOUT_MENU_ITEM = FIRST_MENU_ID + 2;
    public static final int EXIT_MENU_ITEM = FIRST_MENU_ID + 3;
    public static final int BALANCE_MENU_ITEM = FIRST_MENU_ID + 4;
    private static AlertDialog m_AlertDlg;
    BroadcastReceiver br;

    @Override
    public void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Receiver.mContext = this;
    }

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        final Context mContext = this;
        super.onCreate(savedInstanceState);

        String[] param = loadParam();
        if ("".equals(param[0]) & "".equals(param[1])) {
//        if (false) {
            //redirect activity registration
            Intent intent = new Intent(this, Registration.class);
            startActivity(intent);
            this.finish();
        } else {

            //roadcastReceiver
            setContentView(R.layout.main);
            afterCreate();
            Toast.makeText(this, param[0], Toast.LENGTH_SHORT).show();

            Balance = (TextView) findViewById(R.id.des);

            call_button = (Button) findViewById(R.id.call_button);
//            call_button.setOnClickListener(this);

            callback_button = (Button) findViewById(R.id.callback_button);
//            callback_button.setOnClickListener(this);

            br = new BroadcastReceiver() {
                // действия при получении сообщений
                public void onReceive(Context context, Intent intent) {
                    int task = intent.getIntExtra(MainConstant.PARAM_TASK, 0);
                    int status = intent.getIntExtra(MainConstant.PARAM_STATUS, 0);
                    Log.d(MainConstant.LOG_TAG, "onReceive: task = " + task + ", status = " + status);

                    if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {

                        Getbalance();

                    }

                    if (status == MainConstant.STATUS_BALANCE) {

                        switch (task) {

                            case MainConstant.METHOD_TRANSACTION:
                                SetbalnceQr(intent.getStringExtra(MainConstant.PARAM_RESULT));
                                break;

                            case MainConstant.METHOD_GETBALANCE:
                                Getbalance();
                                GetbalanceList();
                                break;

                        }

                    }

                    //receiver service
                    if (status == MainConstant.STATUS_SERVICE) {

                        switch (task) {

                            case MainConstant.METHOD_REGISTRATION:
                                int result = intent.getIntExtra(MainConstant.PARAM_RESULT, 0);

                                switch (result) {

                                    case 401:
                                        settings();
                                        on(mContext, true);
                                        Getbalance();
                                        break;

                                }
                                Log.d(MainConstant.LOG_TAG, "Service Post Result" + result);
                                break;

                            case MainConstant.METHOD_GETBALANCE:
                                String balance = intent.getStringExtra(MainConstant.PARAM_RESULT);
                                billing = balance;
                                ViewBalance(balance);
                                Log.d(MainConstant.LOG_TAG, "Service GET BALANCE  Result" + balance);
                                break;

                            case MainConstant.METHOD_BALANCEHISTORY:
                                billing_list = intent.getStringArrayExtra(MainConstant.PARAM_RESULT);
                                if ( billing_list==null) break;
                                ViewHistory();
                                Log.d(MainConstant.LOG_TAG, "Service GET BALANCELIST  Result" + billing_list[0]);
                                break;

                            case MainConstant.METHOD_TRANSACTION:
                                int transaction_code = intent.getIntExtra(MainConstant.PARAM_RESULT, 0);
                                switch (transaction_code) {

                                    case 401:
                                        ViewHistory();
                                        break;
                                }

                                break;

                        }
                    }
                }
            };

            IntentFilter intFilt = new IntentFilter(MainConstant.BROADCAST_ACTION);
            registerReceiver(br, intFilt);
            registerReceiver(br, new IntentFilter(ACTION_TIME_TICK));
            // BroadcastReceiver
            Receiver.mStart=true;
            StartService();

        }

    }

    void Getbalance() {
        Intent intent;
        intent = new Intent(this, httpService.class)
                .putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_GETBALANCE);
        startService(intent);
    }

    void SetbalnceQr(String data) {
        Intent intent;
        intent = new Intent(this, httpService.class)
                .putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_TRANSACTION)
                .putExtra(MainConstant.PARAM_POST, data);
        startService(intent);
    }

    void GetbalanceList() {
        Intent intent;
        intent = new Intent(this, httpService.class)
                .putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_BALANCEHISTORY);
        startService(intent);
    }

    void ViewHistory() {
        Intent balanceIntent = new Intent(MainConstant.BROADCAST_BALANCE);
        balanceIntent.putExtra(MainConstant.PARAM_LIST, billing_list);
        balanceIntent.putExtra(MainConstant.PARAM_STATUS, MainConstant.STATUS_HISTORY);
        sendBroadcast(balanceIntent);

    }

    void ViewBalance(String value) {
        Intent balanceIntent = new Intent(MainConstant.BROADCAST_BALANCE);
        balanceIntent.putExtra(MainConstant.PARAM_BALANCE, value);
        balanceIntent.putExtra(MainConstant.PARAM_STATUS, MainConstant.STATUS_BALANCE);
        sendBroadcast(balanceIntent);
//        Balance.setText("Balance: " + value);

    }

    public void Callback(String phone) {
        phone = phone.replace("+", "");
        Intent intent;
        intent = new Intent(this, httpService.class)
                .putExtra(MainConstant.PARAM_POST, phone)
                .putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_CALLBACK);
        startService(intent);

        initInCallScreen();

    }

    public String[] loadParam() {
        String userid = null, userpass = null;
        userid = PreferenceManager.getDefaultSharedPreferences(this).getString(Settings.PREF_USERNAME, Settings.DEFAULT_USERNAME);
        userpass = PreferenceManager.getDefaultSharedPreferences(this).getString(Settings.PREF_PASSWORD, Settings.DEFAULT_PASSWORD);
        Log.d(MainConstant.LOG_TAG, "Load main User," + userid);
        Log.d(MainConstant.LOG_TAG, "Load main Password," + userpass);
        String[] UseerParam = {userpass, userid};
        return (UseerParam);
    }

    private void StartService() {
        Intent intent;
        intent = new Intent(this, httpService.class)
                .putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_REGISTRATION);
        startService(intent);
    }

    public static boolean on(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(Settings.PREF_ON, Settings.DEFAULT_ON);
    }

    public static void on(Context context, boolean on) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean(Settings.PREF_ON, on);
        edit.commit();
        if (on) {
            Receiver.engine(context).isRegistered();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Receiver.call_state != UserAgent.UA_STATE_IDLE) {
            Receiver.moveTop(); // вернуть активити вызова
        }
    }

    public void onClick(View v) {
        String Phone;
        EditText Regphone;
        if (v.getId() == R.id.call_button) {
            Regphone = (EditText) findViewById(R.id.regphone);
            Phone = Regphone.getText().toString();
            call_menu(Phone);
        }
        if (v.getId() == R.id.callback_button) {
            Regphone = (EditText) findViewById(R.id.regphone);
            Phone = Regphone.getText().toString();
            Callback(Phone);

        }

    }

    public void initInCallScreen() {
        Intent intent = new Intent(this, CallbackScreen.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);

        MenuItem m = menu.add(0, ABOUT_MENU_ITEM, 0, R.string.menu_about);
        m.setIcon(android.R.drawable.ic_menu_info_details);
        m = menu.add(0, EXIT_MENU_ITEM, 0, R.string.menu_exit);
        m.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        m = menu.add(0, CONFIGURE_MENU_ITEM, 0, R.string.menu_settings);
        m.setIcon(android.R.drawable.ic_menu_preferences);
//        m = menu.add(0, BALANCE_MENU_ITEM, 0, R.string.menu_balance);
//        m.setIcon(android.R.drawable.ic_menu_preferences);

        return result;
    }

    public void call_menu(String Phone) {
        Phone = Phone.replace("+", "");
        String target = Phone;
        if (m_AlertDlg != null) {
            m_AlertDlg.cancel();
        }
        if (target.length() == 0) {
            m_AlertDlg = new AlertDialog.Builder(this)
                    .setMessage(R.string.empty)
                    .setTitle(R.string.app_name)
                    .setIcon(R.drawable.icon22)
                    .setCancelable(true)
                    .show();
        } else if (!Receiver.engine(this).call(target, true)) {
            m_AlertDlg = new AlertDialog.Builder(this)
                    .setMessage(R.string.notfast)
                    .setTitle(R.string.app_name)
                    .setIcon(R.drawable.icon22)
                    .setCancelable(true)
                    .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = super.onOptionsItemSelected(item);
        Intent intent = null;

        switch (item.getItemId()) {
            case ABOUT_MENU_ITEM:
                if (m_AlertDlg != null) {
                    m_AlertDlg.cancel();
                }
                m_AlertDlg = new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.about).replace("\\n", "\n").replace("${VERSION}", getVersion(this)))
                        .setTitle(getString(R.string.menu_about))
                        .setIcon(R.drawable.icon22)
                        .setCancelable(true)
                        .show();
                break;

            case EXIT_MENU_ITEM:
                //on(this,false);
                stopService(new Intent(this, httpService.class));
                Receiver.pos(true);
                Receiver.engine(this).halt();
                Receiver.mSipdroidEngine = null;
                Receiver.reRegister(0);
                Receiver.mStart=false;
                stopService(new Intent(this, RegisterService.class));
                finish();
                break;

            case CONFIGURE_MENU_ITEM: {
                try {
                    intent = new Intent(this, org.sipdroid.sipua.ui.Settings.class);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                }
            }
            break;

            case BALANCE_MENU_ITEM: {
                try {
                    intent = new Intent(this, org.silena.main.BalanceMain.class);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                }
            }
            break;
        }

        return result;
    }

    public static String getVersion() {
        return getVersion(Receiver.mContext);
    }

    void balanceActivity() {
        Balanceintent = new Intent(this, BalanceMain.class);
        startActivityForResult(intent, 1);

    }

    /*
      DEBUG MAIN SETINGS
     */
    void settings() {
        Log.d(MainConstant.LOG_TAG, "------------------------------------------S--------------------------------------------------------------------------------");
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_USERNAME" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_USERNAME, Settings.DEFAULT_USERNAME));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_PASSWORD" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_PASSWORD, Settings.DEFAULT_PASSWORD));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_SERVER" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_SERVER, Settings.DEFAULT_SERVER));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_DOMAIN" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_DOMAIN, Settings.DEFAULT_DOMAIN));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_PROTOCOL" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_PROTOCOL, Settings.DEFAULT_PROTOCOL));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_FROMUSER" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_FROMUSER, Settings.DEFAULT_FROMUSER));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_PORT" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_PORT, Settings.DEFAULT_PORT));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_WLAN" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_WLAN, Settings.DEFAULT_WLAN));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_3G" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_3G, Settings.DEFAULT_3G));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_EDGE" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_EDGE, Settings.DEFAULT_EDGE));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_VPN" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_VPN, Settings.DEFAULT_VPN));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_PREF" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_PREF, Settings.DEFAULT_PREF));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_AUTO_ON" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_AUTO_ON, Settings.DEFAULT_AUTO_ON));
        Log.d(MainConstant.LOG_TAG, "PREF_AUTO_HEADSET" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_AUTO_HEADSET, Settings.DEFAULT_AUTO_HEADSET));
        Log.d(MainConstant.LOG_TAG, "PREF_MWI_ENABLED" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_MWI_ENABLED, Settings.DEFAULT_MWI_ENABLED));
        Log.d(MainConstant.LOG_TAG, "PREF_REGISTRATION" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_REGISTRATION, Settings.DEFAULT_REGISTRATION));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_NOTIFY" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_NOTIFY, Settings.DEFAULT_NOTIFY));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_NODATA," + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_NODATA, Settings.DEFAULT_NODATA));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_SIPRINGTONE" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_SIPRINGTONE, Settings.DEFAULT_SIPRINGTONE));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_SEARCH" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_SEARCH, Settings.DEFAULT_SEARCH));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_EXCLUDEPAT" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_EXCLUDEPAT, Settings.DEFAULT_EXCLUDEPAT));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_OWNWIFI" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_OWNWIFI, Settings.DEFAULT_OWNWIFI));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_STUN" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_STUN, Settings.DEFAULT_STUN));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_STUN_SERVER" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_STUN_SERVER, Settings.DEFAULT_STUN_SERVER));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_STUN_SERVER_PORT" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_STUN_SERVER_PORT, Settings.DEFAULT_STUN_SERVER_PORT));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_MMTEL" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_MMTEL, Settings.DEFAULT_MMTEL));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_MMTEL_QVALUE" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_MMTEL_QVALUE, Settings.DEFAULT_MMTEL_QVALUE));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_CALLRECORD" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_CALLRECORD, Settings.DEFAULT_CALLRECORD));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_PAR" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_PAR, Settings.DEFAULT_PAR));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_IMPROVE" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_IMPROVE, Settings.DEFAULT_IMPROVE));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_POS" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_POS, Settings.DEFAULT_POS));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_CALLBACK" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_CALLBACK, Settings.DEFAULT_CALLBACK));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_CALLTHRU" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_CALLTHRU, Settings.DEFAULT_CALLTHRU));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_CALLTHRU2" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_CALLTHRU2, Settings.DEFAULT_CALLTHRU2));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_CODECS" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_CODECS, Settings.DEFAULT_CODECS));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_DNS" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_DNS, Settings.DEFAULT_DNS));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_VQUALITY" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_VQUALITY, Settings.DEFAULT_VQUALITY));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_MESSAGE" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_MESSAGE, Settings.DEFAULT_MESSAGE));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_BLUETOOTH" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_BLUETOOTH, Settings.DEFAULT_BLUETOOTH));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_KEEPON" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_KEEPON, Settings.DEFAULT_KEEPON));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_SELECTWIFI" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getBoolean(Settings.PREF_SELECTWIFI, Settings.DEFAULT_SELECTWIFI));
        Log.d(MainConstant.LOG_TAG, "Settings.PREF_ACCOUNT" + PreferenceManager.getDefaultSharedPreferences(getUIContext()).getInt(Settings.PREF_ACCOUNT, Settings.DEFAULT_ACCOUNT));
        Log.d(MainConstant.LOG_TAG, "------------------------------------------END-------------------------------------------------------------------------------");
    }

    public Context getUIContext() {
        return Receiver.mContext;
    }

    public static String getVersion(Context context) {
        final String unknown = "Unknown";

        if (context == null) {
            return unknown;
        }
        try {
            String ret = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            if (ret.contains(" + ")) {
                ret = ret.substring(0, ret.indexOf(" + ")) + "b";
            }
            return ret;
        } catch (PackageManager.NameNotFoundException ex) {
        }

        return unknown;
    }

    private void afterCreate() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && MainActivity.this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG}, PERMISSIONS_REQUEST_READ_CONTACTS);
//        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                contacts = fetchContacts();
                return null;
            }
        }.execute();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                calls = getCallDetails();
                return null;
            }
        }.execute();
    }


    private LinkedList<Call> getCallDetails() {

        LinkedList<Call> calls = new LinkedList<>();


        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String callName = managedCursor.getString(name);

            int dircode = Integer.parseInt(callType);

            Call call = new Call();
            call.setPhone(phNumber);
            call.setType(dircode);
            call.setDate(callDayTime);
            call.setDuration(callDuration);
            call.setName(callName);
            calls.add(call);

        }
//        managedCursor.close();
        Collections.reverse(calls);
        return calls;

    }


    public LinkedList<Contact> fetchContacts() {
        LinkedList<Contact> contacts = new LinkedList<>();
        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String PHOTO = ContactsContract.Contacts.PHOTO_URI;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        assert cursor != null;
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                String uriString = cursor.getString(cursor.getColumnIndex( PHOTO ));
                Uri photoUri = null;
                if(uriString != null){
                    photoUri = Uri.parse(uriString);
                }


                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {
                    Contact contact = new Contact();
                    contact.setName(name);
                    contact.setPhotoUri(photoUri);
//                    output.append("\n First Name:").append(name);

                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    assert phoneCursor != null;
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        contact.setPhone(phoneNumber);
                        break;
                    }
                    phoneCursor.close();
                    contacts.add(contact);
                }
            }
        }

        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        cursor.close();
        Log.e("Contacts", contacts.toString());

        return contacts;

    }

//



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new DialerFragment();
                case 1:
                    return new CallLogFragment();
                case 2:
                    return new ContactsFragment();
                case 3:
                    return new MainBalanceFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                case 0:
                    return getSpannableStringFotTab(R.drawable.call, "Dialer");
                case 1:
                    return getSpannableStringFotTab(R.drawable.call_log, "Call log");
                case 2:
                    return getSpannableStringFotTab(R.drawable.contacts, "Contacts");
                case 3:
                    return getSpannableStringFotTab(R.drawable.balance, "Balance");
            }
            return null;
        }
    }

    @NonNull
    private SpannableString getSpannableStringFotTab(int imageResource, String title) {
        // Generate title based on item position
        Drawable image = ContextCompat.getDrawable(MainActivity.this, imageResource);
        image.setBounds(0, 0, image.getIntrinsicHeight(), image.getIntrinsicHeight());
//        int i = 322112/ 2213123;
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + title);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    public void showCallDialog(final String number) {

        final Dialog stolDialog = new Dialog(this);
        stolDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        stolDialog.setContentView(R.layout.call_dialog);
        TextView tvPhone = (TextView) stolDialog.findViewById(R.id.tvPhone);
        Button call_button = (Button) stolDialog.findViewById(R.id.call_button);
        Button callback_button = (Button) stolDialog.findViewById(R.id.callback_button);

        tvPhone.setText(number);
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call_menu(number);
            }
        });

        callback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback(number);
            }
        });

        stolDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        stolDialog.show();
    }

}
