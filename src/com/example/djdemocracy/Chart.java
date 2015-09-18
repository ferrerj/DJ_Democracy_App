package com.example.djdemocracy;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.os.Build;

public class Chart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chart);
		String url=Data.addr+"newChart.php";
		new HttpTask().execute(url);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	public void makeChart(String in){
		try {
			JSONObject o = new JSONObject(in);
			JSONArray ja = o.getJSONArray("result");
			ArrayList<ChartEntry> list = new ArrayList<ChartEntry>();
			for(int i = 0; i<ja.length(); i++){
				list.add(new ChartEntry(ja.getJSONObject(i)));
			}
			ArrayAdapter<ChartEntry> adapter = new ArrayAdapter<ChartEntry>(this, R.layout.listentry, R.id.item, list);
			((ListView)findViewById(R.id.chart)).setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
		return true;
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
			View rootView = inflater.inflate(R.layout.fragment_chart,
					container, false);
			return rootView;
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
	
	public void menu(View v){
		// create menu which shows currently playing song, recently voted for song
		Log.v("Action", "Menu");
		new MenuTask().execute(Data.addr+"update.php");
         //registering popup with OnMenuItemClickListener  
//         popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
//          public boolean onMenuItemClick(MenuItem item) {  
//           Toast.makeText(getBaseContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();  
//           return true;  
//          }  
//         });
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
	public void home(View v){
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
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
		    	//Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
		    	//Log.v("Data",result);
		    	makeChart(result);
		    }
		
		
	}

public void popPop(String result)
{
	 Menu popmenu;
	 ImageView menu = (ImageView) findViewById(R.id.menu);
	 PopupMenu popup = new PopupMenu(this, menu);  
    //Inflating the Popup using xml file  
     popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());  
     popup.show();
     popmenu=popup.getMenu();
     try {
		JSONArray array = new JSONArray(result);
		popmenu.removeItem(R.id.one);
		popmenu.add(array.getJSONObject(0).getString("text"));
		popmenu.add(array.getJSONObject(1).getString("text"));
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
     
}

private class MenuTask extends AsyncTask<String, Void, String> {
	
	String list;
	  @Override
	    protected String doInBackground(String... url) {

	        return getRequest(url[0]);
	    }
	    // onPostExecute displays the results of the AsyncTask.
	    @Override
	    protected void onPostExecute(String result) {
	    	
	    	popPop(result);
	    	
	   }
	
	
}
}
