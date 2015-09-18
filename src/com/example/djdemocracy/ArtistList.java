package com.example.djdemocracy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class ArtistList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_artist_list);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.artist_list, menu);
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

		String[] artist;
		
		public PlaceholderFragment() {
			artist = (new Data()).artistNames();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_artist_list,
					container, false);
			if(artist!=null){
				Log.v("MSG", "There is stuff in the list");
				ListView myListView = (ListView)rootView.findViewById(R.id.artistList);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(), R.layout.listentry, R.id.item, artist);
				myListView.setAdapter(adapter);
				// onclick adapter
				myListView.setOnItemClickListener(new OnItemClickListener() {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view, int position,
		                    long id) {
		            	Data d = new Data();
		            	d.setCurrentArtist(position);
		            	Intent intent = new Intent(view.getContext(), Songs.class);
		        		startActivity(intent);
		            }
		        });
			} else {
				Log.v("MSG", "I lied, the list is empty because I am an asshole");
			}
			return rootView;
		}
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
	public void home(View v){
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);

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
