package it.guesswho.view;

import it.guesswho.R;
import it.guesswho.controller.ControllerGCM;
import it.guesswho.model.GuessWhoApplication;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * Fragement in which is possible to create a PostIt
 * 
 * @author AC, DD
 */
public class GameMessagesFragment extends SherlockFragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	private String tag = "messages";
	private ListView listView;
	private Activity mActivity;
	private ArrayAdapter<String> listAdapter;
	private ArrayList<String> listMessages;
	private ArrayList<String> listAnswers;
	private GuessWhoApplication application;
	private ControllerGCM controllore;
	
	public GameMessagesFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(tag, "onCreate");
		controllore = new ControllerGCM(getActivity());
		if (savedInstanceState != null)
		{	
			listMessages = savedInstanceState.getStringArrayList("messages");
			listAnswers = savedInstanceState.getStringArrayList("answers");
		} 
		if (listMessages == null)
		{
			listMessages = new ArrayList<String>();
			listAnswers = new ArrayList<String>();
		}
		
		application = (GuessWhoApplication) getActivity().getApplication();
		/* TODO caricare la lista? */
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(tag, "onCreateView");
		// Inflate the layout for this fragment
		View V = inflater.inflate(R.layout.fragment_gamemessages, container,
				false);

		listView = (ListView) V.findViewById(R.id.fragment_listview);
		if (savedInstanceState != null)
		{
			listMessages = savedInstanceState.getStringArrayList("messages");
			listAnswers = savedInstanceState.getStringArrayList("answers");
		}

		setList();
		return V;

	}

	/**
	 * Setting the list with the retrieved friends
	 * */
	public void setList() {
		Log.d(tag, mActivity + "-" + listMessages);
		listAdapter = new ArrayAdapter<String>(
				mActivity.getApplicationContext(), R.layout.array_element,
				listMessages);
		listView.setAdapter(listAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
//				        	Toast.makeText(mActivity, "pressed yes button", Toast.LENGTH_SHORT).show();
							controllore.sendGCMMessage(application.getUser().getId(), application.getUser().getId(), ((TextView)view).getText().toString(), "yes");
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
//				        	Toast.makeText(mActivity, "pressed no button", Toast.LENGTH_SHORT).show();
							controllore.sendGCMMessage(application.getUser().getId(), application.getUser().getId(), ((TextView)view).getText().toString(), "no");
				            break;
				        }
				    }
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("What is your answer?").setPositiveButton("Yes", dialogClickListener)
				    .setNegativeButton("No", dialogClickListener).show();
			}

		});
	}

	public void setAnswer(String message, String answer)
	{
		for(int i = 0; i < listMessages.size(); i++)
		{
			if(listMessages.get(i).equals(message))
			{ 
				listAnswers.set(i, answer);
//				if(answer.equals("yes"))
//					(this.listView.getChildAt(i)).setBackgroundColor(Color.GREEN);
//				else
//					(this.listView.getChildAt(i)).setBackgroundColor(Color.RED);

				listMessages.set(i, listMessages.get(i) + " = " + answer );
			}
		}
		this.setList();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null)
		{
			listMessages = savedInstanceState.getStringArrayList("messages");
			listAnswers = savedInstanceState.getStringArrayList("answers");
		}

		if (listMessages == null)
		{
			listMessages = new ArrayList<String>();
			listAnswers = new ArrayList<String>();
		}
		super.onActivityCreated(savedInstanceState);
	}

	public void addInList(String message) {
		if (listMessages == null)
		{
			listMessages = new ArrayList<String>();
			listAnswers = new ArrayList<String>();
		}
		Log.d(tag, "addinlist:" + message + " list " + listMessages);
		listMessages.add(message);
		listAnswers.add("");
		setList();
	}

	@Override
	public void onDestroy() {
		Log.d(tag, "onDestroy");
		super.onDestroy();
	}

	@Override
	public void onResume() {
		Log.d(tag, "onResume");
		mActivity = application.getGameActivity();
		
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		Log.d(tag, "onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(tag, "onSaveInstanceState");
		outState.putStringArrayList("messages", listMessages);
		outState.putStringArrayList("answers", listAnswers);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("Search").setIcon(R.drawable.abs__ic_clear_normal)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);

	}

	@Override
	public void onAttach(Activity activity) {
		Log.d(tag, "onAttach");
		this.mActivity = activity;
		super.onAttach(activity);
	}

}
