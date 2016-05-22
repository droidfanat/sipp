/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silena.main;

import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;
import android.os.Build;
import org.sipdroid.sipua.ui.Settings;
import static org.sipdroid.sipua.ui.Settings.VAL_PREF_SIP;
import org.zoolu.sip.provider.SipStack;
import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import org.sipdroid.sipua.ui.Receiver;
import static org.sipdroid.sipua.ui.Settings.*;
import static org.sipdroid.sipua.ui.Settings.PREF_PORT;
import static org.sipdroid.sipua.ui.Settings.PREF_USERNAME;
/**
 *
 * @author admin2
 */
public class MainSettings {
    
        private final String sharedPrefsFile = "org.sipdroid.sipua_preferences";
	// Default values of the preferences
        public   int code;
	public   String	API_USERNAME;
	public   String	API_PASSWORD;
	public   String	API_SERVER ;
	public   String	API_DOMAIN;
	public   String	API_FROMUSER;
	public   String	API_PORT;
	public   String	API_PROTOCOL;
	public   boolean	API_WLAN;
	public   boolean	API_3G ;
	public   boolean	API_EDGE;
	public   boolean	API_VPN;
	public   String	API_PREF = VAL_PREF_SIP;
	public   boolean	API_AUTO_ON ;
	public   boolean	API_AUTO_ONDEMAND;
	public   boolean	API_AUTO_HEADSET;
	public   boolean	API_MWI_ENABLED;
	public   boolean API_REGISTRATION;
	public   boolean	API_NOTIFY;
	public   boolean	API_NODATA ;
	public   String	API_SIPRINGTONE;
	public   String	API_SEARCH;
	public   String	API_EXCLUDEPAT;
	public   float	API_EARGAIN;
	public   float	API_MICGAIN;
	public   float	API_HEARGAIN;
	public   float	API_HMICGAIN;
	public   boolean	API_OWNWIFI;
	public   boolean	API_STUN;
	public   String	API_STUN_SERVER;
	public   String	API_STUN_SERVER_PORT;
	
	// MMTel configuration (added by mandrajg)
	public   boolean	API_MMTEL = false;
	public   String	API_MMTEL_QVALUE;	

	// Call recording preferences.
	public   boolean API_CALLRECORD;
	
	public   boolean	API_PAR;
	public   boolean	API_IMPROVE;
	public   String	API_POSURL;
	public   boolean	API_POS;
	public   boolean	API_CALLBACK;
	public   boolean	API_CALLTHRU;
	public   String	API_CALLTHRU2;
	public   String	API_CODECS;
	public   String	API_DNS;
	public   String  API_VQUALITY ;
	public   boolean API_MESSAGE;
	public   boolean API_BLUETOOTH;
	public   boolean API_KEEPON;
	public   boolean API_SELECTWIFI;
	public   int     API_ACCOUNT;

        // Default values of the other preferences
	public   boolean	API_OLDVALID;
	public   boolean	API_SETMODE;
	public   int		API_OLDVIBRATE;
	public   int		API_OLDVIBRATE2;
	public   int		API_OLDPOLICY;
	public   int		API_OLDRING;
	public   boolean	API_AUTO_DEMAND;
	public   boolean	API_WIFI_DISABLED;
	public   boolean API_ON_VPN;
	public  boolean	API_NOAPI;
	public  boolean API_NOPORT;
	public   boolean	API_ON;
	public   String	API_PREFIX;
	public   String	API_COMPRESSION;
	//public static  String	API_RINGTONEx = "";
	//public static  String	API_VOLUMEx = "";
       public Context context;
        
   public void setUSERNAME() {
        if (!this.API_USERNAME.equals("")||setStringGet(PREF_USERNAME, DEFAULT_USERNAME)!=API_USERNAME) {
            setStringPref(PREF_USERNAME, this.API_USERNAME);
        }
    }

  public  void setPASSWORD() {
        if (!this.API_PASSWORD.equals("")||setStringGet(PREF_PASSWORD, DEFAULT_PASSWORD)!=API_PASSWORD) {
            setStringPref(PREF_PASSWORD, this.API_PASSWORD);
        }

    }

   public void setSERVER() {
        if (!this.API_SERVER.equals("")||setStringGet(PREF_SERVER, DEFAULT_SERVER)!=API_SERVER) {
            setStringPref(PREF_SERVER, this.API_SERVER);
        }

    }

