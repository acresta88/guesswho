package it.guesswho.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class GUIUtils {
	public static void noConnectionMethod(Activity a)
	{
		Toast.makeText(a.getApplicationContext(), "no connection, check the network settings and open again the application", Toast.LENGTH_LONG).show();
		a.finish();
	}
}
