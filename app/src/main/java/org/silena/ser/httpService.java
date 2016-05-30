/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silena.ser;

/**
 *
 * @author admin2
 */


import static org.silena.main.MainActivity.LOG_TAG;
import org.silena.main.MainConstant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import static org.apache.http.params.CoreProtocolPNames.USER_AGENT;
import org.apache.http.util.EntityUtils;
import org.silena.main.MainSettings;
import org.silena.main.ServerApi;
import org.sipdroid.sipua.ui.Receiver;
import org.sipdroid.sipua.ui.Settings;



public class httpService extends Service {


  ExecutorService es;
  public String cookies ;//=" PHPSESSID=6qk02ekrpb82n6drj1pqv52ov4; path=/";
  boolean regustred = false;
  int  regustredTime=0;
  
  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(LOG_TAG, "Silena http service Start this save http vars");
    es = Executors.newFixedThreadPool(1);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(LOG_TAG, "MyService onDestroy");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(LOG_TAG, "MyService onStartCommand");

    int task = intent.getIntExtra(MainConstant.PARAM_TASK, 0);
    String url = intent.getStringExtra(MainConstant.PARAM_URL);
    String post = intent.getStringExtra(MainConstant.PARAM_POST);
    Log.d(LOG_TAG, "task"+task);
    MyRun mr = new MyRun(startId, url, task,post);
    es.execute(mr);

