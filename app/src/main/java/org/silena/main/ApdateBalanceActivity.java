package org.silena.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

import org.silena.R;

import static org.silena.main.MainConstant.STATUS_BALANCE;
import static org.silena.main.MainConstant.STATUS_HISTORY;

public class ApdateBalanceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int ZBAR_SCANNER_REQUEST = 0;


    private Button scan_btn;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apdate_balance);

        scan_btn = (Button)findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(this);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.scan_btn) {
            QrScaner();
        }
    }

    void QrScaner() {
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
            // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
            Log.d(MainConstant.LOG_TAG,"Qr Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT));
            balanceAmount(data.getStringExtra(ZBarConstants.SCAN_RESULT));
            finish();
            //  Log.d(MainConstant.LOG_TAG, "Qr Scan Result Type = " + data.getIntExtra(ZBarConstants.SCAN_RESULT_TYPE, 0));
            // The value of type indicates one of the symbols listed in Advanced Options below.
        } else if(resultCode == RESULT_CANCELED) {
            Log.d(MainConstant.LOG_TAG, "Qr Camera unavailable");
        }
    }

    void balanceAmount(String data){
        Intent intent = new Intent(MainConstant.BROADCAST_ACTION);
        intent.putExtra(MainConstant.PARAM_STATUS, MainConstant.STATUS_BALANCE);
        intent.putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_TRANSACTION);
        intent.putExtra(MainConstant.PARAM_RESULT,data);
        sendBroadcast(intent);
    }
}
