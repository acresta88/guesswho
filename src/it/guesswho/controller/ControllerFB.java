package it.guesswho.controller;

import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;
import it.guesswho.view.FriendPicker;
import it.guesswho.view.SearchMatchActivity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.model.GraphUser;

public class ControllerFB {
	private GuessWhoApplication application;
	private Activity activity;
	
	public ControllerFB(Activity a) {
		this.application = (GuessWhoApplication) a.getApplication();
		this.activity = a;
	}

	public void askFriendList() {
		// initialization variables

		// retrieving friends
		Request r = new Request(application.getSession(), "me/friends", null,
				HttpMethod.GET, new Callback() {

				@Override
				public void onCompleted(Response response) {
					JSONArray jsonArray;
					ArrayList<User> users = new ArrayList<User>();;

					try {
						jsonArray = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
				
						for (int i = 0; i < jsonArray.length(); i++) {
							String name = jsonArray.getJSONObject(i).getString("name");
							Log.d("friends", name);
							int j;
							for (j = 0; j < users.size(); j++) {
								if (users.get(j).getName().compareTo(name) >= 0)
									break;
								}								
								Log.d("friends", j + "");

								users.add(j, new User(name, jsonArray.getJSONObject(i).getString("id")));
						}
						
						//send result
						application.setFriendList(users);
						//signal to the activity
						((FriendPicker)activity).setList(); //TODO to modify with a better way to notify
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (NullPointerException e) {
						Toast.makeText(activity.getApplicationContext(), "problem occorred during the facebook request", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
			});
		r.executeAsync();
	}
	
	public void askFriendName(String id) {
		
		Log.d("ControllerFB", "ask name of:"+id);
		// retrieving friends
		Request r = new Request(application.getSession(), id, null,
				HttpMethod.GET, new Callback() {

				@Override
				public void onCompleted(Response response) {
					try {
						String name = response.getGraphObject().getInnerJSONObject().getString("name");
						String id = response.getGraphObject().getInnerJSONObject().getString("id");
						Log.d("ControllerFB", "found name: " + name);

						((SearchMatchActivity)activity).callback(id, name);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				};
		});
		r.executeAsync();
	}
}
