/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silena.main;

import org.silena.R;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import net.sourceforge.zbar.Symbol;
import static org.silena.main.MainConstant.STATUS_BALANCE;
import static org.silena.main.MainConstant.STATUS_HISTORY;

/**
 *
 * @author admin2
 */
public class BalanceMain extends ListActivity implements View.OnClickListener {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;

    private BroadcastReceiver broadcastReceiver;
    private String balance=null;
    private String[] balanceList=null;
     Button scan_btn;
     boolean regustred = false;
    int  regustredTime=0;


    @Override
       public void onStart() {
            super.onStart();
setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

   }


     /**
     * Called when the activity is first created.
     * @param icicle
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

         setContentView(R.layout.balance_main);

            scan_btn = (Button)findViewById(R.id.scan_btn);
            scan_btn.setOnClickListener(this);
            broadcastReceiver = new BroadcastReceiver() {
              @Override
             public void onReceive(Context context, Intent intent) {
             int status = intent.getIntExtra(MainConstant.PARAM_STATUS, 0);
                 switch(status) {
    case STATUS_BALANCE:
         balance = intent.getStringExtra(MainConstant.PARAM_BALANCE);

         Log.d(MainConstant.LOG_TAG, "Balance menedger Receive"+balance);
         TextView  Balance= (TextView) findViewById(R.id.balance);
         if(!"".equals(balance)){
          Balance.setText("Balance: "+balance);
         } else{
          Balance.setText("Balance: 0.00");
         }


		break;

         case STATUS_HISTORY:
         balanceList = intent.getStringArrayExtra(MainConstant.PARAM_LIST);
         Log.d(MainConstant.LOG_TAG, "Balance menedger Receive List"+balanceList[0]);
         viewList(balanceList);
                break;

             }


              }
        };

        IntentFilter intFilter = new IntentFilter(MainConstant.BROADCAST_BALANCE);
        registerReceiver(broadcastReceiver, intFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // дерегистрируем (выключаем) BroadcastReceiver
        unregisterReceiver(broadcastReceiver);
    }




    @Override
    protected void onResume() {
        super.onResume();
        Getbalance();

    }




    void viewList(String[] values ){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);

    }



    void Getbalance(){
       Long tsLong  =  System.currentTimeMillis()/1000;
       if( regustred == true  || (tsLong.intValue()-regustredTime)< (0 * 0 * 0 * 60)) {
      }else{
      Intent intent = new Intent(MainConstant.BROADCAST_ACTION);

      intent.putExtra(MainConstant.PARAM_STATUS, MainConstant.STATUS_BALANCE);
         intent.putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_GETBALANCE);
      sendBroadcast(intent);

      }



    }


    void QrScaner() {
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
        startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
    }


void balanceAmount(String data){
      Intent intent = new Intent(MainConstant.BROADCAST_ACTION);
      intent.putExtra(MainConstant.PARAM_STATUS, MainConstant.STATUS_BALANCE);
      intent.putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_TRANSACTION);
      intent.putExtra(MainConstant.PARAM_RESULT,data);
      sendBroadcast(intent);
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
     //  Log.d(MainConstant.LOG_TAG, "Qr Scan Result Type = " + data.getIntExtra(ZBarConstants.SCAN_RESULT_TYPE, 0));
        // The value of type indicates one of the symbols listed in Advanced Options below.
    } else if(resultCode == RESULT_CANCELED) {
       Log.d(MainConstant.LOG_TAG, "Qr Camera unavailable");
    }
}

    public void onClick(View v) {

         if (v.getId() == R.id.scan_btn) {

             QrScaner();

              }


         }

}
