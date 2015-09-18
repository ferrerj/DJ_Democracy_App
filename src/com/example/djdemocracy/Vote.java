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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class Vote extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vote);
		Data a = new Data();
		
		String current="nothing";
		try {
			current = a.getCurrentSongID();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String url=Data.addr+"voteProc.php?song_id="+current;
		new HttpTask().execute(url);
		Log.e("current",current);
    	if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vote, menu);
		return true;
	}
	
	public void loadFalse(){
		TextView tv = (TextView)findViewById(R.id.text);
		tv.setText("Vote Not Submitted");
		ImageView iv = (ImageView)findViewById(R.id.voteRes);
		iv.setImageResource(R.drawable.voteftl);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iv.invalidate();
		tv.invalidate();
		backOut();
	}
	
	public void loadTrue(){
		TextView tv = (TextView)findViewById(R.id.text);
		tv.setText("Vote Submitted");
		ImageView iv = (ImageView)findViewById(R.id.voteRes);
		iv.setImageResource(R.drawable.voteftw);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		iv.invalidate();
		tv.invalidate();
		backOut();
	}
	
	public void backOut(){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finish();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			View rootView = inflater.inflate(R.layout.fragment_vote, container,
					false);
			return rootView;
		}
	}
	public static String getCRequest(String url)
	{
		
		InputStream inputStream = null;
	    String result = "";
	    try {

	        // create HttpClient
	        HttpClient httpclient = new DefaultHttpClient();
	        // make GET request to the given URL, send cookie also?
	        HttpGet get = new HttpGet(url);
	        get.addHeader("Cookie", Data.cookies.getCookie("login"));
	        HttpResponse httpResponse = httpclient.execute(get);
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

		        return getCRequest(url[0]);
		    }
		    // onPostExecute displays the results of the AsyncTask.
		    @Override
		    protected void onPostExecute(String result) {
		    	
		    	//Toast.makeText(getBaseContext(),"the result is:"+result, Toast.LENGTH_LONG).show();
		    	//Log.e("Data",result);
		    	int res = Integer.parseInt(result);
		    	if(res==0){
		    		//load false
		    		loadFalse();
		    	} else {
		    		// load true
		    		loadTrue();
		    	}
		   }
		
		
	}
}
