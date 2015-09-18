package com.example.djdemocracy;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.webkit.CookieManager;

public class Data {
	static JSONArray data  = null;
	//static final String serverAddr = "http://192.168.1.14";
	static int currentArtist;
	static int currentSong;
	static String currentSid;
	static CookieManager cookies;
	static final String addr = "http://10.0.2.2/";
	

	
	public Data(String a) throws JSONException{
		//new HttpAsyncTask().execute(serverAddr+"/lib.json");
		data = (new JSONObject(a)).getJSONArray("data");
		Log.v("Data", a);
	}
	
	public Data(CookieManager cookies)
	{
		this.cookies = cookies;
	}
	
	public Data(){
	}
	
	public String[] artistNames(){
		try {
			List<String> artistList = new ArrayList<String>();
	        for(int i = 0; i < data.length(); i++){
	        	artistList.add(data.getJSONObject(i).getString("artist"));
	        }
	        if(artistList!=null){
	        	return artistList.toArray(new String[artistList.size()]);
	        }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] songList(int artist){
		if(data!=null){
			try {
				JSONArray songs = data.getJSONObject(artist).getJSONArray("song");
				List<String> songList = new ArrayList<String>();
		        for(int i = 0; i < songs.length(); i++){
		        	songList.add(songs.getJSONObject(i).getString("tit"));
		        }
		        if(songList!=null){
		        	return songList.toArray(new String[songList.size()]);
		        }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void setCurrentArtist(int artist){
		currentArtist = artist;
	}
	
	public void setCurrentArtistSong(int song){
		currentSong = song;
	}
	
	public String[] currentArtistSongs(){
		return songList(currentArtist);
	}
	
	public String getCurrentSongID() throws JSONException{
		return getCurrentArtistSongId(currentSong);
	}
	
	public String getCurrentArtistSongId(int s) throws JSONException{
		return data.getJSONObject(currentArtist).getJSONArray("song").getJSONObject(s).getString("id");
	}
	
	public String songByArtist(int a, int s) throws JSONException{
		return data.getJSONObject(a).getJSONArray("song").getJSONObject(s).getString("tit");
	}
	
	public String artistById(int a) throws JSONException{
		return data.getJSONObject(a).getString("artist");
	}
	
	public String artistSong(int a, int s) throws JSONException{
		return songByArtist(a, s) + " by " + artistById(a);
	}
	
	public void setCurrentSid(String s){
		currentSid = s;
	}
	
	public String getCurrentSid(){
		return currentSid;
	}
	

	public CookieManager getCookieManager()
	{
		return cookies;
	}
	public String getAddr(){
		return addr;

	}
}
