package it.guesswho.task;

import org.apache.http.HttpResponse;

public interface OnHttpTaskCompletedCallback {
	public void onTaskCompleted(HttpResponse response, OnResultCallback onResultCallback);
}
