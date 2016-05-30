/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silena.main;

import org.silena.R; 
import static org.silena.main.DownloadImageActivity.DEBUG_TAG;
import static org.silena.main.MainActivity.LOG_TAG;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.utils.HttpDriver;
import org.utils.MainVars;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/**
 *
 * @author admin2
 */
public class RegistrationOld extends Activity implements View.OnClickListener{
    
    private final String USER_AGENT = "Silena/1.1";
    final static String SERV_URL = "http://78.46.251.139/index.php?r=pbx/CliApi";
    final static String PICT_URL = "http://78.46.251.139/index.php?r=pbx/captcha";

    Button reg_button;
    ImageView view;
    final int PROGRESS_DLG_ID = 666;

    UserApi Regapi;
    MainVars mainvars = null;

     
       @Override
    public void onCreate(Bundle savedInstanceState)
    {   setContentView(R.layout.registartion);
        view = (ImageView) findViewById(R.id.capcha);   
        reg_button = (Button)findViewById(R.id.reg_button);
        reg_button.setOnClickListener(this);  
        Log.d(LOG_TAG, "phone: next");
        mainvars = (MainVars) getLastNonConfigurationInstance();
      
    
        try {
      
             capcha();
             
         }  catch (ExecutionException ex) {
             Logger.getLogger(RegistrationOld.class.getName()).log(Level.SEVERE, null, ex);
         }  catch (InterruptedException ex) {
             Logger.getLogger(RegistrationOld.class.getName()).log(Level.SEVERE, null, ex);
         }
   
    super.onCreate(savedInstanceState);
    }

    public void capcha() throws InterruptedException, ExecutionException{
            
            if(mainvars ==null){  
            mainvars = new MainVars();
            saveCookie(null);
            }
            this.mainvars.setCookies(loadCookie());
            Log.d(LOG_TAG, "Destination main lost cookies," + mainvars.getCookies());
            mainvars = new DownloadCapcha().execute(PICT_URL).get();
            view.setImageBitmap(mainvars.getBitmap()); 
             saveCookie(mainvars.getCookies());
            Log.d(LOG_TAG, "Destination main cookies," + mainvars.getCookies());
            
    }
    
