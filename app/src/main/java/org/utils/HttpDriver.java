/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utils;

import static org.silena.main.MainActivity.LOG_TAG;
import static org.apache.http.params.CoreProtocolPNames.USER_AGENT;
import android.util.Log;
import java.io.IOException;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author admin2
 */
public class HttpDriver {
    
    private String Json;
    private String Url;
    private String cookies;
    
    
    public HttpDriver (String Json , String Url, String cookies) {
    this.Json=Json;
    this.Url=Url;
    this.cookies=cookies; 
    }           
    /**
     *
     * @param mainvars
     * @return 
     */
    public String Start() {
        Log.d(LOG_TAG,"Destination Url,"+Url);
        Log.d(LOG_TAG,"Post Send Json,"+Json);
        String responseBody=null;
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Url.toString());
        CookieHandler.setDefault(new CookieManager());        
        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("Json", this.Json));
           // nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
           
         httppost.setHeader("Host", "78.46.251.139");
	 httppost.setHeader("User-Agent", USER_AGENT);
	 httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	 httppost.setHeader("Accept-Language", "en-US,en;q=0.5");
	 httppost.setHeader("Cookie",cookies+";");
	 httppost.setHeader("Connection", "keep-alive");
	 httppost.setHeader("Referer", "https://accounts.google.com/ServiceLoginAuth");
	 httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            
            
          Log.d(LOG_TAG,"Post Send Cookies,"+cookies+";");  
            
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            int responseCode = response.getStatusLine().getStatusCode();
            Log.d(LOG_TAG,"Response code,"+responseCode);
            switch (responseCode) {
                case 200:
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                         responseBody = EntityUtils.toString(entity);
                    }
                    break;
            }
            
            
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    
      
return(responseBody);
    
            }
    


  
    
}
