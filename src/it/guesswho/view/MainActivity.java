package it.guesswho.view;


import java.util.ArrayList;

import it.guesswho.R;
import it.guesswho.controller.ControllerGCM;
import it.guesswho.model.GuessWhoApplication;
import it.guesswho.utils.GUIUtils;
import it.guesswho.utils.NetworkUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

/**
 * MainActivity of the application, it's used for the login method and for starting the game
 * @author andrea
 *
 */
public class MainActivity extends FragmentActivity {

	private String tagLogin = "login";
	private String tagSession = "session";
	
    private Button searchGameButton;	
    private Button newGameButton;	
    private LoginButton loginButton;
    private ProfilePictureView profilePictureView;
    private TextView greeting;
    private UiLifecycleHelper uiHelper;
	private GuessWhoApplication application;
	private ControllerGCM controller;
    
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
        	controller = new ControllerGCM(this);
        	application = (GuessWhoApplication) getApplication();
            
            uiHelper = new UiLifecycleHelper(this, sessionCallback);
            uiHelper.onCreate(null);

	        loginButton = (LoginButton) findViewById(R.id.login_button);
	        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
	            @Override
	            public void onUserInfoFetched(GraphUser user) {
	                application.setUser(user);
	                controller.enableGCM();
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
	        
	        searchGameButton = (Button) findViewById(R.id.searchGameButton);
	        searchGameButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	            	Log.d("http", "search request "+ application.getGcmId() +" - "+ application.getUser().getId());
	            	ArrayList<String> users = NetworkUtils.searchMatches(application.getGcmId(), application.getUser().getId());
	            	//create an appropriate activity
	            	Intent i = new Intent(application.getApplicationContext(), SearchMatchActivity.class);
	            	i.putStringArrayListExtra("users", users);
	            	startActivity(i);
	            	
	            }
	        });
	        
	        Button newMessageButton = (Button) findViewById(R.id.sendMsgButton);
	        newMessageButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d("GCMService", "onClickSendMessage " + application.getGcmId());
					controller.sendGCMMessage(application.getGcmId(), application.getGcmId(), "send messaggio di prova");
				}
			});
	        autoLogin(savedInstanceState);
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
        Log.d(tagLogin, "session active?" + enableButtons + " exp date:" + session.getExpirationDate().toString() + " permissions:" + session.getPermissions().toString());

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
		
		Log.d(tagLogin, "start autologin");
		// check previous user saved
		Session session = Session.getActiveSession();
        if (session == null && savedInstanceState != null) {
            	Log.d(tagLogin, "restore session");
                session = Session.restoreSession(this, null, sessionCallback, savedInstanceState);
                Log.d(tagLogin, "session " + session.getAccessToken());
        }
	    if (session == null || !session.isOpened())
        {
			Log.d(tagLogin, "secondo if");

	    	application.setSession(session);
			// start Facebook Login
			Session.openActiveSession(this, true, new Session.StatusCallback() {
		
				// callback when session changes state
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					if (session.isOpened()) {
		
						// saving the session
//						Session.saveSession(session, null);
							
						Log.d(tagLogin, "sessione aperta richiedo utente");
						// make request to the /me API
						Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
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
					else
						Log.d(tagLogin, "sessione non aperta");
					}
				});
	    }	

        updateUI();
	}
	
	
}