package com.example.djdemocracy;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.os.Build;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
//		try {
////			ListView menu = (ListView)findViewById(R.layout.menu);
////			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listentry, R.id.item, (new MenuData()).menuInfo());
////			menu.setAdapter(adapter);
////			registerForContextMenu(menu);
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

		String[] entries = {"Artists", "Chart", "Search", "Logout"};
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			return rootView;
		}
	}
	
	public void loadArtists(View v){
		// load artist list

		Data d = new Data();
		String[] a = d.artistNames();
		if(a==null){

		} else {

			Intent intent = new Intent(this, ArtistList.class);
			startActivity(intent);
		}
	}
	
	public void loadCharts(View v){
		// load charts

		Intent intent = new Intent(this, Chart.class);
		startActivity(intent);
	}
	
	public void loadSearch(View v){
		// load search option
    	Toast.makeText(getBaseContext(),Data.cookies.getCookie("login") , Toast.LENGTH_LONG).show();
		Intent intent = new Intent(this, Search.class);
		startActivity(intent);
	}

	
	public void logout(View v){
		// destroy cookie and exit
		Log.v("Action", "Logout");
		Data.cookies.removeAllCookie();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void menu(View v){
		// create menu which shows currently playing song, recently voted for song
		Log.v("Action", "Menu");
		new HttpTask().execute(Data.addr+"update.php");
         //registering popup with OnMenuItemClickListener  
//         popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
//          public boolean onMenuItemClick(MenuItem item) {  
//           Toast.makeText(getBaseContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();  
//           return true;  
//          }  
//         });
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
		    	
		    	popPop(result);
		    	
		   }
		
		
	}
}