   public void setDOMAIN() {
        if (!this.API_DOMAIN.equals("")||setStringGet(PREF_DOMAIN, DEFAULT_DOMAIN)!=API_DOMAIN) {
            setStringPref(PREF_DOMAIN, this.API_DOMAIN);
        }

    }

  public  void setFROMUSER() {
        if (!this.API_FROMUSER.equals("")||setStringGet(PREF_FROMUSER, DEFAULT_DOMAIN)!=API_FROMUSER) {
            setStringPref(PREF_FROMUSER, this.API_FROMUSER);
        }

    }

  public  void setPORT() {
        if (!this.API_PORT.equals("")||setStringGet(PREF_PORT, DEFAULT_DOMAIN)!=API_PORT) {

            setStringPref(PREF_PORT, this.API_PORT);
        }

    }

  public  void setPROTOCOL() {
        if (!this.API_PROTOCOL.equals("")||setStringGet(PREF_PROTOCOL, DEFAULT_PROTOCOL)!=API_PROTOCOL) {

            setStringPref(PREF_PROTOCOL, this.API_PROTOCOL);
        }

    }

  public  void setSIPRINGTONE() {
        if (!this.API_SIPRINGTONE.equals("")||setStringGet(PREF_SIPRINGTONE, DEFAULT_SIPRINGTONE)!=API_SIPRINGTONE) {

            setStringPref(PREF_SIPRINGTONE, this.API_SIPRINGTONE);
        }

    }

  public  void setSEARCH() {
        if (!this.API_SEARCH.equals("")||setStringGet(PREF_SEARCH, DEFAULT_SEARCH)!=API_SEARCH) {

            setStringPref(PREF_SEARCH, this.API_SEARCH);
        }

    }

  public  void setEXCLUDEPAT() {
        if (!this.API_EXCLUDEPAT.equals("")||setStringGet(PREF_EXCLUDEPAT, DEFAULT_EXCLUDEPAT)!=API_EXCLUDEPAT) {

            setStringPref(PREF_EXCLUDEPAT, this.API_EXCLUDEPAT);
        }
    }

  public void setSTUN_SERVER() {
        if (!this.API_STUN_SERVER.equals("")||setStringGet(PREF_SERVER, DEFAULT_SERVER)!=API_SERVER) {

            setStringPref(PREF_STUN_SERVER, this.API_STUN_SERVER);
        }

    }

 public   void setSTUN_SERVER_PORT() {
        if (!this.API_STUN_SERVER_PORT.equals("")||setStringGet(PREF_STUN_SERVER_PORT, DEFAULT_STUN_SERVER_PORT)!=API_STUN_SERVER_PORT) {

            setStringPref(PREF_STUN_SERVER_PORT, this.API_STUN_SERVER_PORT);
        }

    }

  public  void setMMTEL_QVALUE() {
        if (!this.API_MMTEL_QVALUE.equals("")||setStringGet(PREF_MMTEL_QVALUE, DEFAULT_MMTEL_QVALUE)!=API_MMTEL_QVALUE) {

            setStringPref(PREF_MMTEL_QVALUE, this.API_MMTEL_QVALUE);
        }

    }

   public void setPOSURL() {
        if (!this.API_POSURL.equals("")||setStringGet(PREF_POSURL, DEFAULT_POSURL)!=API_POSURL) {

            setStringPref(PREF_POSURL, this.API_POSURL);
        }

    }

  public  void setCALLTHRU2() {
        if (!this.API_CALLTHRU2.equals("")||setStringGet(PREF_CALLTHRU2, DEFAULT_CALLTHRU2)!=API_CALLTHRU2) {

            setStringPref(PREF_CALLTHRU2, this.API_CALLTHRU2);
        }

    }

  public  void setCODECS() {
        if (!this.API_CODECS.equals("")||setStringGet(PREF_CODECS, DEFAULT_CODECS)!=API_CODECS) {

            setStringPref(PREF_CODECS, this.API_CODECS);
        }

    }

   public void setDNS() {
        if (!this.API_DNS.equals("")||setStringGet(PREF_DNS, DEFAULT_DNS)!=API_DNS) {
   
            setStringPref(PREF_DNS, this.API_DNS);
        }

    }

  public  void setVQUALITY() {
        if (!this.API_VQUALITY.equals("")||setStringGet(PREF_VQUALITY, DEFAULT_VQUALITY)!=API_VQUALITY) {

            setStringPref(PREF_VQUALITY, this.API_VQUALITY);
        }

    }

