package com.example.djdemocracy;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class SearchResult {

	boolean isArtist;
	JSONObject res;
	
	public SearchResult(JSONObject j, int a){
		this.res = j;

		if(a==0){
			this.isArtist=false;
		} else {
			this.isArtist=true;
		}
	}
	
	@Override
	public String toString(){
		String value = null;
		try{
			
			if(isArtist){
				// return artist name
				Log.v("return", "artist");
				value = res.getString("artist");
			} else {
				// return song
				Log.v("return", "song");
				value = res.getString("song")+" by "+res.getString("artist");
			}
			return value;
		} catch(JSONException e){
			Log.v("JSON error", "hereish");
			return value;
		} 
		
		
	}
	
	public Intent loadClick(Context context) throws JSONException{
		Intent intent;
		Data d = new Data();
		
		d.setCurrentArtist(res.getInt("aid"));
		if(isArtist){
			// show artist page
			return intent = new Intent(context, Songs.class);
		} else {
			// try vote
			String id = res.getString("sid");
			String[] songs = d.currentArtistSongs();
			for(int i = 0; i<songs.length; i++){
				if(songs[i].equals(id)){
					d.setCurrentArtistSong(i);
					d.setCurrentSid(d.getCurrentArtistSongId(i));
				}
			}
			return intent = new Intent(context, Vote.class);
		}
	}
	
}
