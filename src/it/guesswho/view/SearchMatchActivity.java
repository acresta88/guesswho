package it.guesswho.view;

import it.guesswho.R;
import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchMatchActivity extends Activity {
	
	private GuessWhoApplication application;
	private ListView listView;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<User> users;
//	private ControllerFB controller;
	private ArrayList<String> jsonReceivedObject;
	private String tag = "searchmatch";
	
//	private IntentFilter mIntentFilter;
//	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
//	    @Override
//	    public void onReceive(Context context, Intent intent) {
//	    	Log.d("GCMService", intent.getExtras().getString("content"));
//
//	    	ArrayList<User> newusers = new ArrayList<User>();
//	    	JSONArray jsonArray;
//			try {
//				jsonArray = new JSONArray(intent.getExtras().getString("content"));
//				for (int i = 0; i < jsonArray.length(); i++)
//		    	{
//					Log.d(tag, jsonArray.getString(i));
//					
//					if (i < 15)
//					{
//						for(int j = 0; j < users.size(); j++)
//						{
//							if(users.get(j).getId().equals(jsonArray.getString(i)))
//							{
//								newusers.add(new User(users.get(j).getName(), jsonArray.getString(i)));
//								break;
//							}
//						}
//					}
//					else
//						break;
//				}
//
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//	    	
//	    	startAvatarsActivity(newusers);
//
//	    }
//	};

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_friendpicker);
 
//	    mIntentFilter = new IntentFilter();
//	    mIntentFilter.addAction(StaticVariables.actionCreateGame);
//        registerReceiver(mIntentReceiver, mIntentFilter);

	    listView = (ListView) findViewById(R.id.listview);

		//initialization variables
		application = (GuessWhoApplication) getApplication();
//		controller = new ControllerFB(this);
//		controller.askFriendList();
		//setting the opponent list
		jsonReceivedObject = getIntent().getStringArrayListExtra("users");

		ArrayList<User> list = new ArrayList<User>();
		
		for(int i = 0; i < jsonReceivedObject.size(); i++)
		{
			try {
				JSONObject json = new JSONObject(jsonReceivedObject.get(i));
				String userName = ((JSONObject) json.get("user1")).getString("name");
				String userId = ((JSONObject) json.get("user1")).getString("fb_id");
				Log.d(tag, userName);
				User u = new User(userName, userId);
				list.add(u);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}			
		application.setFriendList(list);

		setList();
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
    	ArrayList<User> list = new ArrayList<User>();
    	for(int i = 0; i < jsonReceivedObject.size(); i++)
		{
			try {
		    	Log.d(tag, "reading id_list");

				JSONObject json = new JSONObject(jsonReceivedObject.get(i));
//				String userName = ((JSONObject) json.get("user1")).getString("name");
				String userId = ((JSONObject) json.get("user1")).getString("fb_id");
				if(opponent.equals(userId))
				{
			    	Log.d(tag, "match found " + json.get("id_list").toString());

					JSONArray listId = new JSONArray(json.get("id_list").toString());
					for(int j = 0; j < listId.length(); j++)
					{
				    	Log.d(tag, "addind users");

						User u = new User("", listId.get(j).toString());
						list.add(u);
					}
			    	startAvatarsActivity(list);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    }

	private void startAvatarsActivity(ArrayList<User> users) {
		Intent i = new Intent(this, GameActivity.class);
		application.setCellUsers(users);
		application.clearImages();
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
