package org.gmcalc3;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TableActivity extends NavDrawerActivity {
	
	private class TabAdapter extends FragmentPagerAdapter {
		
		private String[] titles;
		private Fragment[] fragments;

		public TabAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
			super(fm);
			this.titles = titles;
			this.fragments = fragments;
		}
		
		@Override
		public Fragment getItem(int position) {
			return fragments[position];
		}
		
		@Override
		public int getCount() {
			return fragments.length;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}
	
	private WorldExplorerFragment worldExplorer;
	private ListFragment charactersFragment;
	private ListFragment partyFragment;
	private ViewPager tabPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		labelId = R.string.label_table;
		createNavDrawer(savedInstanceState);
		
		// Create the world explorer.
		worldExplorer = new WorldExplorerFragment();
		Bundle wEArgs = new Bundle();
		worldExplorer.setArguments(wEArgs);
		
		// Create the characters view.
		charactersFragment = new ListFragment();
		Bundle cFArgs = new Bundle();
		charactersFragment.setArguments(cFArgs);
		
		// Create the party view.
		partyFragment = new ListFragment();
		Bundle pFArgs = new Bundle();
		partyFragment.setArguments(pFArgs);
		
		// Set up the tab pager.
		tabPager = (ViewPager)findViewById(R.id.tabPager);
		String[] tabTitles = new String[] { "Worlds", "Characters", "Party" };
		Fragment[] tabFragments = new Fragment[] { worldExplorer, charactersFragment, partyFragment };
		TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), tabTitles, tabFragments);
		tabPager.setAdapter(tabAdapter);
		
		// Bind the tab strip to the pager.
		PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabStrip);
		tabStrip.setViewPager(tabPager);
	}

	@Override
	protected void populateDrawerMap() {
		super.populateDrawerMap();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide items related to the content view.
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		
		// TODO Disable items here: menu.findItem(R.id.whatever).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Let the drawer toggle handle it if it can.
		if (drawerToggle.onOptionsItemSelected(item))
			return true;
		
		// Otherwise, see about the other items.
		// TODO
		
		return super.onOptionsItemSelected(item);
	}

}
