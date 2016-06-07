package org.silena.main.modelOtcher;import android.app.Service;import android.content.Intent;import android.os.Handler;import android.os.IBinder;import android.support.annotation.Nullable;import android.util.Log;import org.apache.http.HttpEntity;import org.apache.http.HttpResponse;import org.apache.http.NameValuePair;import org.apache.http.client.HttpClient;import org.apache.http.client.entity.UrlEncodedFormEntity;import org.apache.http.client.methods.HttpGet;import org.apache.http.client.methods.HttpPost;import org.apache.http.impl.client.DefaultHttpClient;import org.apache.http.message.BasicNameValuePair;import org.apache.http.util.EntityUtils;import org.silena.main.MainConstant;import org.sipdroid.sipua.ui.Receiver;import java.io.IOException;import java.io.InputStream;import java.io.UnsupportedEncodingException;import java.net.CookieHandler;import java.net.CookieManager;import java.net.CookiePolicy;import java.util.ArrayList;import java.util.List;import java.util.concurrent.ExecutorService;import java.util.logging.Level;import java.util.logging.Logger;import static org.apache.http.params.CoreProtocolPNames.USER_AGENT;import static org.silena.main.MainActivity.LOG_TAG;/** * Created by admin2 on 06.06.16. */public class HttpsJsonSender {    String callback = null;     class HttpsJsonService extends Service {        ExecutorService es;        private String httpPost(String url, String post) {            String result = null;            CookieManager cm = new CookieManager(null, CookiePolicy.ACCEPT_ALL);            CookieHandler.setDefault(cm);            HttpClient httpclient = new DefaultHttpClient();            HttpPost httppost = new HttpPost(url);            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);            nameValuePairs.add(new BasicNameValuePair("Json", post));            Log.d(LOG_TAG, "Service send post JSON:," + post);            try {                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));            } catch (UnsupportedEncodingException ex) {            }            httppost.setHeader("Host", "78.46.251.139");            httppost.setHeader("User-Agent", USER_AGENT);            httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");            httppost.setHeader("Accept-Language", "en-US,en;q=0.5");            httppost.setHeader("Cookie", Receiver.mCookies);            httppost.setHeader("Connection", "keep-alive");            httppost.setHeader("Referer", "https://xxx/Atlant");            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");            try {                HttpResponse response = httpclient.execute(httppost);                int responseCode = response.getStatusLine().getStatusCode();                Log.d(LOG_TAG, "Response code," + responseCode);                switch (responseCode) {                    case 200:                        Receiver.mCookies = response.getLastHeader("Set-Cookie") == null ? Receiver.mCookies                                : response.getLastHeader("Set-Cookie").toString().replace("Set-Cookie:", "");                        HttpEntity entity = response.getEntity();                        if (entity != null) {                            result = EntityUtils.toString(entity);                            Log.d(LOG_TAG, "Service Response post agent :" + result);                            return (result);                        }                        break;                }            } catch (IOException ex) {                Log.d(LOG_TAG, "Service Response post Server fault:");                result = "{\"code\":404}";            }            return result;        }        @Override        public int onStartCommand(Intent intent, int flags, int startId) {            this.logger("MyService onStartCommand");            int task = intent.getIntExtra(MainConstant.PARAM_TASK, 0);            String post = intent.getStringExtra(MainConstant.PARAM_POST);            this.logger("task" + task);            ;            switch (task) {                case 0:                    String result=null;                    result=httpPost(MainConstant.SERV_URL, post);                    Intent Backintent = new Intent(callback);                    Backintent.putExtra(MainConstant.PARAM_RESULT,result);                    break;            }            return super.onStartCommand(intent, flags, startId);        }        public void Callback() {        }        @Nullable        @Override        public IBinder onBind(Intent intent) {            return null;        }        public void logger(String log) {        }    }}