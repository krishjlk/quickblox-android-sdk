package com.quickblox.chat_v2.apis;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.quickblox.chat_v2.core.ChatApplication;

public class FaceBookManager {
	
	// Get my info
	public void getMyInfo(final Context context, String token) throws MalformedURLException, IOException, JSONException {
		
		String fqlQuery = "SELECT pic FROM user WHERE uid =me()";
		Bundle params = new Bundle();
		params.putString("q", fqlQuery);
		params.putString("access_token", token);
		Session session = Session.getActiveSession();
		Request request = new Request(session, "/fql", params, HttpMethod.GET, new Request.Callback() {
			
			public void onCompleted(Response response) {
				
				GraphObject graphObject = response.getGraphObject();
				
				JSONObject jsonObject = graphObject.getInnerJSONObject();
				JSONArray array;
				try {
					array = jsonObject.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {
						JSONObject jObject = array.getJSONObject(i);
						
						// insert in this field url pricture
						String pic = jObject.getString("pic");
						if (pic != null) {
							ChatApplication.getInstance().getFbUser().setWebsite(pic);
						}
						
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
				
			}
			
		});
		Request.executeBatchAsync(request);
		
	}
}