  public  void setPREFIX() {
        if (!this.API_PREFIX.equals("")||setStringGet(PREF_PREFIX, DEFAULT_PREFIX)!=API_PREFIX) {

            setStringPref(PREF_PREFIX, this.API_PREFIX);
        }

    }
                
    
    
      public      void setACCOUNT(){
        if(this.API_ACCOUNT==0)
        setIntPref(PREF_ACCOUNT,DEFAULT_ACCOUNT);
        else setIntPref(PREF_ACCOUNT,this.API_ACCOUNT);
       
        }
    
    
      public           void setOLDVIBRATE(){
        if(this.API_OLDVIBRATE==0)
        setIntPref(PREF_OLDVIBRATE,DEFAULT_OLDVIBRATE);
        else setIntPref(PREF_OLDVIBRATE,this.API_OLDVIBRATE);
       
        }   
                 
     public         void setOLDVIBRATE2(){
        if(this.API_OLDVIBRATE2==0)
        setIntPref(PREF_OLDVIBRATE2,DEFAULT_OLDVIBRATE2);
        else setIntPref(PREF_OLDVIBRATE2,this.API_OLDVIBRATE2);
       
        }          
           
   public   void setOLDPOLICY(){
        if(this.API_OLDPOLICY==0)
        setIntPref(PREF_OLDPOLICY,DEFAULT_OLDPOLICY);
        else setIntPref(PREF_OLDPOLICY,this.API_OLDPOLICY);
       
        }
             
      

   
   
        
      public        void setWLAN(){
        if(this.API_WLAN==DEFAULT_WLAN)
        setBooleanPref(PREF_WLAN,DEFAULT_WLAN);
        else setBooleanPref(PREF_WLAN,this.API_WLAN);
        }  
        
              
      public        void set3G(){
        if(this.API_3G==DEFAULT_3G)
        setBooleanPref(PREF_3G,DEFAULT_3G);
        else setBooleanPref(PREF_3G,this.API_3G);
        }
       
     public       void setEDGE(){
        if(this.API_EDGE==DEFAULT_EDGE)
        setBooleanPref(PREF_EDGE,DEFAULT_EDGE);
        else setBooleanPref(PREF_EDGE,this.API_EDGE);
        }
            
      public       void setVPN(){
        if(this.API_VPN==DEFAULT_VPN)
        setBooleanPref(PREF_VPN,DEFAULT_VPN);
        else setBooleanPref(PREF_VPN,this.API_VPN);
        }       
              
             
      public            void setAUTO_ON(){
        if(this.API_AUTO_ON==DEFAULT_AUTO_ON)
        setBooleanPref(PREF_AUTO_ON,DEFAULT_AUTO_ON);
        else setBooleanPref(PREF_AUTO_ON,this.API_AUTO_ON);
        }   
             
      public       void setAUTO_ONDEMAND(){
        if(this.API_AUTO_ONDEMAND==DEFAULT_AUTO_ONDEMAND)
        setBooleanPref(PREF_AUTO_ONDEMAND,DEFAULT_AUTO_ONDEMAND);
        else setBooleanPref(PREF_AUTO_ONDEMAND,this.API_AUTO_ONDEMAND);
        }  
             
      public       void setAUTO_HEADSET(){
        if(this.API_AUTO_HEADSET==DEFAULT_AUTO_HEADSET)
        setBooleanPref(PREF_AUTO_HEADSET,DEFAULT_AUTO_HEADSET);
        else setBooleanPref(PREF_AUTO_HEADSET,this.API_AUTO_HEADSET);
        }        
        
     
     public   void setMWI_ENABLED(){
        if(this.API_MWI_ENABLED==DEFAULT_MWI_ENABLED)
        setBooleanPref(PREF_MWI_ENABLED,DEFAULT_MWI_ENABLED);
        else setBooleanPref(PREF_MWI_ENABLED,this.API_MWI_ENABLED);
        }        
           
        
      public     void setREGISTRATION(){
        if(this.API_REGISTRATION==DEFAULT_REGISTRATION)
        setBooleanPref(PREF_REGISTRATION,DEFAULT_REGISTRATION);
        else setBooleanPref(PREF_REGISTRATION,this.API_REGISTRATION);
        }     
      
           
      public      void setNOTIFY(){
        if(this.API_NOTIFY==DEFAULT_NOTIFY)
        setBooleanPref(PREF_NOTIFY,DEFAULT_NOTIFY);
        else setBooleanPref(PREF_NOTIFY,this.API_NOTIFY);
        }       
       
