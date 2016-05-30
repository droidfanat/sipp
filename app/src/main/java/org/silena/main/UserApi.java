/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silena.main;

/** 'code'=>null
 *  'user'=>null,
     'password'=>null,
     'status'=>false,
     'balans'=>0,
     'host'=>null,
     'status'=>false,
     'keepalive'=>30,
     'autch_error'=>0,
     'proto_error'=>0,
     'call_back_phone'=>null,
 * @author admin2
 */
public class UserApi {
  private final int code;  
  private final String user;
  private final String password;
  private final String call_back_phone;


  public UserApi(int code, String user, String password,  String call_back_phone) {
    
    this.code = code;
    this.user = user;
    this.password = password;
    this.call_back_phone=call_back_phone;
    
  }


  public int getCode() {
    return code;
  }

  public String getUser() {
    return user;
  }
  
  public String getPassword() {
    return password;
  }
  
  
  public String getCall_back_phone() {
    return call_back_phone;
   } 


  
//  @Override
//  public String toString() {
//    return String.format("(item: %s, qty: %s, price: %.2f %s)",
 //       name, quantity, priceInMicros / 1000000d, currencyCode);
//  }
}
