package it.guesswho.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import it.guesswho.R;
import it.guesswho.model.GuessWhoApplication;
import it.guesswho.model.User;
import it.guesswho.view.AvatarsActivity.ImageAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
    private Bitmap[] images;
    
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
		    images = new Bitmap[users.size()];
		    
		    GridView gridview = (GridView) V.findViewById(R.id.gridview);
		    gridview.setAdapter(new ImageAdapter(getActivity()));
		    
		    gridview.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

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
		            	
		            	if(images[position] == null)
		    	    	{
		            		Bitmap img = getBitmapFromURL(getUrlFacebookUserAvatar(users.get(position).getId()));
		            		images[position] = img;
		    	    	}
		    	    		
		            	imageView.setImageBitmap(images[position]);
		    	    	
//				   	    imageView.setImageBitmap(getBitmapFromURL(getUrlFacebookUserAvatar(users.get(position).getId())));
		            }
		    	     

		            visualizedUsers[position] = !visualizedUsers[position];

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
	    	if(images[position] == null)
	    	{
        		Bitmap img = getBitmapFromURL(getUrlFacebookUserAvatar(users.get(position).getId()));
        		images[position] = img;
	    	}
	    		
        	imageView.setImageBitmap(images[position]);
	    	
	    	TextView textView = (TextView)grid.findViewById(R.id.text);
	    	textView.setText(users.get(position).getName());

	    	return grid;
	    }
	}
	
	private String getUrlFacebookUserAvatar(String name_or_idUser )
	{
		String address = "http://graph.facebook.com/"+name_or_idUser+"/picture?type=normal";
	    URL url;
	    String newLocation = null;
	    try {
	        url = new URL(address);
	        HttpURLConnection.setFollowRedirects(false); //Do _not_ follow redirects!
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        newLocation = connection.getHeaderField("Location");
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return newLocation;
	}
	
	public Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {   
	        e.printStackTrace();
	        return null;
	    }
	}
	

	
}
