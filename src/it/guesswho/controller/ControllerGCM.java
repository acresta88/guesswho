package it.guesswho.controller;

import com.google.android.gcm.GCMRegistrar;

import it.guesswho.model.GuessWhoApplication;
import it.guesswho.utils.NetworkUtils;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class ControllerGCM {
	private GuessWhoApplication application;
	private Activity activity;
	private boolean registeredToTheServer;
	public ControllerGCM(Activity a) {
		this.activity = a;
		registeredToTheServer = false;
	}

	public void enableGCM() {
		String tag = "GCMService";
		
        /* abilitare GCM */
        GCMRegistrar.checkManifest(activity);
        GCMRegistrar.checkDevice(activity);
        GCMRegistrar.checkManifest(activity);
        String regId = GCMRegistrar.getRegistrationId(activity);
        Log.d(tag, "getReg.Id:" + regId);
        if (regId.equals("")) {
        	GCMRegistrar.register(activity, "830091449019");
        	regId = GCMRegistrar.getRegistrationId(activity);
        } else {
          Log.d(tag, "Already registered to GCM");
        }
		this.application = (GuessWhoApplication) activity.getApplication();
        application.setGcmId(regId);
        
        
        if(!registeredToTheServer)
        {
        	Log.d(tag, "not registered to the server, regitering now!");
        	try{
        	NetworkUtils.registerToServer(application.getGcmId(), application.getUser().getId(), application.getUser().getName());
        	registeredToTheServer = true;
        	} catch(NullPointerException e)
        	{
        		Toast.makeText(activity.getApplicationContext(), "user data incomplete, try to log in again", Toast.LENGTH_SHORT).show();
        	}
        }
        else
        	Log.d(tag, "already registered to my server!");

	}

	
	public void sendGCMMessage(String senderId, String receiverId, String message, String answer)
    {
		NetworkUtils.sendGCMMessage(senderId, receiverId, message, answer);
    }
}
