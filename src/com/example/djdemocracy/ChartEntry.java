package com.example.djdemocracy;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ChartEntry {

	JSONObject obj;
	
	public ChartEntry(JSONObject j){
		obj = j;
		try{
		Log.v("obj", obj.getString("song"));
		//Log.v("obj", obj.getString("artist"));
		//Log.v("obj", obj.getString("count"));
		} catch (JSONException e){
			
		}
	}
	
	public String toString(){
		try {
			return obj.getString("song") + " by " + obj.getString("artist") + " - Votes:" +obj.getString("count");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
