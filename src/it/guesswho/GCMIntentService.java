package it.guesswho;

import it.guesswho.utils.StaticVariables;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class GCMIntentService extends com.google.android.gcm.GCMBaseIntentService{

	private String tag = "GCMService";
	public GCMIntentService() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String[] getSenderIds(Context context) {
		return super.getSenderIds(context);
	}

	public GCMIntentService(String... senderIds) {
		super(senderIds);
		Log.d(tag, "senders:" + senderIds.toString());
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		/* TODO Called when the device tries to register or unregister, but GCM returned 
		 * an error. Typically, there is nothing to be done other than evaluating the error 
		 * (returned by errorId) and trying to fix the problem.
		*/
		Log.d(tag, "error:" + arg1.toString());
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		/* TODO
		 *  Called when the device tries to register or unregister, but the GCM servers are 
		 *  unavailable. The GCM library will retry the operation using exponential backup, 
		 *  unless this method is overridden and returns false. This method is optional and 
		 *  should be overridden only if you want to display the message to the user or cancel 
		 *  the retry attempts.
		 *  */ 
		Log.d(tag, "error:" + errorId.toString());

		return super.onRecoverableError(context, errorId);
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		/* TODO Called when your server sends a message to GCM, and GCM delivers 
		 * it to the device. If the message has a payload, its contents are available 
		 * as extras in the intent. 
		*/
		
		
		Log.d(tag, "onMessage:" + arg1.toString());

		String message = arg1.getExtras().getString("content").toString();
		Log.d(tag, "answer:" + message);
		Log.d(tag, "title:" + arg1.getExtras().getString("title"));
		if(arg1.getExtras().getString("title") != null)
		{
			if(arg1.getExtras().getString("title").equals("game.create"))
			{
				generateNotification(arg0, arg1.getExtras(), StaticVariables.actionCreateGame);
			}
			if(arg1.getExtras().getString("title").equals("gcm.msg"))
			{
				generateNotification(arg0, arg1.getExtras(), StaticVariables.actionReceivedMessage);
			}
		}
	}
	

	@SuppressLint("NewApi")
	private void generateNotification(Context context, Bundle message, String action) {
		Log.d(tag, "generating notification for:" + message);

		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtras(message);
		sendBroadcast(intent);
	}
	
	@Override
	protected void onRegistered(Context arg0, String arg1) {
		/* TODO  Called after a registration intent is received, passes the 
		 * registration ID assigned by GCM to that device/application pair as 
		 * parameter. Typically, you should send the regid to your server so it 
		 * can use it to send messages to this device.
		*/
		Log.d(tag, "onRegistered:" + arg1.toString());

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		/* TODO  Called after the device has been unregistered from GCM. Typically, 
		 * you should send the regid to the server so it unregisters the device. */
		Log.d(tag, "onUnregistered:" + arg1.toString());

	}

}
