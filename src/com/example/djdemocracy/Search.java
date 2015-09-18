package com.example.djdemocracy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.net.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class Search extends Activity {

	EditText tv;
	SearchResult[] done;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(R.layout.activity_search);
	    //new HttpTask().execute("http://172.31.150.151/newSearch.php?q=hel");
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment(this)).commit();
		}
		
		
	}
	
	public void searchFor(String param){
		try{
		param = URLEncoder.encode(param, "UTF-8");
		new HttpTask().execute(Data.addr+"newSearch.php?q="+param);
		} catch (UnsupportedEncodingException e){
			Log.v("error", "sending");
		}
	}
	
	public void home(View v){
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
	}
	
	public void showSearch(String result){
    	try{
    	JSONObject json = new JSONObject(result);
    	JSONArray artists = json.getJSONArray("artists");
    	JSONArray songs = json.getJSONArray("songs");
    	ArrayList<SearchResult> res = new ArrayList<SearchResult>();
    	Log.v("size", Integer.toString(artists.length()));
    	int a = artists.length();
		for(int i=0; i<a; i++){
			SearchResult temp = new SearchResult(artists.getJSONObject(i), 1);
			res.add(temp);
		}
		int b = songs.length();
		for(int i = 0; i<b; i++){
			SearchResult temp = new SearchResult(songs.getJSONObject(i), 0);
			res.add(temp);
		}
    	done = res.toArray(new SearchResult[res.size()]);
    	ListView list = (ListView)findViewById(R.id.results);
    	ArrayAdapter<SearchResult> adapter = new ArrayAdapter<SearchResult>(this, R.layout.listentry, done);
    	list.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
    	} catch (JSONException e){
    		Log.v("error", "with JSON");
    	}
    	
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
	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("ValidFragment")
	public static class PlaceholderFragment extends Fragment {

		Search search;
		
		public PlaceholderFragment(Search s) {
			search = s;
		}
		
		public PlaceholderFragment(){}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_search,
					container, false);
			ListView list = (ListView)rootView.findViewById(R.id.results);
			ArrayList<SearchResult> temp = new ArrayList<SearchResult>();
			SearchResult[] a = temp.toArray(new SearchResult[temp.size()]);
			ArrayAdapter<SearchResult> adapter = new ArrayAdapter<SearchResult>(rootView.getContext(), R.layout.listentry, R.id.item, temp);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {
	        		try {
						startActivity(((SearchResult)parent.getAdapter().getItem(position)).loadClick(search));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });
			EditText tv = (EditText)rootView.findViewById(R.id.search);
			tv.addTextChangedListener(new TextWatcher() {

			      @Override
			      public void onTextChanged(CharSequence s, int start, int before, int count) {

			      }

			      @Override
			      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			      }

			      @Override
			      public void afterTextChanged(Editable s) {
			    	  Log.v("text", s.toString());
			    	  if(s.length()>2){
							search.searchFor(s.toString());
			    	  }
			      }
			    });
			
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
		    protected String doInBackground(String... urls) {

		        return getRequest(urls[0]);
		    }
		    // onPostExecute displays the results of the AsyncTask.
		    @Override
		    protected void onPostExecute(String result) {
		    	Log.v("Returned Search",result);
		    	showSearch(result);
		    	//Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
		    	//
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
