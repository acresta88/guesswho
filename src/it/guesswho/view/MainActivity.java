package it.guesswho.view;


import it.guesswho.R;
import it.guesswho.model.GuessWhoApplication;
import it.guesswho.utils.GUIUtils;
import it.guesswho.utils.HttpConnector;
import it.guesswho.utils.NetworkUtils;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gcm.GCMRegistrar;

/**
 * MainActivity of the application, it's used for the login method and for starting the game
 * @author andrea
 *
 */
public class MainActivity extends FragmentActivity {

	private String tag = "GCMIntentService";
	private String tagSession = "session";
	
    private Button newGameButton;	
    private LoginButton loginButton;
    private ProfilePictureView profilePictureView;
    private TextView greeting;
    private UiLifecycleHelper uiHelper;
	private GuessWhoApplication application;

    /**
     * Definition for the callback of the status' changes 
     */
    private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
        	Log.d(tagSession, "session status callback:" + state.toString());
            onSessionStateChange(session, state, exception);
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        
        setContentView(R.layout.activity_main);

        if(NetworkUtils.isConnected(this))
        {
        	application = (GuessWhoApplication) getApplication();
            
            uiHelper = new UiLifecycleHelper(this, sessionCallback);
            uiHelper.onCreate(null);

	        loginButton = (LoginButton) findViewById(R.id.login_button);
	        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
	            @Override
	            public void onUserInfoFetched(GraphUser user) {
	                application.setUser(user);
	                updateUI();
	                // It's possible that we were waiting for this.user to be populated in order to post a
	                // status update.
	            }
	        });
	        
	        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
	        greeting = (TextView) findViewById(R.id.greeting);
	
	        newGameButton = (Button) findViewById(R.id.newGameButton);
	        newGameButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            	Intent i = new Intent(application.getApplicationContext(), FriendPicker.class);
	            	startActivity(i);
	            }
	        });
	        
	        autoLogin(savedInstanceState);
	        
	       
	        /* abilitare GCM */
	        GCMRegistrar.checkManifest(this);

	        GCMRegistrar.checkDevice(this);
	        GCMRegistrar.checkManifest(this);
	        String regId = GCMRegistrar.getRegistrationId(this);
	        Log.d(tag, "getReg.Id:" + regId);
	        if (regId.equals("")) {
	          GCMRegistrar.register(this, "830091449019");
	          regId = GCMRegistrar.getRegistrationId(this);
	        } else {
	          Log.d(tag, "Already registered");
	        }
	        
	        HttpConnector.postMessage("gcm/create/" + regId, "");
        }
        else
        {
	        GUIUtils.noConnectionMethod(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkUtils.isConnected(this))
        {
        	uiHelper.onResume();
            updateUI();
        }
        else
        {
	        GUIUtils.noConnectionMethod(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(NetworkUtils.isConnected(this))
        {
        	uiHelper.onSaveInstanceState(outState);
        }
        else
        {
	        GUIUtils.noConnectionMethod(this);
        } 
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(NetworkUtils.isConnected(this))
        {        
        	uiHelper.onActivityResult(requestCode, resultCode, data);
        }
        else
        {
	        GUIUtils.noConnectionMethod(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(NetworkUtils.isConnected(this))
        {
        	uiHelper.onPause();
        }
        else
        {
	        GUIUtils.noConnectionMethod(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(NetworkUtils.isConnected(this))
        {
        	uiHelper.onDestroy();
        }
        else
        {
	        GUIUtils.noConnectionMethod(this);
        }
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	Log.d(tagSession, "onSessionStateChange: " + state.toString());
    	application.setSession(session);
        updateUI();
    }

    private void updateUI() {
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());
        Log.d("autologin", "session active?" + enableButtons + " exp date:" + session.getExpirationDate().toString() + " permissions:" + session.getPermissions().toString());

        if (enableButtons && application.getUser() != null) {
        	application.setSession(session);
            profilePictureView.setProfileId(application.getUser().getId());
            greeting.setText(getString(R.string.hello_user, application.getUser().getFirstName()));
        } else {
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }
    }

	private void autoLogin(Bundle savedInstanceState){
		
		Log.d("autologin", "start autologin");
		// check previous user saved
		Session session = Session.getActiveSession();
        if (session == null && savedInstanceState != null) {
            	Log.d("autologin", "restore session");
                session = Session.restoreSession(this, null, sessionCallback, savedInstanceState);
                Log.d("autologin", "session " + session.getAccessToken());
        }
	    if (session == null || !session.isOpened())
        {
	    	application.setSession(session);
	        Log.d("autologin", "else");
			// start Facebook Login
			Session.openActiveSession(this, true, new Session.StatusCallback() {
		
				// callback when session changes state
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					if (session.isOpened()) {
		
						// saving the session
//						Session.saveSession(session, null);
							
						Log.d("autologin", session.toString());
						// make request to the /me API
						Request.executeMeRequestAsync(session,
								new Request.GraphUserCallback() {
												// callback after Graph API response with user
									// object
									@Override
									public void onCompleted(GraphUser newuser,
											Response response) {
										if (newuser != null) {
											Log.d("autologin", newuser.toString());
											application.setUser(newuser);
										}
									}
								});
					}
					}
				});
	    }	
        
        updateUI();
	}
	
}