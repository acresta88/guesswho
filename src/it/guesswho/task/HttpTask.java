package it.guesswho.task;

import it.guesswho.utils.HttpConnector;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;

public class HttpTask extends AsyncTask<Object, HttpResponse, HttpResponse>{

	String url;
	String jsonObject;
	OnHttpTaskCompletedCallback onTaskCompleted;
	OnResultCallback onResultCallback;
	public HttpTask(String url, String jsonObject, OnResultCallback onResultCallback, OnHttpTaskCompletedCallback onTaskCompleted) {
		super();

		this.url = url;
		this.jsonObject = jsonObject;
		this.onTaskCompleted = onTaskCompleted;
		this.onResultCallback = onResultCallback;
	}

	@Override
	protected HttpResponse doInBackground(Object... params) {
		return HttpConnector.postMessage(url, jsonObject);
	}

	@Override
	protected void onPostExecute(HttpResponse result) {
		super.onPostExecute(result);
		onTaskCompleted.onTaskCompleted(result, onResultCallback);
	}

}
