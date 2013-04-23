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

import android.util.Log;

/**
 * Classe di Utilità per eseguire le chiamate presso il serve "HOSTNAME"
 * Qualora si ricevano dei risultati la classe si occupa solamente di restituire il JSON corrispondente.
 * E' compito di chi invoca le funzionalità di HTTOConnector ad interpretare correttamente il JSON restituito
 * @author daniele
 *
 */
public class HttpConnector {
	
//	private static String HOSTNAME = "http://arcane-falls-6796.herokuapp.com/";
	private static String HOSTNAME = "http://192.168.1.65:5000/";

	public static HttpResponse getMessage(String url)
	{
		url = HOSTNAME + url;
		
		HttpResponse response = null;
		try {
		    HttpClient client = new DefaultHttpClient();  
		    
		    HttpGet get = new HttpGet(url);
		    HttpResponse responseGet = client.execute(get);  
		    response = responseGet;
		    Log.i("login","Post ll"+response.getEntity().getContentLength());
		    
		} catch (Exception e) {
			
			 Log.i("login", e.toString());
		}
		
		return response;
	}

public static HttpResponse putMessage(String url,String jsonMessage) {
		
		url = HOSTNAME + url;
		
		HttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(url);
		put.addHeader("Content-Type", "application/json");
		try {
			put.setEntity(new StringEntity(jsonMessage));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("send tag", put.getEntity().toString());
		HttpResponse response = null;
		try {
			response = client.execute(put);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.i("send tag", "IOException");
			e.printStackTrace();
			return null;
		}
		Log.i("send tag", "finish");
		return response;
	}

public static HttpResponse postMessage(String url,String jsonMessage) {
	
	url = HOSTNAME + url;
	
	HttpClient client = new DefaultHttpClient();
	HttpPost put = new HttpPost(url);
	
	if(jsonMessage != null && !jsonMessage.equals(""))
	{
		put.addHeader("Content-Type", "application/json");
		try {
			put.setEntity(new StringEntity(jsonMessage));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i("send tag", put.getEntity().toString());

	}	
	HttpResponse response = null;
	try {
		response = client.execute(put);
		
	} catch (ClientProtocolException e) {
		e.printStackTrace();
		return null;
	} catch (IOException e) {
		Log.i("send tag", "IOException");
		e.printStackTrace();
		return null;
	}
	Log.i("send tag", "finish");
	return response;
}


}