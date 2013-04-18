package it.guesswho.view;


import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guesswho.R;
import com.facebook.FacebookException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.ProfilePictureView;

/**
 * MainActivity of the application, it's used for the login method and for starting the game
 * @author andrea
 *
 */
public class MainActivity extends FragmentActivity {

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
        	Log.d("session", "session status callback:" + state.toString());
            onSessionStateChange(session, state, exception);
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        
        application = (GuessWhoApplication) getApplication();
        
        uiHelper = new UiLifecycleHelper(this, sessionCallback);
        uiHelper.onCreate(null);

        setContentView(R.layout.activity_main);

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
    	Log.d("session", "onSessionStateChange: " + state.toString());
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