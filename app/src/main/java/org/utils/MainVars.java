/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.utils;

import android.graphics.Bitmap;

/**
 *
 * @author admin2
 */
public class MainVars {
  private  int code;  
  private  String user;
  private  String password;
  private  String call_back_phone;
   private String cookies;
   private String response;
   private Bitmap bitmap;
   private String capcha;

//    public MainVars(int code, String user, String password,  String call_back_phone ,String cookies) {
//    
//    this.code = code;
//    this.user = user;
//    this.password = password;
//    this.call_back_phone=call_back_phone;
//    this.cookies=cookies;
//  }


  public int getCode() {
    return this.code;
  }

  public String getUser() {
    return this.user;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  
  public String getCall_back_phone() {
    return this.call_back_phone;
   } 
  
  
  public String getCookies() {
    return this.cookies;
   } 
  
  public String getResponse() {
    return this.response;
  }
  
   public Bitmap getBitmap() {
    return this.bitmap;
  }
   
  public String getCapcha() {
    return this.capcha;
  }
  /////////////////////////seters
  
  
  
  public void setCode(int code) {
   this.code= code;
  }

  public void  setUser(String user) {
    this.user= user;
  }
  
  public void  setPassword(String password) {
    this.password= password;
  }
  
  
  public void  setCall_back_phone(String call_back_phone) {
   this.call_back_phone= call_back_phone;
   } 
  
  
  public void setCookies(String cookies) {
      
      this.cookies= cookies;
    
   } 
  
  
  public void setResponse(String response){
    this.response=response;
  }
  
  public void setBitmap(Bitmap bitmap){
    this.bitmap=bitmap;
  }
  
   public void setCapcha(String capcha) {
    this.capcha=capcha;
  }
  
}
