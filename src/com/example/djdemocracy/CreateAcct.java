package com.example.djdemocracy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class CreateAcct extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_acct);


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
			View rootView = inflater.inflate(R.layout.fragment_create_acct,
					container, false);
			return rootView;
		}
	}
	public void register(View v)
	{
		EditText user = (EditText)findViewById(R.id.user);
		EditText pw = (EditText)findViewById(R.id.password);
		EditText sec = (EditText)findViewById(R.id.secret);
		String uname = user.getText().toString();
		String password = pw.getText().toString();
		String secret = sec.getText().toString();
		if(uname.equals("")||password.equals(""))
		{
			Toast.makeText(getBaseContext(), "Please enter a User name and password",Toast.LENGTH_LONG).show();
		}
		new HttpTask().execute(Data.addr+"androidNewUser.php?user="+uname+"&password="+password+"&secret="+secret);
	}
	public void verify(String result)
	{
		if(!result.equals("1"))
		{
			Toast.makeText(getBaseContext(), "I'm sorry user already exists", Toast.LENGTH_LONG).show();
			
		}
		else
		{
			Intent intent = new Intent(this,Home.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
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
		    	
		    	Toast.makeText(getBaseContext(), result,Toast.LENGTH_LONG).show();
		    	verify(result);
		    }
		
		
	}
}
