package it.guesswho.utils;

import it.guesswho.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtils {
	public static boolean isConnected(Context c)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return(networkInfo != null && networkInfo.isConnected());
	}
	
	
	public static void sendGCMMessage(String senderId, String receiverId, String message, String answer)
	{
		JSONObject json = new JSONObject();
         
        try {
			json.put("answer", answer);
			json.put("text", message);
			json.put("sender", senderId);
	        json.put("receiver", receiverId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
       
        HttpConnector.postMessage("gcm/msg/", json.toString());
	}

	public static void registerToServer(String gcmId, String facebookId, String name)
	{
		JSONObject json = new JSONObject();
        try {
			json.put("gcmId", gcmId);
			json.put("fbId", facebookId);
			json.put("fbId", name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
         
        HttpConnector.postMessage("gcm/create/", json.toString());
	}

	public static void createMatch(String gcmId, String facebookId, String name, String opponentId, String opponentName, ArrayList<User> users)
	{
		JSONObject json = new JSONObject();
		
		JSONArray array=new JSONArray();
	    for (int i = 0; i < users.size(); i++) {
	        array.put(users.get(i).getId());

	    }
        try {
        	json.put("users",array);
			json.put("gcmId", gcmId);
			json.put("fbId", facebookId);
			json.put("name", name);
	        json.put("opponentFbId", opponentId);
	        json.put("opponentFbName", opponentName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        HttpConnector.postMessage("game/create/", json.toString());
	}
	
	public static ArrayList<String> searchMatches(String gcmId, String facebookId)
	{
		ArrayList<String> matches = new ArrayList<String>();
		JSONObject json = new JSONObject();
         
        try {
			json.put("gcm_id", gcmId);
	        json.put("fb_id", facebookId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
       
        HttpResponse response = HttpConnector.postMessage("game/search/", json.toString());
        try {

			JSONArray jsonArray = new JSONArray(EntityUtils.toString(response.getEntity()));
			for(int i = 0; i < jsonArray.length(); i++)
			{
				matches.add(jsonArray.getString(i));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
        Log.d("http", matches.toString());
		return matches;

	}

	public static String getUrlFacebookUserAvatar(String name_or_idUser )
	{
		String address = "http://graph.facebook.com/"+name_or_idUser+"/picture?type=normal";
	    URL url;
	    String newLocation = null;
	    try {
	        url = new URL(address);
	        HttpURLConnection.setFollowRedirects(false); //Do _not_ follow redirects!
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        newLocation = connection.getHeaderField("Location");
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return newLocation;
	}
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {   
	        e.printStackTrace();
	        return null;
	    }
	}


	public static boolean setTarget(String facebookId, String opponent, String target) {
		JSONObject json = new JSONObject();
		
        try {
	        json.put("opponentFbId", opponent); 
        	json.put("target", target);
			json.put("fbId", facebookId);

		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        HttpResponse response = HttpConnector.postMessage("game/target/", json.toString());
        try {
			String answer = EntityUtils.toString(response.getEntity());
			return answer.equals("target set");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
	
	public static boolean closeMatch(String facebookId, String opponent, String guessName) {
		JSONObject json = new JSONObject();
		
        try {
	        json.put("opponentFbId", opponent); 
        	json.put("choice", guessName);
			json.put("fbId", facebookId);

		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        HttpResponse response = HttpConnector.postMessage("game/close/", json.toString());
        try {
			String answer = EntityUtils.toString(response.getEntity());
			return answer.equals("guessed");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
}
