package it.guesswho.model;

import java.util.ArrayList;

import android.app.Application;
import android.util.Log;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gcm.GCMRegistrar;

public class GuessWhoApplication extends Application{
	private ArrayList<User> cellUsers;
	private GraphUser user;
    private Session session;

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
		return user;
	}

	public void setUser(GraphUser user) {
		this.user = user;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
}
