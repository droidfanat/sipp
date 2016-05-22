/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.silena.main;

import org.silena.R; 
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author admin2
 */
public class DownloadImageActivity extends Activity {
	ImageView image;
        Button runButt;
	
	final static String PICT_URL = "http://www.bigfoto.com/sites/main/churfirsten_switzerland-xxx.JPG";
	final int PROGRESS_DLG_ID = 666;
	final static String DEBUG_TAG = "+++ImageDownloader+++";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    loadViews();
	}
	
	private void loadViews(){
	  // runButt = (Button)findViewById(R.id.runButt);
	  //  image =(ImageView)findViewById(R.id.image);
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
	
	public void runButtonHandler(View button){
	//	if(button.getId() == R.id.runButt)
	//	    new DownloadImageTask().execute(PICT_URL);
	}
	
	class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			publishProgress(new Void[]{});
			
		    String url = "";
		    if( params.length > 0 ){
		    	url = params[0];		    	
		    }
		   
		    InputStream input = null;
		    
		    try {
		    	URL urlConn = new URL(url);
		        input = urlConn.openStream();
		    }
	        catch (MalformedURLException e) {
	        	Log.d(DEBUG_TAG,"Oops, Something wrong with URL...");
	        	e.printStackTrace();
			}
	        catch (IOException e) {
	        	Log.d(DEBUG_TAG,"Oops, Something wrong with inpur stream...");
				e.printStackTrace();
			}
	        
	        return BitmapFactory.decodeStream(input);
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			showDialog(PROGRESS_DLG_ID);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			dismissDialog(PROGRESS_DLG_ID);
			image.setImageBitmap(result);	
		}
	}
}
