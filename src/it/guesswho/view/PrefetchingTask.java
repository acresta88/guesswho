package it.guesswho.view;

import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;
import it.guesswho.utils.NetworkUtils;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class PrefetchingTask extends AsyncTask{
	private GuessWhoApplication application;
	
	public PrefetchingTask(GuessWhoApplication application)
	{
		this.application = application;
	}
//<ArrayList<User>, String, String>
	@Override
	protected Object doInBackground(Object... params) {

		ArrayList<User> users = application.getCellUsers();
		for(int i = 0; i < users.size(); i++)
	    {
	    	if(application.getImage(users.size() - i - 1) == null)
	    	{
        		Bitmap img = NetworkUtils.getBitmapFromURL(NetworkUtils.getUrlFacebookUserAvatar(users.get(users.size() - i - 1).getId()));
        		application.setImage(users.size() - i - 1, img);
	    	}
	    }
	    
		return null;
	}

}
