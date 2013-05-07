package it.guesswho.view;

import it.guesswho.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * Fragement in which is possible to create a PostIt
 * @author AC, DD
 */
public class GameMessagesFragment extends SherlockFragment {
	
	public static final String ARG_SECTION_NUMBER = "section_number";

	private MainActivity mActivity;
	
	public GameMessagesFragment()
	{
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			View V = inflater.inflate(R.layout.fragment_gamemessages, container, false);
			return V;
			
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("Search")
        .setIcon(R.drawable.abs__ic_clear_normal)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		super.onCreateOptionsMenu(menu, inflater);
     
    }
//	
//	@Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mActivity = (MainActivity) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " Not MainActivity class instance");
//        }
//    }
	
}
