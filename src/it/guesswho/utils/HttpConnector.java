package it.guesswho.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.util.Log;


/**
 * Classe di Utilità per eseguire le chiamate presso il serve "HOSTNAME"
 * Qualora si ricevano dei risultati la classe si occupa solamente di restituire il JSON corrispondente.
 * E' compito di chi invoca le funzionalità di HTTOConnector ad interpretare correttamente il JSON restituito
 * @author daniele
 *
 */
@SuppressLint("NewApi")
public class HttpConnector {
	
	private static String tag = "http";
	private static String HOSTNAME = "http://arcane-falls-6796.herokuapp.com/";
//	private static String HOSTNAME = "http://192.168.1.76:5000/";
//	private static String HOSTNAME = "http://192.168.1.100:5000/";
//	private static String HOSTNAME = "http://192.168.165.245:5000/";

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static HttpResponse getMessage(String url, String jsonMessage)
	{
		url = HOSTNAME + url;
		
		HttpResponse response = null;
		try {
		    HttpClient client = new DefaultHttpClient();  
		    
		    HttpGet get = new HttpGet(url);
		    HttpResponse responseGet = client.execute(get);  
		    response = responseGet;
		    
		} catch (Exception e) {
			 Log.i(tag, e.toString());
		}
		
		return response;
	}

public static HttpResponse putMessage(String url,String jsonMessage) {
		
		url = HOSTNAME + url;
		Log.d("http", "starting request to " + url);

		HttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(url);
		if(jsonMessage != null && !jsonMessage.equals(""))
		{
			put.addHeader("Content-Type", "application/json");
			try {
				put.setEntity(new StringEntity(jsonMessage));
			} catch (UnsupportedEncodingException e) {
				Log.e("http", e.toString());

				e.printStackTrace();
			}
			
			Log.d("http", put.getEntity().toString());

		}
		
		Log.d(tag, put.getEntity().toString());
		HttpResponse response = null;
		try {
			response = client.execute(put);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.i(tag, "IOException");
			e.printStackTrace();
			return null;
		}
		Log.i(tag, "finish");
		return response;
	}

public static HttpResponse postMessage(String url,String jsonMessage) {
	
   	url = HOSTNAME + url;

	Log.d(tag,"trying "+url+ " with: " +jsonMessage);
	HttpResponse response = null;
	HttpClient client = new DefaultHttpClient();
	HttpPost put = new HttpPost(url);
	
	if(jsonMessage != null && !jsonMessage.equals(""))
	{
		put.addHeader("Content-Type", "application/json");
		try {
			put.setEntity(new StringEntity(jsonMessage));
		} catch (UnsupportedEncodingException e) {
			Log.e(tag,e.toString());

			e.printStackTrace();
		}
		
		try {
			Log.d(tag, EntityUtils.toString(put.getEntity()));

			response = client.execute(put);
			if(response != null)
			{
//				Log.d(tag, "response:" + EntityUtils.toString(response.getEntity()));
			}
			else
			{
				Log.d(tag, "null answer");
			}
		} catch (ClientProtocolException e) {
			Log.d(tag, "errore ricezione");
	
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.i(tag, "IOException");
			e.printStackTrace();
			return null;
		}
	}	
	Log.i(tag, "finish");
	return response;
}


}