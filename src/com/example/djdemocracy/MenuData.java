package com.example.djdemocracy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuData {

	JSONArray m;
	
	public MenuData(){
		// dummy that will allow data access
	}
	
	public MenuData(String in) throws JSONException{
		// gets json object from parent and stores it statically
		m = new JSONArray(in);
	}
	
	public String[] menuInfo() throws JSONException{
		String[] menu = new String[2];
		menu[0] = (m.getJSONObject(0).getString("text"));
		menu[1] = (m.getJSONObject(1).getString("text"));
		return menu;
	}
	
	public void voteForElem(int sid) throws JSONException{
		(new Data()).setCurrentSid(m.getJSONObject(sid).getString("sid"));
	}
	
}
