package it.guesswho.view;


import it.guesswho.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;

@SuppressLint("NewApi")
public class GameActivity extends SherlockFragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	public SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		mViewPager = (ViewPager)findViewById(R.id.pager);

		mViewPager.setAdapter(mSectionsPagerAdapter);

		//Bind the title indicator to the adapter
		TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
		titleIndicator.setViewPager(mViewPager);
		// Set up the ViewPager with the sections adapter.
		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
        
//		 menu.add("attach")
//		 .setTitle("attach")
//         .setIcon(R.drawable.attach)
//         .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		 menu.add("Update")
//		 .setTitle("Update")
//		 .setIcon(R.drawable.refresh)
//		 .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		 menu.add("Gps")
//		 .setTitle("gps")
//		 .setIcon(R.drawable.gps)
//		 .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        
         return true;
    }

	
	
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			
			Fragment fragment = null;
			
			if(position == 0){
				fragment = new GameFragment();
				
			}
			if(position == 1){
				fragment = new GameMessagesFragment();
			}
			
			return fragment;
		}
		
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Game Court";
			case 1:
				return "Message history ";
			}
			return "No fragment";
		}
	}

	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            
     }
//	 
//	  public String getPath(Uri uri) {
//          String[] projection = { MediaStore.Images.Media.DATA };
//          Cursor cursor = managedQuery(uri, projection, null, null, null);
//          startManagingCursor(cursor);
//          int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//          cursor.moveToFirst();
//          return cursor.getString(column_index);
//	  }
	  
	  @Override
	  public void onAttachFragment(Fragment fragment) {
	      // TODO Auto-generated method stub
	      super.onAttachFragment(fragment);
	     // Toast.makeText(getApplicationContext(), fragment.getTag(), Toast.LENGTH_SHORT).show();
	      }

	 

	@Override
	protected void onStop() {
		super.onStop();
	}
	

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			return super.onOptionsItemSelected(item);
	}
}