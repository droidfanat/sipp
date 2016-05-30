/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silena.main;

/**
 *
 * @author admin2
 */



import org.silena.R; 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.silena.ser.httpService;
import org.sipdroid.sipua.ui.Receiver;
import android.telephony.TelephonyManager;


public class Registration extends AppCompatActivity implements View.OnClickListener {
    Button reg_button;
    ImageView view;
    ServerApi Mainapi;


  final int TASK2_CODE = 2;
  final int TASK3_CODE = 3;



  BroadcastReceiver br;
  ProgressDialog pd;
  
  
      @Override
       public void onStart() {
           super.onStart();
setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);             
   }
  
  
  /** Called when the activity is first created.
     * @param savedInstanceState */
  @Override
  public void onCreate(Bundle savedInstanceState) {
      Log.d(MainConstant.LOG_TAG, "Registaration Pre start ");
      Receiver.engine(this);
    super.onCreate(savedInstanceState);
        setContentView(R.layout.registartion);
        view = (ImageView) findViewById(R.id.capcha);   
        reg_button = (Button)findViewById(R.id.reg_button);
        reg_button.setOnClickListener(this); 
        
        if(MainConstant.REG_CALLBACK==0){
       Log.d(MainConstant.LOG_TAG, "Registaration Start Task CApcha loader ");
             loadCapcha();        
     
        }
        
    //  BroadcastReceiver
    br = new BroadcastReceiver() {
      // действия при получении сообщений
      public void onReceive(Context context, Intent intent) {
        int task = intent.getIntExtra(MainConstant.PARAM_TASK, 0);
        int status = intent.getIntExtra(MainConstant.PARAM_STATUS, 0);
        Log.d(MainConstant.LOG_TAG, "onReceive: task = " + task + ", status = " + status);
        
        
        // Ловим сообщения о старте задач
        if (status  == MainConstant.STATUS_START) {
          switch (task) {
          case MainConstant.METHOD_BITMAP:
          
            break;
          }
        }
        
        // Ловим сообщения об окончании задач
        if (status == MainConstant.STATUS_SERVICE) {
        
          switch (task) {
          case MainConstant.METHOD_BITMAP:
              Bitmap bitmap = (Bitmap) intent.getParcelableExtra(MainConstant.PARAM_RESULT);
              view.setImageBitmap(bitmap);
              Log.d(MainConstant.LOG_TAG, "Service http bitmap task end");
            break;
          case MainConstant.METHOD_GET:
    
            break;
          case MainConstant.METHOD_POST:
            Log.d(MainConstant.LOG_TAG, "Service http json task end");
            String result = (String) intent.getStringExtra(MainConstant.PARAM_RESULT);
            if (result!=null){
                    Gson gson = new Gson();
               ServerApi regapi = gson.fromJson(result, ServerApi.class);
                    Log.d(MainConstant.LOG_TAG,"Json apiResponse, " + regapi.getCode());  
              switch (regapi.getCode()) {
                  case 401:
                      //  SaveUser(regapi.getUser(),regapi.getPassword());
                      Mainapi=regapi;
                      initEndValidation(102);
                      break;
                  case 104:
                      Log.d(MainConstant.LOG_TAG, "Registaration Start Task CApcha loader ");
                      RenderError(getString(R.string.error_capcha));
                      Log.d(MainConstant.LOG_TAG, "phone: error capcha reload");
                      loadCapcha();
                      break;
                  case 102:
                      waitCallBack();
                      break;
                  case 103:
                      SaveUser();
                      break;                 
                  default:
                      pd.dismiss();
                      RenderError("error"+ new Integer(regapi.getCode()).toString());
                      break;
              }
            }
            Log.d(MainConstant.LOG_TAG, "Service Post Result"+result);
            break;
          }
        }
      }
    };
  //roadcastReceiver
    IntentFilter intFilt = new IntentFilter(MainConstant.BROADCAST_ACTION);
    // BroadcastReceiver
    registerReceiver(br, intFilt);
  }
  
  protected void loadCapcha(){
       Intent intent;
       intent = new Intent(this, httpService.class).putExtra(MainConstant.PARAM_URL, MainConstant.PICT_URL)
               .putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_BITMAP);
                startService(intent);
  }
  
  
  private void initEndValidation(int cod){
       
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ServerApi startApi = new ServerApi(cod,Mainapi.getUser(),null, null,Mainapi.getPassword(), 30, null,null);
        String post = gson.toJson(startApi);
        
    
             Intent intent;
       intent = new Intent(this, httpService.class).putExtra(MainConstant.PARAM_URL, MainConstant.SERV_URL) 
               .putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_POST).putExtra(MainConstant.PARAM_POST,post); 
        
       startService(intent);     
      
      
   }
  
   
      
   private void waitCallBack(){
   
   BroadcastReceiver waitCallBack;
        waitCallBack = new  BroadcastReceiver() {
            public String phoneNumber="";
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                    //получаем исходящий номер
                    phoneNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
                } else if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
                    String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                    if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        //телефон звонит, получаем входящий номер
                        phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                        //телефон находится в режиме звонка (набор номера / разговор)
                    } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                      // validation Callback
                         Log.d(MainConstant.LOG_TAG, "Registration call end" +phoneNumber); 
                         initEndValidation(103);
                         
                       
                    }
                }
            }
        };
    registerReceiver(waitCallBack, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));    
   
      pd = new ProgressDialog(this);
      pd.setTitle("Registration Process");
      pd.setMessage("Please Wait");
      pd.show();
   }
  
  
  
  
  private void SaveUser(){ 
      MainSettings mainSettings=new MainSettings();
      mainSettings.API_USERNAME=Mainapi.getUser();
      mainSettings.API_FROMUSER =Mainapi.getUser();
      mainSettings.API_PASSWORD=Mainapi.getPassword();
      mainSettings.setUSERNAME();
      mainSettings.setPASSWORD();
      mainSettings.setFROMUSER();
   
      
      Log.d(MainConstant.LOG_TAG, "registration Save user:  "+Mainapi.getUser());
      Log.d(MainConstant.LOG_TAG, "registration Save Password:  "+Mainapi.getPassword());
      Log.d(MainConstant.LOG_TAG, "registration Destroy:  Activity");
          Intent intent = new Intent(this, MainActivity.class);  
        startActivity(intent);
        this.finish();
  }
  
  
  
  
  
  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopService(new Intent(this, httpService.class));
    // дерегистрируем (выключаем) BroadcastReceiver
    unregisterReceiver(br);
  }
  
      public void Registration() {
        
        //get phone or validation
        EditText Regphone = (EditText) findViewById(R.id.regphone);
         String Capcha=null;
        if (Regphone.getText().toString().equals("")) {

            RenderError(getString(R.string.error_phone));
            Log.d(MainConstant.LOG_TAG, "phone: error phone null");
            return;
           
        }
        
        if(MainConstant.REG_CALLBACK==0){
         EditText Regcapcha = (EditText) findViewById(R.id.regcapcha);
        
         if (Regcapcha.getText().toString().equals("")) {

            RenderError(getString(R.string.error_capcha));
            Log.d(MainConstant.LOG_TAG, "phone: error capcha no valid");
            return;
           
        }
 
         Capcha = Regcapcha.getText().toString();
        }
         
        String Phone = Regphone.getText().toString();
        
        
        
        if (PhoneValid(Phone) != true) {
            RenderError(getString(R.string.error_phone));
            Log.d(MainConstant.LOG_TAG, "phone: error phone regxp");
            return;
        }
        
      
   
       if( isOnline()==false){
           Log.d(MainConstant.LOG_TAG, "phone: no Connect Check Internet"); 
            RenderError(getString(R.string.error_internet));
               return;
       }
               
        
    // Phone ok step 2          
        Log.d(MainConstant.LOG_TAG, "phone: Filters ok " + Phone);
        Log.d(MainConstant.LOG_TAG, "registartinon send service reg method");
        
     
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ServerApi regapi = new ServerApi(101, Phone,Capcha, null, null, 30, null,null);
        String post = gson.toJson(regapi);
             //Start task post Service
       
          Log.d(MainConstant.LOG_TAG, "registartinon Start service post method"+post);     
             Intent intent;
       intent = new Intent(this, httpService.class).putExtra(MainConstant.PARAM_URL, MainConstant.SERV_URL) 
               .putExtra(MainConstant.PARAM_TASK, MainConstant.METHOD_POST).putExtra(MainConstant.PARAM_POST,post); 
        
       startService(intent);     
                 
    }

    
    private boolean PhoneValid(String phone) {
        String sDomen = "^(\\+)[0-9]{9,14}";
        Pattern p = Pattern.compile(sDomen);
        Matcher m = p.matcher(phone);
        return (m.matches());
    }
    
 
    
    private void RenderError(String massage) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_error,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(massage);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    
  public boolean isOnline() {

    Runtime runtime = Runtime.getRuntime();
    try {

        Process ipProcess = runtime.exec("/system/bin/ping -c 1 78.46.251.139");
        int     exitValue = ipProcess.waitFor();
        return (exitValue == 0);
//        return true;

    } catch (IOException e)          { e.printStackTrace(); } 
      catch (InterruptedException e) { e.printStackTrace(); }

    return false;
//      return true;
}     



    public void onClick(View v) {
        
        if (v.getId() == R.id.reg_button) {
            Log.d(MainConstant.LOG_TAG, "phone: reg_button");
            Registration();
        }
    }

}