    return super.onStartCommand(intent, flags, startId);
  }

  public IBinder onBind(Intent arg0) {
    return null;
  }

  class MyRun implements Runnable {

    String url;
    String post;
   
    int httpcod;
    int startId;
    int task;
    
    public MyRun(int startId, String url, int task, String post) {
      this.url = url;
      this.startId = startId;
      this.task = task;
      this.post = post;
      Log.d(LOG_TAG, "MyRun#" + startId + " create");
    }

    public void run() {
      InputStream stream;  
      Intent intent = new Intent(MainConstant.BROADCAST_ACTION);
    
      Log.d(LOG_TAG, "Service http start");
     

      
      switch (this.task) {
          case MainConstant.METHOD_BITMAP:
              stream = httpget(this.url);
              Bitmap capcha = BitmapFactory.decodeStream(stream);
              intent.putExtra(MainConstant.PARAM_RESULT, capcha);
              intent.putExtra(MainConstant.PARAM_TASK, this.task);
              Log.d(LOG_TAG, "Capcha load");
              break;

          case MainConstant.METHOD_POST:
              String result = httpPost(this.url, this.post);
              intent.putExtra(MainConstant.PARAM_RESULT, result);
              intent.putExtra(MainConstant.PARAM_TASK, this.task);
             break;
    
    case MainConstant.METHOD_GETBALANCE:  
 
        String json = getBAlance();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ServerApi regapi = gson.fromJson(json, ServerApi.class);

        switch (regapi.getCode()) {
            case 401:
                intent.putExtra(MainConstant.PARAM_RESULT, regapi.getBalans());
                Log.d(MainConstant.LOG_TAG, "Service regapi get balance :" + regapi.getBalans());
                break;

            case 403:
                this.task = MainConstant.METHOD_REGISTRATION;
                break;

            case 404:
                regustred = false;
                intent.putExtra(MainConstant.PARAM_RESULT, "0");
                Log.d(MainConstant.LOG_TAG, "Service regapi get balance :" + regapi.getCode());
                break;
        }

        intent.putExtra(MainConstant.PARAM_TASK, this.task);
 break;     
  
     
      
          case MainConstant.METHOD_CALLBACK:
              initCallback(this.post);
break;
  
              
              
          case MainConstant.METHOD_BALANCEHISTORY :
      intent.putExtra(MainConstant.PARAM_TASK, this.task);    
      json =null;  
      String[] param = loadParam();
      gson = new GsonBuilder().setPrettyPrinting().create();
      regapi = new ServerApi(119,param[0],null, null,null, 30, null,null);
      json = gson.toJson(regapi);
      json = httpPost(MainConstant.SERV_URL,json);    
      regapi = gson.fromJson(json, ServerApi.class);
      
             switch (regapi.getCode()) {
                case 401:  
      intent.putExtra(MainConstant.PARAM_RESULT,regapi.getBalanceHistory());
      intent.putExtra(MainConstant.PARAM_TASK, this.task);
                 break;
              case 404:
                              regustred = false;    
              String[] list = null;
              intent.putExtra(MainConstant.PARAM_RESULT,list);
              intent.putExtra(MainConstant.PARAM_TASK, this.task);         
              break;              
      
                 
       }      

   break;      
          
           
              
          case MainConstant.METHOD_TRANSACTION:
              json = null;
              param = loadParam();
              gson = new GsonBuilder().setPrettyPrinting().create();
              regapi = new ServerApi(125, param[0], null, null, null, 30, null, post);
              json = gson.toJson(regapi);
              json = httpPost(MainConstant.SERV_URL, json);
              regapi = gson.fromJson(json, ServerApi.class);
              Log.d(MainConstant.LOG_TAG, "Service response Update Balance code  :" + regapi.getCode());

              intent.putExtra(MainConstant.PARAM_RESULT, regapi.getCode());
              intent.putExtra(MainConstant.PARAM_TASK, this.task);

break;


      case MainConstant.METHOD_REGISTRATION: 
      
          Long tsLong = System.currentTimeMillis() / 1000;
          if (regustred == true || (tsLong.intValue() - regustredTime) < (0 * 12 * 60 * 60)) {
              Log.d(MainConstant.LOG_TAG, "Service Registration time :" + regustredTime);
          } else {

              param = loadParam();
              gson = new GsonBuilder().setPrettyPrinting().create();
              regapi = new ServerApi(104, param[0], null, null, param[1], 30, null, null);
              json = gson.toJson(regapi);
              json = httpPost(MainConstant.SERV_URL, json);
              Log.d(MainConstant.LOG_TAG, "Service Registration response" + json);
              regapi = gson.fromJson(json, ServerApi.class);

              switch (regapi.getCode()) {
                  case 401:
                      regustred = true;
                      regustredTime = tsLong.intValue();
                      Log.d(MainConstant.LOG_TAG, "Service Registration time :" + regustredTime);
                      postSettings();
                      intent.putExtra(MainConstant.PARAM_RESULT, 401);

                      break;

                  case 403:
                      intent.putExtra(MainConstant.PARAM_RESULT, 403);
                      break;

                  case 404:
                      regustred = false;
                      break;
              }
          }
          Log.d(MainConstant.LOG_TAG, "Service main task :" + this.task);
          intent.putExtra(MainConstant.PARAM_TASK, this.task);

          break;
      





      }
      
      
  

         
       
       
 
        

      
      

     
  

     

      
      
      
      
        // сообщаем об окончании задачи
        intent.putExtra(MainConstant.PARAM_STATUS, MainConstant.STATUS_SERVICE);
        sendBroadcast(intent);

 
    }

    
public void initCallback(String phone){
      String json =null; 
     Gson gson = new GsonBuilder().setPrettyPrinting().create();
     String[] param = loadParam();
     ServerApi regapi = new ServerApi(120,param[0],null, null,null, 30,phone,null);
     json = gson.toJson(regapi);
     json = httpPost(MainConstant.SERV_URL,json);
     
}    
    
    
public String getBAlance(){
      String json =null;  
      Log.d(MainConstant.LOG_TAG, "Service Get settings cookies  :" + cookies);
      String[] param = loadParam();
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      ServerApi regapi = new ServerApi(118,param[0],null, null,null, 30, null,null);
      json = gson.toJson(regapi);
        Log.d(MainConstant.LOG_TAG, "Service get balance json  :"+ json);
      json = httpPost(MainConstant.SERV_URL,json);
       Log.d(MainConstant.LOG_TAG, "Service response balance json  :"+ json);
       
       return json;
}    
    
    
    
public void postSettings(){
      String json =null;  
       Log.d(MainConstant.LOG_TAG, "Service Get settings cookies  :" + cookies);
      String[] param = loadParam();
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      ServerApi regapi = new ServerApi(117,param[0],null, null,null, 30, null,null);
      json = gson.toJson(regapi);  
      json = httpPost(MainConstant.SERV_URL,json);
      MainSettings settings = gson.fromJson(json, MainSettings.class);
      Log.d(MainConstant.LOG_TAG, "Service response settings sip  :" + settings.getCode());
      Log.d(MainConstant.LOG_TAG, "Service response settings cookies  :" + cookies);
      if(settings.getCode()==401){
          saveParam(settings);
      }
      
}    
    
    
    
public void saveParam(MainSettings api) {
         api.setSERVER();
         api.setDOMAIN();
         api.setPORT();
         api.setPROTOCOL();
        // api.setCODECS();
    }    
    
    
    
public String[] loadParam() {
        String userid = null, userpass = null;
        userid = (String)PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_USERNAME, Settings.DEFAULT_USERNAME);
        userpass = (String) PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(Settings.PREF_PASSWORD, Settings.DEFAULT_PASSWORD);
        Log.d(MainConstant.LOG_TAG, "Service Load main User," + userid);  
        Log.d(MainConstant.LOG_TAG, "Service Load main Password," + userpass);
        String[] UseerParam = {userid,userpass};
        return (UseerParam);
    }
      
    public Context getUIContext() {
        return Receiver.mContext;
    }

    
      private  InputStream  httpget(String url) {
          HttpClient httpclient = new DefaultHttpClient();
          InputStream input = null; 

          HttpGet httppost = new HttpGet(url);
          httppost.setHeader("Host", "78.46.251.139");
          httppost.setHeader("User-Agent", USER_AGENT);
          httppost.setHeader("Cookie",cookies+";");
          httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
          httppost.setHeader("Accept-Language", "en-US,en;q=0.5");
          httppost.setHeader("Connection", "keep-alive");
          httppost.setHeader("Referer", "https://xxx/Atlant");
          httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");

          CookieHandler.setDefault(new CookieManager());
          HttpResponse response;
          try {
              response = httpclient.execute(httppost);

              cookies = response.getLastHeader("Set-Cookie") == null ? cookies
                      : response.getLastHeader("Set-Cookie").toString().replace("Set-Cookie:", "");
             
            HttpEntity entity = response.getEntity();
        input = entity.getContent();
              
              return(input);
          } catch (IOException ex) {
              Logger.getLogger(httpService.class.getName()).log(Level.SEVERE, null, ex);
          }
            
          return(input);
      }
    

   private String httpPost(String url,String post){
    String result =null;
       CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
       CookieHandler.setDefault(cm);
       HttpClient httpclient = new DefaultHttpClient();
       HttpPost httppost = new HttpPost(url);


       List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
       
       nameValuePairs.add(new BasicNameValuePair("Json",post));
      Log.d(LOG_TAG,"Service send post JSON:,"+post);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(httpService.class.getName()).log(Level.SEVERE, null, ex);
        }

        httppost.setHeader("Host", "78.46.251.139");
	httppost.setHeader("User-Agent", USER_AGENT);
	httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	httppost.setHeader("Accept-Language", "en-US,en;q=0.5");
	httppost.setHeader("Cookie",cookies);
	httppost.setHeader("Connection", "keep-alive");
	httppost.setHeader("Referer", "https://xxx/Atlant");
	httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        
        try {
            HttpResponse response = httpclient.execute(httppost);
                        int responseCode = response.getStatusLine().getStatusCode();
            Log.d(LOG_TAG,"Response code,"+responseCode);
            switch (responseCode) {
                case 200:         
                    cookies = response.getLastHeader("Set-Cookie") == null ? cookies
                      : response.getLastHeader("Set-Cookie").toString().replace("Set-Cookie:","");
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                         result = EntityUtils.toString(entity);
                        Log.d(LOG_TAG,"Service Response post agent :"+result);
                         return(result);
                    }
                    break;
            }
                   
        
        
        
        
        } catch (IOException ex) {
          Log.d(LOG_TAG,"Service Response post Server fault:");
            //Logger.getLogger(httpService.class.getName()).log(Level.SEVERE, null, ex);
            result="{\"code\":404}";
        }

        
        
    return result;    
   }
    
    
    
    void stop() {
      Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult("
          + startId + ") = " + stopSelfResult(startId));
    }

   
  }
}