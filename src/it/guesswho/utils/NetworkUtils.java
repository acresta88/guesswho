package it.guesswho.utils;

import it.guesswho.model.User;
import it.guesswho.task.HttpTask;
import it.guesswho.task.OnHttpTaskCompletedCallback;
import it.guesswho.task.OnResultCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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
	private static String tag = "NetworkUtils";
	public static boolean isConnected(Context c)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return(networkInfo != null && networkInfo.isConnected());
	}
	
	
	public static void sendGCMMessage(String senderId, String receiverId, String message, String answer, OnResultCallback callback)
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
       
        HttpTask httpTask = new HttpTask("gcm/msg/", json.toString(), callback, new OnHttpTaskCompletedCallback() {
			
			@Override
			public void onTaskCompleted(HttpResponse response, OnResultCallback onResultCallback) {
				String answer = "";
				try {
					answer = EntityUtils.toString(response.getEntity());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				onResultCallback.onTaskCompleted(answer);
			}
		});
        httpTask.execute();
	}

	public static void registerToServer(String gcmId, String facebookId, String name, OnResultCallback callback)
	{
		JSONObject json = new JSONObject();
        try {
			json.put("gcmId", gcmId);
			json.put("fbId", facebookId);
			json.put("fbId", name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
         
        HttpTask httpTask = new HttpTask("game/create/", json.toString(), callback, new OnHttpTaskCompletedCallback() {

			@Override
			public void onTaskCompleted(HttpResponse response,
					OnResultCallback onResultCallback) {
				onResultCallback.onTaskCompleted(response);
			}
        	
        });
        httpTask.execute();
	}

	public static void createMatch(String gcmId, String facebookId, String name, String opponentId, String opponentName, ArrayList<User> users, OnResultCallback callback)
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
        
        HttpTask httpTask = new HttpTask("game/create/", json.toString(), callback, new OnHttpTaskCompletedCallback() {

			@Override
			public void onTaskCompleted(HttpResponse response,
					OnResultCallback onResultCallback) {
				onResultCallback.onTaskCompleted(response);
			}
        	
        });
        httpTask.execute();
	}
	
	public static void searchMatches(String gcmId, String facebookId, OnResultCallback callback)
	{
		JSONObject json = new JSONObject();
         
        try {
			json.put("gcm_id", gcmId);
	        json.put("fb_id", facebookId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
       
        HttpTask httpTask = new HttpTask("game/search/", json.toString(), callback, new OnHttpTaskCompletedCallback() {
			
			@Override
			public void onTaskCompleted(HttpResponse response, OnResultCallback onResultCallback) {
				ArrayList<String> matches = new ArrayList<String>();

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
				onResultCallback.onTaskCompleted(matches);
			}
		});
        httpTask.execute();
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


	public static void setTarget(String facebookId, String opponent, String target, OnResultCallback callback) {
		JSONObject json = new JSONObject();
		
        try {
	        json.put("opponentFbId", opponent); 
        	json.put("target", target);
			json.put("fbId", facebookId);

		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        HttpTask httpTask = new HttpTask("game/target/", json.toString(), callback, new OnHttpTaskCompletedCallback() {
			
			@Override
			public void onTaskCompleted(HttpResponse response, OnResultCallback onResultCallback) {
				String answer = "";
				try {
					answer = EntityUtils.toString(response.getEntity());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				onResultCallback.onTaskCompleted(answer);
			}
		});
        httpTask.execute();
	}
	
	public static void closeMatch(String facebookId, String opponent, String guessName, OnResultCallback callback) {
		JSONObject json = new JSONObject();
		
        try {
	        json.put("opponentFbId", opponent); 
        	json.put("choice", guessName);
			json.put("fbId", facebookId);

		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        HttpTask httpTask = new HttpTask("game/close/", json.toString(), callback, new OnHttpTaskCompletedCallback() {
			
			@Override
			public void onTaskCompleted(HttpResponse response, OnResultCallback onResultCallback) {
				String answer = "";
				try {
					if(response != null)
						answer = EntityUtils.toString(response.getEntity());
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				onResultCallback.onTaskCompleted(answer.equals("guessed"));
			}
		});
        httpTask.execute();
        
	}
}
