package it.guesswho.view;

import it.guesswho.R;
import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;
import it.guesswho.task.OnResultCallback;
import it.guesswho.utils.NetworkUtils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * Fragement in which is possible to create a PostIt
 * @author AC, DD
 */
public class GameFragment extends SherlockFragment {

	private ArrayList<User> users;
    private Boolean[] visualizedUsers;
    private GuessWhoApplication application;
    private String tag = "GameFragment";
    
	public static final String ARG_SECTION_NUMBER = "section_number";

	public GameFragment()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			View V = inflater.inflate(R.layout.fragment_game, container, false);
			
			application = (GuessWhoApplication) getActivity().getApplication();
		    users = application.getCellUsers();
		    
		    if(application.getImages() == null || application.getImages().length != users.size())
		    	application.setImages(new Bitmap[users.size()]);
		    
		    
		    GridView gridview = (GridView) V.findViewById(R.id.gridview);
		    gridview.setAdapter(new ImageAdapter(getActivity()));
		    
		    gridview.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					Log.d(tag, "onItemClick");

					if(application.getTarget() == null || application.getTarget().equals(""))
					{
						Log.d(tag, "onLongClick");
						View grid = (View)v; 
						TextView textView = (TextView)grid.findViewById(R.id.text);
				   	    String text = (String) textView.getText();
				   	    Log.d(tag, "onLongClick text:"+text);
				   	    String idT = null;
				   	    for(int i = 0; i < application.getCellUsers().size(); i++)
				   	    {
				   	    	if(application.getCellUsers().get(i).getName().equals(text))
				   	    	{
				   	    		idT = application.getCellUsers().get(i).getId();
				   	    		Log.d(tag, "onLongClick id: "+id);
				   	    		break;
				   	    	}
				   	    }
				   	    boolean res = false;
				   	    if(idT != null)
				   	    	NetworkUtils.setTarget(application.getUser().getId(), application.getOpponent(), idT, new OnResultCallback() {
								
								@Override
								public void onTaskCompleted(Object response) {
									String res = (String)response; 
									if(res != null && !res.equals(""))
									{
										application.setTarget(res);
								   	    Log.d(tag, "target set:"+res);
									}
									else
										Log.d(tag, "target didn't set");
								}
							});
					}
					else
					{
			            if(visualizedUsers[position])
			            {
			            	View grid = (View)v; 
			            	ImageView imageView = (ImageView)grid.findViewById(R.id.image);
					   	    imageView.setImageResource(R.drawable.default_pic);
	
			            }
			            else
			            {
			            	View grid = (View)v; 
			            	ImageView imageView = (ImageView)grid.findViewById(R.id.image);
			            	
			            	if(application.getImage(position) == null)
			    	    	{
			            		Bitmap img = NetworkUtils.getBitmapFromURL(NetworkUtils.getUrlFacebookUserAvatar(users.get(position).getId()));
			            		application.setImage(position, img);
			    	    	}
			    	    		
			            	imageView.setImageBitmap(application.getImage(position));
			    	    	
	//				   	    imageView.setImageBitmap(getBitmapFromURL(getUrlFacebookUserAvatar(users.get(position).getId())));
			            }
			    	     
	
			            visualizedUsers[position] = !visualizedUsers[position];
					}
		        }
		    });

		    gridview.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					Log.d(tag, "onLongClick");
					View grid = (View)v; 
					TextView textView = (TextView)grid.findViewById(R.id.text);
			   	    String text = (String) textView.getText();
			   	    Log.d(tag, "onLongClick text:"+text);
			   	    String id = null;
			   	    for(int i = 0; i < application.getCellUsers().size(); i++)
			   	    {
			   	    	if(application.getCellUsers().get(i).getName().equals(text))
			   	    	{
			   	    		id = application.getCellUsers().get(i).getId();
			   	    		Log.d(tag, "onLongClick id: "+id);
			   	    		break;
			   	    	}
			   	    }
			   	    if(id != null)
			   	    {
			   	    	
			   	    	NetworkUtils.setTarget(application.getUser().getId(), application.getOpponent(), id, new OnResultCallback() {
							
							@Override
							public void onTaskCompleted(Object response) {
								String res = (String)response; 
								if(res != null && !res.equals(""))
								{
									application.setTarget(res);
							   	    Log.d(tag, "target set:"+res);
								}
								else
									Log.d(tag, "target didn't set");
							}
						});
			   	    }
					return false;
				}
		    });
			return V;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("Search")
        .setIcon(R.drawable.abs__ic_clear_normal)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		super.onCreateOptionsMenu(menu, inflater);
     
    }
    
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		Toast.makeText(getActivity(), "the first avatar you will click it will be your target for this match", Toast.LENGTH_LONG).show();
	}

	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    private LayoutInflater layoutInflater;
	     
	      
	    public ImageAdapter(Context c) {
	        mContext = c;
		    layoutInflater = LayoutInflater.from(mContext);

	        // initialization array of boolean
	        visualizedUsers = new Boolean[users.size()];
	        for(int i = 0; i < visualizedUsers.length; i++)
	        {
	        	visualizedUsers[i] = true;
	        }
	    }

	    public int getCount() {
	        return users.size();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View grid;
	    	if(convertView == null ){
	    		grid = new View(mContext);
	    		grid = layoutInflater.inflate(R.layout.image, null);
	    	}
	    	else{
	    		grid = (View)convertView;		   
	    	}
		     
	    	ImageView imageView = (ImageView)grid.findViewById(R.id.image);
	    	if(application.getImage(position) == null)
	    	{
        		Bitmap img = NetworkUtils.getBitmapFromURL(NetworkUtils.getUrlFacebookUserAvatar(users.get(position).getId()));
        		application.setImage(position, img);
	    	}
	    		
        	imageView.setImageBitmap(application.getImage(position));
	    	
	    	TextView textView = (TextView)grid.findViewById(R.id.text);
	    	textView.setText(users.get(position).getName());

	    	return grid;
	    }
	}
	
	
	
}
