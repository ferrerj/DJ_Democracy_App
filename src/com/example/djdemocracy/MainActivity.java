package com.example.djdemocracy;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	EditText user;
	EditText pw;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		HttpTask task = new HttpTask();
	    task.execute(Data.addr+"lib.json");
		//task.execute("http://www.eden.rutgers.edu/~rioscm/lib.json");
//	    try {
//			Data d = new Data(loadJSONFromAsset());
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void login(View v){
		// check the user credentials
		user = (EditText)findViewById(R.id.user);
		pw = (EditText)findViewById(R.id.password);
		Log.v("User", user.getText().toString());
		Log.v("Password", pw.getText().toString());
		String url = Data.addr+"android.php?user="+user.getText().toString()+"&password="+pw.getText().toString();
		new LoginTask().execute(url);
		
		}
	
	public void verify(String result)
	{
		if(result.equals("1")){
			Intent intent = new Intent(this, Home.class);
			startActivity(intent);
		}
		else
		{
        	Toast.makeText(getBaseContext(),"Invalid Login, Please try again" , Toast.LENGTH_LONG).show();
        	

		}
	}
	public void createAcct(View v){
		Intent intent = new Intent(this, CreateAcct.class);
		startActivity(intent);
	}
	
	public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("lib.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


	public static String getRequest(String url){
	    InputStream inputStream = null;
	    String result = "";
	    try {

	        // create HttpClient
	        HttpClient httpclient = new DefaultHttpClient();
	        
	        // make GET request to the given URL
	        HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
	       
	        // receive response as inputStream
	        inputStream = httpResponse.getEntity().getContent();

	        // convert inputstream to string
	        if(inputStream != null)
	            result = ParseStream(inputStream);
	        else
	            result = "Unable to contact server";

	    } catch (Exception e) {
	        Log.d("InputStream", e.getLocalizedMessage());
	    }

	    return result;
	}
	public static String getCRequest(String url)
	{
		
		InputStream inputStream = null;
	    String result = "";
	    try {

	        // create HttpClient
	        HttpClient httpclient = new DefaultHttpClient();
	        
	        // make GET request to the given URL
	        HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
	       
	        // receive response as inputStream
	        inputStream = httpResponse.getEntity().getContent();
	        //set cookies
	        
	        Header[] headers = httpResponse.getHeaders("Set-Cookie");
	        CookieManager cookieManager =CookieManager.getInstance();
	        if(headers!=null)
	        {
		        for(Header h : headers)
		        {
		        	cookieManager.setCookie("login", h.getValue());
		        
		        }
	        }
	        Data c = new Data(cookieManager);
	        // convert inputstream to string
	        if(inputStream != null)
	            result = ParseStream(inputStream);
	        else
	            result = "Unable to contact server";

	    } catch (Exception e) {
	        Log.d("InputStream", e.getLocalizedMessage());
	    }

	    return result;
		
		
		
	}
	private static String ParseStream(InputStream inputStream) throws IOException{
	    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	    String line="";
	    String result="";
	    while((line = bufferedReader.readLine()) != null)
	        result += line;

	    inputStream.close();
	    return result;
	    
	}	
	private class HttpTask extends AsyncTask<String, Void, String> {
		
		String list;
		  @Override
		    protected String doInBackground(String... url) {

		        return getRequest(url[0]);
		    }
		    // onPostExecute displays the results of the AsyncTask.
		    @Override
		    protected void onPostExecute(String result) {
		    	
		    	
		    	try {
					Data d = new Data(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   }
		
		
	}
	private class LoginTask extends AsyncTask<String,Void,String>
	{
		protected String doInBackground(String... url) {
			Log.v("hello","in here");
	        return getCRequest(url[0]);
	    }
	    // onPostExecute displays the results of the AsyncTask.
	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	Log.e("Huh",result);
	    	verify(result);
	   }
		
	}
}
