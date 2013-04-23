package it.guesswho.view;

import it.guesswho.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ChallengesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_challenges);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.challenges, menu);
		return true;
	}

}
