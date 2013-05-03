package it.guesswho.model;

import java.util.ArrayList;

import android.app.Application;
import android.util.Log;

import com.facebook.Session;
import com.facebook.model.GraphUser;

public class GuessWhoApplication extends Application{
	private ArrayList<User> cellUsers;
	private GraphUser graphUser;
	private String gcmId;
    private Session session;
    private ArrayList<User> friends;
    
    private String tag = "application";
    
    @Override
    public void onCreate()
    {
    	super.onCreate();
    }

	public ArrayList<User> getCellUsers() {
		return cellUsers;
	}

	public void setCellUsers(ArrayList<User> cellUsers) {
		this.cellUsers = cellUsers;
	}

	public GraphUser getUser() {
		return graphUser;
	}

	public void setUser(GraphUser user) {
		if (user != null)
			Log.d(tag, "setUser:" + user.getName());
		else
			Log.d(tag, "setUser:" + "null user");
		this.graphUser = user;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		Log.d(tag, session.getAccessToken());
		this.session = session;
	}

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		Log.d(tag, "setGcmId:" + gcmId);
		this.gcmId = gcmId;
	}

	public void setFriendList(ArrayList<User> users) {
		Log.d(tag, "setfriendlist:" + users.size());
		this.friends = users;
	}
	
	public ArrayList<User> getFriendList() {
		return this.friends;
	}
}
