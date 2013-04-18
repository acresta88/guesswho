package it.guesswho.view;

import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.guesswho.R;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;

public class FriendPicker extends Activity {
	
	private GuessWhoApplication application;
	private ListView listView;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<User> users;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_friendpicker);

		//initialization variables
		application = (GuessWhoApplication) getApplication();
		users = new ArrayList<User>();
		
	    listView = (ListView) findViewById(R.id.listview);

        //retrieving friends
    	Request r = new Request(application.getSession(), "me/friends", null, HttpMethod.GET, new Callback() {

			@Override
			public void onCompleted(Response response) {
				JSONArray jsonArray;
				try {
					jsonArray = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
					final ArrayList<String> listapp = new ArrayList<String>();
					
					Log.d("friends", "numero" + jsonArray.length());
			    	for (int i = 0; i < jsonArray.length(); i++)
					{
			    		String name = jsonArray.getJSONObject(i).getString("name");
						Log.d("friends", name);
						int j;
						for(j = 0; j < users.size(); j++)
						{
							if(users.get(j).getName().compareTo(name) >= 0)
								break;
						}
						Log.d("friends", j + "");

						users.add(j, new User(name, jsonArray.getJSONObject(i).getString("id")));
						listapp.add(j, name);
					}
			    	
			    	listAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.array_element, listapp);
			 	    listView.setAdapter(listAdapter);

			 	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				 	    @Override
				 	    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				 	    	onClickNewGame(users.get(position).getId());
				 	    }

			 	    });
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
    	});
    	r.executeAsync();
    	
    }
    
	/**
	 * da muovere in avatar activity
	 */
	private void onClickNewGame(String opponent) {
    	
    	Bundle b = new Bundle();
    	b.putString("user", opponent);

    	Request r = new Request(application.getSession(), "me/mutualfriends", b, HttpMethod.GET, new Callback() {
			
        	int maxAvatars = 150;

        	@Override
			public void onCompleted(Response response) {
				try {
					Log.d("session", response.toString());
					JSONArray jsonArray = response.getGraphObject().getInnerJSONObject().getJSONArray("data");

			    	ArrayList<User> users = new ArrayList<User>();
			    	Log.d("mutualfriends", "numero" + jsonArray.length());
					for (int i = 0; i < jsonArray.length(); i++)
					{
						Log.d("mutualfriends", jsonArray.getString(i));
						if (i < maxAvatars)
							users.add(new User(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("id")));
						else
							break;
					}
					//start avatars activity
			    	startAvatarsActivity(users);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		});
    	r.executeAsync();
    }

	private void startAvatarsActivity(ArrayList<User> users) {
		Intent i = new Intent(this, AvatarsActivity.class);
		application.setCellUsers(users);
		
		if(users != null && users.size() > 0)
			startActivity(i);
		else
			showAlert("error", "no mutual friends");
	}
	
	private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }
}
