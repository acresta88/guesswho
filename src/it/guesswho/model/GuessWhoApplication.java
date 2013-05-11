package it.guesswho.model;

import it.guesswho.view.GameActivity;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.facebook.Session;
import com.facebook.model.GraphUser;

public class GuessWhoApplication extends Application{
	private ArrayList<User> cellUsers;
	private GraphUser graphUser;
	private String gcmId;
    private Session session;
    private ArrayList<User> friends;
    private String target;
    private String opponent;
    
    private String tag = "application";
	private GameActivity gameActivity;
    private static Bitmap[] images;
    
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

	public void setGameActivity(GameActivity gameActivity) {
		this.gameActivity = gameActivity;
	}
	
	public GameActivity getGameActivity()
	{
		return this.gameActivity;
	}

	public static Bitmap[] getImages() {
		return images;
	}

	public static void setImages(Bitmap[] images) {
		GuessWhoApplication.images = images;
	}
	
	public void setImage (int position, Bitmap image)
	{
		if(position < this.images.length)
			images[position] = image;
		else 
			Log.d(tag, "error during the insert of the image");
	}
	
	public Bitmap getImage (int position)
	{
		if(position < this.images.length)
			return images[position];
		else 
			Log.d(tag, "error during the retrieve of the image");
		return null;
	}

	public void clearImages() {
		images = null;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getOpponent() {
		return this.opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
}
