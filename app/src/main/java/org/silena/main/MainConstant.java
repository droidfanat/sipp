/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silena.main;

import android.view.Menu;

/**
 *
 * @author admin2
 */
public class MainConstant {
  public final static int STATUS_START = 100;
  public final static int STATUS_SERVICE = 200;
  public final static int STATUS_BALANCE=300;
  public final static int STATUS_HISTORY=400;
  public final static String PARAM_TIME = "time";
  public final static String PARAM_TASK = "task";
  public final static String PARAM_RESULT = "result";
  public final static String PARAM_BALANCE = "balance";  
    public final static String PARAM_LIST = "list"; 
  public final static String PARAM_STATUS = "status";
  public final static String PARAM_URL = "url";
  public final static String PARAM_METHOD = "METHOD";
  public final static String PARAM_POST="MPOST";
  public final static int METHOD_GET = 1;
  public final static int METHOD_POST = 2;
  public final static int METHOD_BITMAP = 3;
  public final static int METHOD_REGISTRATION = 4;
  public final static int METHOD_GETBALANCE=5;
  public final static int METHOD_CALLBACK=7;
  public final static int METHOD_TRANSACTION=8;
   public final static int METHOD_BALANCEHISTORY=9;
  public final static int REG_CALLBACK=0;
  public final static String BROADCAST_ACTION = "org.silena.service.demo";
    public static final String BROADCAST_BALANCE = "action_billing";
  public final static String SAVED_USERID= "USER";
  public final static String SAVED_USERPASS="PASSWORD";
  public static String LOG_TAG = "Silena_Log";   
  public final static String USER_AGENT = "Silena/1.1";
  public final static String SERV_URL = "http://78.46.251.139/index.php?r=pbx/CliApi";
  public final static String PICT_URL = "http://78.46.251.139/index.php?r=pbx/captcha";    
  
  //Profiles Setup context
  
  
  public final static String SIP_USER="sipuser";
  public final static String SIP_PASSWORD="sippassword";
  public final static String SIP_REALM="siprealm";
  public final static String SIP_REALM_ORIG="siprealm_orig";
  public final static String SIP_FROM_URL="sipfrom_url";
  public final static String SIP_QVALUE="sipqvalue";
  public final static String SIP_MMTEL="sipmmtel";
  public final static String SIP_PUB="sippub";
  
  
//D/Silena_Log(22503): Engine user_profile suffix ,
//D/Silena_Log(22503): Engine user_profile user_profile.username ,test
//D/Silena_Log(22503): Engine user_profile user_profile.passwd ,Qa12345
//D/Silena_Log(22503): Engine user_profile user_profile.realm ,78.46.251.139
//D/Silena_Log(22503): Engine user_profile user_profile.realm_orig ,78.46.251.139
//D/Silena_Log(22503): Engine user_profile  user_profile.from_url ,test
//D/Silena_Log(22503): Engine user_profile user_profile.qvalue ,1.00
//D/Silena_Log(22503): Engine user_profile user_profile.mmtel ,true
//D/Silena_Log(22503): Engine user_profile user_profile.pub ,true

public static final int FIRST_MENU_ID = Menu.FIRST;
	public static final int HANG_UP_MENU_ITEM = FIRST_MENU_ID + 1;
	public static final int HOLD_MENU_ITEM = FIRST_MENU_ID + 2;
	public static final int MUTE_MENU_ITEM = FIRST_MENU_ID + 3;
	public static final int VIDEO_MENU_ITEM = FIRST_MENU_ID + 5;
	public static final int SPEAKER_MENU_ITEM = FIRST_MENU_ID + 6;
	public static final int TRANSFER_MENU_ITEM = FIRST_MENU_ID + 7;
	public static final int ANSWER_MENU_ITEM = FIRST_MENU_ID + 8;
	public static final int BLUETOOTH_MENU_ITEM = FIRST_MENU_ID + 9;
	public static final int DTMF_MENU_ITEM = FIRST_MENU_ID + 10;  
  
}