      public        void setNODATA(){
        if(this.API_NODATA==DEFAULT_NODATA)
        setBooleanPref(PREF_NODATA,DEFAULT_NODATA);
        else setBooleanPref(PREF_NODATA,this.API_NODATA);
        }      
       
      public         void setOWNWIFI(){
        if(this.API_OWNWIFI==DEFAULT_OWNWIFI)
        setBooleanPref(PREF_OWNWIFI,DEFAULT_OWNWIFI);
        else setBooleanPref(PREF_OWNWIFI,this.API_OWNWIFI);
        }       
       
      
      public       void setSTUN(){
        if(this.API_STUN==DEFAULT_STUN)
        setBooleanPref(PREF_STUN,DEFAULT_STUN);
        else setBooleanPref(PREF_STUN,this.API_STUN);
        }          
   
             
      public               void setMMTEL(){
        if(this.API_MMTEL==DEFAULT_MMTEL)
        setBooleanPref(PREF_MMTEL,DEFAULT_MMTEL);
        else setBooleanPref(PREF_MMTEL,this.API_MMTEL);
        }

     
    
      public  void setCALLRECORD(){
        if(this.API_CALLRECORD==DEFAULT_CALLRECORD)
        setBooleanPref(PREF_CALLRECORD,DEFAULT_CALLRECORD);
        else setBooleanPref(PREF_CALLRECORD,this.API_CALLRECORD);
        }                 
       
      public        void setPAR(){
        if(this.API_PAR==DEFAULT_PAR)
        setBooleanPref(PREF_PAR,DEFAULT_PAR);
        else setBooleanPref(PREF_PAR,this.API_PAR);
        }
  
        
     public          void setIMPROVE(){
        if(this.API_IMPROVE==DEFAULT_IMPROVE)
        setBooleanPref(PREF_IMPROVE,DEFAULT_IMPROVE);
        else setBooleanPref(PREF_IMPROVE,this.API_IMPROVE);
        }       
               
    public           void setPOS(){
        if(this.API_POS==DEFAULT_POS)
        setBooleanPref(PREF_POS,DEFAULT_POS);
        else setBooleanPref(PREF_POS,this.API_POS);
        }        
       
            
    public         void setCALLBACK(){
        if(this.API_CALLBACK==DEFAULT_CALLBACK)
        setBooleanPref(PREF_CALLBACK,DEFAULT_CALLBACK);
        else setBooleanPref(PREF_CALLBACK,this.API_CALLBACK);
        }         
               
    public          void setCALLTHRU(){
        if(this.API_CALLTHRU==DEFAULT_CALLTHRU)
        setBooleanPref(PREF_CALLTHRU,DEFAULT_CALLTHRU);
        else setBooleanPref(PREF_CALLTHRU,this.API_CALLTHRU);
        }
              
              
    
    public    void setMESSAGE(){
        if(this.API_MESSAGE==DEFAULT_MESSAGE)
        setBooleanPref(PREF_MESSAGE,DEFAULT_MESSAGE);
        else setBooleanPref(PREF_MESSAGE,this.API_MESSAGE);
        }
       
    public          void setBLUETOOTH(){
        if(this.API_BLUETOOTH==DEFAULT_BLUETOOTH)
        setBooleanPref(PREF_BLUETOOTH,DEFAULT_BLUETOOTH);
        else setBooleanPref(PREF_BLUETOOTH,this.API_BLUETOOTH);
        }
  
   public            void setKEEPON(){
        if(this.API_KEEPON==DEFAULT_KEEPON)
        setBooleanPref(PREF_KEEPON,DEFAULT_KEEPON);
        else setBooleanPref(PREF_KEEPON,this.API_KEEPON);
        } 
  
    public                  void setSELECTWIFI(){
        if(this.API_SELECTWIFI==DEFAULT_SELECTWIFI)
        setBooleanPref(PREF_SELECTWIFI,DEFAULT_SELECTWIFI);
        else setBooleanPref(PREF_SELECTWIFI,this.API_SELECTWIFI);
        }
    
