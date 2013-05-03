package it.guesswho.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
	public static boolean isConnected(Context c)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return(networkInfo != null && networkInfo.isConnected());
	}
	
	
	public static void sendGCMMessage(String senderId, String receiverId, String message)
	{
		JSONObject json = new JSONObject();
         
        try {
			json.put("text", message);
			json.put("sender", senderId);
	        json.put("receiver", receiverId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
       
        HttpConnector.postMessage("gcm/msg/", json.toString());
	}

	public static void registerToServer(String gcmId, String facebookId)
	{
		JSONObject json = new JSONObject();
        try {
			json.put("gcmId", gcmId);
			json.put("fbId", facebookId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        HttpConnector.postMessage("gcm/create/", json.toString());
	}

	public static void createMatch(String gcmId, String facebookId, String opponentId, String token)
	{
		JSONObject json = new JSONObject();
        
        try {
        	json.put("access_token", token);
			json.put("gcmId", gcmId);
			json.put("fbId", facebookId);
	        json.put("opponentFbId", opponentId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        HttpConnector.postMessage("game/create/", json.toString());
	}
}
