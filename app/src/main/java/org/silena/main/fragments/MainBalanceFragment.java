package org.silena.main.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

import org.silena.R;
import org.silena.main.ApdateBalanceActivity;
import org.silena.main.MainConstant;
import org.silena.main.adapter.BalanceAdapter;

import java.util.Arrays;

import static org.silena.main.MainConstant.STATUS_BALANCE;
import static org.silena.main.MainConstant.STATUS_HISTORY;

/**
 * Created by дима on 25.05.2016.
 */
public class MainBalanceFragment extends Fragment implements View.OnClickListener {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    private static final int UPDATE_BALANCE = 111;

    private BroadcastReceiver broadcastReceiver;
    private String balance = null;
    private String[] balanceList = null;
    private Button scan_btn;
    private boolean regustred = false;
    private int regustredTime = 0;
    private View view;
    private ListView lvBalance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_balance, null);

        lvBalance = (ListView) view.findViewById(R.id.lvBalance);
        scan_btn = (Button) view.findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(this);

        view.findViewById(R.id.loadProgress).setVisibility(View.VISIBLE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(MainConstant.PARAM_STATUS, 0);
                switch (status) {
                    case STATUS_BALANCE:
                        balance = intent.getStringExtra(MainConstant.PARAM_BALANCE);

                        Log.d(MainConstant.LOG_TAG, "Balance menedger Receive" + balance);
                        TextView Balance = (TextView) view.findViewById(R.id.balance);
                        if (!"".equals(balance)) {
                            Balance.setText("Balance: " + balance);
                        } else {
                            Balance.setText("Balance: 0.00");
                        }
                        break;

                    case STATUS_HISTORY:
                        balanceList = intent.getStringArrayExtra(MainConstant.PARAM_LIST);
//                        Log.d(MainConstant.LOG_TAG, "Balance menedger Receive List" + balanceList[0]);
                        viewList(balanceList);
                        view.findViewById(R.id.loadProgress).setVisibility(View.GONE);
                        break;
                }


            }
        };

        IntentFilter intFilter = new IntentFilter(MainConstant.BROADCAST_BALANCE);
        getActivity().registerReceiver(broadcastReceiver, intFilter);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Getbalance();
    }

    void viewList(String[] values) {
        BalanceAdapter adapter = new BalanceAdapter(getActivity(), Arrays.asList(values));
        lvBalance.setAdapter(adapter);
    }


    void Getbalance() {
        Long tsLong = System.currentTimeMillis() / 1000;
        if (regustred == true || (tsLong.intValue() - regustredTime) < (0 * 0 * 0 * 60)) {
        } else {
            Intent intent = new Intent(MainConstant.BROADCAST_ACTION);

            intent.putExtra(MainConstant.PARAM_STATUS, MainConstant.STATUS_BALANCE);
            intent.putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_GETBALANCE);
            getActivity().sendBroadcast(intent);
        }
    }


    void QrScaner() {
        Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
    }


    void balanceAmount(String data) {
        Intent intent = new Intent(MainConstant.BROADCAST_ACTION);
        intent.putExtra(MainConstant.PARAM_STATUS, MainConstant.STATUS_BALANCE);
        intent.putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_TRANSACTION);
        intent.putExtra(MainConstant.PARAM_RESULT, data);
        getActivity().sendBroadcast(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
            // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
            Log.d(MainConstant.LOG_TAG, "Qr Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT));
            balanceAmount(data.getStringExtra(ZBarConstants.SCAN_RESULT));
            //  Log.d(MainConstant.LOG_TAG, "Qr Scan Result Type = " + data.getIntExtra(ZBarConstants.SCAN_RESULT_TYPE, 0));
            // The value of type indicates one of the symbols listed in Advanced Options below.
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d(MainConstant.LOG_TAG, "Qr Camera unavailable");
        }
    }

    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ApdateBalanceActivity.class);
        startActivityForResult(intent, UPDATE_BALANCE);
//        if (v.getId() == R.id.scan_btn) {
//            QrScaner();
//        }
    }

}