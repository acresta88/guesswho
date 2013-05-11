package it.guesswho.task;

import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;
import it.guesswho.utils.NetworkUtils;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class PrefetchingTask extends AsyncTask{
	private GuessWhoApplication application;
	private String fb_id;
	
	public PrefetchingTask(GuessWhoApplication application, String fb_id )
	{
		this.application = application;
		this.fb_id = fb_id;
	}
//<ArrayList<User>, String, String>
	@Override
	protected Object doInBackground(Object... params) {

		ArrayList<User> users = application.getCellUsers();
		for(int i = 0; i < users.size(); i++)
	    {
			if(fb_id.equals(users.get(i).getId()))
			{
		    	if(application.getImage(i) == null)
		    	{
	        		Bitmap img = NetworkUtils.getBitmapFromURL(NetworkUtils.getUrlFacebookUserAvatar(users.get(i).getId()));
	        		application.setImage(i, img);
		    	}				
			}
	    }
	    
		return null;
	}

}
