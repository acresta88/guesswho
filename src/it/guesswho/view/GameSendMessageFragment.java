package it.guesswho.view;

import it.guesswho.R;
import it.guesswho.controller.ControllerGCM;
import it.guesswho.model.GuessWhoApplication;
import it.guesswho.task.OnResultCallback;
import it.guesswho.utils.NetworkUtils;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * Fragement in which is possible to create a PostIt
 * @author AC, DD
 */
public class GameSendMessageFragment extends SherlockFragment {
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	private GuessWhoApplication application;
	private String tag = "messages";
	private MainActivity mActivity;
	private Button sendButton;
	private Button guessButton;
	private EditText text;
	private ControllerGCM controllore;
	
	public GameSendMessageFragment()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			View V = inflater.inflate(R.layout.fragment_gamesendmessage, container, false);
			
			controllore = new ControllerGCM(getActivity());
			application = (GuessWhoApplication) getActivity().getApplication();
			text = (EditText) V.findViewById(R.id.fragment_text);
			sendButton = (Button) V.findViewById(R.id.fragment_sendbutton);
			sendButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String t;
					if(text.getText() == null)
						t = "Messaggio vuoto";
					else
						t = text.getText().toString();
					
					Log.d(tag, "onclick text: " + t);
					controllore.sendGCMMessage(application.getUser().getId(), application.getOpponent(), t, "");
				}
			});
			
			guessButton = (Button) V.findViewById(R.id.fragment_guessbutton);
			guessButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String t;
					if(text.getText() == null)
						t = "Messaggio vuoto";
					else
						t = text.getText().toString();
					
					Log.d(tag, "onclick text: " + t);
					int i;
					boolean res = false;
					for(i = 0; i < application.getCellUsers().size(); i++)
					{
						if(application.getCellUsers().get(i).getName().equals(t))
						{
							Log.d(tag, "trying to guess " + application.getCellUsers().get(i).getId());
							break;
						}
					}
					
					if(i == application.getCellUsers().size())
						Toast.makeText(getActivity(), "The inserted name doesn't exist!", Toast.LENGTH_SHORT).show();
					else
					{
						//founded the user, have to ask to the server if is correct 
						NetworkUtils.closeMatch(application.getUser().getId(), 
								application.getOpponent(), 
								application.getCellUsers().get(i).getId(),
								new OnResultCallback() {
									
									@Override
									public void onTaskCompleted(Object response) {
										boolean res = (Boolean)response; 
										if(res)
										{
											application.clearImages();
											application.setCellUsers(null);
											application.setFriendList(null);
											Toast.makeText(getActivity(), "YOU WIN!!", Toast.LENGTH_SHORT).show();
											Intent intent = new Intent(getActivity(), MainActivity.class);
											startActivity(intent);
										}
										else
											Toast.makeText(getActivity(), "The inserted name is not the correct one!", Toast.LENGTH_SHORT).show();
									}
								});
					}
				}
			});
			return V;
			
	}
    
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("Search")
        .setIcon(R.drawable.abs__ic_clear_normal)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		super.onCreateOptionsMenu(menu, inflater);
     
    }

}
