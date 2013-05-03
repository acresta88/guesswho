package it.guesswho.view;

import it.guesswho.R;
import it.guesswho.controller.ControllerFB;
import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;
import it.guesswho.utils.NetworkUtils;
import it.guesswho.utils.StaticVariables;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;

public class FriendPicker extends Activity {
	
	private GuessWhoApplication application;
	private ListView listView;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<User> users;
	private ControllerFB controller;
	
	private String tag = "friendpicker";
	
	private IntentFilter mIntentFilter;
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	Log.d("GCMService", intent.getExtras().getString("content"));

	    	ArrayList<User> newusers = new ArrayList<User>();
	    	JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(intent.getExtras().getString("content"));
				for (int i = 0; i < jsonArray.length(); i++)
		    	{
					Log.d(tag, jsonArray.getString(i));
					
					if (i < 15)
					{
						for(int j = 0; j < users.size(); j++)
						{
							if(users.get(j).getId().equals(jsonArray.getString(i)))
							{
								newusers.add(new User(users.get(j).getName(), jsonArray.getString(i)));
								break;
							}
						}
					}
					else
						break;
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	startAvatarsActivity(newusers);

	    }
	};

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_friendpicker);
 
	    mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(StaticVariables.actionCreateGame);
        registerReceiver(mIntentReceiver, mIntentFilter);

	    listView = (ListView) findViewById(R.id.listview);

		//initialization variables
		application = (GuessWhoApplication) getApplication();
		controller = new ControllerFB(this);
		controller.askFriendList();
    }
    
    /**
     * Setting the list with the retrieved friends
     * */
    public void setList()
    {
    	Log.d(tag, "onSetList");
    	users = application.getFriendList();
    	ArrayList<String> applist = new ArrayList<String>();
    	for(int i = 0; i < users.size(); i++)
    	{
    		applist.add(users.get(i).getName());
    	}
    	
    	listAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.array_element, applist);
 	    listView.setAdapter(listAdapter);

 	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	 	    @Override
	 	    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	 	    	onClickNewGame(users.get(position).getId());
	 	    }

 	    });
    }
    
	/**
	 * da muovere in avatar activity
	 */
	private void onClickNewGame(String opponent) {
    	Log.d(tag, "onClickNewGame");
		/* message to the server for creating the match */
		NetworkUtils.createMatch(application.getGcmId(), application.getUser().getId(), opponent, application.getSession().getAccessToken());
		
		
		/* retrieving mutual friends */
//    	Bundle b = new Bundle();
//    	b.putString("user", opponent);
//
//    	Request r = new Request(application.getSession(), "me/mutualfriends", b, HttpMethod.GET, new Callback() {
//			
//        	int maxAvatars = 150;
//
//        	@Override
//			public void onCompleted(Response response) {
//				try {
//					Log.d("session", response.toString());
//					JSONArray jsonArray = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
//
//			    	ArrayList<User> users = new ArrayList<User>();
//			    	Log.d("mutualfriends", "numero" + jsonArray.length());
//					for (int i = 0; i < jsonArray.length(); i++)
//					{
//						Log.d("mutualfriends", jsonArray.getString(i));
//						if (i < maxAvatars)
//							users.add(new User(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("id")));
//						else
//							break;
//					}
//					//start avatars activity
//			    	startAvatarsActivity(users);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (NullPointerException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//    	r.executeAsync();
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