    	@Override
	protected Dialog onCreateDialog(int dialogId){
		ProgressDialog progress = null;
		switch (dialogId) {
		case PROGRESS_DLG_ID:
			progress = new ProgressDialog(this);
		        progress.setMessage("Loading...");
		    
			break;
		}
		return progress;
	}
    
  
    public void onClick(View v) {
        
        if (v.getId() == R.id.reg_button) {
            Log.d(LOG_TAG, "phone: reg_button");
            Registration();
        }
    }
    
    
    public void Registration() {
        
        //get phone or validation
        EditText Regphone = (EditText) findViewById(R.id.regphone);
        EditText Regcapcha = (EditText) findViewById(R.id.regcapcha);
        if (Regphone.getText().toString().equals("")) {

            RenderError(getString(R.string.error_phone));
            Log.d(LOG_TAG, "phone: error phone null");
            return;
           
        }
        
         if (Regcapcha.getText().toString().equals("")) {

            RenderError(getString(R.string.error_capcha));
            Log.d(LOG_TAG, "phone: error phone null");
            return;
           
        }

        String Phone = Regphone.getText().toString();
        String Capcha = Regcapcha.getText().toString();
        
        
        if (PhoneValid(Phone) != true) {
            RenderError(getString(R.string.error_phone));
            Log.d(LOG_TAG, "phone: error phone regxp");
            return;
        }
        
      
   
       if( isOnline()==false){
           Log.d(LOG_TAG, "phone: no Connect Check Internet"); 
            RenderError(getString(R.string.error_internet));
               return;
       }
               
        
    // Phone ok step 2          
        Log.d(LOG_TAG, "phone: Filters ok " + Phone);
        mainvars.setCookies(loadCookie());
        mainvars.setUser(Phone);
        mainvars.setCapcha(Capcha);
        mainvars.setCode(101);
        
        Log.d(LOG_TAG, "mainvars" + mainvars.getCookies());
        try {
        String Result = new  DownloadImageTask().execute(SERV_URL).get();
         if(Result=="401"){
        Intent intent = new Intent(this, MainActivity.class);  
        startActivity(intent);
        this.finish();
        }else if(Result=="104"){RenderError(getString(R.string.error_capcha)); capcha(); }
        
        
        } catch (InterruptedException ex) {
            Logger.getLogger(RegistrationOld.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(RegistrationOld.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        
        
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
        return;
    }

    
  public boolean isOnline() {

    Runtime runtime = Runtime.getRuntime();
    try {

        Process ipProcess = runtime.exec("/system/bin/ping -c 1 78.46.251.139");
        int     exitValue = ipProcess.waitFor();
        return (exitValue == 0);

    } catch (IOException e)          { e.printStackTrace(); } 
      catch (InterruptedException e) { e.printStackTrace(); }

    return false;
}
    
     @Override
   public Object onRetainNonConfigurationInstance() {
    return mainvars;
  }
  
  
  void saveCookie(String cookies) {
    SharedPreferences sPref ; 
    sPref = getSharedPreferences("silena", MODE_PRIVATE);
    Editor ed = sPref.edit();
    ed.putString("Session",cookies);
    ed.commit();
  }
  
  String loadCookie() {
    SharedPreferences sPref;  
    sPref = getSharedPreferences("silena", MODE_PRIVATE);
    String saved = sPref.getString("Session", "");
    return saved;
  }

  
    class DownloadImageTask extends AsyncTask<String, Void, String> {
    final String SAVED_USERID = "user";
    final String SAVED_USERPASS = "password";
        
               @Override
		protected String doInBackground(String... params) {
			publishProgress(new Void[]{});
			
		    String url = "";
		    if( params.length > 0 ){
		    	url = params[0];		    	
		    }
		   
		    InputStream input = null;
                    HttpDriver response = new HttpDriver(Send(),url,mainvars.getCookies());
                    String response_body=response.Start();
		    
                    Log.d(LOG_TAG,"response test," + response_body);
                     
                    return(ParseResult(response_body));
                   	    
	       
                }
		
       
                
        public String Send() {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ServerApi regapi = new ServerApi(mainvars.getCode(), mainvars.getUser(),mainvars.getCapcha(), null, null, 30, null,null);
            String json = gson.toJson(regapi);
            Log.d(LOG_TAG, "Json test," + json);
            return (json);
        }
                
                
                public String ParseResult(String json){
                     
                    Gson gson = new Gson();
                    ServerApi regapi = gson.fromJson(json, ServerApi.class);
                    Log.d(LOG_TAG,"Json apiResponse," + regapi.getCode());
                    
                    if (regapi.getCode() == 401) {
                        SharedPreferences sPref;
                        sPref = getSharedPreferences("silena", MODE_PRIVATE);
                        Editor ed = sPref.edit();
                        ed.putString(SAVED_USERID, regapi.getUser());
                        ed.putString(SAVED_USERPASS, regapi.getPassword());
                        ed.commit();
                        Log.d(LOG_TAG,"Server Server User," + regapi.getUser());
                        Log.d(LOG_TAG,"Server Server Password," + regapi.getPassword());
                        return("401");
                    }else if(regapi.getCode() == 104){                    
                    Log.d(LOG_TAG,"Server Validation Error," + regapi.getCode());
                    return("104");
                    }else if (regapi.getCode() == 500){  
                    Log.d(LOG_TAG,"Server Internal Error," + regapi.getCode());
                    return("500");
                    } 
                    return("500");
                }
                
                
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			showDialog(PROGRESS_DLG_ID);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dismissDialog(PROGRESS_DLG_ID);
		Log.d(LOG_TAG,result);	
                  //image.setImageBitmap(result);	
		}
	} 


class DownloadCapcha extends AsyncTask<String, Void, MainVars> {
    private String cookies;
		@Override
		protected MainVars doInBackground(String... params) {
			publishProgress(new Void[]{});
			
		    String url = "";
		    if( params.length > 0 ){
		    	url = params[0];		    	
		    }
		   
		    InputStream input = null;
		    
		    try {
                        
                        
	HttpClient httpclient = new DefaultHttpClient();
        HttpGet httppost = new HttpGet(url);
         httppost.setHeader("Host", "78.46.251.139");
	 httppost.setHeader("User-Agent", USER_AGENT);
         httppost.setHeader("Cookie",mainvars.getCookies()+";");
	 httppost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	 httppost.setHeader("Accept-Language", "en-US,en;q=0.5");
	 httppost.setHeader("Connection", "keep-alive");
	 httppost.setHeader("Referer", "https://xxx/Atlant");
	 httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
         Log.d(LOG_TAG,"Cookie"+mainvars.getCookies()+";");
        CookieHandler.setDefault(new CookieManager());  
        HttpResponse response = httpclient.execute(httppost);
        mainvars.setCookies(response.getFirstHeader("Set-Cookie") == null ? mainvars.getCookies() : 
                     response.getFirstHeader("Set-Cookie").toString().replace("Set-Cookie:",""));
        HttpEntity entity = response.getEntity();
        input = entity.getContent();
                        
                        
                        
		    }
	        catch (IOException e) {
	        	Log.d(DEBUG_TAG,"Oops, Something wrong with URL...");	        	   
	        					
			} catch (IllegalStateException e) {
                            Log.d(DEBUG_TAG,"Oops, Something wrong with URL...");
                          
                    }
	        
	          mainvars.setBitmap(BitmapFactory.decodeStream(input));
		
                        return mainvars;
                }
		
                  public void setCookies(String cookies) {
	            mainvars.setCookies(cookies);
                  }
                  
                  public String getCookies() {
                    Log.d(LOG_TAG,"CApcha Get cookies,"+ cookies);
	           return  mainvars.getCookies();
                  }
                
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			showDialog(PROGRESS_DLG_ID);
		}
		
		@Override
		protected void onPostExecute(MainVars result) {
			super.onPostExecute(mainvars);
			dismissDialog(PROGRESS_DLG_ID);
                        mainvars.setCookies(cookies);
			//view.setImageBitmap(mainvars.getBitmap());	
		}
	}


 
     
    


}
 

  