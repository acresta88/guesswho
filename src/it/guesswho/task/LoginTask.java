package it.guesswho.task;

import it.guesswho.controller.ControllerGCM;
import it.guesswho.model.GuessWhoApplication;
import it.guesswho.view.MainActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class LoginTask extends AsyncTask<Bundle, Bundle, Object> {

	private String tagLogin = "login";
	private String tagSession = "login";
	private MainActivity mActivity;
	private GuessWhoApplication application;
	private ControllerGCM controller;

	public LoginTask(MainActivity a) {
		this.mActivity = a;
		application = (GuessWhoApplication) a.getApplication();
		controller = new ControllerGCM(mActivity);

	}

	/**
	 * Definition for the callback of the status' changes
	 */
	private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			Log.d(tagSession, "session status callback:" + state.toString());
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		Log.d(tagSession, "onSessionStateChange: " + state.toString());
		application.setSession(session);
		mActivity.updateUI();
	}

	@Override
	protected Object doInBackground(Bundle... params) {
		
		Bundle savedInstanceState;
		if(params.length != 0)
			savedInstanceState = params[0];
		else
			savedInstanceState = null;
		
		Log.d(tagLogin, "start autologin");
		// check previous user saved
		Session session = Session.getActiveSession();
		if (session == null && savedInstanceState != null) {
			Log.d(tagLogin, "restore session");
			session = Session.restoreSession(mActivity, null, sessionCallback,
					savedInstanceState);
			Log.d(tagLogin, "session " + session.getAccessToken());
		}
		if (session == null || !session.isOpened()) {
			Log.d(tagLogin, "secondo if");

			application.setSession(session);
			// start Facebook Login
			Session.openActiveSession(mActivity, true, new Session.StatusCallback() {

				// callback when session changes state
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					if (session.isOpened()) {

						// saving the session
						// Session.saveSession(session, null);

						Log.d(tagLogin, "sessione aperta richiedo utente");
						// make request to the /me API
						Request.executeMeRequestAsync(session,
								new Request.GraphUserCallback() {
									// callback after Graph API response with
									// user
									// object
									@Override
									public void onCompleted(GraphUser newuser,
											Response response) {
										Log.d(tagLogin,
												"onCompleted callback me");

										if (newuser != null) {
											Log.d(tagLogin, newuser.toString());
											application.setUser(newuser);
											controller.enableGCM();

										}
									}
								});
					} else
						Log.d(tagLogin, "sessione non aperta");
				}
			});
		} else {
			Log.d(tagLogin, "sessione aperta richiedo utente");
			// make request to the /me API
			Request.executeMeRequestAsync(session,
					new Request.GraphUserCallback() {
						// callback after Graph API response with user
						// object
						@Override
						public void onCompleted(GraphUser newuser,
								Response response) {
							Log.d(tagLogin, "onCompleted callback me");

							if (newuser != null) {
								Log.d(tagLogin, newuser.toString());
								application.setUser(newuser);
								controller.enableGCM();

							}
						}
					});
		}
		mActivity.updateUI();

		return null;
	}

}