   public         void setOLDVALID(){
        if(this.API_OLDVALID==DEFAULT_OLDVALID)
        setBooleanPref(PREF_OLDVALID,DEFAULT_OLDVALID);
        else setBooleanPref(PREF_OLDVALID,this.API_OLDVALID);
        }                  
        
   public            void setSETMODE(){
        if(this.API_SETMODE==DEFAULT_SETMODE)
        setBooleanPref(PREF_SETMODE,DEFAULT_SETMODE);
        else setBooleanPref(PREF_SETMODE,this.API_SETMODE);
        }     
     
   
  public      void setAUTO_DEMAND(){
        if(this.API_AUTO_DEMAND==DEFAULT_AUTO_DEMAND)
        setBooleanPref(PREF_AUTO_DEMAND,DEFAULT_AUTO_DEMAND);
        else setBooleanPref(PREF_AUTO_DEMAND,this.API_AUTO_DEMAND);
        }
            
  public          void setWIFI_DISABLED(){
        if(this.API_WIFI_DISABLED==DEFAULT_WIFI_DISABLED)
        setBooleanPref(PREF_WIFI_DISABLED,DEFAULT_WIFI_DISABLED);
        else setBooleanPref(PREF_WIFI_DISABLED,this.API_WIFI_DISABLED);
        }          


   public    void setON_VPN(){
        if(this.API_ON_VPN==DEFAULT_ON_VPN)
        setBooleanPref(PREF_ON_VPN,DEFAULT_ON_VPN);
        else setBooleanPref(PREF_ON_VPN,this.API_ON_VPN);
        }

  public      void setNOPORT(){
        if(this.API_NOPORT==DEFAULT_NOPORT)
        setBooleanPref(PREF_NOPORT,DEFAULT_NOPORT);
        else setBooleanPref(PREF_NOPORT,this.API_NOPORT);
        }

       
        void setupStart(){
         setUSERNAME();
         setPASSWORD();
         setSERVER();
         setDOMAIN();
         setFROMUSER();
         setPORT();
         setPROTOCOL();
         setCODECS();
        }
        
        
       void setSetupAll(){
         setNOPORT();
         setON_VPN();  
         setWIFI_DISABLED();
         setAUTO_DEMAND();
         setSETMODE();        
         setOLDVALID();        
         setSELECTWIFI();
         setKEEPON();        
         setBLUETOOTH();               
         setCALLTHRU();        
         setCALLBACK();
         setUSERNAME();
         setPASSWORD();
         setSERVER();
         setDOMAIN(); 
         setFROMUSER();        
         setPORT();
         setPROTOCOL();  
         setSIPRINGTONE();
         setSEARCH();
         setEXCLUDEPAT();
         setSTUN_SERVER();
         setSTUN_SERVER_PORT();
         setMMTEL_QVALUE();
         setPOSURL();
         setCALLTHRU2();
         setCODECS();        
         setVQUALITY();
         setPREFIX();
         setACCOUNT();
         setOLDVIBRATE();
         setOLDVIBRATE2();
         setOLDPOLICY();
         setWLAN();
         set3G();
         setEDGE();
         setVPN();
         setAUTO_ON();
         setAUTO_ONDEMAND();
         setAUTO_HEADSET();
         setMWI_ENABLED();        
         setREGISTRATION();
         setNOTIFY();
         setNODATA();
         setOWNWIFI();
         setSTUN();
         setMMTEL();        
         setCALLRECORD();        
         setPAR();
         setIMPROVE();
         setPOS();
         setCALLBACK();
         setCALLTHRU();
         setMESSAGE();
         setBLUETOOTH();        
                 
       } 
        
        
            
       void setStringPref(String context,String value){         
         
           PreferenceManager.getDefaultSharedPreferences(getUIContext()).edit().putString(context,value).commit();
           
       }
        
           String  setStringGet(String context,String defcontext){         
         
       return(PreferenceManager.getDefaultSharedPreferences(getUIContext()).getString(context, defcontext));
       }
       
       
           void setIntPref(String context,Integer value){         
         
           PreferenceManager.getDefaultSharedPreferences(getUIContext()).edit().putInt(context,value).commit();
           
       }
       
       
        void setBooleanPref(String context,Boolean value){         
         
           PreferenceManager.getDefaultSharedPreferences(getUIContext()).edit().putBoolean(context,value).commit();
           
       } 
        
       public Context getUIContext() {
        return Receiver.mContext;
    }
        
        public int getCode() {
        return code;
        }   
}
