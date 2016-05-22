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
public class ServerApi {
  private final int code;  
  private final String user;
  private final String password;
  private final String balans;
  private final String Qr;
  private final String call_back_phone;
  private final int keepalive; 
  private final String capcha;
  public String[] balance_history;

  
  
  public ServerApi (int code, String user,String capcha , String balans, String password, int keepalive , String call_back_phone ,String Qr) {
    
    this.code = code;
    this.user = user;
    this.password = password;
    this.balans = balans;
    this.call_back_phone=call_back_phone;
    this.keepalive=keepalive;
    this.capcha =capcha;
    this.Qr=Qr;
  }




  public String getUser() {
    return user;
  }
  
  public String[] getBalanceHistory(){  
      return balance_history;
  }
 
  
  public String getPassword() {
    return password;
  }
  
  public int getCode() {
    return code;
  }
  
  public String getCapcha() {
      return capcha;
  }
  
  public String getBalans() {
    return balans;
  }

   public int getKeepalive() {
    return keepalive;
   } 
  
     public String getQr() {
    return Qr;
   }
   
  public String getCall_back_phone() {
    return call_back_phone;
   }  
}
