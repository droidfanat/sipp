/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utils;

import static org.silena.main.MainActivity.LOG_TAG;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.net.CookieHandler;
import java.net.CookieManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author admin2
 */
public class HttpCapcha {
    
    private String Url;
    private String cookies;
    private MainVars mainvars;
    private final String USER_AGENT = "Silena/1.1";
    
    public HttpCapcha (String Url) {
    this.Url=Url;
    }           
    /**
     *
     * @return 
     */
    public MainVars Start(MainVars mainvars) {
        Log.d(LOG_TAG,"Destination Url,"+Url);
        String responseBody=null;
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httppost = new HttpGet(Url.toString());
        CookieHandler.setDefault(new CookieManager());        
        try {
            // Add your data
            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
          //  nameValuePairs.add(new BasicNameValuePair("Json", this.Json));
           // nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
           // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
           
            	 httppost.setHeader("Host", "78.46.251.139");
	 httppost.setHeader("User-Agent", USER_AGENT);
	 httppost.setHeader("Accept", 
             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	 httppost.setHeader("Accept-Language", "en-US,en;q=0.5");
	 httppost.setHeader("Cookie", getCookies());
	 httppost.setHeader("Connection", "keep-alive");
	 httppost.setHeader("Referer", "https://accounts.google.com/ServiceLoginAuth");
	 httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            
            
            
            
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
          //  setCookies(response.getFirstHeader("Set-Cookie") == null ? "" : 
            //         response.getFirstHeader("Set-Cookie").toString()); 
            
//            int responseCode = response.getStatusLine().getStatusCode();
//            Log.d(LOG_TAG,"Response code,"+responseCode);
//            switch (responseCode) {
//                case 200:
//                    HttpEntity entity = response.getEntity();
//                    if (entity != null) {
//                      //   responseBody = EntityUtils.toString(entity);
//                        // InputStream instream = entity.getContent();
//                         //mainvars.setBitmap(BitmapFactory.decodeStream(instream));
//                    }
//                    break;
//            }
            
            
        
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    
//     mainvars.setResponse(responseBody);
     
return(mainvars);
    
            }
    
      public String getCookies() {
          Log.d(LOG_TAG,"Destination Get cookies,"+ mainvars.getCookies());
          
	return cookies ;
  }

  public void setCookies(String cookies) {
                Log.d(LOG_TAG,"Destination Set cookies,"+ cookies);
	this.cookies = cookies;
  }
    
}
